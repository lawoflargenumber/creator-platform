package creatorplatform.domain;

import creatorplatform.domain.*;
import creatorplatform.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class AccessDenied extends AbstractEvent {

    private Long id;
    private Integer points;
    private Boolean hasSufficientPoints;

    public AccessDenied(UserAccessProfile aggregate) {
        super(aggregate);
    }

    public AccessDenied() {
        super();
    }
}
//>>> DDD / Domain Event
