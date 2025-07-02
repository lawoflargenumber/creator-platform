package creatorplatform.domain;

import creatorplatform.infra.AbstractEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class GenerationCompleted extends AbstractEvent {
    private Long id;
    private Long authorId;
    private String authorNickname;
    private String title;
    private String content;
    private String summary;
    private Integer price;
    private String coverImageUrl;
    private Category category;

    public GenerationCompleted(AiGeneratedContent aggregate) {
        super(aggregate);
        this.id = aggregate.getId();
        this.authorId = aggregate.getAuthorId();
        this.authorNickname = aggregate.getAuthorNickname();
        this.title = aggregate.getTitle();
        this.content = aggregate.getContent();
        this.summary = aggregate.getSummary();
        this.price = aggregate.getPrice();
        this.coverImageUrl = aggregate.getCoverImageUrl();
        this.category = aggregate.getCategory();
    }

    public GenerationCompleted() {
        super();
    }
}
