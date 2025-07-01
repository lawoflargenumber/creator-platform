package creatorplatform.config;

import creatorplatform.domain.*;
import creatorplatform.infra.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.Date;

@Component
public class DataInitializer {

    @Autowired
    private DraftsRepository draftsRepository;

    @Autowired
    private CheckAuthorsRepository checkAuthorsRepository;

    @PostConstruct
    public void initializeData() {
        // 테스트용 초기 데이터 생성
        createTestAuthors();
        createTestDrafts();
    }

    private void createTestAuthors() {
        // 작가 1
        CheckAuthors author1 = new CheckAuthors();
        author1.setNickname("작가김철수");
        checkAuthorsRepository.save(author1);

        // 작가 2  
        CheckAuthors author2 = new CheckAuthors();
        author2.setNickname("작가이영희");
        checkAuthorsRepository.save(author2);

        // 작가 3
        CheckAuthors author3 = new CheckAuthors();
        author3.setNickname("작가박민수");
        checkAuthorsRepository.save(author3);

        System.out.println("✅ CheckAuthors 테스트 데이터 3개 생성 완료");
    }

    private void createTestDrafts() {
        // Draft 1 - DRAFT 상태
        Drafts draft1 = new Drafts();
        draft1.setAuthorId(1L);
        draft1.setAuthorNickname("작가김철수");
        draft1.setTitle("Spring Boot 마이크로서비스 아키텍처");
        draft1.setContent("Spring Boot를 활용한 마이크로서비스 아키텍처 설계 방법을 다룹니다...");
        draft1.setStatus(Drafts.Status.DRAFT);
        draft1.setCreatedAt(new Date());
        draft1.setLastUpdatedAt(new Date());
        draftsRepository.save(draft1);

        // Draft 2 - DRAFT 상태
        Drafts draft2 = new Drafts();
        draft2.setAuthorId(2L);
        draft2.setAuthorNickname("작가이영희");
        draft2.setTitle("Kafka 이벤트 드리븐 아키텍처 구현");
        draft2.setContent("Apache Kafka를 활용한 이벤트 드리븐 아키텍처 구현 방법을 살펴봅시다...");
        draft2.setStatus(Drafts.Status.DRAFT);
        draft2.setCreatedAt(new Date());
        draft2.setLastUpdatedAt(new Date());
        draftsRepository.save(draft2);

        // Draft 3 - REQUESTED 상태
        Drafts draft3 = new Drafts();
        draft3.setAuthorId(1L);
        draft3.setAuthorNickname("작가김철수");
        draft3.setTitle("Docker 컨테이너 오케스트레이션");
        draft3.setContent("Docker와 Kubernetes를 활용한 컨테이너 오케스트레이션 가이드...");
        draft3.setStatus(Drafts.Status.REQUESTED);
        draft3.setCreatedAt(new Date());
        draft3.setLastUpdatedAt(new Date());
        draftsRepository.save(draft3);

        // Draft 4 - PUBLISHED 상태
        Drafts draft4 = new Drafts();
        draft4.setAuthorId(2L);
        draft4.setAuthorNickname("작가이영희");
        draft4.setTitle("클라우드 네이티브 애플리케이션 개발");
        draft4.setContent("클라우드 환경에서 네이티브 애플리케이션을 개발하는 방법론...");
        draft4.setStatus(Drafts.Status.PUBLISHED);
        draft4.setCreatedAt(new Date());
        draft4.setLastUpdatedAt(new Date());
        draftsRepository.save(draft4);

        // Draft 5 - DRAFT 상태 (작가3)
        Drafts draft5 = new Drafts();
        draft5.setAuthorId(3L);
        draft5.setAuthorNickname("작가박민수");
        draft5.setTitle("DevOps CI/CD 파이프라인 구축");
        draft5.setContent("Jenkins와 GitLab을 활용한 CI/CD 파이프라인 구축 실습...");
        draft5.setStatus(Drafts.Status.DRAFT);
        draft5.setCreatedAt(new Date());
        draft5.setLastUpdatedAt(new Date());
        draftsRepository.save(draft5);

        System.out.println("✅ Drafts 테스트 데이터 5개 생성 완료");
        System.out.println("   - DRAFT 상태: 3개 (ID: 1, 2, 5)");
        System.out.println("   - REQUESTED 상태: 1개 (ID: 3)");
        System.out.println("   - PUBLISHED 상태: 1개 (ID: 4)");
    }
} 