package creatorplatform.domain.event;

public class AuthorshipAppliedEvent {
    public String id;
    public String authorsProfile;

    public AuthorshipAppliedEvent(String id, String authorsProfile) {
        this.id = id;
        this.authorsProfile = authorsProfile;
    }
}
