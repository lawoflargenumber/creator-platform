package creatorplatform.infra;

import creatorplatform.domain.command.*;
import creatorplatform.domain.*;

import java.time.LocalDateTime;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import creatorplatform.domain.command.ApplyForAuthorshipCommand;
import creatorplatform.domain.command.DeclineApplicationCommand;
import creatorplatform.domain.service.UserCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//<<< Clean Arch / Inbound Adaptor

@RestController
// @RequestMapping(value="/users")
@Transactional
@RequiredArgsConstructor
public class UsersController {

    private final UsersRepository usersRepository;
    private final UserCommandService userCommandService;

    @RequestMapping(
        value = "/users/{id}/apply",
        method = RequestMethod.POST,
        produces = "application/json;charset=UTF-8"
    )
    public ResponseEntity<?> applyForAuthorship(
        @PathVariable(value = "id") Long id,
        @RequestBody ApplyForAuthorshipCommand applyForAuthorshipCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /users/applyForAuthorship  called #####");
        userCommandService.handleApplyForAuthorship(id, applyForAuthorshipCommand);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

//    @RequestMapping(
//        value = "/users/{id}/declineapplication",
//        method = RequestMethod.PUT,
//        produces = "application/json;charset=UTF-8"
//    )
//    public Users declineApplication(
//        @PathVariable(value = "id") Long id,
//        @RequestBody DeclineApplicationCommand declineApplicationCommand,
//        HttpServletRequest request,
//        HttpServletResponse response
//    ) throws Exception {
//        System.out.println("##### /users/declineApplication  called #####");
//        Optional<Users> optionalUsers = usersRepository.findById(id);
//
//        optionalUsers.orElseThrow(() -> new Exception("No Entity Found"));
//        Users users = optionalUsers.get();
//
//        usersRepository.save(users);
//        return users;
//    }

    @RequestMapping(
        value = "/users/{id}/subscribe",
        method = RequestMethod.POST,
        produces = "application/json;charset=UTF-8"
    )
    public ResponseEntity<?> startSubscribe(
        @PathVariable(value = "id") Long id,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        LocalDateTime expiresAt = userCommandService.handleStartSubscribe(id);
        return ResponseEntity.ok(expiresAt);
    }
}
//>>> Clean Arch / Inbound Adaptor
