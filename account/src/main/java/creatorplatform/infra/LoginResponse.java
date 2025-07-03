package creatorplatform.infra;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private LoginUserDto user;  // JWT 사용시 User 제거
} 