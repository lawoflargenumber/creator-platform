package creatorplatform.controller;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String nickname;
    private Integer points;
    private Boolean marketingConsent; // 추가
}
