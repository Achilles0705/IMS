package com.ims.vo;

import lombok.Data;

@Data
public class LoginProfileVO {

    private String userId;

    private String username;

    private String role;

    private String relatedId;

    private String displayName;
}
