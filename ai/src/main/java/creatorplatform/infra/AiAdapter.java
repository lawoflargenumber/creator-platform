package creatorplatform.infra;

import creatorplatform.domain.Category;
import creatorplatform.domain.port.AiGeneratorPort;
import lombok.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
@RequiredArgsConstructor
public class AiAdapter implements AiGeneratorPort {
    private final RestTemplate restTemplate = new RestTemplate();
    private final TextAdapter textAdapter;
    private final ImageAdapter imageAdapter;

    @Override
    public AiGeneratedResult generate(String title, String content) {
        return this.processAiGeneration(title, content, "");
    }

    @Override
    public AiGeneratedResult regenerate(String title, String content, String userPrompt) {
        return this.processAiGeneration(title, content, userPrompt);
    }

    private AiGeneratedResult processAiGeneration(String title, String content, String userPrompt) {
        TextAdapter.TextResult textResult = textAdapter.generateTextualContent(title, content);
        String imageResult = imageAdapter.generateImage(title, textResult.getSummary(), textResult.getCategory(), userPrompt);

        Category category = Category.fromDisplayName(textResult.getCategory());
        return new AiGeneratedResult(textResult.getSummary(), category, textResult.getPrice(), imageResult);
    }

}