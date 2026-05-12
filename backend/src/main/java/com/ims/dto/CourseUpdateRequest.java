package com.ims.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CourseUpdateRequest {

    private String courseId;

    @NotBlank(message = "课程名称不能为空")
    private String courseName;

    private Integer credit;

    private Integer creditHours;

    private String deptId;
}
