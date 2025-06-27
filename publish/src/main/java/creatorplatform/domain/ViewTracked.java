package creatorplatform.domain;

import creatorplatform.domain.*;
import creatorplatform.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class ViewTracked extends AbstractEvent {

    private Long id;
    private Long userId;
    private Date createdAt;

    public ViewTracked(Products aggregate) {
        super(aggregate);
    }

    public ViewTracked() {
        super();
    }
}
//>>> DDD / Domain Event
