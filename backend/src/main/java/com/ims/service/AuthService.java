package com.ims.service;

import com.ims.dto.LoginRequest;
import com.ims.vo.LoginProfileVO;

public interface AuthService {

    LoginProfileVO login(LoginRequest request);

    LoginProfileVO profile(String userId, String role, String relatedId);
}
