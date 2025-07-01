package creatorplatform.controller;

import creatorplatform.model.User;
import creatorplatform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/users") @RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepo;

    @GetMapping("/me")
    public ResponseEntity<User> getMe(@AuthenticationPrincipal UserPrincipal p) {
        var user = userRepo.findByEmail(p.getUsername()).orElseThrow();
        return ResponseEntity.ok(user);
    }

    @PutMapping("/me")
    public ResponseEntity<User> updateMe(@AuthenticationPrincipal UserPrincipal p,
        @RequestBody UpdateUserRequest req) {
        var u = userRepo.findByEmail(p.getUsername()).orElseThrow();
        if (req.getNickname() != null) u.setNickname(req.getNickname());
        if (req.getPoints() != null) u.setPoints(req.getPoints());
        if (req.getMarketingConsent() != null) u.setMarketingConsent(req.getMarketingConsent());
        return ResponseEntity.ok(userRepo.save(u));
    }
}
