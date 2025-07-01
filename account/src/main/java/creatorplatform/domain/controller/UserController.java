package creatorplatform.domain.controller;

import creatorplatform.domain.Users;
import creatorplatform.domain.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import creatorplatform.domain.security.UserPrincipal;

@RestController @RequestMapping("/users") @RequiredArgsConstructor
public class UserController {
    private final UsersRepository userRepo;

    @GetMapping("/me")
    public ResponseEntity<Users> getMe(@AuthenticationPrincipal UserPrincipal p) {
        Users user = userRepo.findByEmail(p.getUsername()).orElseThrow();
        return ResponseEntity.ok(user);
    }
    // 
    // @PutMapping("/me")
    // public ResponseEntity<Users> updateMe(@AuthenticationPrincipal UserPrincipal p,
    //     @RequestBody UpdateUserRequest req) {
    //     Users u = userRepo.findByEmail(p.getUsername()).orElseThrow();
    //     if (req.getNickname() != null) u.setNickname(req.getNickname());
    //     if (req.getPoints() != null) u.setPoints(req.getPoints());
    //     if (req.getMarketingConsent() != null) u.setMarketingConsent(req.getMarketingConsent());
    //     return ResponseEntity.ok(userRepo.save(u));
    // }
}