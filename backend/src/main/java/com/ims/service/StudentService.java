package com.ims.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ims.dto.StudentCreateRequest;
import com.ims.dto.StudentUpdateRequest;
import com.ims.entity.Student;

import java.util.List;

public interface StudentService extends IService<Student> {

    List<Student> listStudents(String deptId, String keyword);

    Student createStudent(StudentCreateRequest request);

    Student updateStudent(String studentId, StudentUpdateRequest request);
}
