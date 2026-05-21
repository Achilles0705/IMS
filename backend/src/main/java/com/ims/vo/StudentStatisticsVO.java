package com.ims.vo;

import lombok.Data;

@Data
public class StudentStatisticsVO {

    private Long selectedCourseCount;

    private Long gradedCourseCount;

    private Long passedCourseCount;

    private Long failedCourseCount;

    private Integer earnedCredits;

    private Double averageScore;
}
