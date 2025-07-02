package creatorplatform.domain;

import creatorplatform.domain.*;
import creatorplatform.infra.AbstractEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import lombok.*;
import org.apache.tomcat.jni.Local;

//<<< DDD / Domain Event
@Data
@ToString
public class SubscriptionStarted extends AbstractEvent {

    private Long id;
    private String nickname;
    private String authorshipStatus;
    private Date createdAt;
    private Boolean subscriber;
    private String authorsProfile;
    private LocalDateTime subscribtionStartedAt;

    public SubscriptionStarted(Users aggregate) {

        super(aggregate);
        this.id = aggregate.getId();
        this.nickname = aggregate.getNickname();
        this.authorshipStatus = aggregate.getAuthorshipStatus();
        this.createdAt = aggregate.getCreatedAt();
        this.subscriber = aggregate.getSubscriber();
        this.authorsProfile = aggregate.getAuthorsProfile();
        this.subscribtionStartedAt = LocalDateTime.now();
    }

    public SubscriptionStarted() {
        super();
    }
}
//>>> DDD / Domain Event
