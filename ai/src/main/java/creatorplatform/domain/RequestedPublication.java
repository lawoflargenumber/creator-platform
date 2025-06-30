package creatorplatform.domain;

import creatorplatform.domain.*;
import creatorplatform.infra.AbstractEvent;
import java.util.*;
import lombok.*;

@Data
@ToString
public class RequestedPublication extends AbstractEvent {

    private Long id;
    private Long authorId;
    private String authorNickname;
    private String title;
    private String content;
}
