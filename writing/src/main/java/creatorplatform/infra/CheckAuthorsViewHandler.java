package creatorplatform.infra;

import creatorplatform.config.kafka.KafkaProcessor;
import creatorplatform.domain.*;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class CheckAuthorsViewHandler {

    //<<< DDD / CQRS
    @Autowired
    private CheckAuthorsRepository checkAuthorsRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenAuthorshipAccepted_then_CREATE_1(
        @Payload AuthorshipAccepted authorshipAccepted
    ) {
        try {
            if (!authorshipAccepted.validate()) return;

            // view 객체 생성
            CheckAuthors checkAuthors = new CheckAuthors();
            // view 객체에 이벤트의 Value 를 set 함
            checkAuthors.setId(authorshipAccepted.getId());
            checkAuthors.setNickname(authorshipAccepted.getNickname());
            // view 레파지 토리에 save
            checkAuthorsRepository.save(checkAuthors);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //>>> DDD / CQRS
}
