package creatorplatform.domain;

import creatorplatform.infra.AbstractEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CompletedGeneration extends AbstractEvent {
    private Long id;
    private Long authorId;
    private String authorNickname;
    private String title;
    private String content;
    private String summary;
    private Integer price;
    private String coverImageUrl;
    private Category category;

    public CompletedGeneration(AiGeneratedContent aggregate) {
        super(aggregate);
    }

    public CompletedGeneration() {
        super();
    }
}
