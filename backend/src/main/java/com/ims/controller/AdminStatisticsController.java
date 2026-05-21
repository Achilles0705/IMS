package com.ims.controller;

import com.ims.common.Result;
import com.ims.service.AdminStatisticsService;
import com.ims.vo.AdminCourseGradeVO;
import com.ims.vo.AdminOverviewVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/statistics")
@RequiredArgsConstructor
public class AdminStatisticsController {

    private final AdminStatisticsService adminStatisticsService;

    @GetMapping("/overview")
    public Result<AdminOverviewVO> overview(@RequestHeader("X-Role") String role) {
        return Result.success(adminStatisticsService.getOverview(role));
    }

    @GetMapping("/course-grades")
    public Result<List<AdminCourseGradeVO>> courseGrades(@RequestHeader("X-Role") String role,
                                                         @RequestParam(required = false) String semester,
                                                         @RequestParam(required = false) String courseId) {
        return Result.success(adminStatisticsService.listCourseGrades(role, semester, courseId));
    }
}
