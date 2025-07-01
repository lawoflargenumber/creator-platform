package creatorplatform.controller;

import creatorplatform.model.User;
import creatorplatform.domain.RefreshToken;
import creatorplatform.repository.UserRepository;
import creatorplatform.repository.RefreshTokenRepository;
import creatorplatform.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepo;
    private final RefreshTokenRepository tokenRepo;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        var userOpt = userRepo.findByEmail(req.getEmail());
        if (userOpt.isEmpty() || !userOpt.get().getPassword().equals(req.getPassword())) {
            return ResponseEntity.badRequest().body(null);
        }
        User user = userOpt.get();
        String access = jwtUtils.generateJwtToken(user.getEmail());
        String refresh = UUID.randomUUID().toString();
        tokenRepo.deleteByUserId(user.getId());
        tokenRepo.save(RefreshToken.builder()
            .user(user).token(refresh)
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusMillis(jwtUtils.getRefreshExpirationMs()))
            .revoked(false).build());

        // Return tokens along with user info
        return ResponseEntity.ok(new LoginResponse(access, refresh, user));
    }

    // ... existing /refresh and /logout methods unchanged ...
}
