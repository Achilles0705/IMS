package com.ims.vo;

import lombok.Data;

@Data
public class TeacherStatisticsVO {

    private Long classCount;

    private Long studentCount;

    private Long gradedCount;

    private Long ungradedCount;

    private Double averageScore;

    private Long failedCount;
}
