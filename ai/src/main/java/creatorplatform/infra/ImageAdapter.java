package creatorplatform.infra;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class ImageAdapter extends AbstractOpenAiAdapter{

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String Image_API_URL = "https://api.openai.com/v1/images/generations";

    public String generateImage(String title, String summary, String category, String userPrompt) {
        HttpHeaders headers = createHeaders();

        String imagePrompt = createImagePromptFrom(title, summary, category, userPrompt);
        Map<String, Object> requestBody = Map.of(
                "model", "dall-e-3",
                "prompt", imagePrompt,
                "n", 1,
                "size", "1024x1792",
                "quality", "hd"
        );
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        System.out.println(">>> Sending request to image API...");
        ResponseEntity<String> response = restTemplate.postForEntity(Image_API_URL, entity, String.class);
        System.out.println("<<< Received response from image API.");

        return parseImageResponse(response.getBody());
    }

    private String createImagePromptFrom(String title, String summary, String category, String userPrompt) {
        return String.format(
                "Create a professional, high-quality book cover for an online content platform. " +
                "The book belongs to the '%s' category, so the image must have an appropriate tone" +
                "\nThe main title of the book is '%s'. This title MUST be clearly visible " +
                "Place the title in the upper half of the image. " +
                "\n--- summary of the content---\n%s" +
                "\nCRITICAL: Do NOT include any other text, words, letters, or numbers in the image. Only the main title. " +
                "\n--- Additional Requirements ---\n%s",
                category, title, summary, userPrompt
        );
    }

    private String parseImageResponse(String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            return rootNode.path("data").get(0).path("url").asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse DALL-E response", e);
        }
    }

}
