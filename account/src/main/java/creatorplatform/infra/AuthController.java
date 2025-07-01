package creatorplatform.infra;

import creatorplatform.domain.Users;
import creatorplatform.domain.RefreshToken;
import creatorplatform.domain.repository.UsersRepository;
import creatorplatform.domain.repository.RefreshTokenRepository;
import creatorplatform.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.UUID;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UsersRepository usersRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<Users> userOpt = usersRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty() || !userOpt.get().getPassword().equals(request.getPassword())) {
            return ResponseEntity.badRequest().body("Invalid credentials");
        }
        Users user = userOpt.get();
        String accessToken = jwtUtils.generateJwtToken(user.getEmail());
        String refreshTokenStr = UUID.randomUUID().toString();
        RefreshToken rt = RefreshToken.builder()
                .user(user)
                .token(refreshTokenStr)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusMillis(jwtUtils.getRefreshExpirationMs()))
                .revoked(false)
                .build();
        refreshTokenRepository.deleteByUserId(user.getId());
        refreshTokenRepository.save(rt);
        return ResponseEntity.ok(new JwtResponse(accessToken, refreshTokenStr));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody TokenRefreshRequest request) {
        Optional<RefreshToken> rtOpt = refreshTokenRepository.findByToken(request.getRefreshToken());
        if (rtOpt.isEmpty() || rtOpt.get().isRevoked() || rtOpt.get().getExpiresAt().isBefore(Instant.now())) {
            return ResponseEntity.badRequest().body("Invalid refresh token");
        }
        RefreshToken rt = rtOpt.get();
        String newAccessToken = jwtUtils.generateJwtToken(rt.getUser().getEmail());
        String newRefreshTokenStr = UUID.randomUUID().toString();
        rt.setToken(newRefreshTokenStr);
        rt.setIssuedAt(Instant.now());
        rt.setExpiresAt(Instant.now().plusMillis(jwtUtils.getRefreshExpirationMs()));
        refreshTokenRepository.save(rt);
        return ResponseEntity.ok(new JwtResponse(newAccessToken, newRefreshTokenStr));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody TokenRefreshRequest request) {
        Optional<RefreshToken> rtOpt = refreshTokenRepository.findByToken(request.getRefreshToken());
        rtOpt.ifPresent(rt -> {
            rt.setRevoked(true);
            refreshTokenRepository.save(rt);
        });
        return ResponseEntity.ok("Logged out");
    }
}
