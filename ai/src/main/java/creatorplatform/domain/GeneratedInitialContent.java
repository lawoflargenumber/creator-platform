package creatorplatform.domain;

import creatorplatform.domain.*;
import creatorplatform.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class GeneratedInitialContent extends AbstractEvent {

    private Long id;

    public GeneratedInitialContent() {
        super();
    }
}
//>>> DDD / Domain Event
