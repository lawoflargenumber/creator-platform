package creatorplatform.infra;

import creatorplatform.domain.AiGeneratedContent;
import creatorplatform.domain.AiGeneratedContentRepository;
import creatorplatform.domain.port.AiGeneratorPort;
import creatorplatform.infra.dto.AiContentResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@RestController
@RequestMapping(value = "/ai")
public class AiController {

    @Autowired
    private AiGeneratedContentRepository repository;

    @Autowired
    private AiGeneratorPort aiGenerator;

    @GetMapping("/{id}")
    public ResponseEntity<AiContentResponse> getAiContents(@PathVariable Long id) {
        return repository.findById(id)
                .map(AiContentResponse::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/regenerate")
    @Transactional
    public ResponseEntity<AiContentResponse> regenerateContent(
            @PathVariable Long id,
            @RequestBody RegenerationRequest request
    ) {
        return repository.findById(id).map(process -> {
            AiGeneratorPort.AiGeneratedResult aiResult = aiGenerator.regenerate(
                    process.getTitle(),
                    process.getContent(),
                    request.getUserPrompt()
            );

            process.applyGeneratedContent(
                    aiResult.getSummary(),
                    aiResult.getPrice(),
                    aiResult.getImageUrl(),
                    aiResult.getCategory()
            );

            AiGeneratedContent updatedContent = repository.save(process);

            return ResponseEntity.ok(AiContentResponse.fromEntity(updatedContent));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/complete")
    @Transactional
    public ResponseEntity<Void> completeGeneration(@PathVariable Long id) {
        return repository.findById(id).map(process -> {
            process.completeGeneration();
            repository.save(process);
            return ResponseEntity.ok().<Void>build(); // 성공(200 OK) 응답
        }).orElse(ResponseEntity.notFound().build());
    }

    @Setter
    @Getter
    public static class RegenerationRequest {
        private String userPrompt;
    }


}
