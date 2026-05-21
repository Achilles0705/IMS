package com.ims.service;

import com.ims.vo.StudentClassVO;
import com.ims.vo.StudentCreditSummaryVO;
import com.ims.vo.StudentGradeVO;
import com.ims.vo.StudentSelectionVO;
import com.ims.vo.StudentStatisticsVO;

import java.util.List;

public interface StudentPortalService {

    List<StudentClassVO> listClasses(String role, String studentId, String semester);

    List<StudentSelectionVO> listSelections(String role, String studentId, String semester);

    List<StudentGradeVO> listGrades(String role, String studentId, String semester);

    StudentStatisticsVO getStatistics(String role, String studentId);

    StudentCreditSummaryVO getCreditSummary(String role, String studentId);
}
