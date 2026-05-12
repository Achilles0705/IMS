package com.ims.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentCreateRequest {

    @NotBlank(message = "学生编号不能为空")
    private String studentId;

    @NotBlank(message = "学生姓名不能为空")
    private String name;

    private String sex;

    private LocalDate dateOfBirth;

    private String nativePlace;

    private String mobilePhone;

    private String deptId;

    private String status;
}
