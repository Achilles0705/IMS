package com.ims.service;

import com.ims.vo.TeacherClassStudentVO;
import com.ims.vo.TeacherClassVO;
import com.ims.vo.TeacherStatisticsVO;

import java.util.List;

public interface TeacherPortalService {

    List<TeacherClassVO> listClasses(String role, String staffId, String semester);

    List<TeacherClassStudentVO> listClassStudents(String role, String staffId, String semester, String courseId);

    TeacherStatisticsVO getStatistics(String role, String staffId, String semester);
}
