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
public class ViewHistoryViewHandler {

    //<<< DDD / CQRS
    @Autowired
    private ViewHistoryRepository viewHistoryRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenViewTracked_then_CREATE_1(
        @Payload ViewTracked viewTracked
    ) {
        try {
            if (!viewTracked.validate()) return;

            // view 객체 생성
            ViewHistory viewHistory = new ViewHistory();
            // view 객체에 이벤트의 Value 를 set 함
            viewHistory.setId(viewTracked.getUserId());
            viewHistory.setProductId(viewTracked.getId());
            viewHistory.setViewedAt(viewTracked.getCreatedAt());
            // view 레파지 토리에 save
            viewHistoryRepository.save(viewHistory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //>>> DDD / CQRS
}
