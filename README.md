# Creator Platform - AI 기반 웹 출간 플랫폼 프로젝트

## 📌 개요
에이블스쿨 5차 미니프로젝트
크리에이터들이 콘텐츠를 작성, 발행할 수 있는 종합 플랫폼입니다. 
마이크로서비스 아키텍처 기반으로 구축되어 사용자 관리, 콘텐츠 작성, 발행, 조회, AI 기반 콘텐츠 생성 기능을 제공합니다.

## 🛠 기술 스택
- **Backend**: Java 11, Spring Boot 2.3.1, Spring Cloud Gateway
- **Database**: H2 Database (개발), JPA/Hibernate
- **Message Queue**: Apache Kafka, Zookeeper
- **Container**: Docker, Kubernetes
- **Security**: Spring Security, JWT
- **AI Integration**: OpenAI API
- **DevOps**: Azure DevOps

## ✨ 주요 기능
- **사용자 관리**: 회원가입, 로그인, 작가 신청 및 승인 시스템
- **콘텐츠 작성**: 드래프트 저장, 수정, 발행 요청 워크플로우
- **콘텐츠 발행**: 자동 발행 처리, 카테고리별 분류, 가격 설정
- **조회 및 구독**: 콘텐츠 조회, 구독 관리, 접근 권한 제어
- **AI 콘텐츠 생성**: OpenAI 기반 자동 콘텐츠 생성 및 요약
- **실시간 이벤트**: Kafka 기반 마이크로서비스 간 이벤트 스트리밍
- **베스트셀러 시스템**: 조회수 기반 인기 콘텐츠 자동 선정

## 📂 프로젝트 구조
```
creator-platform/
├── account/                    # 사용자 관리 서비스
│   ├── src/main/java/creatorplatform/
│   │   ├── domain/            # 사용자 도메인 모델
│   │   │   ├── Users.java     # 사용자 엔티티
│   │   │   ├── RegisterUserCommand.java
│   │   │   └── AuthorshipAccepted.java
│   │   └── infra/             # 인프라스트럭처 레이어
│   └── kubernetes/            # K8s 배포 설정
├── writing/                   # 콘텐츠 작성 서비스
│   ├── src/main/java/creatorplatform/
│   │   ├── domain/            # 작성 도메인 모델
│   │   │   ├── Drafts.java    # 드래프트 엔티티
│   │   │   ├── SaveDraftCommand.java
│   │   │   └── RequestedPublication.java
│   │   └── infra/             # REST API 컨트롤러
│   └── kubernetes/            # K8s 배포 설정
├── publish/                   # 콘텐츠 발행 서비스
│   ├── src/main/java/creatorplatform/
│   │   ├── domain/            # 발행 도메인 모델
│   │   │   ├── Products.java  # 상품 엔티티
│   │   │   ├── TrackViewCommand.java
│   │   │   └── ViewTracked.java
│   │   └── infra/             # 발행 정책 및 컨트롤러
│   └── kubernetes/            # K8s 배포 설정
├── view/                      # 조회 및 구독 서비스
│   ├── src/main/java/creatorplatform/
│   │   ├── domain/            # 조회 도메인 모델
│   │   │   ├── UserAccessProfile.java
│   │   │   ├── ViewHistory.java
│   │   │   └── AccessToContentCommand.java
│   │   ├── service/           # 비즈니스 로직
│   │   └── infra/             # 뷰 핸들러 및 저장소
│   └── kubernetes/            # K8s 배포 설정
├── ai/                        # AI 콘텐츠 생성 서비스
│   ├── src/main/java/creatorplatform/
│   │   ├── domain/            # AI 도메인 모델
│   │   │   ├── AiGeneratedContent.java
│   │   │   ├── AiService.java
│   │   │   └── ProcessingStatus.java
│   │   └── infra/             # AI API 컨트롤러
│   │       └── AiController.java
│   └── kubernetes/            # K8s 배포 설정
├── gateway/                   # API 게이트웨이
│   ├── src/main/java/creatorplatform/
│   │   └── security/          # JWT 인증 필터
│   │       ├── JwtUtils.java
│   │       └── JwtAuthenticationFilter.java
│   └── kubernetes/            # K8s 배포 설정
├── frontend/                  # Vue.js 프론트엔드
│   ├── src/
│   │   ├── components/        # UI 컴포넌트
│   │   │   ├── Users.vue      # 사용자 관리
│   │   │   ├── Drafts.vue     # 드래프트 관리
│   │   │   ├── Products.vue   # 상품 관리
│   │   │   ├── ui/            # 그리드 컴포넌트
│   │   │   └── vo/            # 값 객체 컴포넌트
│   │   ├── router/            # Vue Router 설정
│   │   ├── plugins/           # Vuetify, Axios 플러그인
│   │   └── layouts/           # 레이아웃 컴포넌트
│   ├── package.json           # 프론트엔드 의존성
│   └── vite.config.js         # Vite 빌드 설정
├── infra/                     # 인프라스트럭처
│   └── docker-compose.yml     # Kafka & Zookeeper 설정
├── kubernetes/                # 전체 K8s 설정
│   ├── template.yml           # 배포 템플릿
│   └── test.yml               # 테스트 설정
├── istio-1.20.8/             # Istio 서비스 메시
│   ├── manifests/            # Istio 매니페스트
│   └── samples/              # 예제 설정
└── github/workflows/         # CI/CD 파이프라인
    └── github-action.yml     # GitHub Actions 설정
```
