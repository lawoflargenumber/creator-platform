package creatorplatform.domain.service;

import creatorplatform.domain.*;
import creatorplatform.domain.RegisterUserCommand;
import creatorplatform.domain.RefreshToken;
import creatorplatform.domain.repository.RefreshTokenRepository;
import creatorplatform.domain.security.JwtUtils;
import creatorplatform.domain.controller.JwtResponse;
import creatorplatform.infra.LoginRequest;
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
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;
import java.util.Optional;

@Service
public class UserCommandService {

    @Autowired private UsersRepository usersRepository;
    @Autowired private ApplicationEventPublisher publisher;
    @Autowired private JwtUtils jwtUtils;
    @Autowired private RefreshTokenRepository refreshTokenRepository;

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


    @Transactional
    public void handleStartSubscribe(StartSubscribeCommand cmd) {
        Users user = usersRepository.findById(Long.parseLong(cmd.id)).orElseThrow();
    public LocalDateTime handleStartSubscribe(Long id) {
        Users user = usersRepository.findById(id).orElseThrow();
        user.setSubscriber(true);
        usersRepository.save(user);

    @Transactional
    public void handleAcceptApplication(AcceptApplicationCommand cmd) {
        Users user = usersRepository.findById(Long.parseLong(cmd.id)).orElseThrow();
        user.setAuthorshipStatus("ACCEPTED");
        usersRepository.save(user);
        publisher.publishEvent(new AuthorshipAcceptedEvent(cmd.id, user.getAuthorNickname()));
    }

    @Transactional
    public void handleDeclineApplication(DeclineApplicationCommand cmd) {
        Users user = usersRepository.findById(Long.parseLong(cmd.id)).orElseThrow();
        user.setAuthorshipStatus("DECLINED");
        usersRepository.save(user);
    }

    @Transactional
    public void handleUpdateUser(UpdateUserCommand cmd) {
        Users user = usersRepository.findById(Long.parseLong(cmd.id)).orElseThrow();
        user.setNickname(cmd.nickname);
        user.setAgreedToMarketing(cmd.agreedToMarketing);
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

    @Transactional
    public JwtResponse handleLogin(LoginRequest request) {
        Optional<Users> userOpt = usersRepository.findByAccountId(request.getAccountId());
        if (userOpt.isEmpty() || !userOpt.get().getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        
        Users user = userOpt.get();
        
        String accessToken = jwtUtils.generateJwtToken(user.getAccountId());
        String refreshTokenStr = UUID.randomUUID().toString();
        
        RefreshToken rt = RefreshToken.builder()
                .user(user)
                .token(refreshTokenStr)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusMillis(jwtUtils.getRefreshExpirationMs()))
                .revoked(false)
                .build();
        
        refreshTokenRepository.deleteByUserId(user.getId());
        refreshTokenRepository.save(rt);
        
        return new JwtResponse(accessToken, refreshTokenStr);
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