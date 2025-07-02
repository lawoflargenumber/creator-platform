package creatorplatform.domain;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

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

    @Lob
    private String summary;

    private Integer price;

    @Lob
    private String coverImageUrl;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private ProcessingStatus status;

    public static AiGeneratedContent fromEvent(RequestedPublication event) {
        AiGeneratedContent content = new AiGeneratedContent();

        content.setId(event.getDraftId());
        content.setAuthorId(event.getAuthorId());
        content.setAuthorNickname(event.getAuthorNickname());
        content.setTitle(event.getTitle());
        content.setContent(event.getContent());

        content.setStatus(ProcessingStatus.PENDING);
        return content;
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

        GenerationCompleted generationCompleted = new GenerationCompleted(this);
        generationCompleted.publishAfterCommit();
    }
}
//>>> DDD / Aggregate Root
