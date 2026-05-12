package com.ims.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ims.dto.CourseCreateRequest;
import com.ims.dto.CourseUpdateRequest;
import com.ims.entity.Course;

import java.util.List;

public interface CourseService extends IService<Course> {

    List<Course> listCourses(String deptId, String keyword);

    Course createCourse(CourseCreateRequest request);

    Course updateCourse(String courseId, CourseUpdateRequest request);
}
