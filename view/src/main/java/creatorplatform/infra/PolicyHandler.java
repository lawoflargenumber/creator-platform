package creatorplatform.infra;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import creatorplatform.config.kafka.KafkaProcessor;
import creatorplatform.domain.*;
import javax.naming.NameParser;
import javax.naming.NameParser;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

//<<< Clean Arch / Inbound Adaptor
@Service
@Transactional
public class PolicyHandler {

    @Autowired
    UserAccessProfileRepository userAccessProfileRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='UserRegistered'"
    )
    public void wheneverUserRegistered_AddUser(
        @Payload UserRegistered userRegistered
    ) {
        UserRegistered event = userRegistered;
        System.out.println(
            "\n\n##### listener AddUser : " + userRegistered + "\n\n"
        );

        // Sample Logic //
        UserAccessProfile.addUser(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='SubscriptionStarted'"
    )
    public void wheneverSubscriptionStarted_AddSubscription(
        @Payload SubscriptionStarted subscriptionStarted
    ) {
        SubscriptionStarted event = subscriptionStarted;
        System.out.println(
            "\n\n##### listener AddSubscription : " +
            subscriptionStarted +
            "\n\n"
        );

        // Sample Logic //
        UserAccessProfile.addSubscription(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
