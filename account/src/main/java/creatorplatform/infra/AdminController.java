package creatorplatform.infra;

import creatorplatform.domain.Users;
import creatorplatform.domain.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UsersRepository repository;

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
}
