package creatorplatform.infra;

import creatorplatform.domain.*;
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
    ProductsRepository productsRepository;

    @RequestMapping(
        value = "/products/{id}/trackview",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public Products trackView(
        @PathVariable(value = "id") Long id,
        @RequestBody TrackViewCommand trackViewCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /products/trackView  called #####");
        Optional<Products> optionalProducts = productsRepository.findById(id);

        optionalProducts.orElseThrow(() -> new Exception("No Entity Found"));
        Products products = optionalProducts.get();
        products.trackView(trackViewCommand);

        productsRepository.save(products);
        return products;
    }
}
//>>> Clean Arch / Inbound Adaptor
