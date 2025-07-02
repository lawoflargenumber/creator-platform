package creatorplatform.domain.service;

import creatorplatform.domain.Users;
import creatorplatform.domain.UserRegistered;
import creatorplatform.domain.RegisterUserCommand;
import creatorplatform.domain.aggregate.RegisteredUser;
import creatorplatform.domain.command.*;
import creatorplatform.domain.event.*;
import creatorplatform.domain.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class UserCommandService {

    @Autowired private UsersRepository usersRepository;
    @Autowired private ApplicationEventPublisher publisher;

    public void handleRegisterUser(RegisterUserCommand cmd) {
        Users user = new Users();
        user.setAccountId(cmd.getAccountId());
        user.setNickname(cmd.getNickname());
        user.setPassword(cmd.getPassword());
        user.setAuthorshipStatus("DEFAULT");
        user.setSubscriber(false);
        user.setAgreedToMarketing(cmd.getAgreedToMarketing());
        user.setCreatedAt(new Date()); // 생성 시간 설정
        usersRepository.save(user);
        
        UserRegistered userRegistered = new UserRegistered(user);
        userRegistered.publishAfterCommit(); // 트랜잭션 커밋 후 발행
    }

    public void handleApplyForAuthorship(ApplyForAuthorshipCommand cmd) {
        Users user = usersRepository.findById(Long.parseLong(cmd.id)).orElseThrow();
        user.setAuthorshipStatus("PENDING");
        user.setAuthorsProfile(cmd.authorsProfile);
        user.setAuthorNickname(cmd.authorNickname);
        usersRepository.save(user);
        publisher.publishEvent(new AuthorshipAppliedEvent(cmd.id, user.getAuthorshipStatus(), user.getAuthorsProfile()));
    }

    public void handleStartSubscribe(StartSubscribeCommand cmd) {
        Users user = usersRepository.findById(Long.parseLong(cmd.id)).orElseThrow();
        user.setSubscriber(true);
        usersRepository.save(user);
        publisher.publishEvent(new SubscriptionStartedEvent(cmd.id));
    }

    public void handleAcceptApplication(AcceptApplicationCommand cmd) {
        Users user = usersRepository.findById(Long.parseLong(cmd.id)).orElseThrow();
        user.setAuthorshipStatus("ACCEPTED");
        usersRepository.save(user);
        publisher.publishEvent(new AuthorshipAcceptedEvent(cmd.id, user.getAuthorNickname()));
    }

    public void handleDeclineApplication(DeclineApplicationCommand cmd) {
        Users user = usersRepository.findById(Long.parseLong(cmd.id)).orElseThrow();
        user.setAuthorshipStatus("DECLINED");
        usersRepository.save(user);
    }

    public void handleUpdateUser(UpdateUserCommand cmd) {
        Users user = usersRepository.findById(Long.parseLong(cmd.id)).orElseThrow();
        user.setNickname(cmd.nickname);
        user.setAgreedToMarketing(cmd.agreedToMarketing);
        usersRepository.save(user);
    }
}