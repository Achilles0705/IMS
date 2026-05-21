package com.ims.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "登录名不能为空")
    private String username;

    @NotBlank(message = "登录密码不能为空")
    private String password;

    @NotBlank(message = "登录角色不能为空")
    private String role;
}
