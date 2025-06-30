package creatorplatform.infra;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import creatorplatform.domain.Category;
import creatorplatform.domain.port.AiGeneratorPort;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class AiAdapter implements AiGeneratorPort {
    private static final Logger log = LoggerFactory.getLogger(AiAdapterGPT.class);
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${gemini.api.key}")
    private String apiKey;

    // Gemini API 엔드포인트 URL (gemini-1.5-flash 모델 사용)
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent";

    @Override
    public AiGeneratedResult generate(String title, String content) {
        String originalText = "## 제목:\n" + title + "\n\n## 내용:\n" + content;
        return this.processAiGeneration(originalText, "");
    }

    @Override
    public AiGeneratedResult regenerate(String title, String content, String userPrompt) {
        String originalText = "## 제목:\n" + title + "\n\n## 내용:\n" + content;
        return this.processAiGeneration(originalText, userPrompt);
    }

    private AiGeneratedResult processAiGeneration(String originalText, String userPrompt) {
        // 1. Gemini 텍스트 생성
        String gptResultJson = generateGeminiText(originalText, userPrompt);
        GptTextResult gptResult = parseGeminiResponse(gptResultJson);

        // 2. 이미지 URL 생성 (더미 데이터)
        String imageUrl = createImagePlaceholderUrl(gptResult.getSummary());

        Category categoryEnum = Category.fromDisplayName(gptResult.getCategory());

        return new AiGeneratedResult(gptResult.getSummary(), categoryEnum, gptResult.getPrice(), imageUrl);
    }

    private String generateGeminiText(String originalText, String userPrompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String prompt = createGeminiPrompt(originalText, userPrompt);

        // Gemini API 요청 본문 구조
        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", prompt)
                        ))
                ),
                "generationConfig", Map.of(
                        "responseMimeType", "application/json"
                )
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        String urlWithKey = GEMINI_API_URL + "?key=" + apiKey;

        log.info(">>> Sending request to Gemini API...");
        String response = restTemplate.postForObject(urlWithKey, entity, String.class);
        log.info("<<< Received response from Gemini API.");
        return response;
    }

    // DALL-E 호출 대신 예시 URL을 생성하는 메서드
    private String createImagePlaceholderUrl(String seedText) {
        return "https://picsum.photos/seed/" + seedText.hashCode() + "/512/512";
    }

    private String createGeminiPrompt(String originalText, String userPrompt) {
        // AI가 응답할 카테고리 목록을 한글로 제시 (Category Enum의 fromDisplayName과 매칭)
        String categories = "\"문학\", \"경제\", \"자기계발\", \"라이프스타일\", \"기타\"";

        return String.format(
                "당신은 콘텐츠의 가치를 평가하는 전문 편집자입니다. 아래의 '원본 텍스트'를 분석하여, 반드시 다음 규칙에 따라 JSON 객체 형식으로만 응답해주십시오.\n\n" +
                        "### 규칙:\n" +
                        "1. 'summary': 원본 텍스트의 핵심 내용을 담은 한 문장 요약.\n" +
                        "2. 'category': 반드시 다음 5가지 카테고리 중 가장 적합한 하나를 선택: [%s].\n" +
                        "3. 'price': 1에서 500 사이의 정수. 아래 '가격 책정 기준'을 참고하여 책정.\n\n" +
                        "### 가격 책정 기준:\n" +
                        "- 글의 분량 및 깊이: 글이 길고 심도 깊은 내용을 다룰수록 높은 가격.\n\n" +
                        "--- 원본 텍스트 ---\n" +
                        "%s\n\n" +
                        "--- 추가적인 요청사항 ---\n" +
                        "%s",
                categories, originalText, userPrompt
        );
    }

    private GptTextResult parseGeminiResponse(String jsonResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            // Gemini 응답 구조에 맞게 파싱 경로 수정
            JsonNode textNode = rootNode.path("candidates").get(0).path("content").path("parts").get(0).path("text");

            if (textNode.isMissingNode()) {
                log.error("Gemini response is missing 'text' field. Response: {}", jsonResponse);
                throw new RuntimeException("Failed to parse Gemini response: 'text' field is missing.");
            }

            // AI가 응답한 JSON 문자열을 다시 파싱
            JsonNode contentNode = objectMapper.readTree(textNode.asText());

            String summary = contentNode.path("summary").asText();
            String category = contentNode.path("category").asText();
            Integer price = contentNode.path("price").asInt();

            return new GptTextResult(summary, category, price);

        } catch (Exception e) {
            log.error("Failed to parse Gemini JSON response. Response: {}", jsonResponse, e);
            throw new RuntimeException("Failed to parse Gemini response.", e);
        }
    }

    @RequiredArgsConstructor
    @Getter
    @ToString
    private static class GptTextResult {
        private final String summary;
        private final String category;
        private final Integer price;
    }
}