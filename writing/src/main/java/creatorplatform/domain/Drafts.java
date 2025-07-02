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
public class Drafts {

    public enum Status { DRAFT, REQUESTED, PUBLISHED }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long authorId;
    private String authorNickname;
    private String title;

    @Lob
    private String content;

    @Enumerated(EnumType.STRING)
    private Status status = Status.DRAFT;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedAt;

    public void saveDraft(SaveDraftCommand cmd) {
        this.authorId = cmd.getAuthorId();
        this.title = cmd.getTitle();
        this.content = cmd.getContent();
        this.createdAt = new Date();
        this.lastUpdatedAt = new Date();
        this.status = Status.DRAFT;

        new DraftSaved(this).publishAfterCommit();
    }

    public void requestPublication() {
        if (this.status != Status.DRAFT)
            throw new IllegalStateException("이미 요청됨/발행됨");

        this.status = Status.REQUESTED;
        this.lastUpdatedAt = new Date();

        new RequestedPublication(this).publishAfterCommit();
    }

    public void updateDraft(UpdateDraftCommand cmd) {
        if (this.status != Status.DRAFT)
            throw new IllegalStateException("요청된 상태에서는 수정 불가");

        if (cmd.getTitle() != null) this.title = cmd.getTitle();
        if (cmd.getContent() != null) this.content = cmd.getContent();
        this.lastUpdatedAt = new Date();
    }
}
