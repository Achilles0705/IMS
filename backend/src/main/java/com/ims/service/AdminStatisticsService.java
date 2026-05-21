package com.ims.service;

import com.ims.vo.AdminCourseGradeVO;
import com.ims.vo.AdminOverviewVO;

import java.util.List;

public interface AdminStatisticsService {

    AdminOverviewVO getOverview(String role);

    List<AdminCourseGradeVO> listCourseGrades(String role, String semester, String courseId);
}
