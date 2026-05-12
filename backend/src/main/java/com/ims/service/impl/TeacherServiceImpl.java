package com.ims.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ims.common.BusinessException;
import com.ims.dto.TeacherCreateRequest;
import com.ims.dto.TeacherUpdateRequest;
import com.ims.entity.Teacher;
import com.ims.mapper.TeacherMapper;
import com.ims.service.TeacherService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    @Override
    public List<Teacher> listTeachers(String deptId, String keyword) {
        LambdaQueryWrapper<Teacher> wrapper = new LambdaQueryWrapper<Teacher>()
                .eq(StringUtils.hasText(deptId), Teacher::getDeptId, deptId)
                .and(StringUtils.hasText(keyword), query -> query
                        .like(Teacher::getStaffId, keyword)
                        .or()
                        .like(Teacher::getName, keyword))
                .orderByAsc(Teacher::getStaffId);
        return list(wrapper);
    }

    @Override
    public Teacher createTeacher(TeacherCreateRequest request) {
        if (getById(request.getStaffId()) != null) {
            throw new BusinessException("教师编号已存在");
        }
        Teacher teacher = new Teacher();
        BeanUtils.copyProperties(request, teacher);
        save(teacher);
        return teacher;
    }

    @Override
    public Teacher updateTeacher(String staffId, TeacherUpdateRequest request) {
        Teacher existing = getById(staffId);
        if (existing == null) {
            throw new BusinessException(404, "教师不存在");
        }
        Teacher teacher = new Teacher();
        BeanUtils.copyProperties(request, teacher);
        teacher.setStaffId(staffId);
        updateById(teacher);
        return getById(staffId);
    }
}
