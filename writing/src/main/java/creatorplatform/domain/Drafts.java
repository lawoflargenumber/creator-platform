//drafts.java
// creator-platform/writing/src/main/java/creatorplatform/domain/Drafts.java
package creatorplatform.domain;

import creatorplatform.WritingApplication;
import javax.persistence.*;
import java.util.Date;
import lombok.Data;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;



@Entity
@Table(name = "Drafts_table")
@Data
// <<< DDD / Aggregate Root
public class Drafts {
    public enum Status { DRAFT, REQUESTED, PUBLISHED }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long authorId;
    private String authorNickname;
    private String title;
    private String content;
    /* ✅ 추가 */  
    @Enumerated(EnumType.STRING)
    private Status status = Status.DRAFT;  // 기본값  // ← status를 실제 필드로 보유해야 this.status 사용 가능

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedAt;

   
    public static DraftsRepository repository() {
        return WritingApplication.applicationContext.getBean(DraftsRepository.class);
    }

   
    public void saveDraft(SaveDraftCommand cmd) {
        // 1) Business validation – external Author Service
        /*var authorService = WritingApplication.applicationContext
                .getBean(creatorplatform.external.AuthorService.class);
        boolean authorExists = authorService.checkAuthor(cmd.getAuthorId());
        if (!authorExists) {
            throw new IllegalArgumentException("Author not found: " + cmd.getAuthorId());
        }*/

        // 2) Copy fields
        this.authorId = cmd.getAuthorId();
        this.authorNickname = cmd.getAuthorNickname();
        this.title = cmd.getTitle();
        this.content = cmd.getContent();
        this.createdAt = new Date();
        this.lastUpdatedAt = new Date();
        this.status         = Status.DRAFT;
        // 3) Publish domain event
        DraftSaved draftSaved = new DraftSaved(this);
        draftSaved.publishAfterCommit();
    }

    public void requestPublication(){
        if(this.status != Status.DRAFT)
            throw new IllegalStateException("이미 요청됨/발행됨");

        this.status        = Status.REQUESTED;
        this.lastUpdatedAt = new Date();

        new RequestedPublication(this).publishAfterCommit();
    }

  
    /* =========================================================
     * Policy handler – react to CompletedPublication event
     * ========================================================= */
    public static void deleteDraft(CompletedPublication completedPublication) {
        repository().findById(completedPublication.getDraftId()).ifPresent(draft -> {
            repository().delete(draft);
        });
    }
    public void updateDraft(UpdateDraftCommand cmd) {
    // 예: 상태 검증 – 이미 출판 요청된 글은 수정 불가
    // if(this.status == Status.REQUESTED) throw new IllegalStateException();

    if(cmd.getTitle() != null)   this.title   = cmd.getTitle();
    if(cmd.getContent() != null) this.content = cmd.getContent();
    this.lastUpdatedAt = new Date();

    // 필요하면 DraftUpdated 이벤트 발행
    // DraftUpdated event = new DraftUpdated(this);
    // event.publishAfterCommit();
}

}
// >>> DDD / Aggregate Root
