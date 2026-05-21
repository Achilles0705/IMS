package com.ims.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ims.common.BusinessException;
import com.ims.dto.ClassCreateRequest;
import com.ims.dto.ClassUpdateRequest;
import com.ims.entity.ClassInfo;
import com.ims.mapper.CourseMapper;
import com.ims.mapper.ClassInfoMapper;
import com.ims.mapper.TeacherMapper;
import com.ims.service.ClassInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassInfoServiceImpl extends ServiceImpl<ClassInfoMapper, ClassInfo> implements ClassInfoService {

    private final CourseMapper courseMapper;
    private final TeacherMapper teacherMapper;

    @Override
    public List<ClassInfo> listClasses(String semester, String courseId, String staffId) {
        LambdaQueryWrapper<ClassInfo> wrapper = new LambdaQueryWrapper<ClassInfo>()
                .eq(StringUtils.hasText(semester), ClassInfo::getSemester, semester)
                .eq(StringUtils.hasText(courseId), ClassInfo::getCourseId, courseId)
                .eq(StringUtils.hasText(staffId), ClassInfo::getStaffId, staffId)
                .orderByDesc(ClassInfo::getSemester)
                .orderByAsc(ClassInfo::getCourseId)
                .orderByAsc(ClassInfo::getStaffId);
        return list(wrapper);
    }

    @Override
    public ClassInfo createClass(ClassCreateRequest request) {
        if (courseMapper.selectById(request.getCourseId()) == null) {
            throw new BusinessException("课程不存在");
        }
        if (teacherMapper.selectById(request.getStaffId()) == null) {
            throw new BusinessException("教师不存在");
        }
        ClassInfo existing = getOne(new LambdaQueryWrapper<ClassInfo>()
                .eq(ClassInfo::getSemester, request.getSemester())
                .eq(ClassInfo::getCourseId, request.getCourseId())
                .eq(ClassInfo::getStaffId, request.getStaffId()));
        if (existing != null) {
            throw new BusinessException("开课记录已存在");
        }
        ClassInfo classInfo = new ClassInfo();
        classInfo.setSemester(request.getSemester());
        classInfo.setCourseId(request.getCourseId());
        classInfo.setStaffId(request.getStaffId());
        classInfo.setClassTime(request.getClassTime());
        save(classInfo);
        return classInfo;
    }

    @Override
    public ClassInfo updateClass(String semester, String courseId, String staffId, ClassUpdateRequest request) {
        ClassInfo existing = getOne(new LambdaQueryWrapper<ClassInfo>()
                .eq(ClassInfo::getSemester, semester)
                .eq(ClassInfo::getCourseId, courseId)
                .eq(ClassInfo::getStaffId, staffId));
        if (existing == null) {
            throw new BusinessException(404, "开课记录不存在");
        }
        lambdaUpdate()
                .eq(ClassInfo::getSemester, semester)
                .eq(ClassInfo::getCourseId, courseId)
                .eq(ClassInfo::getStaffId, staffId)
                .set(ClassInfo::getClassTime, request.getClassTime())
                .update();
        return getOne(new LambdaQueryWrapper<ClassInfo>()
                .eq(ClassInfo::getSemester, semester)
                .eq(ClassInfo::getCourseId, courseId)
                .eq(ClassInfo::getStaffId, staffId));
    }
}
