package creatorplatform.domain.controller;
import lombok.AllArgsConstructor;
import lombok.Data;
@Data @AllArgsConstructor
public class JwtResponse{
    private String accessToken;
    private String refreshToken;
}