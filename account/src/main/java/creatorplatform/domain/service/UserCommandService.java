package creatorplatform.domain.service;

import creatorplatform.domain.*;
import creatorplatform.domain.RegisterUserCommand;
import creatorplatform.domain.aggregate.RegisteredUser;
import creatorplatform.domain.command.*;
import creatorplatform.domain.command.ApplyForAuthorshipCommand;
import creatorplatform.domain.command.DeclineApplicationCommand;
import creatorplatform.domain.event.*;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;

@Service
public class UserCommandService {

    @Autowired private UsersRepository usersRepository;
    @Autowired private ApplicationEventPublisher publisher;

    @Transactional
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

    @Transactional
    public void handleApplyForAuthorship(Long id, ApplyForAuthorshipCommand cmd) {

        Users user = usersRepository.findById(id).orElseThrow();
        user.setAuthorshipStatus("PENDING");
        user.setAuthorsProfile(cmd.authorsProfile);
        user.setAuthorNickname(cmd.authorNickname);
        publisher.publishEvent(new AuthorshipApplied(user));

        usersRepository.save(user);
    }

    public LocalDateTime handleStartSubscribe(Long id) {
        Users user = usersRepository.findById(id).orElseThrow();
        user.setSubscriber(true);
        usersRepository.save(user);

        SubscriptionStarted event = new SubscriptionStarted(user);
        LocalDateTime startDate = event.getSubscribtionStartedAt();
        publisher.publishEvent(event);

        return startDate.plusMonths(1);
    }

    @Transactional
    public void handleAcceptApplication(Long id) {
        Users user = usersRepository.findById(id).orElseThrow();
        user.setAuthorshipStatus("ACCEPTED");
        usersRepository.save(user);
        publisher.publishEvent(new AuthorshipAccepted(user));
    }

//    public void handleDeclineApplication(DeclineApplicationCommand cmd) {
//        Users user = usersRepository.findById(Long.parseLong(cmd.id)).orElseThrow();
//        user.setAuthorshipStatus("DECLINED");
//        usersRepository.save(user);
//    }
    
    // public void handleUpdateUser(UpdateUserCommand cmd) {
    //     Users user = usersRepository.findById(Long.parseLong(cmd.id)).orElseThrow();
    //     user.setNickname(cmd.nickname);
    //     user.setAgreedToMarketing(cmd.agreedToMarketing);
    //     usersRepository.save(user);
    //}
}