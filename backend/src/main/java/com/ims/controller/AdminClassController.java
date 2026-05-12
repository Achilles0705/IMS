package com.ims.controller;

import com.ims.common.Result;
import com.ims.entity.ClassInfo;
import com.ims.service.ClassInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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
}
