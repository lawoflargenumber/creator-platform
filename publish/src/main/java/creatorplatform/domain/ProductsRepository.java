package creatorplatform.domain;

import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//<<< PoEAA / Repository
@RepositoryRestResource(collectionResourceRel = "products", path = "products")
public interface ProductsRepository extends PagingAndSortingRepository<Products, Long> {

    List<Products> findByCategory(String category);

    List<Products> findByIsBestsellerTrueOrderByViewsDesc();

    List<Products> findAllByOrderByIsBestsellerDescPublishedAtDesc();


}
