package creatorplatform.infra;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import creatorplatform.config.kafka.KafkaProcessor;
import creatorplatform.domain.*;
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

    // @Autowired
    // Repository Repository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='RequestedPublication'"
    )
    public void wheneverRequestedPublication_GenerateInitialContent(
        @Payload RequestedPublication requestedPublication
    ) {
        RequestedPublication event = requestedPublication;
        System.out.println(
            "\n\n##### listener GenerateInitialContent : " +
            requestedPublication +
            "\n\n"
        );
        // Sample Logic //

    }
}
//>>> Clean Arch / Inbound Adaptor
