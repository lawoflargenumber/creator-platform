package creatorplatform.infra;

import creatorplatform.config.kafka.KafkaProcessor;
import creatorplatform.domain.*;

import javax.transaction.Transactional;

import creatorplatform.domain.port.AiGeneratorPort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

//<<< Clean Arch / Inbound Adaptor
@Service
@Transactional
@RequiredArgsConstructor
public class PolicyHandler {

    private static final Logger log = LoggerFactory.getLogger(PolicyHandler.class);

    private final AiGeneratedContentRepository repository;
    private final AiService aiService;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='RequestedPublication'"
    )
    public void wheneverRequestedPublication_GenerateInitialContent(
        @Payload RequestedPublication requestedPublication
    ) {
        aiService.generateInitialContent(requestedPublication);
    }
}
//>>> Clean Arch / Inbound Adaptor
