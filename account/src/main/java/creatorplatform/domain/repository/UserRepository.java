
package creatorplatform.domain.repository;

import creatorplatform.domain.Users;
import creatorplatform.domain.model.User;
import creatorplatform.infra.PendingAuthorResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<Users> findByEmail(String email);
}
