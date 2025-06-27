package creatorplatform.infra;

import creatorplatform.domain.*;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(
    collectionResourceRel = "checkPrices",
    path = "checkPrices"
)
public interface CheckPriceRepository
    extends PagingAndSortingRepository<CheckPrice, Long> {}
