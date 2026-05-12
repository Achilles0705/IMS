package com.ims.controller;

import com.ims.common.Result;
import com.ims.entity.Course;
import com.ims.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public Result<List<Course>> listCourses(@RequestParam(required = false) String deptId,
                                            @RequestParam(required = false) String keyword) {
        return Result.success(courseService.listCourses(deptId, keyword));
    }
}
