package com.ims.controller;

import com.ims.common.Result;
import com.ims.dto.LoginRequest;
import com.ims.service.AuthService;
import com.ims.vo.LoginProfileVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Result<LoginProfileVO> login(@Valid @RequestBody LoginRequest request) {
        return Result.success(authService.login(request));
    }

    @GetMapping("/profile")
    public Result<LoginProfileVO> profile(@RequestHeader("X-User-Id") String userId,
                                          @RequestHeader("X-Role") String role,
                                          @RequestHeader(value = "X-Related-Id", required = false) String relatedId) {
        return Result.success(authService.profile(userId, role, relatedId));
    }
}
