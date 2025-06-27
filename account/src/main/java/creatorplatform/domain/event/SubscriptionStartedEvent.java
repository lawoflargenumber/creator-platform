package creatorplatform.domain.event;

public class SubscriptionStartedEvent {
    public String id;

    public SubscriptionStartedEvent(String id) {
        this.id = id;
    }
}
