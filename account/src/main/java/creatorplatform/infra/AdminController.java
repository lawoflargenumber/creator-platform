package creatorplatform.infra;

import creatorplatform.domain.AccpetApplicationCommand;
import creatorplatform.domain.Users;
import creatorplatform.domain.UsersRepository;
import creatorplatform.domain.service.UserCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UsersRepository repository;
    private final UserCommandService userCommandService;

    @GetMapping("/requests")
    public ResponseEntity<List<?>> getPendingAuthors() {
        List<Users> pendingAccounts = repository
                .findByAuthorshipStatus("PENDING");

        List<PendingAuthorResponse> response = pendingAccounts.stream()
                .map(PendingAuthorResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/requests/{id}")
    public ResponseEntity<PendingAuthorResponse> getDetailedPendingAuthor(@PathVariable(value = "id") Long id) {
        return repository.findById(id)
                .map(PendingAuthorResponse::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @RequestMapping(
            value = "/admin/requests{id}/approve",
            method = RequestMethod.PUT,
            produces = "application/json;charset=UTF-8"
    )
    public ResponseEntity<?> acceptApplication(
            @PathVariable(value = "id") Long id,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /users/accpetApplication  called #####");
        userCommandService.handleAcceptApplication(id);
        return ResponseEntity.ok().build();
    }
}
