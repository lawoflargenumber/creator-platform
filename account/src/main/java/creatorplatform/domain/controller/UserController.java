
package creatorplatform.domain.controller;

import creatorplatform.domain.command.*;
import creatorplatform.domain.service.UserCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserCommandService service;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterUserCommand cmd) {
        service.handleRegisterUser(cmd);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/apply-authorship")
    public ResponseEntity<Void> applyForAuthorship(@RequestBody ApplyForAuthorshipCommand cmd) {
        service.handleApplyForAuthorship(cmd);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/start-subscription")
    public ResponseEntity<Void> startSubscribe(@RequestBody StartSubscribeCommand cmd) {
        service.handleStartSubscribe(cmd);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/accept-application")
    public ResponseEntity<Void> accept(@RequestBody AcceptApplicationCommand cmd) {
        service.handleAcceptApplication(cmd);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/decline-application")
    public ResponseEntity<Void> decline(@RequestBody DeclineApplicationCommand cmd) {
        service.handleDeclineApplication(cmd);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update-user")
    public ResponseEntity<Void> update(@RequestBody UpdateUserCommand cmd) {
        service.handleUpdateUser(cmd);
        return ResponseEntity.ok().build();
    }
}
