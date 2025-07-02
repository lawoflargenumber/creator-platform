package creatorplatform.domain;

import creatorplatform.domain.*;
import creatorplatform.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class AuthorshipAccepted extends AbstractEvent {

    private Long id;
    private String nickname;
    private String authorshipStatus;
    private Date createdAt;
    private Boolean subscriber;

    public AuthorshipAccepted(Users aggregate) {
        super(aggregate);
        this.id = aggregate.getId();
        this.nickname = aggregate.getNickname();
        this.authorshipStatus = aggregate.getAuthorshipStatus();
        this.createdAt = aggregate.getCreatedAt();
        this.subscriber = aggregate.getSubscriber();
    }

    public AuthorshipAccepted() {
        super();
    }
}
//>>> DDD / Domain Event
