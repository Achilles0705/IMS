package com.ims.service.impl;

import com.ims.common.BusinessException;
import com.ims.dto.LoginRequest;
import com.ims.entity.Student;
import com.ims.entity.Teacher;
import com.ims.mapper.StudentMapper;
import com.ims.mapper.TeacherMapper;
import com.ims.service.AuthService;
import com.ims.util.RoleCheckUtil;
import com.ims.vo.LoginProfileVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";

    private final StudentMapper studentMapper;
    private final TeacherMapper teacherMapper;

    @Override
    public LoginProfileVO login(LoginRequest request) {
        String role = request.getRole().trim().toUpperCase();
        return switch (role) {
            case "ADMIN" -> adminLogin(request);
            case "STUDENT" -> studentLogin(request);
            case "TEACHER" -> teacherLogin(request);
            default -> throw new BusinessException(400, "不支持的登录角色");
        };
    }

    @Override
    public LoginProfileVO profile(String userId, String role, String relatedId) {
        if (!StringUtils.hasText(userId)) {
            throw new BusinessException(401, "缺少用户ID");
        }
        String normalizedRole = role == null ? "" : role.trim().toUpperCase();
        return switch (normalizedRole) {
            case "ADMIN" -> buildAdminProfile();
            case "STUDENT" -> buildStudentProfile(userId, RoleCheckUtil.requireRelatedId(relatedId));
            case "TEACHER" -> buildTeacherProfile(userId, RoleCheckUtil.requireRelatedId(relatedId));
            default -> throw new BusinessException(400, "不支持的角色信息");
        };
    }

    private LoginProfileVO adminLogin(LoginRequest request) {
        if (!ADMIN_USERNAME.equalsIgnoreCase(request.getUsername())
                || !ADMIN_PASSWORD.equals(request.getPassword())) {
            throw new BusinessException(400, "账号或密码错误");
        }
        return buildAdminProfile();
    }

    private LoginProfileVO studentLogin(LoginRequest request) {
        Student student = studentMapper.selectById(request.getUsername());
        if (student == null) {
            throw new BusinessException(400, "账号或密码错误");
        }
        return buildStudentProfile("U" + student.getStudentId(), student.getStudentId());
    }

    private LoginProfileVO teacherLogin(LoginRequest request) {
        Teacher teacher = teacherMapper.selectById(request.getUsername());
        if (teacher == null) {
            throw new BusinessException(400, "账号或密码错误");
        }
        return buildTeacherProfile("U" + teacher.getStaffId(), teacher.getStaffId());
    }

    private LoginProfileVO buildAdminProfile() {
        LoginProfileVO vo = new LoginProfileVO();
        vo.setUserId("UADMIN");
        vo.setUsername(ADMIN_USERNAME);
        vo.setRole("ADMIN");
        vo.setRelatedId(null);
        vo.setDisplayName("系统管理员");
        return vo;
    }

    private LoginProfileVO buildStudentProfile(String userId, String studentId) {
        Student student = studentMapper.selectById(studentId);
        if (student == null) {
            throw new BusinessException(404, "学生不存在");
        }
        LoginProfileVO vo = new LoginProfileVO();
        vo.setUserId(userId);
        vo.setUsername(studentId);
        vo.setRole("STUDENT");
        vo.setRelatedId(studentId);
        vo.setDisplayName(student.getName());
        return vo;
    }

    private LoginProfileVO buildTeacherProfile(String userId, String staffId) {
        Teacher teacher = teacherMapper.selectById(staffId);
        if (teacher == null) {
            throw new BusinessException(404, "教师不存在");
        }
        LoginProfileVO vo = new LoginProfileVO();
        vo.setUserId(userId);
        vo.setUsername(staffId);
        vo.setRole("TEACHER");
        vo.setRelatedId(staffId);
        vo.setDisplayName(teacher.getName());
        return vo;
    }
}
