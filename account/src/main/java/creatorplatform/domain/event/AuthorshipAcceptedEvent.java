package creatorplatform.domain.event;

public class AuthorshipAcceptedEvent {
    public String id;
    public String nickname;

    public AuthorshipAcceptedEvent(String id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }
}
