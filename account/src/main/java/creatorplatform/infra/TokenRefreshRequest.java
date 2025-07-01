package creatorplatform.infra;

import lombok.Data;

@Data
public class TokenRefreshRequest {
    private String refreshToken;
}
