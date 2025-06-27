package creatorplatform.infra;

import creatorplatform.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class UsersHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<Users>> {

    @Override
    public EntityModel<Users> process(EntityModel<Users> model) {
        model.add(
            Link
                .of(model.getRequiredLink("self").getHref() + "/registeruser")
                .withRel("registeruser")
        );
        model.add(
            Link
                .of(
                    model.getRequiredLink("self").getHref() +
                    "/applyforauthorship"
                )
                .withRel("applyforauthorship")
        );
        model.add(
            Link
                .of(
                    model.getRequiredLink("self").getHref() +
                    "/declineapplication"
                )
                .withRel("declineapplication")
        );
        model.add(
            Link
                .of(model.getRequiredLink("self").getHref() + "/startsubscribe")
                .withRel("startsubscribe")
        );
        model.add(
            Link
                .of(
                    model.getRequiredLink("self").getHref() +
                    "/accpetapplication"
                )
                .withRel("accpetapplication")
        );

        return model;
    }
}
