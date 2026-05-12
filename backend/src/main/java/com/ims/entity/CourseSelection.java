package com.ims.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("course_selection")
public class CourseSelection {

    private String studentId;

    private String semester;

    private String courseId;

    private String staffId;

    private Integer score;
}
