package creatorplatform.infra;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import creatorplatform.domain.Category;
import creatorplatform.domain.port.AiGeneratorPort;
import lombok.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Value;
import java.util.Map;

@Component
public class AiAdapterGPT implements AiGeneratorPort {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${openai.api.key}")
    private String apiKey;

    private static final String GPT_API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String DALLE_API_URL = "https://api.openai.com/v1/images/generations";

    @Override
    public AiGeneratedResult generate(String title, String content) {
        return this.processAiGeneration("## 제목:\n" + title + "\n\n## 내용:\n" + content, "");
    }

    @Override
    public AiGeneratedResult regenerate(String title, String content, String userPrompt) {
        return this.processAiGeneration("## 제목:\n" + title + "\n\n## 내용:\n" + content, userPrompt);
    }

    private AiGeneratedResult processAiGeneration(String originalText, String userPrompt) {
        GptTextResult gptResult = generateTextualContent(originalText, userPrompt);
        String imagePrompt = createImagePromptFrom(gptResult.getSummary());
        String imageUrl = generateImage(imagePrompt);

        Category categoryEnum = Category.fromDisplayName(gptResult.getCategory());

        return new AiGeneratedResult(gptResult.getSummary(), categoryEnum, gptResult.getPrice(), imageUrl);
    }

    private GptTextResult generateTextualContent(String originalText, String userPrompt) {
        HttpHeaders headers = createHeaders();

        String gptPrompt = createGptPrompt(originalText, userPrompt);
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4o-mini",
                "messages", new Object[]{ Map.of("role", "user", "content", gptPrompt) },
                "response_format", Map.of("type", "json_object")
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        System.out.println(">>> Sending request to GPT API...");
        ResponseEntity<String> response = restTemplate.postForEntity(GPT_API_URL, entity, String.class);
        System.out.println("<<< Received response from GPT API.");

        return parseGptResponse(response.getBody());
    }

    private String generateImage(String imagePrompt) {
        HttpHeaders headers = createHeaders();

        Map<String, Object> requestBody = Map.of(
                "model", "dall-e-2",
                "prompt", imagePrompt,
                "n", 1,
                "size", "512x512"
        );
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        System.out.println(">>> Sending request to DALL-E API...");
        ResponseEntity<String> response = restTemplate.postForEntity(DALLE_API_URL, entity, String.class);
        System.out.println("<<< Received response from DALL-E API.");

        return parseDalleResponse(response.getBody());
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private String createGptPrompt(String originalText, String userPrompt) {
        String categories = "\"문학\", \"경제\", \"자기계발\", \"라이프스타일\", \"기타\"";

        return String.format(
                "당신은 콘텐츠의 가치를 평가하는 전문 편집자입니다. 아래의 '원본 텍스트'를 분석하여, 반드시 다음 규칙에 따라 JSON 객체 형식으로만 응답해주십시오.\n\n" +
                        "### 규칙:\n" +
                        "1. 'summary': 원본 텍스트의 핵심 내용을 담은 한 문장 요약.\n" +
                        "2. 'category': 반드시 다음 5가지 카테고리 중 가장 적합한 하나를 선택: [%s].\n" +
                        "3. 'price': 1에서 500 사이의 정수. 아래 '가격 책정 기준'을 참고하여 책정.\n\n" +
                        "### 가격 책정 기준:\n" +
                        "- 글의 분량 및 깊이: 글이 길고 심도 깊은 내용을 다룰수록 높은 가격.\n" +
                        "--- 원본 텍스트 ---\n" +
                        "%s\n\n" +
                        "--- 추가적인 요청사항 ---\n" +
                        "%s",
                categories, originalText, userPrompt
        );
    }

    private String createImagePromptFrom(String summary) {
        return String.format(
                "다음 글의 요약을 읽으시고 책의 커버 이미지를 생성해주세요: \n%s", summary
        );
    }

    private GptTextResult parseGptResponse(String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            String contentJson = rootNode.path("choices").get(0).path("message").path("content").asText();
            JsonNode contentNode = objectMapper.readTree(contentJson);

            String summary = contentNode.path("summary").asText();
            String category = contentNode.path("category").asText();
            Integer price = contentNode.path("price").asInt();

            return new GptTextResult(summary, category, price);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse GPT response", e);
        }
    }

    private String parseDalleResponse(String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            return rootNode.path("data").get(0).path("url").asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse DALL-E response", e);
        }
    }

    @AllArgsConstructor
    @Getter
    @ToString
    public static class GptTextResult {
        private final String summary;
        private final String category;
        private final Integer price;
    }

}