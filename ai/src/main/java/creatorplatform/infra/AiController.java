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

    // @PostMapping("/test")
    // @Transactional
    // public ResponseEntity<String> testPublishEvent() {
    //     RequestedPublication event = new RequestedPublication();

    //     event.setId(999L);
    //     event.setAuthorId(111L);
    //     event.setAuthorNickname("홍길동");
    //     event.setTitle("기형도 / 빈집");
    //     event.setContent("사랑을 잃고 나는 쓰네\n" + //
    //                     "\n" + //
    //                     "잘 있거라, 짧았던 밤들아\n" + //
    //                     "창밖을 떠돌던 겨울 안개들아\n" + //K
    //                     "아무것도 모르던 촛불들아, 잘 있거라\n" + //
    //                     "공포를 기다리던 흰 종이들아\n" + //
    //                     "망설임을 대신하던 눈물들아\n" + //
    //                     "잘 있거라, 더 이상 내 것이 아닌 열망들아\n" + //
    //                     "\n" + //
    //                     "장님처럼 나 이제 더듬거리며 문을 잠그네\n" + //
    //                     "가엾은 내 사랑 빈집에 갇혔네 ");

    //     event.publishAfterCommit();
    //     return ResponseEntity.ok("Test event 'RequestedPublication' has been published successfully.");
    // }
}
