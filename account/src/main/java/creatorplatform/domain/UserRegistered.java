package creatorplatform.domain;

import creatorplatform.domain.*;
import creatorplatform.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class UserRegistered extends AbstractEvent {

    private Long id;
    private String nickname;
    private String accountId;
    private String authorshipStatus;
    private String authorsProfile;
    private String authorNickname;
    
    public UserRegistered(Users aggregate) {

        super(aggregate);

        this.id = aggregate.getId();
        this.nickname = aggregate.getNickname();
        this.accountId = aggregate.getAccountId();
        this.authorshipStatus = aggregate.getAuthorshipStatus();
        this.authorsProfile = aggregate.getAuthorsProfile();
        this.authorNickname = aggregate.getAuthorNickname();
    }

    public UserRegistered() {
        super();
    }
}
//>>> DDD / Domain Event
