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
// @RequestMapping(value="/userAccessProfiles")
@Transactional
public class UserAccessProfileController {

    @Autowired
    UserAccessProfileRepository userAccessProfileRepository;

    @RequestMapping(
        value = "/userAccessProfiles/{id}/accesstocontent",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public UserAccessProfile accessToContent(
        @PathVariable(value = "id") Long id,
        @RequestBody AccessToContentCommand accessToContentCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println(
            "##### /userAccessProfile/accessToContent  called #####"
        );
        Optional<UserAccessProfile> optionalUserAccessProfile = userAccessProfileRepository.findById(
            id
        );

        optionalUserAccessProfile.orElseThrow(() ->
            new Exception("No Entity Found")
        );
        UserAccessProfile userAccessProfile = optionalUserAccessProfile.get();
        userAccessProfile.accessToContent(accessToContentCommand);

        userAccessProfileRepository.save(userAccessProfile);
        return userAccessProfile;
    }
}
//>>> Clean Arch / Inbound Adaptor
