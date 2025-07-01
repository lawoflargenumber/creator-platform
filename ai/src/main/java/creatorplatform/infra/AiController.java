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
