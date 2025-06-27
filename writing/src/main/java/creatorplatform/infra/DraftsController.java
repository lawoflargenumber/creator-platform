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
// @RequestMapping(value="/drafts")
@Transactional
public class DraftsController {

    @Autowired
    DraftsRepository draftsRepository;

    @RequestMapping(
        value = "/drafts/savedraft",
        method = RequestMethod.POST,
        produces = "application/json;charset=UTF-8"
    )
    public Drafts saveDraft(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody SaveDraftCommand saveDraftCommand
    ) throws Exception {
        System.out.println("##### /drafts/saveDraft  called #####");
        Drafts drafts = new Drafts();
        drafts.saveDraft(saveDraftCommand);
        draftsRepository.save(drafts);
        return drafts;
    }

    @RequestMapping(
        value = "/drafts/{id}/requestpublication",
        method = RequestMethod.DELETE,
        produces = "application/json;charset=UTF-8"
    )
    public Drafts requestPublication(
        @PathVariable(value = "id") Long id,
        @RequestBody RequestPublicationCommand requestPublicationCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /drafts/requestPublication  called #####");
        Optional<Drafts> optionalDrafts = draftsRepository.findById(id);

        optionalDrafts.orElseThrow(() -> new Exception("No Entity Found"));
        Drafts drafts = optionalDrafts.get();
        drafts.requestPublication(requestPublicationCommand);

        draftsRepository.delete(drafts);
        return drafts;
    }
}
//>>> Clean Arch / Inbound Adaptor
