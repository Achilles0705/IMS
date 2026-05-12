package com.ims.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("course")
public class Course {

    @TableId(value = "course_id", type = IdType.INPUT)
    private String courseId;

    private String courseName;

    private Integer credit;

    private Integer creditHours;

    private String deptId;
}
