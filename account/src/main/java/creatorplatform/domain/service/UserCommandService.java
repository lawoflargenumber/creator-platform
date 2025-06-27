
package creatorplatform.domain.service;

import creatorplatform.domain.aggregate.RegisteredUser;
import creatorplatform.domain.command.*;
import creatorplatform.domain.event.*;
import creatorplatform.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class UserCommandService {

    @Autowired private UserRepository userRepository;
    @Autowired private ApplicationEventPublisher publisher;

    public void handleRegisterUser(RegisterUserCommand cmd) {
        RegisteredUser user = new RegisteredUser();
        user.setId(cmd.id);
        user.setNickname(cmd.nickname);
        user.setPassword(cmd.password);
        user.setAuthorshipStatus("PENDING");
        user.setSubscribed(false);
        user.setAgreedToMarketing(cmd.agreedToMarketing);
        userRepository.save(user);
        publisher.publishEvent(new UserRegisteredEvent(cmd.id, cmd.nickname));
    }

    public void handleApplyForAuthorship(ApplyForAuthorshipCommand cmd) {
        RegisteredUser user = userRepository.findById(cmd.id).orElseThrow();
        user.setAuthorshipStatus("PENDING");
        user.setAuthorsProfile(cmd.authorsProfile);
        userRepository.save(user);
        publisher.publishEvent(new AuthorshipAppliedEvent(cmd.id, cmd.authorsProfile));
    }

    public void handleStartSubscribe(StartSubscribeCommand cmd) {
        RegisteredUser user = userRepository.findById(cmd.id).orElseThrow();
        user.setSubscribed(true);
        userRepository.save(user);
        publisher.publishEvent(new SubscriptionStartedEvent(cmd.id));
    }

    public void handleAcceptApplication(AcceptApplicationCommand cmd) {
        RegisteredUser user = userRepository.findById(cmd.id).orElseThrow();
        user.setAuthorshipStatus("ACCEPTED");
        userRepository.save(user);
        publisher.publishEvent(new AuthorshipAcceptedEvent(cmd.id, user.getNickname()));
    }

    public void handleDeclineApplication(DeclineApplicationCommand cmd) {
        RegisteredUser user = userRepository.findById(cmd.id).orElseThrow();
        user.setAuthorshipStatus("DECLINED");
        userRepository.save(user);
    }

    public void handleUpdateUser(UpdateUserCommand cmd) {
        RegisteredUser user = userRepository.findById(cmd.id).orElseThrow();
        user.setNickname(cmd.nickname);
        user.setAgreedToMarketing(cmd.agreedToMarketing);
        userRepository.save(user);
    }
}
