package com.ims.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ims.dto.StudentSelectionRequest;
import com.ims.dto.TeacherGradeRequest;
import com.ims.entity.CourseSelection;

public interface CourseSelectionService extends IService<CourseSelection> {

    CourseSelection selectCourse(String role, String studentId, StudentSelectionRequest request);

    boolean dropCourse(String role, String studentId, StudentSelectionRequest request);

    CourseSelection updateGrade(String role, String staffId, TeacherGradeRequest request);
}
