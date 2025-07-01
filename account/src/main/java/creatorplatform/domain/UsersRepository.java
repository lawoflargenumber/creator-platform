package creatorplatform.domain;

import creatorplatform.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.Optional;

//<<< PoEAA / Repository
@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface UsersRepository
    extends PagingAndSortingRepository<Users, Long> {
    Optional<Users> findByEmail(String email);
}