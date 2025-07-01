package creatorplatform.domain.port;


import creatorplatform.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

public interface AiGeneratorPort {

    @AllArgsConstructor
    @Getter
    class AiGeneratedResult {
        private final String summary;
        private final Category category;
        private Integer price;
        private final String imageUrl;
    }

    AiGeneratedResult generate(String title, String content);
    AiGeneratedResult regenerate(String title, String content, String userPrompt);
}