package creatorplatform.domain.controller;

import creatorplatform.domain.Users;
import creatorplatform.domain.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/users") @RequiredArgsConstructor
public class UserController {
    private final UsersRepository userRepo;

    @GetMapping("/me")
    public ResponseEntity<Users> getMe(@RequestHeader("X-User-Id") String userId) {
        // Gateway에서 전달받은 사용자 ID로 사용자 조회
        Users user = userRepo.findByAccountId(userId).orElseThrow(
            () -> new RuntimeException("User not found: " + userId)
        );
        return ResponseEntity.ok(user);
    }
    // 
    // @PutMapping("/me")
    // public ResponseEntity<Users> updateMe(@RequestHeader("X-User-Id") String userId,
    //     @RequestBody UpdateUserRequest req) {
    //     Users u = userRepo.findByAccountId(userId).orElseThrow();
    //     if (req.getNickname() != null) u.setNickname(req.getNickname());
    //     if (req.getPoints() != null) u.setPoints(req.getPoints());
    //     if (req.getMarketingConsent() != null) u.setMarketingConsent(req.getMarketingConsent());
    //     return ResponseEntity.ok(userRepo.save(u));
    // }
}