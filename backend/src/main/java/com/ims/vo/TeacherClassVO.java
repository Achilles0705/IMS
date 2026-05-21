package com.ims.vo;

import lombok.Data;

@Data
public class TeacherClassVO {

    private String semester;

    private String courseId;

    private String courseName;

    private Integer credit;

    private String staffId;

    private String classTime;

    private Long studentCount;
}
