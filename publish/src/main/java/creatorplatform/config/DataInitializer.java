package creatorplatform.config;

import creatorplatform.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.Date;

@Component
public class DataInitializer {

    @Autowired
    private ProductsRepository productsRepository;

    @PostConstruct
    public void initializeData() {
        // 테스트용 초기 데이터 생성
        createTestProducts();
    }

    private void createTestProducts() {
        // Product 1 - 일반 상품
        Products product1 = new Products();
        product1.setAuthorId(1L);
        product1.setAuthorNickname("작가김철수");
        product1.setTitle("Spring Boot 마스터클래스");
        product1.setContent("Spring Boot의 기본부터 고급 기능까지 완벽 가이드입니다. DI, AOP, Security, JPA 등 실무에 필요한 모든 내용을 다룹니다...");
        product1.setCategory("문학");
        product1.setPrice(15000);
        product1.setPublishedAt(new Date());
        product1.setViews(5);
        product1.setCoverImageUrl("https://example.com/spring-boot-cover.jpg");
        product1.setSummary("Spring Boot 완벽 가이드");
        product1.setIsBestseller(false);
        productsRepository.save(product1);

        // Product 2 - 베스트셀러 상품 (조회수 높음)
        Products product2 = new Products();
        product2.setAuthorId(2L);
        product2.setAuthorNickname("작가이영희");
        product2.setTitle("Kafka 실전 가이드");
        product2.setContent("Apache Kafka를 활용한 대용량 데이터 처리와 이벤트 드리븐 아키텍처 구현 방법을 실습을 통해 학습합니다...");
        product2.setCategory("경제");
        product2.setPrice(20000);
        product2.setPublishedAt(new Date());
        product2.setViews(25);  // 10 이상으로 베스트셀러
        product2.setCoverImageUrl("https://example.com/kafka-cover.jpg");
        product2.setSummary("실전 Kafka 마스터하기");
        product2.setIsBestseller(true);
        productsRepository.save(product2);

        // Product 3 - 무료 상품
        Products product3 = new Products();
        product3.setAuthorId(1L);
        product3.setAuthorNickname("작가김철수");
        product3.setTitle("Docker 기초 입문");
        product3.setContent("Docker 컨테이너 기술의 기본 개념부터 실습까지. 개발 환경 구성과 배포 자동화를 경험해보세요...");
        product3.setCategory("기타");
        product3.setPrice(0);  // 무료
        product3.setPublishedAt(new Date());
        product3.setViews(12);
        product3.setCoverImageUrl("https://example.com/docker-cover.jpg");
        product3.setSummary("Docker 기초부터 실습까지");
        product3.setIsBestseller(true);
        productsRepository.save(product3);

        // Product 4 - 고가 상품
        Products product4 = new Products();
        product4.setAuthorId(3L);
        product4.setAuthorNickname("작가박민수");
        product4.setTitle("클라우드 아키텍처 설계");
        product4.setContent("AWS, Azure, GCP 등 주요 클라우드 플랫폼에서의 확장 가능한 아키텍처 설계 방법론을 다룹니다...");
        product4.setCategory("자기계발");
        product4.setPrice(50000);
        product4.setPublishedAt(new Date());
        product4.setViews(8);
        product4.setCoverImageUrl("https://example.com/cloud-cover.jpg");
        product4.setSummary("클라우드 아키텍처 전문가 과정");
        product4.setIsBestseller(false);
        productsRepository.save(product4);

        // Product 5 - 인기 상품
        Products product5 = new Products();
        product5.setAuthorId(2L);
        product5.setAuthorNickname("작가이영희");
        product5.setTitle("React와 Node.js 풀스택 개발");
        product5.setContent("React 프론트엔드와 Node.js 백엔드를 활용한 풀스택 웹 애플리케이션 개발 완전정복...");
        product5.setCategory("라이프스타일");
        product5.setPrice(30000);
        product5.setPublishedAt(new Date());
        product5.setViews(18);
        product5.setCoverImageUrl("https://example.com/fullstack-cover.jpg");
        product5.setSummary("React + Node.js 풀스택 개발");
        product5.setIsBestseller(true);
        productsRepository.save(product5);

        System.out.println("✅ Products 테스트 데이터 5개 생성 완료");
        System.out.println("   - 일반 상품: 2개 (ID: 1, 4)");
        System.out.println("   - 베스트셀러: 3개 (ID: 2, 3, 5)");
        System.out.println("   - 무료 상품: 1개 (ID: 3)");
        System.out.println("   - 고가 상품: 1개 (ID: 4 - 50,000원)");
    }
} 