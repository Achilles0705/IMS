package com.ims.vo;

import lombok.Data;

@Data
public class StudentClassVO {

    private String semester;

    private String courseId;

    private String courseName;

    private Integer credit;

    private Integer creditHours;

    private String staffId;

    private String teacherName;

    private String classTime;

    private Boolean selected;
}
