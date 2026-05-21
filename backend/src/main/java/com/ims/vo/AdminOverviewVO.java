package com.ims.vo;

import lombok.Data;

@Data
public class AdminOverviewVO {

    private Long departmentCount;

    private Long studentCount;

    private Long teacherCount;

    private Long courseCount;

    private Long classCount;

    private Long selectionCount;
}
