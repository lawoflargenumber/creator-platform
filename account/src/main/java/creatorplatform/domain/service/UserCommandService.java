package creatorplatform.domain.service;

import creatorplatform.domain.*;
import creatorplatform.domain.RegisterUserCommand;
import creatorplatform.domain.RefreshToken;
import creatorplatform.domain.repository.RefreshTokenRepository;
import creatorplatform.domain.security.JwtUtils;
import creatorplatform.domain.controller.JwtResponse;
import creatorplatform.infra.LoginRequest;
import creatorplatform.infra.LoginResponse;
import creatorplatform.infra.LoginUserDto;
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
        
        // 🎯 비즈니스 로직: 포인트 계산 (마케팅 동의 시 5000, 아니면 1000)
        Integer calculatedPoints = (cmd.getAgreedToMarketing() != null && cmd.getAgreedToMarketing()) ? 5000 : 1000;
        System.out.println("💰 [Service] 비즈니스 로직 - 포인트 계산: agreedToMarketing=" + cmd.getAgreedToMarketing() + ", points=" + calculatedPoints);
        
        UserRegistered userRegistered = new UserRegistered(user);
        userRegistered.setPoints(calculatedPoints); // 계산된 포인트 설정
        System.out.println("📤 [Service] UserRegistered 이벤트 발행 준비: points=" + calculatedPoints);
        userRegistered.publishAfterCommit(); // 트랜잭션 커밋 후 발행
    }

    @Transactional
    public void handleApplyForAuthorship(Long id, ApplyForAuthorshipCommand cmd) {

        Users user = usersRepository.findById(id).orElseThrow();
        user.setAuthorshipStatus("PENDING");
        user.setAuthorsProfile(cmd.authorsProfile);
        user.setAuthorNickname(cmd.authorNickname);
        
        AuthorshipApplied event = new AuthorshipApplied(user);
        event.publishAfterCommit();

        usersRepository.save(user);
    }

    @Transactional
    public LocalDateTime handleStartSubscribe(Long id) {
        Users user = usersRepository.findById(id).orElseThrow();
        user.setSubscriber(true);
        usersRepository.save(user);

        SubscriptionStarted event = new SubscriptionStarted(user);
        LocalDateTime startDate = event.getSubscribtionStartedAt();
        event.publishAfterCommit();

        return startDate.plusMonths(1);
    }

    @Transactional
    public void handleAcceptApplication(Long id) {
        Users user = usersRepository.findById(id).orElseThrow();
        user.setAuthorshipStatus("ACCEPTED");
        usersRepository.save(user);
        
        AuthorshipAccepted event = new AuthorshipAccepted(user);
        event.publishAfterCommit();
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
    @Transactional
    public LoginResponse handleLogin(LoginRequest request) {
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
        
        LoginUserDto loginUserDto = LoginUserDto.fromEntity(user);
        
        return new LoginResponse(accessToken, refreshTokenStr, loginUserDto);
    }

    @Transactional
    public JwtResponse handleRefreshToken(String refreshToken) {
        Optional<RefreshToken> rtOpt = refreshTokenRepository.findByToken(refreshToken);
        if (rtOpt.isEmpty() || rtOpt.get().isRevoked() || rtOpt.get().getExpiresAt().isBefore(Instant.now())) {
            throw new RuntimeException("Invalid refresh token");
        }
        
        RefreshToken rt = rtOpt.get();
        String newAccessToken = jwtUtils.generateJwtToken(rt.getUser().getAccountId());
        String newRefreshTokenStr = UUID.randomUUID().toString();
        
        rt.setToken(newRefreshTokenStr);
        rt.setIssuedAt(Instant.now());
        rt.setExpiresAt(Instant.now().plusMillis(jwtUtils.getRefreshExpirationMs()));
        refreshTokenRepository.save(rt);
        // AccessToken 과 RefreshToken 을 모두 발급해주는 것이 맞는지 논의가 필요함.
        return new JwtResponse(newAccessToken, newRefreshTokenStr);
    }
}