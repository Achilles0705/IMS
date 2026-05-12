package com.ims.controller;

import com.ims.common.Result;
import com.ims.dto.CourseCreateRequest;
import com.ims.dto.CourseUpdateRequest;
import com.ims.entity.Course;
import com.ims.service.CourseService;
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
@RequestMapping("/api/admin/courses")
@RequiredArgsConstructor
public class AdminCourseController {

    private final CourseService courseService;

    @GetMapping
    public Result<List<Course>> listCourses(@RequestParam(required = false) String deptId,
                                            @RequestParam(required = false) String keyword) {
        return Result.success(courseService.listCourses(deptId, keyword));
    }

    @PostMapping
    public Result<Course> createCourse(@Valid @RequestBody CourseCreateRequest request) {
        return Result.success(courseService.createCourse(request));
    }

    @PutMapping("/{courseId}")
    public Result<Course> updateCourse(@PathVariable String courseId,
                                       @Valid @RequestBody CourseUpdateRequest request) {
        return Result.success(courseService.updateCourse(courseId, request));
    }
}
