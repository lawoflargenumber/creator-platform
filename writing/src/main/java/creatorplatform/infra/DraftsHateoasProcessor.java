//drafts
// creator-platform/writing/src/main/java/creatorplatform/infra/DraftsHateoasProcessor
package creatorplatform.infra;

import creatorplatform.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class DraftsHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<Drafts>> {

    @Override
    public EntityModel<Drafts> process(EntityModel<Drafts> model) {
        model.add(
            Link
                .of(model.getRequiredLink("self").getHref() + "/savedraft")
                .withRel("savedraft")
        );
        model.add(
            Link
                .of(
                    model.getRequiredLink("self").getHref() +
                    "/requestpublication"
                )
                .withRel("requestpublication")
        );

        return model;
    }
}
