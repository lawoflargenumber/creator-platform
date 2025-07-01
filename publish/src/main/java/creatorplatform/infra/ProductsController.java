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


    // 출간 요청 처리 (POST /products)
    @PostMapping("/products")
    public Products completePublication(
        @RequestBody CompletePublication command,
        HttpServletRequest request,
        HttpServletResponse response
        ) throws Exception {
            System.out.println("##### /products [POST] - completePublication called #####");

            Products product = Products.createFrom(command);  // 도메인 객체 생성
            productsRepository.save(product);                 // DB에 저장

            return product;  // 저장된 객체 반환
        }

    @GetMapping("/products")
    public List<ProductSummaryDto> getAllProductSummaries(
        @RequestParam(required = false) String category
    ) {
        List<Products> productsList;

        if (category != null && !category.isEmpty()) {
            productsList = productsRepository.findByCategory(category);
        } else {
        productsList = (List<Products>) productsRepository.findAll();
        }

        // 필요한 필드만 담아서 DTO로 변환
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





}
//>>> Clean Arch / Inbound Adaptor
