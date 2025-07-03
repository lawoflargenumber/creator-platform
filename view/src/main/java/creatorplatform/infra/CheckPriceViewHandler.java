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
public class CheckPriceViewHandler {

    //<<< DDD / CQRS
    @Autowired
    private CheckPriceRepository checkPriceRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenGenerationCompleted_then_CREATE_1(
        @Payload GenerationCompleted generationCompleted
    ) {
        try {
            if (!generationCompleted.validate()) return;

            // view 객체 생성
            CheckPrice checkPrice = new CheckPrice();
            // view 객체에 이벤트의 Value 를 set 함
            checkPrice.setProductId(generationCompleted.getId());
            checkPrice.setPrice(generationCompleted.getPrice());
            // view 레파지 토리에 save
            checkPriceRepository.save(checkPrice);
            
            System.out.println("✅ CheckPrice created from GenerationCompleted: " + 
                "productId=" + generationCompleted.getId() + 
                ", price=" + generationCompleted.getPrice());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //>>> DDD / CQRS
}
