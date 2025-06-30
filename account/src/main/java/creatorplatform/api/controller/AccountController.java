package creatorplatform.api.controller;

import creatorplatform.domain.command.RegisterUserCommand;
import creatorplatform.domain.command.UpdateUserCommand;
import creatorplatform.domain.service.UserCommandService;
import lombok.RequiredArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {
    private final UserCommandService userService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterRequest req) {
        userService.registerUser(req.toCommand());
        return ResponseEntity.status(201).build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Valid UpdateRequest req) {
        userService.updateUser(req.toCommand());
        return ResponseEntity.noContent().build();
    }
}

@Data
class RegisterRequest {
    private Long id;
    private String nickname;
    private String password;
    private boolean agreedToMarketing;
    RegisterUserCommand toCommand() {
        return new RegisterUserCommand(id, nickname, password, agreedToMarketing);
    }
}

@Data
class UpdateRequest {
    private Long id;
    private String nickname;
    private String password;
    private boolean agreedToMarketing;
    UpdateUserCommand toCommand() {
        return new UpdateUserCommand(id, nickname, password, agreedToMarketing);
    }
}
