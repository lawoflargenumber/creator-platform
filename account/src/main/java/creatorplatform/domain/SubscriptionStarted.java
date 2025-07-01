package creatorplatform.domain;

import creatorplatform.domain.*;
import creatorplatform.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

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
    private Date subscribtionStartedAt;

    public SubscriptionStarted(Users aggregate) {
        super(aggregate);
    }

    public SubscriptionStarted() {
        super();
    }
}
//>>> DDD / Domain Event
