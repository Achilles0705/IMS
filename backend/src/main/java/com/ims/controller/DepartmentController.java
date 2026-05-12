package com.ims.controller;

import com.ims.common.Result;
import com.ims.entity.Department;
import com.ims.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping
    public Result<List<Department>> listDepartments() {
        return Result.success(departmentService.listDepartments());
    }
}
