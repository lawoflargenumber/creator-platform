package creatorplatform.infra;

import creatorplatform.domain.*;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(
    collectionResourceRel = "checkIfBoughts",
    path = "checkIfBoughts"
)
public interface CheckIfBoughtRepository
    extends PagingAndSortingRepository<CheckIfBought, Long> {}
