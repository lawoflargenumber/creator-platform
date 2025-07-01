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

    // private Long draftId;

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

    private Boolean isBestseller = false;

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

        // 조회수 10 이상이면 베스트셀러 설정
        if (this.views >= 10 && !Boolean.TRUE.equals(this.isBestseller)) {
            this.isBestseller = true;
        }

        ViewTracked viewTracked = new ViewTracked(this);
        viewTracked.setUserId(trackViewCommand.getUserId());
        viewTracked.setCreatedAt(new Date());
        viewTracked.publishAfterCommit();

    }
    //>>> Clean Arch / Port Method

    //<<< Clean Arch / Factory Method
    public static Products createFrom(CompletePublication command) {
        Products product = new Products();

        product.setDraftId(command.getDraftId());
        product.setAuthorId(command.getAuthorId());
        product.setAuthorNickname(command.getAuthorNickname());
        product.setTitle(command.getTitle());
        product.setContent(command.getContent());
        product.setCategory(command.getCategory());
        product.setPrice(command.getPrice());
        product.setCoverImageUrl(command.getCoverImageUrl());
        product.setSummary(command.getSummary());
        product.setPublishedAt(new Date());
        product.setViews(0); // 출간 시 조회수는 0부터 시작
        product.setIsBestseller(false); // 출간 시에는 베스트셀러 아님

        return product;
    }
    //>>> Clean Arch / Factory Method


}
//>>> DDD / Aggregate Root
