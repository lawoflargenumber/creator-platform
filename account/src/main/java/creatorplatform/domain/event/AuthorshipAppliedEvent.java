package creatorplatform.domain.event;

public class AuthorshipAppliedEvent {
    public String id;
    public String authorshipStatus;

    public AuthorshipAppliedEvent(String id, String authorshipStatus) {
        this.id = id;
        this.authorshipStatus = authorshipStatus;
    }
}
