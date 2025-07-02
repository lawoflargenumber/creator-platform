package creatorplatform.infra;

import lombok.Data;

@Data
public class LoginRequest {
    private String accountId;
    private String password;
}
