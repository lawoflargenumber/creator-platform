package creatorplatform.domain;

import creatorplatform.domain.*;
import creatorplatform.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class AccessGranted extends AbstractEvent {

    private Long id;
    private Boolean isSubscribed;
    private Date subscribtionDue;
    private Integer points;
    private Integer price;
    private Long productId;

    public AccessGranted(UserAccessProfile aggregate) {
        super(aggregate);
    }

    public AccessGranted() {
        super();
    }
}
//>>> DDD / Domain Event
