package creatorplatform.policy;

import creatorplatform.config.kafka.KafkaProcessor;
import creatorplatform.domain.GenerationCompleted;
import creatorplatform.domain.Products;
import creatorplatform.domain.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PublicationPolicy {

    @Autowired
    ProductsRepository productsRepository;

    @StreamListener(value = KafkaProcessor.INPUT)
    public void wheneverGenerationCompleted_SaveProduct(@Payload GenerationCompleted event) {
        
        if (event == null) return;

        System.out.println("### GenerationCompleted 이벤트 수신됨 ###");

        Products product = new Products();
        product.setAuthorId(event.getAuthorId());
        product.setAuthorNickname(event.getAuthorNickname());
        product.setTitle(event.getTitle());
        product.setContent(event.getContent());
        product.setCategory(event.getCategory());
        product.setPrice(event.getPrice());
        product.setCoverImageUrl(event.getCoverImageUrl());
        product.setSummary(event.getSummary());
        product.setPublishedAt(new Date());
        product.setViews(0);
        product.setIsBestseller(false); // 베스트셀러 초기값

        productsRepository.save(product);
    }
}
