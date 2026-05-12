package com.ims.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ims.common.BusinessException;
import com.ims.dto.CourseCreateRequest;
import com.ims.dto.CourseUpdateRequest;
import com.ims.entity.Course;
import com.ims.mapper.CourseMapper;
import com.ims.service.CourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Override
    public List<Course> listCourses(String deptId, String keyword) {
        LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<Course>()
                .eq(StringUtils.hasText(deptId), Course::getDeptId, deptId)
                .and(StringUtils.hasText(keyword), query -> query
                        .like(Course::getCourseId, keyword)
                        .or()
                        .like(Course::getCourseName, keyword))
                .orderByAsc(Course::getCourseId);
        return list(wrapper);
    }

    @Override
    public Course createCourse(CourseCreateRequest request) {
        if (getById(request.getCourseId()) != null) {
            throw new BusinessException("课程编号已存在");
        }
        Course course = new Course();
        BeanUtils.copyProperties(request, course);
        save(course);
        return course;
    }

    @Override
    public Course updateCourse(String courseId, CourseUpdateRequest request) {
        Course existing = getById(courseId);
        if (existing == null) {
            throw new BusinessException(404, "课程不存在");
        }
        Course course = new Course();
        BeanUtils.copyProperties(request, course);
        course.setCourseId(courseId);
        updateById(course);
        return getById(courseId);
    }
}
