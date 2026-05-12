package com.ims.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@TableName("teacher")
public class Teacher {

    @TableId(value = "staff_id", type = IdType.INPUT)
    private String staffId;

    private String name;

    private String sex;

    private LocalDate dateOfBirth;

    private String professionalTitle;

    private BigDecimal salary;

    private String deptId;
}
