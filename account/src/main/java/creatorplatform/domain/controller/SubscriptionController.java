package creatorplatform.controller;
import creatorplatform.model.Subscription;
import creatorplatform.repository.SubscriptionRepository;
import creatorplatform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/users/me/subscription") @RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionRepository subRepo;
    private final UserRepository userRepo;
    @GetMapping public ResponseEntity<Subscription> getMySub(
        @AuthenticationPrincipal UserPrincipal p){
        var u=userRepo.findByEmail(p.getUsername()).orElseThrow();
        var s=subRepo.findByUserId(u.getId()).orElseThrow();
        return ResponseEntity.ok(s);
    }
    @PostMapping("/start") public ResponseEntity<Subscription> startSub(
        @AuthenticationPrincipal UserPrincipal p,
        @RequestBody StartSubscriptionRequest req){
        var u=userRepo.findByEmail(p.getUsername()).orElseThrow();
        var s=Subscription.builder().user(u).plan(req.getPlan())
            .expiresAt(req.calculateExpiry()).build();
        return ResponseEntity.ok(subRepo.save(s));
    }
}
