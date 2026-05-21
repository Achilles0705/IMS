package com.ims.controller;

import com.ims.common.Result;
import com.ims.dto.TeacherGradeRequest;
import com.ims.entity.CourseSelection;
import com.ims.service.CourseSelectionService;
import com.ims.service.TeacherPortalService;
import com.ims.vo.TeacherClassStudentVO;
import com.ims.vo.TeacherClassVO;
import com.ims.vo.TeacherStatisticsVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherPortalService teacherPortalService;
    private final CourseSelectionService courseSelectionService;

    @GetMapping("/classes")
    public Result<List<TeacherClassVO>> listClasses(@RequestHeader("X-Role") String role,
                                                    @RequestHeader("X-Related-Id") String relatedId,
                                                    @RequestParam(required = false) String semester) {
        return Result.success(teacherPortalService.listClasses(role, relatedId, semester));
    }

    @GetMapping("/classes/{semester}/{courseId}/students")
    public Result<List<TeacherClassStudentVO>> listClassStudents(@RequestHeader("X-Role") String role,
                                                                 @RequestHeader("X-Related-Id") String relatedId,
                                                                 @PathVariable String semester,
                                                                 @PathVariable String courseId) {
        return Result.success(teacherPortalService.listClassStudents(role, relatedId, semester, courseId));
    }

    @PutMapping("/grades")
    public Result<CourseSelection> updateGrade(@RequestHeader("X-Role") String role,
                                               @RequestHeader("X-Related-Id") String relatedId,
                                               @Valid @RequestBody TeacherGradeRequest request) {
        return Result.success(courseSelectionService.updateGrade(role, relatedId, request));
    }

    @GetMapping("/statistics")
    public Result<TeacherStatisticsVO> statistics(@RequestHeader("X-Role") String role,
                                                  @RequestHeader("X-Related-Id") String relatedId,
                                                  @RequestParam(required = false) String semester) {
        return Result.success(teacherPortalService.getStatistics(role, relatedId, semester));
    }
}
