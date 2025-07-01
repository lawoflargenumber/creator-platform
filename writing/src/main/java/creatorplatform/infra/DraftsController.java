//DraftsController.java
// creator-platform/writing/src/main/java/creatorplatform/infra/DraftsController.java
package creatorplatform.infra;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import creatorplatform.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequiredArgsConstructor
@Transactional
@RequestMapping("/draft")
public class DraftsController {

    private final DraftsRepository repo;

    // ---------- 드래프트 저장 ----------
    @PostMapping("/savedraft")
    public EntityModel<Drafts> saveDraft(@RequestBody SaveDraftCommand cmd) {
        Drafts draft = new Drafts();
        draft.saveDraft(cmd);
        Drafts saved = repo.save(draft);

        return EntityModel.of(saved,
            linkTo(methodOn(DraftsController.class).getDraft(saved.getId())).withSelfRel(),
            linkTo(methodOn(DraftsController.class).listDrafts()).withRel("drafts")
        );
    }

    // ---------- 출판 요청 ----------
    @PostMapping("/{id}/publish")
    public Drafts publishDraft(@PathVariable Long id) {
        Drafts draft = repo.findById(id).orElseThrow(() -> new RuntimeException("Draft not found"));
        draft.requestPublication();
        return repo.save(draft);
    }


    // ---------- 직접 출판 ----------
    @PostMapping("/publish")
    public Drafts directPublish(@RequestBody SaveDraftCommand cmd) {
        Drafts draft = new Drafts();
        draft.saveDraft(cmd);

        // 1️⃣ 먼저 저장해서 id 생성
        draft = repo.save(draft);

        // 2️⃣ 이제 id 있는 상태로 출판 요청
        draft.requestPublication();

        // 3️⃣ 상태 변경된 draft 다시 저장
        return repo.save(draft);
    }

    // ---------- 전체 목록 조회 ----------
    @GetMapping
    public Iterable<Drafts> listDrafts() {
        return repo.findAll();
    }

    // ---------- 단건 조회 ----------
    @GetMapping("/{id}")
    public EntityModel<Drafts> getDraft(@PathVariable Long id) {
        Drafts draft = repo.findById(id).orElseThrow(() -> new RuntimeException("Draft not found"));
        return EntityModel.of(draft,
            linkTo(methodOn(DraftsController.class).getDraft(id)).withSelfRel(),
            linkTo(methodOn(DraftsController.class).listDrafts()).withRel("drafts")
        );
    }
    // ---------- 수정 요청 ----------
    @PutMapping("/{id}")
    public Drafts updateDraft(@PathVariable Long id, @RequestBody UpdateDraftCommand cmd) {
        Drafts draft = repo.findById(id).orElseThrow(() -> new RuntimeException("Draft not found"));
        draft.updateDraft(cmd);
        return repo.save(draft);
    }

    // ---------- 삭제 요청 ----------
    @DeleteMapping("/{id}")
    public void deleteDraft(@PathVariable Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
        } else {
            throw new RuntimeException("Draft not found");
        }
    }
}
