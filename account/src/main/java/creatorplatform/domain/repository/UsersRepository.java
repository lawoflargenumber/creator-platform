package creatorplatform.domain.repository;

import creatorplatform.domain.aggregate.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {}
