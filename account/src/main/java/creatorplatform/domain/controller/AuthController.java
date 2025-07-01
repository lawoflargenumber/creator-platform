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

@RestController @RequestMapping("/auth") @RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepo;
    private final RefreshTokenRepository tokenRepo;
    private final JwtUtils jwtUtils;
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req){
        var userOpt = userRepo.findByEmail(req.getEmail());
        if(userOpt.isEmpty()||!userOpt.get().getPassword().equals(req.getPassword()))
            return ResponseEntity.badRequest().body("Invalid credentials");
        var user=userOpt.get();
        String access=jwtUtils.generateJwtToken(user.getEmail());
        String refresh=UUID.randomUUID().toString();
        tokenRepo.deleteByUserId(user.getId());
        tokenRepo.save(RefreshToken.builder()
            .user(user).token(refresh)
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusMillis(jwtUtils.getRefreshExpirationMs()))
            .revoked(false).build());
        return ResponseEntity.ok(new JwtResponse(access,refresh));
    }
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody TokenRefreshRequest req){
        var rt=tokenRepo.findByToken(req.getRefreshToken()).orElse(null);
        if(rt==null||rt.isRevoked()||rt.getExpiresAt().isBefore(Instant.now()))
            return ResponseEntity.badRequest().body("Invalid refresh token");
        String access=jwtUtils.generateJwtToken(rt.getUser().getEmail());
        String refresh=UUID.randomUUID().toString();
        rt.setToken(refresh); rt.setIssuedAt(Instant.now());
        rt.setExpiresAt(Instant.now().plusMillis(jwtUtils.getRefreshExpirationMs()));
        tokenRepo.save(rt);
        return ResponseEntity.ok(new JwtResponse(access,refresh));
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody TokenRefreshRequest req){
        tokenRepo.findByToken(req.getRefreshToken()).ifPresent(rt->{
            rt.setRevoked(true); tokenRepo.save(rt);
        });
        return ResponseEntity.ok("Logged out");
    }
}
