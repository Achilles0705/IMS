package com.ims.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;

@Data
@TableName("student")
public class Student {

    @TableId(value = "student_id", type = IdType.INPUT)
    private String studentId;

    private String name;

    private String sex;

    private LocalDate dateOfBirth;

    private String nativePlace;

    private String mobilePhone;

    private String deptId;

    @TableField("Status")
    private String status;
}
