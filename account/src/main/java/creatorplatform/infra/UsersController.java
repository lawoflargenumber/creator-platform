package creatorplatform.infra;

import creatorplatform.domain.command.RegisterUserCommand;
import creatorplatform.domain.*;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import creatorplatform.domain.service.UserCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//<<< Clean Arch / Inbound Adaptor

@RestController
// @RequestMapping(value="/users")
@Transactional
@RequiredArgsConstructor
public class UsersController {

    private final UserCommandService userCommandService;

    @Autowired
    UsersRepository usersRepository;

    @RequestMapping(
        value = "/users/register",
        method = RequestMethod.POST,
        produces = "application/json;charset=UTF-8"
    )
    public Users registerUser(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody RegisterUserCommand registerUserCommand
    ) throws Exception {
        System.out.println("##### /users/registerUser  called #####");

        return userCommandService.handleRegisterUser(registerUserCommand);
    }

    @RequestMapping(
        value = "/users/{id}/applyforauthorship",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public Users applyForAuthorship(
        @PathVariable(value = "id") Long id,
        @RequestBody ApplyForAuthorshipCommand applyForAuthorshipCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /users/applyForAuthorship  called #####");
        Optional<Users> optionalUsers = usersRepository.findById(id);

        optionalUsers.orElseThrow(() -> new Exception("No Entity Found"));
        Users users = optionalUsers.get();
        users.applyForAuthorship(applyForAuthorshipCommand);

        usersRepository.save(users);
        return users;
    }

    @RequestMapping(
        value = "/users/{id}/declineapplication",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public Users declineApplication(
        @PathVariable(value = "id") Long id,
        @RequestBody DeclineApplicationCommand declineApplicationCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /users/declineApplication  called #####");
        Optional<Users> optionalUsers = usersRepository.findById(id);

        optionalUsers.orElseThrow(() -> new Exception("No Entity Found"));
        Users users = optionalUsers.get();
        users.declineApplication(declineApplicationCommand);

        usersRepository.save(users);
        return users;
    }

    @RequestMapping(
        value = "/users/{id}/startsubscribe",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public Users startSubscribe(
        @PathVariable(value = "id") Long id,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /users/startSubscribe  called #####");
        Optional<Users> optionalUsers = usersRepository.findById(id);

        optionalUsers.orElseThrow(() -> new Exception("No Entity Found"));
        Users users = optionalUsers.get();
        users.startSubscribe();

        usersRepository.save(users);
        return users;
    }

    @RequestMapping(
        value = "/users/{id}/accpetapplication",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public Users accpetApplication(
        @PathVariable(value = "id") Long id,
        @RequestBody AccpetApplicationCommand accpetApplicationCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /users/accpetApplication  called #####");
        Optional<Users> optionalUsers = usersRepository.findById(id);

        optionalUsers.orElseThrow(() -> new Exception("No Entity Found"));
        Users users = optionalUsers.get();
        users.accpetApplication(accpetApplicationCommand);

        usersRepository.save(users);
        return users;
    }
}
//>>> Clean Arch / Inbound Adaptor
