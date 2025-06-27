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
    DraftsRepository draftsRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='CompletedPublication'"
    )
    public void wheneverCompletedPublication_DeleteDraft(
        @Payload CompletedPublication completedPublication
    ) {
        CompletedPublication event = completedPublication;
        System.out.println(
            "\n\n##### listener DeleteDraft : " + completedPublication + "\n\n"
        );

        // Sample Logic //
        Drafts.deleteDraft(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
