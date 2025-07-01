package creatorplatform.infra;

import creatorplatform.domain.*;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//<<< Clean Arch / Inbound Adaptor

@RestController
// @RequestMapping(value="/products")
@Transactional
public class ProductsController {

    @Autowired
    private ProductsRepository productsRepository;

    @RequestMapping(
        value = "/products/{id}/trackview",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public void trackView(
        @PathVariable(value = "id") Long id,
        @RequestBody TrackViewCommand trackViewCommand
)   throws Exception {
        System.out.println("##### /products/trackView  called #####");
        Optional<Products> optionalProducts = productsRepository.findById(id);

        optionalProducts.orElseThrow(() -> new Exception("No Entity Found"));
        Products products = optionalProducts.get();
        products.trackView(trackViewCommand);

        productsRepository.save(products);
    }

    @GetMapping("/products/literature")
    public List<ProductSummaryDto> getLiteratureProducts() {
        return getProductsByCategory("문학");
    }

    @GetMapping("/products/economy")
    public List<ProductSummaryDto> getEconomyProducts() {
        return getProductsByCategory("경제");
    }

    @GetMapping("/products/selfdevelopment")
    public List<ProductSummaryDto> getSelfDevelopmentProducts() {
        return getProductsByCategory("자기계발");
    }

    @GetMapping("/products/lifestyle")
    public List<ProductSummaryDto> getLifestyleProducts() {
        return getProductsByCategory("라이프스타일");
    }

    @GetMapping("/products/others")
    public List<ProductSummaryDto> getOthersProducts() {
        return getProductsByCategory("기타");
    }

    // 공통 로직
    private List<ProductSummaryDto> getProductsByCategory(String category) {
        List<Products> productsList = productsRepository.findByCategory(category);
        return productsList.stream()
            .map(p -> new ProductSummaryDto(
                p.getId(),
                p.getCoverImageUrl(),
                p.getCategory(),
                p.getAuthorNickname(),
                p.getTitle(),
                p.getPublishedAt(),
                p.getIsBestseller()
            ))
            .toList();
    }


    @GetMapping("/products/bestsellers")
    public List<ProductSummaryDto> getBestSellers() {
        return productsRepository.findByIsBestsellerTrueOrderByViewsDesc().stream()
            .map(p -> new ProductSummaryDto(
                p.getId(),
                p.getCoverImageUrl(),
                p.getCategory(),
                p.getAuthorNickname(),
                p.getTitle(),
                p.getPublishedAt(),
                p.getIsBestseller() 
            ))
            .toList();
    }


    @GetMapping("/products/{id}")
    public ProductDetailDto getProductDetail(@PathVariable Long id) throws Exception {
        Products product = productsRepository.findById(id)
            .orElseThrow(() -> new Exception("No product found"));

        return new ProductDetailDto(
            product.getId(),
            product.getCoverImageUrl(),
            product.getCategory(),
            product.getPrice(),
            product.getAuthorNickname(),
            product.getPublishedAt(),
            product.getTitle(),
            product.getSummary(),
            product.getIsBestseller()
        );
    }

    @GetMapping("/products/{id}/content")
    public ProductContentDto getProductContent(@PathVariable Long id) throws Exception {
        Products product = productsRepository.findById(id)
            .orElseThrow(() -> new Exception("No product found"));

        return new ProductContentDto(
            product.getId(),
            product.getTitle(),
            product.getAuthorNickname(),
            product.getContent(),
            product.getIsBestseller()
        );
    }

    @GetMapping("/products/all")
    public List<ProductSummaryDto> getAllProductsSorted() {
        List<Products> productsList = productsRepository.findAllByOrderByIsBestsellerDescPublishedAtDesc();

        return productsList.stream()
            .map(p -> new ProductSummaryDto(
                p.getId(),
                p.getCoverImageUrl(),
                p.getCategory(),
                p.getAuthorNickname(),
                p.getTitle(),
                p.getPublishedAt(),
                p.getIsBestseller()
            ))
            .toList();
    }






}
//>>> Clean Arch / Inbound Adaptor
