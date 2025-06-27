package creatorplatform.infra;

import creatorplatform.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class ProductsHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<Products>> {

    @Override
    public EntityModel<Products> process(EntityModel<Products> model) {
        model.add(
            Link
                .of(model.getRequiredLink("self").getHref() + "/trackview")
                .withRel("trackview")
        );

        return model;
    }
}
