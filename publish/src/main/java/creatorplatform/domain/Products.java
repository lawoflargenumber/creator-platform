package creatorplatform.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import creatorplatform.PublishApplication;
import creatorplatform.domain.CompletedPublication;
import creatorplatform.domain.ViewTracked;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Products_table")
@Data
//<<< DDD / Aggregate Root
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long draftId;

    private Long authorId;

    private String authorNickname;

    private String title;

    private String content;

    private String category;

    private Integer price;

    private Date publishedAt;

    private Integer views = 0; // 기본값 0으로 설정, null 방지 

    private String coverImageUrl;

    private String summary;

    @PostPersist
    public void onPostPersist() {
        CompletedPublication completedPublication = new CompletedPublication(
            this
        );
        completedPublication.publishAfterCommit();

        ViewTracked viewTracked = new ViewTracked(this);
        viewTracked.publishAfterCommit();
    }

    public static ProductsRepository repository() {
        ProductsRepository productsRepository = PublishApplication.applicationContext.getBean(
            ProductsRepository.class
        );
        return productsRepository;
    }

    //<<< Clean Arch / Port Method
    public void trackView(TrackViewCommand trackViewCommand) {
        this.views += 1;  

        ViewTracked viewTracked = new ViewTracked(this);
        viewTracked.setUserId(trackViewCommand.getUserId());
        viewTracked.setCreatedAt(new Date());
        viewTracked.publishAfterCommit();

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
