package com.ims.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StudentSelectionRequest {

    @NotBlank(message = "学期不能为空")
    private String semester;

    @NotBlank(message = "课程编号不能为空")
    private String courseId;

    @NotBlank(message = "教师编号不能为空")
    private String staffId;
}
