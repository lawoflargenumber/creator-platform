package creatorplatform.domain;

import creatorplatform.domain.*;
import creatorplatform.infra.AbstractEvent;
import java.util.*;
import lombok.*;

@Data
@ToString
public class UserRegistered extends AbstractEvent {

    private Long id;
    private String nickname;
    private String accountId;
    private String authorshipStatus;
    private String authorsProfile;
    private String authorNickname;
    private Integer points;
}
