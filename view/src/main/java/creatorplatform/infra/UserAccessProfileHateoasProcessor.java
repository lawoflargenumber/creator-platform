package creatorplatform.infra;

import creatorplatform.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class UserAccessProfileHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<UserAccessProfile>> {

    @Override
    public EntityModel<UserAccessProfile> process(
        EntityModel<UserAccessProfile> model
    ) {
        model.add(
            Link
                .of(
                    model.getRequiredLink("self").getHref() + "/accesstocontent"
                )
                .withRel("accesstocontent")
        );

        return model;
    }
}
