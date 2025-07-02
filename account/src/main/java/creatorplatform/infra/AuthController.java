package creatorplatform.infra;

import creatorplatform.domain.Users;
import creatorplatform.domain.RefreshToken;
import creatorplatform.domain.UsersRepository;
import creatorplatform.domain.repository.RefreshTokenRepository;
import creatorplatform.domain.RegisterUserCommand;
import creatorplatform.domain.security.JwtUtils;
import creatorplatform.domain.service.UserCommandService;
import creatorplatform.infra.LoginRequest;
import creatorplatform.domain.controller.JwtResponse;
import creatorplatform.domain.controller.TokenRefreshRequest;
import creatorplatform.infra.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
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
    private final UserCommandService userCommandService;

    @PostMapping("/register")
    public ResponseEntity<Users> register(@RequestBody RegisterUserCommand req) {
        if (usersRepository.findByAccountId(req.getAccountId()).isPresent()) {
            return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(null);
        }
        userCommandService.handleRegisterUser(req);
        // 사용자 생성 확인
        usersRepository.findByAccountId(req.getAccountId())
            .orElseThrow(() -> new RuntimeException("User creation failed"));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = userCommandService.handleLogin(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody TokenRefreshRequest request) {
        try {
            JwtResponse response = userCommandService.handleRefreshToken(request.getRefreshToken());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Invalid refresh token");
        }
    }

    // jwt 사용하지 않는 로그아웃을 어떻게 구현해야할지 논의가 필요함.
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