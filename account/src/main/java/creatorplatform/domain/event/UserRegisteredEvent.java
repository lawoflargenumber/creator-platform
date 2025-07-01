package creatorplatform.domain.event;

public class UserRegisteredEvent {
    public String id;
    public String nickname;

    public UserRegisteredEvent(String id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }
}
