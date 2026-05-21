package com.ims.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ims.entity.ClassInfo;
import com.ims.entity.Course;
import com.ims.entity.CourseSelection;
import com.ims.entity.Department;
import com.ims.entity.Student;
import com.ims.entity.Teacher;
import com.ims.mapper.ClassInfoMapper;
import com.ims.mapper.CourseMapper;
import com.ims.mapper.CourseSelectionMapper;
import com.ims.mapper.DepartmentMapper;
import com.ims.mapper.StudentMapper;
import com.ims.mapper.TeacherMapper;
import com.ims.service.AdminStatisticsService;
import com.ims.util.RoleCheckUtil;
import com.ims.vo.AdminCourseGradeVO;
import com.ims.vo.AdminOverviewVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminStatisticsServiceImpl implements AdminStatisticsService {

    private final DepartmentMapper departmentMapper;
    private final StudentMapper studentMapper;
    private final TeacherMapper teacherMapper;
    private final CourseMapper courseMapper;
    private final ClassInfoMapper classInfoMapper;
    private final CourseSelectionMapper courseSelectionMapper;

    @Override
    public AdminOverviewVO getOverview(String role) {
        RoleCheckUtil.requireRole(role, "ADMIN");
        AdminOverviewVO vo = new AdminOverviewVO();
        vo.setDepartmentCount(departmentMapper.selectCount(new LambdaQueryWrapper<Department>()));
        vo.setStudentCount(studentMapper.selectCount(new LambdaQueryWrapper<Student>()));
        vo.setTeacherCount(teacherMapper.selectCount(new LambdaQueryWrapper<Teacher>()));
        vo.setCourseCount(courseMapper.selectCount(new LambdaQueryWrapper<Course>()));
        vo.setClassCount(classInfoMapper.selectCount(new LambdaQueryWrapper<ClassInfo>()));
        vo.setSelectionCount(courseSelectionMapper.selectCount(new LambdaQueryWrapper<CourseSelection>()));
        return vo;
    }

    @Override
    public List<AdminCourseGradeVO> listCourseGrades(String role, String semester, String courseId) {
        RoleCheckUtil.requireRole(role, "ADMIN");

        List<ClassInfo> classInfos = classInfoMapper.selectList(new LambdaQueryWrapper<ClassInfo>()
                .eq(StringUtils.hasText(semester), ClassInfo::getSemester, semester)
                .eq(StringUtils.hasText(courseId), ClassInfo::getCourseId, courseId));
        if (classInfos.isEmpty()) {
            return Collections.emptyList();
        }

        Set<String> courseIds = classInfos.stream()
                .map(ClassInfo::getCourseId)
                .collect(Collectors.toSet());
        Map<String, Course> courseMap = courseMapper.selectBatchIds(courseIds).stream()
                .collect(Collectors.toMap(Course::getCourseId, item -> item));

        Map<String, List<CourseSelection>> selectionsByCourse = courseSelectionMapper.selectList(new LambdaQueryWrapper<CourseSelection>()
                        .eq(StringUtils.hasText(semester), CourseSelection::getSemester, semester)
                        .eq(StringUtils.hasText(courseId), CourseSelection::getCourseId, courseId))
                .stream()
                .collect(Collectors.groupingBy(item -> buildCourseKey(item.getSemester(), item.getCourseId())));

        Map<String, AdminCourseGradeVO> statsMap = new LinkedHashMap<>();
        for (ClassInfo classInfo : classInfos) {
            String key = buildCourseKey(classInfo.getSemester(), classInfo.getCourseId());
            statsMap.computeIfAbsent(key, unused -> {
                AdminCourseGradeVO vo = new AdminCourseGradeVO();
                vo.setSemester(classInfo.getSemester());
                vo.setCourseId(classInfo.getCourseId());
                Course course = courseMap.get(classInfo.getCourseId());
                vo.setCourseName(course == null ? null : course.getCourseName());
                vo.setStudentCount(0L);
                vo.setGradedCount(0L);
                vo.setAverageScore(null);
                vo.setMaxScore(null);
                vo.setMinScore(null);
                vo.setFailedCount(0L);
                return vo;
            });
        }

        for (Map.Entry<String, AdminCourseGradeVO> entry : statsMap.entrySet()) {
            List<CourseSelection> selections = selectionsByCourse.getOrDefault(entry.getKey(), new ArrayList<>());
            AdminCourseGradeVO vo = entry.getValue();
            vo.setStudentCount((long) selections.size());

            List<Integer> gradedScores = selections.stream()
                    .map(CourseSelection::getScore)
                    .filter(score -> score != null)
                    .toList();
            vo.setGradedCount((long) gradedScores.size());
            if (!gradedScores.isEmpty()) {
                vo.setAverageScore(roundTo2(gradedScores.stream().mapToInt(Integer::intValue).average().orElse(0D)));
                vo.setMaxScore(gradedScores.stream().max(Comparator.naturalOrder()).orElse(null));
                vo.setMinScore(gradedScores.stream().min(Comparator.naturalOrder()).orElse(null));
                vo.setFailedCount(gradedScores.stream().filter(score -> score < 60).count());
            }
        }

        List<AdminCourseGradeVO> result = new ArrayList<>(statsMap.values());
        result.sort(Comparator.comparing(AdminCourseGradeVO::getSemester).reversed()
                .thenComparing(AdminCourseGradeVO::getCourseId));
        return result;
    }

    private String buildCourseKey(String semester, String courseId) {
        return semester + "|" + courseId;
    }

    private Double roundTo2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
