// creator-platform/writing/src/main/java/creatorplatform/infra/AbstractEvent.java
package creatorplatform.infra;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import creatorplatform.WritingApplication;
import creatorplatform.config.kafka.KafkaProcessor;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.MimeTypeUtils;
private static final Logger log = LoggerFactory.getLogger(AbstractEvent.class);
//<<< Clean Arch / Outbound Adaptor
public class AbstractEvent {

    String eventType;
    Long timestamp;

    public AbstractEvent(Object aggregate) {
        this();
        BeanUtils.copyProperties(aggregate, this);
    }

    public AbstractEvent() {
        this.setEventType(this.getClass().getSimpleName());
        this.timestamp = System.currentTimeMillis();
    }

    public void publish() {
        KafkaProcessor processor = WritingApplication.applicationContext.getBean(KafkaProcessor.class);
        MessageChannel out = processor.outboundTopic();

        out.send(
            MessageBuilder.withPayload(this) // ★ 직렬화 자동
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .setHeader("type", getEventType()) // 이벤트 타입 헤더
                .build()
        );

        log.info("🚀 Published event -> {}", this); // ← 무조건 로그로 확인
    }

    public void publishAfterCommit() {
        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronizationAdapter() {
                @Override
                public void afterCompletion(int status) {
                    AbstractEvent.this.publish();
                }
            }
        );
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean validate() {
        return getEventType().equals(getClass().getSimpleName());
    }

    public String toJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;

        try {
            json = objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON format exception", e);
        }

        return json;
    }
}
//>>> Clean Arch / Outbound Adaptor
