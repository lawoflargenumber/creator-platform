package creatorplatform.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import creatorplatform.AiApplication;
import creatorplatform.domain.GeneratedInitialContent;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "AiGeneratedContent_table")
@Data
//<<< DDD / Aggregate Root
public class AiGeneratedContent {

    @Id
    private Long id;

    private Long authorId;

    private String authorNickname;

    private String title;

    private String content;

    private String summary;

    private String coverImageurl;

    public class AiGeneratedContent(RequestedPublication request) {
        this.id = request.getId();
        this.authorId = request.getAuthorId();
        this.authorNickname = request.getAuthorNickname();
        this.title = request.getTitle();
        this.content = request.getContent();
    }

    public static AiGeneratedContentRepository repository() {
        AiGeneratedContentRepository aiGeneratedContentRepository = AiApplication.applicationContext.getBean(
            AiGeneratedContentRepository.class
        );
        return aiGeneratedContentRepository;
    }

    //<<< Clean Arch / Port Method
    public static void generateInitialContent(
        RequestedPublication requestedPublication
    ) {
        
    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
