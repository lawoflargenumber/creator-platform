package creatorplatform.domain.controller;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private String nickname;
    // 새 필드
    private Boolean marketingConsent;
}
