package com.ims.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ims.common.Result;
import com.ims.entity.Department;
import com.ims.mapper.DepartmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
public class HealthController {

    private final DepartmentMapper departmentMapper;

    @GetMapping
    public Result<Map<String, String>> health() {
        return Result.success(Map.of("status", "UP", "message", "教学事务管理系统后端运行正常"));
    }

    @GetMapping("/db")
    public Result<Map<String, Object>> databaseHealth() {
        Long departmentCount = departmentMapper.selectCount(new QueryWrapper<Department>());
        return Result.success(Map.of(
                "status", "UP",
                "message", "数据库连接正常",
                "departmentCount", departmentCount
        ));
    }
}
