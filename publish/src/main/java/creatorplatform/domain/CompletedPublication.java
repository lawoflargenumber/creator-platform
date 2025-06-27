package creatorplatform.domain;

import creatorplatform.domain.*;
import creatorplatform.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class CompletedPublication extends AbstractEvent {

    private Long id;
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

    public CompletedPublication(Products aggregate) {
        super(aggregate);
    }

    public CompletedPublication() {
        super();
    }
}
//>>> DDD / Domain Event
