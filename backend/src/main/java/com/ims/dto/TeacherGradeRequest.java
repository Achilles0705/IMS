package com.ims.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TeacherGradeRequest {

    @NotBlank(message = "学生编号不能为空")
    private String studentId;

    @NotBlank(message = "学期不能为空")
    private String semester;

    @NotBlank(message = "课程编号不能为空")
    private String courseId;

    @NotNull(message = "成绩不能为空")
    @Min(value = 0, message = "成绩不能小于0")
    @Max(value = 100, message = "成绩不能大于100")
    private Integer score;
}
