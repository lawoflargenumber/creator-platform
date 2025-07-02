package creatorplatform.domain;

import creatorplatform.domain.*;
import creatorplatform.infra.AbstractEvent;
import java.util.*;
import lombok.*;

@Data
@ToString
public class RequestedPublication extends AbstractEvent {

    private Long draftId;       // 이벤트 소비자가 바로 쓰도록 필드명 변경
    private Long authorId;
    private String authorNickname;
    private String title;
    private String content;
}
