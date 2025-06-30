package creatorplatform.domain;

import creatorplatform.AiApplication;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "AiGeneratedContent_table")
@Getter
@Setter
@NoArgsConstructor
//<<< DDD / Aggregate Root
public class AiGeneratedContent {

    @Id
    private Long id;
    private Long authorId;
    private String authorNickname;
    private String title;
    @Lob
    private String content;
    private String summary;
    private Integer price;
    private String coverImageUrl;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private ProcessingStatus status;

    public static AiGeneratedContent createFrom(Long bookId, Long authorId, String authorNickname, String title, String content) {
        AiGeneratedContent generatedContent = new AiGeneratedContent();
        generatedContent.id = bookId;
        generatedContent.authorId = authorId;
        generatedContent.authorNickname = authorNickname;
        generatedContent.title = title;
        generatedContent.content = content;
        generatedContent.status = ProcessingStatus.PENDING;

        return generatedContent;
    }

    public void applyGeneratedContent(String summary, Integer price, String coverImageUrl, Category category) {
        this.summary = summary;
        this.price = price;
        this.coverImageUrl = coverImageUrl;
        this.category = category;
        this.status = ProcessingStatus.GENERATED;
    }

    public void completeGeneration() {
        this.status = ProcessingStatus.COMPLETED;

        CompletedGeneration completedGeneration = new CompletedGeneration(this);
        completedGeneration.publishAfterCommit();
    }
}
//>>> DDD / Aggregate Root
