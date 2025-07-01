package creatorplatform.infra;

import creatorplatform.config.kafka.KafkaProcessor;
import creatorplatform.domain.*;

import javax.transaction.Transactional;

import creatorplatform.domain.port.AiGeneratorPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

//<<< Clean Arch / Inbound Adaptor
@Service
@Transactional
public class PolicyHandler {

    private static final Logger log = LoggerFactory.getLogger(PolicyHandler.class);

    @Autowired
    private AiGeneratedContentRepository repository;

    @Autowired
    private AiGeneratorPort aiGenerator;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='RequestedPublication'"
    )
    public void wheneverRequestedPublication_GenerateInitialContent(
        @Payload RequestedPublication requestedPublication
    ) {
        log.info("##### listener GenerateInitialContent : {}", requestedPublication);

        if (repository.findById(requestedPublication.getDraftId()).isPresent()) {
            log.warn("Process for bookId {} already exists. Skipping.", requestedPublication.getDraftId());
            return;
        }

        try {
            AiGeneratorPort.AiGeneratedResult aiResult = aiGenerator.generate(
                    requestedPublication.getTitle(),
                    requestedPublication.getContent()
            );

            AiGeneratedContent process = new AiGeneratedContent();
            process.setId(requestedPublication.getDraftId());
            process.setAuthorId(requestedPublication.getAuthorId());
            process.setAuthorNickname(requestedPublication.getAuthorNickname());
            process.setTitle(requestedPublication.getTitle());
            process.setContent(requestedPublication.getContent());
            process.setStatus(ProcessingStatus.PENDING);
            process.applyGeneratedContent(
                    aiResult.getSummary(),
                    aiResult.getPrice(),
                    aiResult.getImageUrl(),
                    aiResult.getCategory()
            );

            repository.save(process);
            log.info("AiProcessing data saved for bookId: {}", process.getId());
        } catch (Exception e) {
            log.error("Failed to process publication request for bookId: {}", requestedPublication.getDraftId(), e);
        }
    }
}
//>>> Clean Arch / Inbound Adaptor
