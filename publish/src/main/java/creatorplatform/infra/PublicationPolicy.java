package creatorplatform.policy;

import com.fasterxml.jackson.databind.ObjectMapper;
import creatorplatform.config.kafka.KafkaProcessor;
import creatorplatform.domain.Products;
import creatorplatform.domain.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class PublicationPolicy {

    @Autowired
    ProductsRepository productsRepository;

    @StreamListener(value = KafkaProcessor.INPUT)
    public void wheneverGenerationCompleted_SaveProduct(@Payload String eventString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> event = objectMapper.readValue(eventString, Map.class);

            // 이벤트 타입 확인
            if (!"GenerationCompleted".equals(event.get("eventType"))) return;

            System.out.println("### GenerationCompleted 이벤트 수신됨 ###");

            Products product = new Products();
            product.setAuthorId(Long.valueOf(event.get("authorId").toString()));
            product.setAuthorNickname((String) event.get("authorNickname"));
            product.setTitle((String) event.get("title"));
            product.setContent((String) event.get("content"));
            product.setCategory((String) event.get("category"));
            product.setPrice((Integer) event.get("price"));
            product.setCoverImageUrl((String) event.get("coverImageUrl"));
            product.setSummary((String) event.get("summary"));
            product.setPublishedAt(new Date());
            product.setViews(0);
            product.setIsBestseller(false); // 초기값

            productsRepository.save(product);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
