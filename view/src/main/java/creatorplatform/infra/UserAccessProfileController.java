package creatorplatform.infra;

import creatorplatform.domain.*;
import creatorplatform.service.UserAccessProfileService;
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
    private UserAccessProfileService userAccessProfileService;

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
        
        return userAccessProfileService.accessToContent(id, accessToContentCommand);
    }
}
//>>> Clean Arch / Inbound Adaptor
