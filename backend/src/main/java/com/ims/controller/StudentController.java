package com.ims.controller;

import com.ims.common.Result;
import com.ims.dto.StudentSelectionRequest;
import com.ims.entity.CourseSelection;
import com.ims.service.CourseSelectionService;
import com.ims.service.StudentPortalService;
import com.ims.vo.StudentClassVO;
import com.ims.vo.StudentCreditSummaryVO;
import com.ims.vo.StudentGradeVO;
import com.ims.vo.StudentSelectionVO;
import com.ims.vo.StudentStatisticsVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentPortalService studentPortalService;
    private final CourseSelectionService courseSelectionService;

    @GetMapping("/classes")
    public Result<List<StudentClassVO>> listClasses(@RequestHeader("X-Role") String role,
                                                    @RequestHeader("X-Related-Id") String relatedId,
                                                    @RequestParam String semester) {
        return Result.success(studentPortalService.listClasses(role, relatedId, semester));
    }

    @GetMapping("/selections")
    public Result<List<StudentSelectionVO>> listSelections(@RequestHeader("X-Role") String role,
                                                           @RequestHeader("X-Related-Id") String relatedId,
                                                           @RequestParam(required = false) String semester) {
        return Result.success(studentPortalService.listSelections(role, relatedId, semester));
    }

    @PostMapping("/selections")
    public Result<CourseSelection> selectCourse(@RequestHeader("X-Role") String role,
                                                @RequestHeader("X-Related-Id") String relatedId,
                                                @Valid @RequestBody StudentSelectionRequest request) {
        return Result.success(courseSelectionService.selectCourse(role, relatedId, request));
    }

    @DeleteMapping("/selections")
    public Result<Map<String, Boolean>> dropCourse(@RequestHeader("X-Role") String role,
                                                   @RequestHeader("X-Related-Id") String relatedId,
                                                   @Valid @RequestBody StudentSelectionRequest request) {
        boolean deleted = courseSelectionService.dropCourse(role, relatedId, request);
        return Result.success(Map.of("deleted", deleted));
    }

    @GetMapping("/grades")
    public Result<List<StudentGradeVO>> listGrades(@RequestHeader("X-Role") String role,
                                                   @RequestHeader("X-Related-Id") String relatedId,
                                                   @RequestParam(required = false) String semester) {
        return Result.success(studentPortalService.listGrades(role, relatedId, semester));
    }

    @GetMapping("/statistics")
    public Result<StudentStatisticsVO> statistics(@RequestHeader("X-Role") String role,
                                                  @RequestHeader("X-Related-Id") String relatedId) {
        return Result.success(studentPortalService.getStatistics(role, relatedId));
    }

    @GetMapping("/credit-summary")
    public Result<StudentCreditSummaryVO> creditSummary(@RequestHeader("X-Role") String role,
                                                        @RequestHeader("X-Related-Id") String relatedId) {
        return Result.success(studentPortalService.getCreditSummary(role, relatedId));
    }
}
