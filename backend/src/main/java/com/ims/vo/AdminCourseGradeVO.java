package com.ims.vo;

import lombok.Data;

@Data
public class AdminCourseGradeVO {

    private String semester;

    private String courseId;

    private String courseName;

    private Long studentCount;

    private Long gradedCount;

    private Double averageScore;

    private Integer maxScore;

    private Integer minScore;

    private Long failedCount;
}
