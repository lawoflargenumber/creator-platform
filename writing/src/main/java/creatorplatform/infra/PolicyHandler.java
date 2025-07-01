package creatorplatform.infra;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import creatorplatform.config.kafka.KafkaProcessor;
import creatorplatform.domain.*;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PolicyHandler {

    private static final Logger log = LoggerFactory.getLogger(PolicyHandler.class);

    private final DraftsRepository draftsRepository;
    private final ObjectMapper mapper;

    @Autowired
    public PolicyHandler(DraftsRepository draftsRepository) {
        this.draftsRepository = draftsRepository;
        this.mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='CompletedPublication'"
    )
    public void onCompletedPublication(@Payload CompletedPublication event) {
        log.info("✅ CompletedPublication received: {}", event);
        draftsRepository.findById(event.getDraftId())
                    .ifPresent(draftsRepository::delete);
    }  
}
