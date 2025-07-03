package creatorplatform.domain;

import creatorplatform.infra.AbstractEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class GenerationCompleted extends AbstractEvent {
    private Long id;           // productId로 사용
    private Long authorId;
    private String authorNickname;
    private String title;
    private String content;
    private String summary;
    private Integer price;     // 가격 정보
    private String coverImageUrl;
    private String category;   // AI에서는 Category enum이지만 View에서는 String으로 받기

    public GenerationCompleted() {
        super();
    }
} 