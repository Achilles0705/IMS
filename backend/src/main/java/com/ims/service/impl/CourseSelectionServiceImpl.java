package com.ims.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ims.common.BusinessException;
import com.ims.dto.StudentSelectionRequest;
import com.ims.dto.TeacherGradeRequest;
import com.ims.entity.ClassInfo;
import com.ims.entity.CourseSelection;
import com.ims.entity.Student;
import com.ims.mapper.ClassInfoMapper;
import com.ims.mapper.CourseSelectionMapper;
import com.ims.mapper.StudentMapper;
import com.ims.service.CourseSelectionService;
import com.ims.util.RoleCheckUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseSelectionServiceImpl extends ServiceImpl<CourseSelectionMapper, CourseSelection>
        implements CourseSelectionService {

    private final StudentMapper studentMapper;
    private final ClassInfoMapper classInfoMapper;

    @Override
    public CourseSelection selectCourse(String role, String studentId, StudentSelectionRequest request) {
        RoleCheckUtil.requireRole(role, "STUDENT");
        String relatedStudentId = RoleCheckUtil.requireRelatedId(studentId);
        ensureStudentExists(relatedStudentId);
        ensureClassExists(request.getSemester(), request.getCourseId(), request.getStaffId());

        CourseSelection existing = getOne(new LambdaQueryWrapper<CourseSelection>()
                .eq(CourseSelection::getStudentId, relatedStudentId)
                .eq(CourseSelection::getSemester, request.getSemester())
                .eq(CourseSelection::getCourseId, request.getCourseId())
                .eq(CourseSelection::getStaffId, request.getStaffId()));
        if (existing != null) {
            throw new BusinessException("当前课程已选，不能重复选课");
        }

        CourseSelection selection = new CourseSelection();
        selection.setStudentId(relatedStudentId);
        selection.setSemester(request.getSemester());
        selection.setCourseId(request.getCourseId());
        selection.setStaffId(request.getStaffId());
        selection.setScore(null);
        save(selection);
        return selection;
    }

    @Override
    public boolean dropCourse(String role, String studentId, StudentSelectionRequest request) {
        RoleCheckUtil.requireRole(role, "STUDENT");
        String relatedStudentId = RoleCheckUtil.requireRelatedId(studentId);
        ensureStudentExists(relatedStudentId);

        CourseSelection existing = getOne(new LambdaQueryWrapper<CourseSelection>()
                .eq(CourseSelection::getStudentId, relatedStudentId)
                .eq(CourseSelection::getSemester, request.getSemester())
                .eq(CourseSelection::getCourseId, request.getCourseId())
                .eq(CourseSelection::getStaffId, request.getStaffId()));
        if (existing == null) {
            throw new BusinessException(404, "选课记录不存在");
        }
        if (existing.getScore() != null) {
            throw new BusinessException("已录入成绩的课程不允许退课");
        }
        return remove(new LambdaQueryWrapper<CourseSelection>()
                .eq(CourseSelection::getStudentId, relatedStudentId)
                .eq(CourseSelection::getSemester, request.getSemester())
                .eq(CourseSelection::getCourseId, request.getCourseId())
                .eq(CourseSelection::getStaffId, request.getStaffId()));
    }

    @Override
    public CourseSelection updateGrade(String role, String staffId, TeacherGradeRequest request) {
        RoleCheckUtil.requireRole(role, "TEACHER");
        String relatedStaffId = RoleCheckUtil.requireRelatedId(staffId);
        ensureClassExists(request.getSemester(), request.getCourseId(), relatedStaffId);

        CourseSelection selection = getOne(new LambdaQueryWrapper<CourseSelection>()
                .eq(CourseSelection::getStudentId, request.getStudentId())
                .eq(CourseSelection::getSemester, request.getSemester())
                .eq(CourseSelection::getCourseId, request.getCourseId())
                .eq(CourseSelection::getStaffId, relatedStaffId));
        if (selection == null) {
            throw new BusinessException(404, "学生未选择该课程");
        }

        lambdaUpdate()
                .eq(CourseSelection::getStudentId, request.getStudentId())
                .eq(CourseSelection::getSemester, request.getSemester())
                .eq(CourseSelection::getCourseId, request.getCourseId())
                .eq(CourseSelection::getStaffId, relatedStaffId)
                .set(CourseSelection::getScore, request.getScore())
                .update();

        return getOne(new LambdaQueryWrapper<CourseSelection>()
                .eq(CourseSelection::getStudentId, request.getStudentId())
                .eq(CourseSelection::getSemester, request.getSemester())
                .eq(CourseSelection::getCourseId, request.getCourseId())
                .eq(CourseSelection::getStaffId, relatedStaffId));
    }

    private void ensureStudentExists(String studentId) {
        Student student = studentMapper.selectById(studentId);
        if (student == null) {
            throw new BusinessException(404, "学生不存在");
        }
    }

    private void ensureClassExists(String semester, String courseId, String staffId) {
        ClassInfo classInfo = classInfoMapper.selectOne(new LambdaQueryWrapper<ClassInfo>()
                .eq(ClassInfo::getSemester, semester)
                .eq(ClassInfo::getCourseId, courseId)
                .eq(ClassInfo::getStaffId, staffId));
        if (classInfo == null) {
            throw new BusinessException(404, "开课记录不存在");
        }
    }
}
