package creatorplatform.domain.event;

public class AuthorshipAppliedEvent {
    public String id;
    public String authorshipStatus;
    public String authorsProfile;

    public AuthorshipAppliedEvent(String id, String authorshipStatus, String authorsProfile) {
        this.id = id;
        this.authorshipStatus = authorshipStatus;
        this.authorsProfile = authorsProfile;
    }
}
