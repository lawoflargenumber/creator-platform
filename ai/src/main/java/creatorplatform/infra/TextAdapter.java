package creatorplatform.infra;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class TextAdapter extends AbstractOpenAiAdapter {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String Text_API_URL = "https://api.openai.com/v1/chat/completions";

    public TextResult generateTextualContent(String title, String content) {
        HttpHeaders headers = createHeaders();

        String textPrompt = createTextPrompt(title, content);
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4o-mini",
                "messages", new Object[]{ Map.of("role", "user", "content", textPrompt) },
                "response_format", Map.of("type", "json_object")
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        System.out.println(">>> Sending request to GPT API...");
        ResponseEntity<String> response = restTemplate.postForEntity(Text_API_URL, entity, String.class);
        System.out.println("<<< Received response from GPT API.");

        return parseGptResponse(response.getBody());
    }


    private String createTextPrompt(String title, String content) {
        String categories = "\"문학\", \"경제\", \"자기계발\", \"라이프스타일\", \"기타\"";

        return String.format(
                "당신은 콘텐츠의 가치를 평가하는 전문 편집자입니다. 아래의 '원본 텍스트'를 분석하여, 반드시 다음 규칙에 따라 JSON 객체 형식으로만 응답해주십시오.\n\n" +
                        "### 규칙:\n" +
                        "1. 'summary': 원본 텍스트의 핵심 내용을 담은 한 문단 요약.\n" +
                        "2. 'category': 반드시 다음 5가지 카테고리 중 가장 적합한 하나를 선택: [%s].\n" +
                        "3. 'price': 1에서 500 사이의 정수. 아래 '가격 책정 기준'을 참고하여 책정.\n\n" +
                        "### 가격 책정 기준:\n" +
                        "- 글의 분량 및 깊이: 글이 길고 심도 깊은 내용을 다룰수록 높은 가격.\n" +
                        "--- 원본 텍스트 ---\n" +
                        "[%s]\n\n" +
                        "%s\n\n",
                categories, title, content
        );
    }

    private TextResult parseGptResponse(String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            String contentJson = rootNode.path("choices").get(0).path("message").path("content").asText();
            JsonNode contentNode = objectMapper.readTree(contentJson);

            String summary = contentNode.path("summary").asText();
            String category = contentNode.path("category").asText();
            Integer price = contentNode.path("price").asInt();

            return new TextResult(summary, category, price);

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse GPT response", e);
        }
    }

    @AllArgsConstructor
    @Getter
    @ToString
    public static class TextResult {
        private final String summary;
        private final String category;
        private final Integer price;
    }
}
