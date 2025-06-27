package creatorplatform.domain;

import creatorplatform.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//<<< PoEAA / Repository
@RepositoryRestResource(collectionResourceRel = "products", path = "products")
public interface ProductsRepository
    extends PagingAndSortingRepository<Products, Long> {}
