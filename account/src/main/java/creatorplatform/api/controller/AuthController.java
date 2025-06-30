package creatorplatform.api.controller;

import creatorplatform.domain.exception.InvalidTokenException;
import creatorplatform.domain.model.RefreshToken;
import creatorplatform.domain.repository.RefreshTokenRepository;
import creatorplatform.domain.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import lombok.Data;

import java.time.Instant;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final TokenService tokenService;
    private final RefreshTokenRepository refreshRepo;
    private final org.springframework.security.authentication.AuthenticationManager authManager;
    private final org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthRequest req) {
        var authToken = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword());
        authManager.authenticate(authToken);

        String access = tokenService.generateAccessToken(req.getUsername());
        String refresh = tokenService.generateRefreshToken(req.getUsername());
        refreshRepo.deleteByUsername(req.getUsername());
        refreshRepo.save(RefreshToken.builder()
                .username(req.getUsername())
                .token(refresh)
                .expiresAt(Instant.now().plusSeconds(refreshExpiry()))
                .build());

        return ResponseEntity.ok(new AuthResponse(access, refresh));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody @Valid RefreshRequest req) {
        RefreshToken stored = refreshRepo.findByToken(req.getRefreshToken())
                .orElseThrow(() -> new InvalidTokenException("Invalid refresh token"));
        if (stored.isExpired()) {
            refreshRepo.delete(stored);
            throw new InvalidTokenException("Refresh token expired");
        }
        String newAccess = tokenService.generateAccessToken(stored.getUsername());
        return ResponseEntity.ok(new AuthResponse(newAccess, req.getRefreshToken()));
    }

    private long refreshExpiry() {
        return tokenService.getRefreshExpiry();
    }
}

@Data
class AuthRequest { 
    private String username; 
    private String password; 
}

@Data
class RefreshRequest { 
    private String refreshToken; 
}

@Data
class AuthResponse { 
    private final String accessToken; 
    private final String refreshToken; 
}
