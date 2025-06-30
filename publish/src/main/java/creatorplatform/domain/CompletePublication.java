package creatorplatform.domain;

import lombok.Data;

@Data
public class CompletePublication {
    private Long draftId;
    private Long authorId;
    private String authorNickname;
    private String title;
    private String content;
    private String category;
    private Integer price;
    private String coverImageUrl;
    private String summary;

    public CompletePublication(Long draftId, Long authorId, String authorNickname,
                               String title, String content, String category,
                               Integer price, String coverImageUrl, String summary) {
        this.draftId = draftId;
        this.authorId = authorId;
        this.authorNickname = authorNickname;
        this.title = title;
        this.content = content;
        this.category = category;
        this.price = price;
        this.coverImageUrl = coverImageUrl;
        this.summary = summary;
    }
}
