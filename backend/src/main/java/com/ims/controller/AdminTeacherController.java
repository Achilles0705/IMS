package com.ims.controller;

import com.ims.common.Result;
import com.ims.dto.TeacherCreateRequest;
import com.ims.dto.TeacherUpdateRequest;
import com.ims.entity.Teacher;
import com.ims.service.TeacherService;
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
@RequestMapping("/api/admin/teachers")
@RequiredArgsConstructor
public class AdminTeacherController {

    private final TeacherService teacherService;

    @GetMapping
    public Result<List<Teacher>> listTeachers(@RequestParam(required = false) String deptId,
                                              @RequestParam(required = false) String keyword) {
        return Result.success(teacherService.listTeachers(deptId, keyword));
    }

    @PostMapping
    public Result<Teacher> createTeacher(@Valid @RequestBody TeacherCreateRequest request) {
        return Result.success(teacherService.createTeacher(request));
    }

    @PutMapping("/{staffId}")
    public Result<Teacher> updateTeacher(@PathVariable String staffId,
                                         @Valid @RequestBody TeacherUpdateRequest request) {
        return Result.success(teacherService.updateTeacher(staffId, request));
    }
}
