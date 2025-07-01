package creatorplatform.controller;

import creatorplatform.model.User;
import creatorplatform.repository.UserRepository;
import creatorplatform.repository.RefreshTokenRepository;
import creatorplatform.security.JwtUtils;
import creatorplatform.domain.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.UUID;

@RestController @RequestMapping("/auth") @RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepo;
    private final RefreshTokenRepository tokenRepo;
    private final JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        if (userRepo.existsByEmail(req.getEmail())) {
            return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("Email already in use");
        }
        User user = User.builder()
            .email(req.getEmail())
            .password(req.getPassword())
            .nickname(req.getNickname())
            .marketingConsent(Boolean.TRUE.equals(req.getMarketingConsent()))
            .isAuthor(false)
            .points(0)
            .build();
        User saved = userRepo.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
    // other methods unchanged
}
