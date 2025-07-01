
package creatorplatform.domain;

import creatorplatform.domain.event.AuthorshipAcceptedEvent;
import creatorplatform.domain.event.SubscriptionStartedEvent;
import creatorplatform.domain.event.UserRegisteredEvent;
import creatorplatform.domain.aggregate.RegisteredUser;
import creatorplatform.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AccountEventListener {

    @Autowired
    private UserRepository userRepository;

    @EventListener
    public void handleUserRegistered(UserRegisteredEvent event) {
        RegisteredUser user = new RegisteredUser();
        user.setId(event.id);
        user.setNickname(event.nickname);
        userRepository.save(user);
    }

    @EventListener
    public void handleSubscriptionStarted(SubscriptionStartedEvent event) {
        userRepository.findById(event.id).ifPresent(user -> {
            user.setSubscribed(true);
            userRepository.save(user);
        });
    }

    @EventListener
    public void handleAuthorshipAccepted(AuthorshipAcceptedEvent event) {
        userRepository.findById(event.id).ifPresent(user -> {
            user.setAuthorshipStatus("ACCEPTED");
            userRepository.save(user);
        });
    }
}
