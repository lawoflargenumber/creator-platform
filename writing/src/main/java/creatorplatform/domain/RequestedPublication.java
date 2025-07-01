//requested_publication.java
// creator-platform/writing/src/main/java/creatorplatform/domain/RequestedPublication.java
package creatorplatform.domain;

import creatorplatform.domain.*;
import creatorplatform.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event

@ToString
@Data
public class RequestedPublication extends AbstractEvent {
    private Long draftId;       // 이벤트 소비자가 바로 쓰도록 필드명 변경
    private Long authorId;
    private String title;

    public RequestedPublication(Drafts d){
        super(d);
        this.draftId  = d.getId();
        this.authorId = d.getAuthorId();
        this.title    = d.getTitle();
    }
}
