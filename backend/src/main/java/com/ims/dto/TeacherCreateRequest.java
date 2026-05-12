package com.ims.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TeacherCreateRequest {

    @NotBlank(message = "教师编号不能为空")
    private String staffId;

    @NotBlank(message = "教师姓名不能为空")
    private String name;

    private String sex;

    private LocalDate dateOfBirth;

    private String professionalTitle;

    private BigDecimal salary;

    private String deptId;
}
