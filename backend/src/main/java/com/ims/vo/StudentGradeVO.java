package com.ims.vo;

import lombok.Data;

@Data
public class StudentGradeVO {

    private String semester;

    private String courseId;

    private String courseName;

    private Integer credit;

    private String staffId;

    private String teacherName;

    private Integer score;

    private Boolean passed;
}
