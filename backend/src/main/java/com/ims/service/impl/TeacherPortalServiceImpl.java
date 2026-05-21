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
import com.ims.service.TeacherPortalService;
import com.ims.util.RoleCheckUtil;
import com.ims.vo.TeacherClassStudentVO;
import com.ims.vo.TeacherClassVO;
import com.ims.vo.TeacherStatisticsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherPortalServiceImpl implements TeacherPortalService {

    private final TeacherMapper teacherMapper;
    private final ClassInfoMapper classInfoMapper;
    private final CourseMapper courseMapper;
    private final CourseSelectionMapper courseSelectionMapper;
    private final StudentMapper studentMapper;

    @Override
    public List<TeacherClassVO> listClasses(String role, String staffId, String semester) {
        RoleCheckUtil.requireRole(role, "TEACHER");
        String relatedStaffId = RoleCheckUtil.requireRelatedId(staffId);
        ensureTeacherExists(relatedStaffId);

        List<ClassInfo> classes = classInfoMapper.selectList(new LambdaQueryWrapper<ClassInfo>()
                .eq(ClassInfo::getStaffId, relatedStaffId)
                .eq(StringUtils.hasText(semester), ClassInfo::getSemester, semester)
                .orderByDesc(ClassInfo::getSemester)
                .orderByAsc(ClassInfo::getCourseId));
        if (classes.isEmpty()) {
            return Collections.emptyList();
        }

        Map<String, Course> courseMap = loadCourseMap(classes.stream()
                .map(ClassInfo::getCourseId)
                .collect(Collectors.toSet()));

        Map<String, Long> selectionCountMap = courseSelectionMapper.selectList(new LambdaQueryWrapper<CourseSelection>()
                        .eq(CourseSelection::getStaffId, relatedStaffId)
                        .eq(StringUtils.hasText(semester), CourseSelection::getSemester, semester))
                .stream()
                .collect(Collectors.groupingBy(
                        item -> buildClassKey(item.getSemester(), item.getCourseId(), item.getStaffId()),
                        Collectors.counting()));

        List<TeacherClassVO> result = new ArrayList<>(classes.size());
        for (ClassInfo classInfo : classes) {
            Course course = courseMap.get(classInfo.getCourseId());
            TeacherClassVO vo = new TeacherClassVO();
            vo.setSemester(classInfo.getSemester());
            vo.setCourseId(classInfo.getCourseId());
            vo.setCourseName(course == null ? null : course.getCourseName());
            vo.setCredit(course == null ? null : course.getCredit());
            vo.setStaffId(classInfo.getStaffId());
            vo.setClassTime(classInfo.getClassTime());
            vo.setStudentCount(selectionCountMap.getOrDefault(
                    buildClassKey(classInfo.getSemester(), classInfo.getCourseId(), classInfo.getStaffId()), 0L));
            result.add(vo);
        }
        return result;
    }

    @Override
    public List<TeacherClassStudentVO> listClassStudents(String role, String staffId, String semester, String courseId) {
        RoleCheckUtil.requireRole(role, "TEACHER");
        String relatedStaffId = RoleCheckUtil.requireRelatedId(staffId);
        ensureTeacherExists(relatedStaffId);
        ensureClassExists(semester, courseId, relatedStaffId);

        List<CourseSelection> selections = courseSelectionMapper.selectList(new LambdaQueryWrapper<CourseSelection>()
                .eq(CourseSelection::getSemester, semester)
                .eq(CourseSelection::getCourseId, courseId)
                .eq(CourseSelection::getStaffId, relatedStaffId)
                .orderByAsc(CourseSelection::getStudentId));
        if (selections.isEmpty()) {
            return Collections.emptyList();
        }

        Set<String> studentIds = selections.stream()
                .map(CourseSelection::getStudentId)
                .collect(Collectors.toSet());
        Map<String, Student> studentMap = studentMapper.selectBatchIds(studentIds).stream()
                .collect(Collectors.toMap(Student::getStudentId, item -> item));

        List<TeacherClassStudentVO> result = new ArrayList<>(selections.size());
        for (CourseSelection selection : selections) {
            Student student = studentMap.get(selection.getStudentId());
            TeacherClassStudentVO vo = new TeacherClassStudentVO();
            vo.setStudentId(selection.getStudentId());
            vo.setStudentName(student == null ? null : student.getName());
            vo.setSex(student == null ? null : student.getSex());
            vo.setDeptId(student == null ? null : student.getDeptId());
            vo.setMobilePhone(student == null ? null : student.getMobilePhone());
            vo.setScore(selection.getScore());
            result.add(vo);
        }
        return result;
    }

    @Override
    public TeacherStatisticsVO getStatistics(String role, String staffId, String semester) {
        RoleCheckUtil.requireRole(role, "TEACHER");
        String relatedStaffId = RoleCheckUtil.requireRelatedId(staffId);
        ensureTeacherExists(relatedStaffId);

        long classCount = classInfoMapper.selectCount(new LambdaQueryWrapper<ClassInfo>()
                .eq(ClassInfo::getStaffId, relatedStaffId)
                .eq(StringUtils.hasText(semester), ClassInfo::getSemester, semester));

        List<CourseSelection> selections = courseSelectionMapper.selectList(new LambdaQueryWrapper<CourseSelection>()
                .eq(CourseSelection::getStaffId, relatedStaffId)
                .eq(StringUtils.hasText(semester), CourseSelection::getSemester, semester));

        long studentCount = selections.size();
        List<CourseSelection> graded = selections.stream()
                .filter(item -> item.getScore() != null)
                .toList();
        long gradedCount = graded.size();
        long ungradedCount = studentCount - gradedCount;
        long failedCount = graded.stream()
                .filter(item -> item.getScore() < 60)
                .count();
        Double averageScore = graded.isEmpty() ? null : roundTo2(graded.stream()
                .mapToInt(CourseSelection::getScore)
                .average()
                .orElse(0D));

        TeacherStatisticsVO vo = new TeacherStatisticsVO();
        vo.setClassCount(classCount);
        vo.setStudentCount(studentCount);
        vo.setGradedCount(gradedCount);
        vo.setUngradedCount(ungradedCount);
        vo.setAverageScore(averageScore);
        vo.setFailedCount(failedCount);
        return vo;
    }

    private void ensureTeacherExists(String staffId) {
        Teacher teacher = teacherMapper.selectById(staffId);
        if (teacher == null) {
            throw new BusinessException(404, "教师不存在");
        }
    }

    private void ensureClassExists(String semester, String courseId, String staffId) {
        ClassInfo classInfo = classInfoMapper.selectOne(new LambdaQueryWrapper<ClassInfo>()
                .eq(ClassInfo::getSemester, semester)
                .eq(ClassInfo::getCourseId, courseId)
                .eq(ClassInfo::getStaffId, staffId));
        if (classInfo == null) {
            throw new BusinessException(404, "开课记录不存在或无权限");
        }
    }

    private Map<String, Course> loadCourseMap(Set<String> courseIds) {
        if (courseIds.isEmpty()) {
            return new HashMap<>();
        }
        return courseMapper.selectBatchIds(courseIds).stream()
                .collect(Collectors.toMap(Course::getCourseId, item -> item));
    }

    private String buildClassKey(String semester, String courseId, String staffId) {
        return semester + "|" + courseId + "|" + staffId;
    }

    private Double roundTo2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
