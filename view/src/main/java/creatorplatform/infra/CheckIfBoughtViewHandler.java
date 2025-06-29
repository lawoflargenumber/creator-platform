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
public class CheckIfBoughtViewHandler {

    //<<< DDD / CQRS
    @Autowired
    private CheckIfBoughtRepository checkIfBoughtRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenAccessGranted_then_CREATE_1(
        @Payload AccessGranted accessGranted
    ) {
        try {
            if (!accessGranted.validate()) return;

            // view 객체 생성
            CheckIfBought checkIfBought = new CheckIfBought();
            // view 객체에 이벤트의 Value 를 set 함
            checkIfBought.setId(accessGranted.getId());
            checkIfBought.setProductId(accessGranted.getProductId());
            // checkId는 자동 생성되므로 설정하지 않음
            // view 레파지 토리에 save
            checkIfBoughtRepository.save(checkIfBought);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //>>> DDD / CQRS
}
