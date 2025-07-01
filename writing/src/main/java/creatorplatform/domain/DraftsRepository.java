package creatorplatform.domain;

import creatorplatform.domain.*;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//<<< PoEAA / Repository
@RepositoryRestResource(collectionResourceRel = "drafts", path = "drafts")
public interface DraftsRepository
    extends PagingAndSortingRepository<Drafts, Long> {
    List<Drafts> findByAuthorId(Long authorId);
}
