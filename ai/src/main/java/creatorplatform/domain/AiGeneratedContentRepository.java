package creatorplatform.domain;

import creatorplatform.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

//<<< PoEAA / Repository
@RepositoryRestResource(
    collectionResourceRel = "aiGeneratedContents",
    path = "aiGeneratedContents"
)
public interface AiGeneratedContentRepository
    extends PagingAndSortingRepository<AiGeneratedContent, Long> {
}
