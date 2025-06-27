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

    private Integer views;

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
        //implement business logic here:

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
