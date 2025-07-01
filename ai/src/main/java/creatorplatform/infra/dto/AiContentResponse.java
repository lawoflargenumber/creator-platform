package creatorplatform.infra.dto;

import creatorplatform.domain.AiGeneratedContent;
import creatorplatform.domain.Category;
import lombok.Getter;

@Getter
public class AiContentResponse {

    private final Long id;
    private final String summary;
    private final String coverImageUrl;
    private final Integer price;
    private final Category category;

    private AiContentResponse(AiGeneratedContent entity) {
        this.id = entity.getId();
        this.summary = entity.getSummary();
        this.coverImageUrl = entity.getCoverImageUrl();
        this.price = entity.getPrice();
        this.category = entity.getCategory();
    }

    public static AiContentResponse fromEntity(AiGeneratedContent entity) {
        return new AiContentResponse(entity);
    }
}
