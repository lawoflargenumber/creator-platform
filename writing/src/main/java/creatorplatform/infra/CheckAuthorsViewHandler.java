package creatorplatform.infra;

import creatorplatform.config.kafka.KafkaProcessor;
import creatorplatform.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class CheckAuthorsViewHandler {

    @Autowired
    private CheckAuthorsRepository checkAuthorsRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenAuthorshipAccepted_then_CREATE_1(
        @Payload AuthorshipAccepted authorshipAccepted
    ) {
        try {
            if (!authorshipAccepted.validate()) return;

            CheckAuthors checkAuthors = new CheckAuthors();
            checkAuthors.setId(authorshipAccepted.getId());
            checkAuthors.setNickname(authorshipAccepted.getNickname());

            checkAuthorsRepository.save(checkAuthors);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
