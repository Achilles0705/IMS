package com.ims.controller;

import com.ims.common.Result;
import com.ims.dto.StudentCreateRequest;
import com.ims.dto.StudentUpdateRequest;
import com.ims.entity.Student;
import com.ims.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/students")
@RequiredArgsConstructor
public class AdminStudentController {

    private final StudentService studentService;

    @GetMapping
    public Result<List<Student>> listStudents(@RequestParam(required = false) String deptId,
                                              @RequestParam(required = false) String keyword) {
        return Result.success(studentService.listStudents(deptId, keyword));
    }

    @PostMapping
    public Result<Student> createStudent(@Valid @RequestBody StudentCreateRequest request) {
        return Result.success(studentService.createStudent(request));
    }

    @PutMapping("/{studentId}")
    public Result<Student> updateStudent(@PathVariable String studentId,
                                         @Valid @RequestBody StudentUpdateRequest request) {
        return Result.success(studentService.updateStudent(studentId, request));
    }
}
