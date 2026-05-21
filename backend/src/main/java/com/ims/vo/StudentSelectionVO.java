package com.ims.vo;

import lombok.Data;

@Data
public class StudentSelectionVO {

    private String studentId;

    private String semester;

    private String courseId;

    private String courseName;

    private Integer credit;

    private String staffId;

    private String teacherName;

    private String classTime;

    private Integer score;
}
