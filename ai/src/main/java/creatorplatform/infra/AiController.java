package creatorplatform.infra;

import creatorplatform.domain.AiGeneratedContent;
import creatorplatform.domain.AiGeneratedContentRepository;
import creatorplatform.domain.AiService;
import creatorplatform.domain.ProcessingStatus;
import creatorplatform.domain.port.AiGeneratorPort;
import creatorplatform.infra.dto.AiContentResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Optional;

@RestController
@RequestMapping(value = "/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiGeneratedContentRepository repository;
    private final AiService aiService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getAiContents(@PathVariable Long id) {
        Optional<AiGeneratedContent> entity = repository.findById(id);

        if (entity.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        AiGeneratedContent contentEntity = entity.get();

        if (contentEntity.getStatus().equals(ProcessingStatus.PENDING)) {
            return ResponseEntity.badRequest().body("AI 콘텐츠가 아직 생성 중입니다.");
        }

        return ResponseEntity.ok(AiContentResponse.fromEntity(contentEntity));
    }

    @PostMapping("/{id}/regenerate")
    @Transactional
    public ResponseEntity<AiContentResponse> regenerateContent(
            @PathVariable Long id,
            @RequestBody RegenerationRequest request
    ) {
        return repository.findById(id)
                .map(entity -> {
                    aiService.regenerateContent(id, request.getUserPrompt());
                    return ResponseEntity.ok(AiContentResponse.fromEntity(entity));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/complete")
    @Transactional
    public ResponseEntity<Void> completeGeneration(
            @PathVariable Long id,
            @RequestBody CompleteRequest request
    ) {return repository.findById(id).map(process -> {
            process.setSummary(request.getSummary());
            process.completeGeneration();
            repository.save(process);
            return ResponseEntity.ok().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @Setter
    @Getter
    public static class RegenerationRequest {
        private String userPrompt;
    }

    @Setter
    @Getter
    public static class CompleteRequest {
        private String summary;
    }
}
