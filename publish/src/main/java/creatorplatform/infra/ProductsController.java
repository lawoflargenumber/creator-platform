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
@RequestMapping(value="/products")
@Transactional
public class ProductsController {

    @Autowired
    private ProductsRepository productsRepository;

    @RequestMapping(
        value = "/{id}/trackview",
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

    @GetMapping("/literature")
    public List<ProductSummaryDto> getLiteratureProducts() {
        return getProductsByCategory("문학");
    }

    @GetMapping("/economy")
    public List<ProductSummaryDto> getEconomyProducts() {
        return getProductsByCategory("경제");
    }

    @GetMapping("/selfdevelopment")
    public List<ProductSummaryDto> getSelfDevelopmentProducts() {
        return getProductsByCategory("자기계발");
    }

    @GetMapping("/lifestyle")
    public List<ProductSummaryDto> getLifestyleProducts() {
        return getProductsByCategory("라이프스타일");
    }

    @GetMapping("/others")
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


    @GetMapping("/bestsellers")
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


    @GetMapping("/{id}")
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

    @GetMapping("/{id}/content")
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

    @GetMapping("/all")
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
