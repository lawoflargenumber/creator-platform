package creatorplatform.infra;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import creatorplatform.config.kafka.KafkaProcessor;
import creatorplatform.domain.*;
import creatorplatform.service.UserAccessProfileService;
import javax.naming.NameParser;
import javax.naming.NameParser;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * Infrastructure Layer - Event Router
 * 역할: Kafka 이벤트 수신 후 Application Service로 라우팅
 * vs Application Service: 비즈니스 프로세스 처리는 Service Layer에서 담당
 */
//<<< Clean Arch / Inbound Adaptor
@Service
@Transactional
public class PolicyHandler {

    @Autowired
    UserAccessProfileRepository userAccessProfileRepository;
    
    @Autowired
    UserAccessProfileService userAccessProfileService;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    /**
     * 사용자 등록 이벤트 라우팅
     * Account 서비스 → View 서비스 이벤트 전달
     */
    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='UserRegistered'"
    )
    public void wheneverUserRegistered_AddUser(
        @Payload UserRegistered userRegistered
    ) {
        UserRegistered event = userRegistered;

        // Application Service로 비즈니스 프로세스 위임
        userAccessProfileService.processUserRegistration(event);
    }

    /**
     * 구독 시작 이벤트 라우팅
     * Account 서비스 → View 서비스 이벤트 전달
     */
    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='SubscriptionStarted'"
    )
    public void wheneverSubscriptionStarted_AddSubscription(
        @Payload SubscriptionStarted subscriptionStarted
    ) {
        SubscriptionStarted event = subscriptionStarted;

        // Application Service로 비즈니스 프로세스 위임
        userAccessProfileService.processSubscriptionActivation(event);
    }

    /**
     * 작가 신청 이벤트 라우팅
     * Account 서비스 → View 서비스 이벤트 전달
     */
    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='AuthorshipApplied'"
    )
    public void wheneverAuthorshipApplied_UpdateAuthorProfile(
        @Payload AuthorshipApplied authorshipApplied
    ) {
        AuthorshipApplied event = authorshipApplied;

        // Application Service로 비즈니스 프로세스 위임
        userAccessProfileService.processAuthorshipApplication(event);
    }

    /**
     * 작가 승인 이벤트 라우팅
     * Account 서비스 → View 서비스 이벤트 전달
     */
    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='AuthorshipAccepted'"
    )
    public void wheneverAuthorshipAccepted_UpdateAuthorStatus(
        @Payload AuthorshipAccepted authorshipAccepted
    ) {
        AuthorshipAccepted event = authorshipAccepted;

        // Application Service로 비즈니스 프로세스 위임
        userAccessProfileService.processAuthorshipAcceptance(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
