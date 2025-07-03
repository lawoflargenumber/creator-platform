package creatorplatform.domain;

import creatorplatform.infra.AbstractEvent;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AuthorshipApplied extends AbstractEvent {

    private Long id;
    private String authorshipStatus;
    private String authorsProfile;

    public AuthorshipApplied(Users aggregate) {
        super(aggregate);

        this.id = aggregate.getId();
        this.authorshipStatus = aggregate.getAuthorshipStatus();
        this.authorsProfile = aggregate.getAuthorsProfile();
    }

    public  AuthorshipApplied() {}
}
