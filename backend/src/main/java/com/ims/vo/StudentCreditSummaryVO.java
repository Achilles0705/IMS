package com.ims.vo;

import lombok.Data;

@Data
public class StudentCreditSummaryVO {

    private String studentId;

    private Long passedCourseCount;

    private Integer earnedCredits;

    private Double averageScore;
}
