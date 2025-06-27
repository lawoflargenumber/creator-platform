package creatorplatform.domain.repository;

import creatorplatform.domain.aggregate.RegisteredUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<RegisteredUser, String> {
}
