package com.ims.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ims.dto.TeacherCreateRequest;
import com.ims.dto.TeacherUpdateRequest;
import com.ims.entity.Teacher;

import java.util.List;

public interface TeacherService extends IService<Teacher> {

    List<Teacher> listTeachers(String deptId, String keyword);

    Teacher createTeacher(TeacherCreateRequest request);

    Teacher updateTeacher(String staffId, TeacherUpdateRequest request);
}
