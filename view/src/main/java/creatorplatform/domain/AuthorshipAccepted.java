package creatorplatform.domain;

import creatorplatform.infra.AbstractEvent;
import java.util.*;
import lombok.*;

@Data
@ToString
public class AuthorshipAccepted extends AbstractEvent {
    
    private Long id;
    private String nickname;
    private String authorshipStatus;
    private Date createdAt;
    private Boolean subscriber;

    public AuthorshipAccepted() {
        super();
    }
} 