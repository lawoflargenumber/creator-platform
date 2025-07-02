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
import creatorplatform.external.DraftsService;
@RestController
@RequiredArgsConstructor
@Transactional
@RequestMapping("/draft")
public class DraftsController {

    private final DraftsRepository repo;
    private final CheckAuthorsRepository checkAuthorsRepository;

    // ---------- 드래프트 저장 ----------
    private final DraftsService draftsService;

    @PostMapping("/savedraft")
    public EntityModel<Drafts> saveDraft(@RequestBody SaveDraftCommand cmd) {
        Drafts saved = draftsService.saveDraft(cmd);
        return EntityModel.of(saved,
            linkTo(methodOn(DraftsController.class).getDraft(saved.getId())).withSelfRel(),
            linkTo(methodOn(DraftsController.class).listDrafts(saved.getAuthorId())).withRel("drafts")
        );
    }

    // ---------- 출판 요청 ----------
    @PostMapping("/{id}/publish")
    public Drafts publishDraft(@PathVariable Long id, @RequestBody SaveDraftCommand cmd) {
        Drafts draft = repo.findById(id).orElseThrow(() -> new RuntimeException("Draft not found"));
        draft.saveDraft(cmd);

        draft.requestPublication();
        return repo.save(draft);
    }


    // ---------- 직접 출판 ----------
    @PostMapping("/publish")
    public Drafts directPublish(@RequestBody SaveDraftCommand cmd) {
        String nickname = checkAuthorsRepository.findById(cmd.getAuthorId())
                .map(CheckAuthors::getNickname)
                .orElse(null);
        

        Drafts draft = new Drafts();
        draft.saveDraft(cmd);
        draft.setAuthorNickname(nickname); // Drafts 객체에 직접 설정
        // 1️⃣ DB에 먼저 저장 (id 생성)
        Drafts saved = repo.save(draft);

        // 2️⃣ ID가 생성된 draft로 출판 요청
        saved.requestPublication();

        // 3️⃣ 상태 변경된 draft 다시 저장
        Drafts published = repo.save(saved);

        return published;
    }


    // ---------- 전체 목록 조회 (DRAFT 상태만) ----------
    @GetMapping("author/{authorId}")
    public Iterable<Drafts> listDrafts(@PathVariable Long authorId) {
        // 전진구 수정: DRAFT 상태만 조회
        return repo.findByAuthorIdAndStatus(authorId, Drafts.Status.DRAFT);
    }

    // ---------- 단건 조회 ----------
    @GetMapping("/{id}")
    public EntityModel<Drafts> getDraft(@PathVariable Long id) {
        Drafts draft = repo.findById(id).orElseThrow(() -> new RuntimeException("Draft not found"));
        return EntityModel.of(draft,
            linkTo(methodOn(DraftsController.class).getDraft(id)).withSelfRel(),
            linkTo(methodOn(DraftsController.class).listDrafts(draft.getAuthorId())).withRel("drafts")
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
