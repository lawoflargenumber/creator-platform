package creatorplatform.domain;

import creatorplatform.domain.port.AiGeneratorPort;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class AiService {

    private static final Logger log = LoggerFactory.getLogger(AiService.class);
    private final AiGeneratedContentRepository repository;
    private final AiGeneratorPort aiGenerator;

    @Async
    @Transactional
    public void generateInitialContent(RequestedPublication event) {
        AiGeneratedContent contentEntity = AiGeneratedContent.fromEvent(event);
        repository.save(contentEntity);

        AiGeneratorPort.AiGeneratedResult aiResult = aiGenerator.generate(
                contentEntity.getTitle(), contentEntity.getContent());

        contentEntity.applyGeneratedContent(
                aiResult.getSummary(),
                aiResult.getPrice(),
                aiResult.getImageUrl(),
                aiResult.getCategory()
        );

        repository.save(contentEntity);
        log.info("Finished initial content generation for bookId: {}", event.getDraftId());
    }

    @Async
    @Transactional
    public void regenerateContent(Long id, String userPrompt) {
        log.info("Starting content regeneration for id: {}", id);
        repository.findById(id).ifPresent(contentEntity -> {
            AiGeneratorPort.AiGeneratedResult aiResult = aiGenerator.regenerate(
                    contentEntity.getTitle(),
                    contentEntity.getContent(),
                    userPrompt
            );

            contentEntity.applyGeneratedContent(
                    aiResult.getSummary(),
                    aiResult.getPrice(),
                    aiResult.getImageUrl(),
                    aiResult.getCategory()
            );

            repository.save(contentEntity);
            log.info("Finished content regeneration for id: {}", id);
        });
    }

}
