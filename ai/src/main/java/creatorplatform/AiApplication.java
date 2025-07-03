package creatorplatform;

import creatorplatform.config.kafka.KafkaProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableBinding(KafkaProcessor.class)
@EnableFeignClients
@EnableAsync
public class AiApplication {

    public static ApplicationContext applicationContext;

    public static void main(String[] args) {
        applicationContext = SpringApplication.run(AiApplication.class, args);
    }
}
