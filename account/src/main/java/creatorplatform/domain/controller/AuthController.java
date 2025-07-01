package creatorplatform.domain.controller;

import creatorplatform.domain.model.User;
import creatorplatform.domain.repository.UserRepository;
import creatorplatform.domain.repository.RefreshTokenRepository;
import creatorplatform.domain.security.JwtUtils;
import creatorplatform.domain.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.UUID;
import creatorplatform.domain.controller.RegisterRequest;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepo;
    private final RefreshTokenRepository tokenRepo;
    private final JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest req) {
        if (userRepo.findByEmail(req.getEmail()).isPresent()) {
            return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(null);
        }
        // 포인트 지급 로직: 동의 시 5000, 미동의 시 1000
        int initialPoints = Boolean.TRUE.equals(req.getMarketingConsent()) ? 5000 : 1000;
        User user = User.builder()
            .email(req.getEmail())
            .password(req.getPassword())
            .nickname(req.getNickname())
            .marketingConsent(Boolean.TRUE.equals(req.getMarketingConsent()))
            .isAuthor(false)
            .points(initialPoints)
            .build();
        User saved = userRepo.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
    // 기존 로그인, refresh, logout 메서드...
}