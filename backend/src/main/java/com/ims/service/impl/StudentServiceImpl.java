package com.ims.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ims.common.BusinessException;
import com.ims.dto.StudentCreateRequest;
import com.ims.dto.StudentUpdateRequest;
import com.ims.entity.Student;
import com.ims.mapper.StudentMapper;
import com.ims.service.StudentService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Override
    public List<Student> listStudents(String deptId, String keyword) {
        LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<Student>()
                .eq(StringUtils.hasText(deptId), Student::getDeptId, deptId)
                .and(StringUtils.hasText(keyword), query -> query
                        .like(Student::getStudentId, keyword)
                        .or()
                        .like(Student::getName, keyword))
                .orderByAsc(Student::getStudentId);
        return list(wrapper);
    }

    @Override
    public Student createStudent(StudentCreateRequest request) {
        if (getById(request.getStudentId()) != null) {
            throw new BusinessException("学生编号已存在");
        }
        Student student = new Student();
        BeanUtils.copyProperties(request, student);
        save(student);
        return student;
    }

    @Override
    public Student updateStudent(String studentId, StudentUpdateRequest request) {
        Student existing = getById(studentId);
        if (existing == null) {
            throw new BusinessException(404, "学生不存在");
        }
        Student student = new Student();
        BeanUtils.copyProperties(request, student);
        student.setStudentId(studentId);
        updateById(student);
        return getById(studentId);
    }
}
