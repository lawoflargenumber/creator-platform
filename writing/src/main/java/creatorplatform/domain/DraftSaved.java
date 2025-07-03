package creatorplatform.domain;

import creatorplatform.infra.AbstractEvent;
import lombok.*;

/** 드래프트 저장 완료 이벤트 */
@Data
@NoArgsConstructor
@ToString
public class DraftSaved extends AbstractEvent {

    private Long id;
    private Long authorId;
    private String title;
    private String content;
    
    public DraftSaved(Drafts aggregate) {
        super(aggregate);
        this.id       = aggregate.getId();
        this.authorId = aggregate.getAuthorId();
        this.title    = aggregate.getTitle();
    }
}
