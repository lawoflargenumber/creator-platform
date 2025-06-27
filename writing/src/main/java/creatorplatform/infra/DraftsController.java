package creatorplatform.infra;

import creatorplatform.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Transactional
@RequestMapping("/drafts")   // 공통 prefix
public class DraftsController {

    private final DraftsRepository repo;

    /* ---------- 드래프트 저장 ---------- */
    @PostMapping
    public Drafts saveDraft(@RequestBody SaveDraftCommand cmd) {
        Drafts draft = new Drafts();
        draft.saveDraft(cmd);     // 내부에서 이벤트 발행
        return repo.save(draft);  // DB 반영
    }

    /* ------ 출판 요청(상태 변경) ------ */
    @PutMapping("/{id}/submit")   // 또는 POST
    public Drafts requestPublication(@PathVariable Long id,
                                     @RequestBody RequestPublicationCommand cmd) {
        Drafts draft = repo.findById(id)
                           .orElseThrow(() -> new RuntimeException("Draft not found"));
        draft.requestPublication(cmd);
        return repo.save(draft);
    }

    /* ---------- 전체 목록 조회 ---------- */
    @GetMapping
    public Iterable<Drafts> listDrafts() {
        return repo.findAll();
    }

    /* ----------- 단건 조회 ----------- */
    @GetMapping("/{id}")
    public Drafts getDraft(@PathVariable Long id) {
        return repo.findById(id)
                   .orElseThrow(() -> new RuntimeException("Draft not found"));
    }
    /* ----------- 수정 요청 ----------- */
    @PutMapping("/{id}")
    public Drafts updateDraft(@PathVariable Long id, @RequestBody UpdateDraftCommand cmd) {
        Drafts draft = repo.findById(id)
                           .orElseThrow(() -> new RuntimeException("Draft not found"));
    draft.updateDraft(cmd);
    return repo.save(draft);     
}
}
  