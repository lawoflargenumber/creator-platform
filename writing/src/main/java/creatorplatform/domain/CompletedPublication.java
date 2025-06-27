package creatorplatform.domain;

import creatorplatform.infra.AbstractEvent;
import lombok.*;
import java.util.Date;

@Data
@NoArgsConstructor
@ToString
public class CompletedPublication extends AbstractEvent {

    /* ---- 기존 필드 ---- */
    private Long id;
    private Long authorId;
    private String authorNickname;
    private String title;
    private String content;
    private String category;
    private Integer price;
    private Date publishedAt;
    private Integer views;
    private String coverImageUrl;
    private String summary;

    /* ---- 추가: Draft 식별용 필드 ---- */
    private Long draftId;

    /* ---- 이벤트 생성자 ---- */
    public CompletedPublication(Drafts aggregate) {
        super(aggregate);
        this.draftId = aggregate.getId();          // 🔑 드래프트 ID 저장
        this.id              = aggregate.getId();
        this.authorId        = aggregate.getAuthorId();
        this.authorNickname  = aggregate.getAuthorNickname();
        this.title           = aggregate.getTitle();
        this.content         = aggregate.getContent();
        // 나머지 필드는 상황에 맞게 채우거나 null 유지
    }

    /* ---- Drafts.deleteDraft()에서 호출할 게터 ---- */
    public Long getDraftId() {
        return draftId;
    }
}
