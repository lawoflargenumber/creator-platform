package creatorplatform.domain;

import creatorplatform.domain.*;
import creatorplatform.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class RequestedPublication extends AbstractEvent {

    private Long id;

    public RequestedPublication(Drafts aggregate) {
        super(aggregate);
    }

    public RequestedPublication() {
        super();
    }
}
//>>> DDD / Domain Event
