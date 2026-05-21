package com.ims.controller;

import com.ims.common.Result;
import com.ims.dto.ClassCreateRequest;
import com.ims.dto.ClassUpdateRequest;
import com.ims.entity.ClassInfo;
import com.ims.service.ClassInfoService;
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
@RequestMapping("/api/admin/classes")
@RequiredArgsConstructor
public class AdminClassController {

    private final ClassInfoService classInfoService;

    @GetMapping
    public Result<List<ClassInfo>> listClasses(@RequestParam(required = false) String semester,
                                               @RequestParam(required = false) String courseId,
                                               @RequestParam(required = false) String staffId) {
        return Result.success(classInfoService.listClasses(semester, courseId, staffId));
    }

    @PostMapping
    public Result<ClassInfo> createClass(@Valid @RequestBody ClassCreateRequest request) {
        return Result.success(classInfoService.createClass(request));
    }

    @PutMapping("/{semester}/{courseId}/{staffId}")
    public Result<ClassInfo> updateClass(@PathVariable String semester,
                                         @PathVariable String courseId,
                                         @PathVariable String staffId,
                                         @RequestBody ClassUpdateRequest request) {
        return Result.success(classInfoService.updateClass(semester, courseId, staffId, request));
    }
}
