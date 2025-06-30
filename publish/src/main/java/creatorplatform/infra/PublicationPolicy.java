package creatorplatform.policy;

import creatorplatform.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PublicationPolicy {

    @Autowired
    ProductsRepository productsRepository;

    @StreamListener(value = KafkaProcessor.INPUT)
    public void wheneverGenerationCompleted_SaveProduct(@Payload GenerationCompleted event) {
        if (event == null || event.getDraftId() == null) return;

        System.out.println("### GenerationCompleted 이벤트 수신됨 ###");

        Products product = new Products();
        product.setDraftId(event.getDraftId());
        product.setAuthorId(event.getAuthorId());
        product.setAuthorNickname(event.getAuthorNickname());
        product.setTitle(event.getTitle());
        product.setContent(event.getContent());
        product.setCategory(event.getCategory());
        product.setPrice(event.getPrice());
        product.setCoverImageUrl(event.getCoverImageUrl());
        product.setSummary(event.getSummary());
        product.setPublishedAt(new java.util.Date());
        product.setViews(0);

        productsRepository.save(product);
    }
}
