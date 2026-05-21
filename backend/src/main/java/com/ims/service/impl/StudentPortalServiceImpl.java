package com.ims.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ims.common.BusinessException;
import com.ims.entity.ClassInfo;
import com.ims.entity.Course;
import com.ims.entity.CourseSelection;
import com.ims.entity.Student;
import com.ims.entity.Teacher;
import com.ims.mapper.ClassInfoMapper;
import com.ims.mapper.CourseMapper;
import com.ims.mapper.CourseSelectionMapper;
import com.ims.mapper.StudentMapper;
import com.ims.mapper.TeacherMapper;
import com.ims.service.StudentPortalService;
import com.ims.util.RoleCheckUtil;
import com.ims.vo.StudentClassVO;
import com.ims.vo.StudentCreditSummaryVO;
import com.ims.vo.StudentGradeVO;
import com.ims.vo.StudentSelectionVO;
import com.ims.vo.StudentStatisticsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentPortalServiceImpl implements StudentPortalService {

    private final StudentMapper studentMapper;
    private final ClassInfoMapper classInfoMapper;
    private final CourseMapper courseMapper;
    private final TeacherMapper teacherMapper;
    private final CourseSelectionMapper courseSelectionMapper;

    @Override
    public List<StudentClassVO> listClasses(String role, String studentId, String semester) {
        RoleCheckUtil.requireRole(role, "STUDENT");
        String relatedStudentId = RoleCheckUtil.requireRelatedId(studentId);
        ensureStudentExists(relatedStudentId);
        if (!StringUtils.hasText(semester)) {
            throw new BusinessException("学期不能为空");
        }

        List<ClassInfo> classList = classInfoMapper.selectList(new LambdaQueryWrapper<ClassInfo>()
                .eq(ClassInfo::getSemester, semester)
                .orderByAsc(ClassInfo::getCourseId)
                .orderByAsc(ClassInfo::getStaffId));
        if (classList.isEmpty()) {
            return Collections.emptyList();
        }

        Map<String, Course> courseMap = loadCourseMap(classList.stream()
                .map(ClassInfo::getCourseId)
                .collect(Collectors.toSet()));
        Map<String, Teacher> teacherMap = loadTeacherMap(classList.stream()
                .map(ClassInfo::getStaffId)
                .collect(Collectors.toSet()));

        Set<String> selectedKeys = courseSelectionMapper.selectList(new LambdaQueryWrapper<CourseSelection>()
                        .eq(CourseSelection::getStudentId, relatedStudentId)
                        .eq(CourseSelection::getSemester, semester))
                .stream()
                .map(selection -> buildSelectionKey(selection.getSemester(), selection.getCourseId(), selection.getStaffId()))
                .collect(Collectors.toSet());

        List<StudentClassVO> result = new ArrayList<>(classList.size());
        for (ClassInfo classInfo : classList) {
            Course course = courseMap.get(classInfo.getCourseId());
            Teacher teacher = teacherMap.get(classInfo.getStaffId());
            StudentClassVO vo = new StudentClassVO();
            vo.setSemester(classInfo.getSemester());
            vo.setCourseId(classInfo.getCourseId());
            vo.setCourseName(course == null ? null : course.getCourseName());
            vo.setCredit(course == null ? null : course.getCredit());
            vo.setCreditHours(course == null ? null : course.getCreditHours());
            vo.setStaffId(classInfo.getStaffId());
            vo.setTeacherName(teacher == null ? null : teacher.getName());
            vo.setClassTime(classInfo.getClassTime());
            vo.setSelected(selectedKeys.contains(
                    buildSelectionKey(classInfo.getSemester(), classInfo.getCourseId(), classInfo.getStaffId())));
            result.add(vo);
        }
        return result;
    }

    @Override
    public List<StudentSelectionVO> listSelections(String role, String studentId, String semester) {
        RoleCheckUtil.requireRole(role, "STUDENT");
        String relatedStudentId = RoleCheckUtil.requireRelatedId(studentId);
        ensureStudentExists(relatedStudentId);

        List<CourseSelection> selections = courseSelectionMapper.selectList(new LambdaQueryWrapper<CourseSelection>()
                .eq(CourseSelection::getStudentId, relatedStudentId)
                .eq(StringUtils.hasText(semester), CourseSelection::getSemester, semester)
                .orderByDesc(CourseSelection::getSemester)
                .orderByAsc(CourseSelection::getCourseId)
                .orderByAsc(CourseSelection::getStaffId));
        if (selections.isEmpty()) {
            return Collections.emptyList();
        }

        Map<String, Course> courseMap = loadCourseMap(selections.stream()
                .map(CourseSelection::getCourseId)
                .collect(Collectors.toSet()));
        Map<String, Teacher> teacherMap = loadTeacherMap(selections.stream()
                .map(CourseSelection::getStaffId)
                .collect(Collectors.toSet()));

        Set<String> semesters = selections.stream()
                .map(CourseSelection::getSemester)
                .collect(Collectors.toSet());
        Set<String> courseIds = selections.stream()
                .map(CourseSelection::getCourseId)
                .collect(Collectors.toSet());
        Set<String> staffIds = selections.stream()
                .map(CourseSelection::getStaffId)
                .collect(Collectors.toSet());
        Map<String, ClassInfo> classMap = classInfoMapper.selectList(new LambdaQueryWrapper<ClassInfo>()
                        .in(!semesters.isEmpty(), ClassInfo::getSemester, semesters)
                        .in(!courseIds.isEmpty(), ClassInfo::getCourseId, courseIds)
                        .in(!staffIds.isEmpty(), ClassInfo::getStaffId, staffIds))
                .stream()
                .collect(Collectors.toMap(
                        item -> buildSelectionKey(item.getSemester(), item.getCourseId(), item.getStaffId()),
                        item -> item,
                        (left, right) -> left,
                        LinkedHashMap::new));

        List<StudentSelectionVO> result = new ArrayList<>(selections.size());
        for (CourseSelection selection : selections) {
            Course course = courseMap.get(selection.getCourseId());
            Teacher teacher = teacherMap.get(selection.getStaffId());
            ClassInfo classInfo = classMap.get(buildSelectionKey(
                    selection.getSemester(), selection.getCourseId(), selection.getStaffId()));
            StudentSelectionVO vo = new StudentSelectionVO();
            vo.setStudentId(selection.getStudentId());
            vo.setSemester(selection.getSemester());
            vo.setCourseId(selection.getCourseId());
            vo.setCourseName(course == null ? null : course.getCourseName());
            vo.setCredit(course == null ? null : course.getCredit());
            vo.setStaffId(selection.getStaffId());
            vo.setTeacherName(teacher == null ? null : teacher.getName());
            vo.setClassTime(classInfo == null ? null : classInfo.getClassTime());
            vo.setScore(selection.getScore());
            result.add(vo);
        }
        return result;
    }

    @Override
    public List<StudentGradeVO> listGrades(String role, String studentId, String semester) {
        List<StudentSelectionVO> selections = listSelections(role, studentId, semester);
        return selections.stream().map(selection -> {
            StudentGradeVO vo = new StudentGradeVO();
            vo.setSemester(selection.getSemester());
            vo.setCourseId(selection.getCourseId());
            vo.setCourseName(selection.getCourseName());
            vo.setCredit(selection.getCredit());
            vo.setStaffId(selection.getStaffId());
            vo.setTeacherName(selection.getTeacherName());
            vo.setScore(selection.getScore());
            vo.setPassed(selection.getScore() == null ? null : selection.getScore() >= 60);
            return vo;
        }).toList();
    }

    @Override
    public StudentStatisticsVO getStatistics(String role, String studentId) {
        RoleCheckUtil.requireRole(role, "STUDENT");
        String relatedStudentId = RoleCheckUtil.requireRelatedId(studentId);
        ensureStudentExists(relatedStudentId);

        List<CourseSelection> selections = courseSelectionMapper.selectList(new LambdaQueryWrapper<CourseSelection>()
                .eq(CourseSelection::getStudentId, relatedStudentId));

        long selectedCount = selections.size();
        List<CourseSelection> graded = selections.stream()
                .filter(item -> item.getScore() != null)
                .toList();
        long gradedCount = graded.size();
        long passedCount = graded.stream()
                .filter(item -> item.getScore() >= 60)
                .count();
        long failedCount = graded.stream()
                .filter(item -> item.getScore() < 60)
                .count();

        Map<String, Course> courseMap = loadCourseMap(selections.stream()
                .map(CourseSelection::getCourseId)
                .collect(Collectors.toSet()));
        int earnedCredits = graded.stream()
                .filter(item -> item.getScore() >= 60)
                .map(item -> courseMap.get(item.getCourseId()))
                .filter(Objects::nonNull)
                .map(Course::getCredit)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .sum();
        Double averageScore = gradedCount == 0 ? null : roundTo2(graded.stream()
                .mapToInt(CourseSelection::getScore)
                .average()
                .orElse(0D));

        StudentStatisticsVO vo = new StudentStatisticsVO();
        vo.setSelectedCourseCount(selectedCount);
        vo.setGradedCourseCount(gradedCount);
        vo.setPassedCourseCount(passedCount);
        vo.setFailedCourseCount(failedCount);
        vo.setEarnedCredits(earnedCredits);
        vo.setAverageScore(averageScore);
        return vo;
    }

    @Override
    public StudentCreditSummaryVO getCreditSummary(String role, String studentId) {
        StudentStatisticsVO statistics = getStatistics(role, studentId);
        StudentCreditSummaryVO vo = new StudentCreditSummaryVO();
        vo.setStudentId(studentId);
        vo.setPassedCourseCount(statistics.getPassedCourseCount());
        vo.setEarnedCredits(statistics.getEarnedCredits());
        vo.setAverageScore(statistics.getAverageScore());
        return vo;
    }

    private void ensureStudentExists(String studentId) {
        Student student = studentMapper.selectById(studentId);
        if (student == null) {
            throw new BusinessException(404, "学生不存在");
        }
    }

    private Map<String, Course> loadCourseMap(Set<String> courseIds) {
        if (courseIds.isEmpty()) {
            return new HashMap<>();
        }
        return courseMapper.selectBatchIds(courseIds).stream()
                .collect(Collectors.toMap(Course::getCourseId, item -> item));
    }

    private Map<String, Teacher> loadTeacherMap(Set<String> staffIds) {
        if (staffIds.isEmpty()) {
            return new HashMap<>();
        }
        return teacherMapper.selectBatchIds(staffIds).stream()
                .collect(Collectors.toMap(Teacher::getStaffId, item -> item));
    }

    private String buildSelectionKey(String semester, String courseId, String staffId) {
        return semester + "|" + courseId + "|" + staffId;
    }

    private Double roundTo2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
