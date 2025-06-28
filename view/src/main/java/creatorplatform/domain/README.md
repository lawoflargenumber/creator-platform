# Domain Layer Architecture Guide

## 📋 개요

이 문서는 Creator Platform의 View 서비스에서 구현된 Domain Layer 아키텍처와 데이터 흐름을 설명합니다.

## 🏗️ 현재 아키텍처 구조

### 전체 계층 구조
```
┌─────────────────────────────────┐
│         Controller              │ ← HTTP 요청/응답 처리
│     (Presentation Layer)        │
└─────────────────────────────────┘
                 ↓
┌─────────────────────────────────┐
│    Application Service          │ ← 트랜잭션 경계, 인프라 조율
│    (Application Layer)          │
└─────────────────────────────────┘
                 ↓
┌─────────────────────────────────┐
│       Domain Entity             │ ← 비즈니스 로직, 이벤트 처리
│      (Domain Layer)             │
└─────────────────────────────────┘
                 ↓
┌─────────────────────────────────┐
│       Repository                │ ← 데이터 영속성
│   (Infrastructure Layer)        │
└─────────────────────────────────┘
```

## 🔄 데이터 흐름 분석

### accessToContent 기능 흐름

#### 1단계: Controller (HTTP 처리)
**파일**: `UserAccessProfileController.java`
```java
@PutMapping("/userAccessProfiles/{id}/accesstocontent")
public UserAccessProfile accessToContent(
    @PathVariable Long id,
    @RequestBody AccessToContentCommand command
) {
    return userAccessProfileService.accessToContent(id, command);
}
```

**역할**:
- HTTP 요청 파라미터 파싱
- Request Body를 Command 객체로 변환
- Application Service로 위임
- Response 반환

#### 2단계: Application Service (인프라 조율)
**파일**: `UserAccessProfileService.java`
```java
public UserAccessProfile accessToContent(Long userAccessProfileId, AccessToContentCommand command) {
    // 1. 구매 여부 확인 (인프라 의존성)
    boolean isBought = checkIfBoughtRepository.existsByIdAndProductId(
        userAccessProfileId, command.getProductId()
    );
    
    // 2. 사용자 프로필 조회
    UserAccessProfile profile = userAccessProfileRepository.findById(userAccessProfileId).get();
    
    // 3. 도메인 비즈니스 로직 실행
    profile.accessToContent(command, isBought);
    
    // 4. 영속성 저장
    userAccessProfileRepository.save(profile);
    
    return profile;
}
```

**역할**:
- Repository를 통한 데이터 조회
- 외부 의존성 처리 (구매 여부 확인)
- Domain Entity 비즈니스 로직 호출
- 트랜잭션 경계 관리
- 데이터 영속성 처리

#### 3단계: Domain Entity (비즈니스 로직)
**파일**: `UserAccessProfile.java`
```java
public void accessToContent(AccessToContentCommand accessToContentCommand, boolean isBought) {
    // 비즈니스 규칙 검증
    boolean hasSubscription = isSubscribed != null && isSubscribed;
    boolean hasSufficientPoints = points != null && points >= 100;
    
    // 접근 권한 결정 및 이벤트 발행
    if (isBought || hasSubscription || hasSufficientPoints) {
        AccessGranted accessGranted = new AccessGranted(this);
        accessGranted.setProductId(accessToContentCommand.getProductId());
        accessGranted.publishAfterCommit();
    } else {
        AccessDenied accessDenied = new AccessDenied(this);
        accessDenied.publishAfterCommit();
    }
}
```

**역할**:
- 순수 비즈니스 로직 실행
- 도메인 규칙 적용
- 도메인 이벤트 생성 및 발행
- 엔티티 상태 관리

## 📊 Domain 폴더 구성 요소

### Entity Types
| 파일명 | 타입 | 역할 | DDD 패턴 |
|--------|------|------|----------|
| `UserAccessProfile.java` | Aggregate Root | 핵심 비즈니스 로직 | Domain Entity |
| `CheckIfBought.java` | CQRS View | 읽기 최적화 모델 | CQRS Pattern |

### Event Types  
| 파일명 | 타입 | 역할 |
|--------|------|------|
| `AccessGranted.java` | Domain Event | 접근 허용 알림 |
| `AccessDenied.java` | Domain Event | 접근 거부 알림 |

### Command Types
| 파일명 | 타입 | 역할 |
|--------|------|------|
| `AccessToContentCommand.java` | Command DTO | 요청 데이터 캡슐화 |

### Repository Types
| 파일명 | 타입 | 역할 |
|--------|------|------|
| `UserAccessProfileRepository.java` | Repository Interface | 데이터 접근 추상화 |

## 🎯 설계 원칙

### DDD 원칙 적용
- ✅ **Aggregate Root**: UserAccessProfile이 일관성 경계 관리
- ✅ **Domain Events**: 상태 변경을 이벤트로 전파
- ✅ **Repository Pattern**: 데이터 접근 추상화
- ✅ **Command Pattern**: 요청 데이터 캡슐화

### 계층별 책임 분리
- **Controller**: HTTP 프로토콜 처리만
- **Application Service**: 인프라 조율 및 트랜잭션 관리
- **Domain Entity**: 순수 비즈니스 로직
- **Repository**: 데이터 영속성

## 🔧 이벤트 처리 방식

### 현재 방식: Domain Entity에서 직접 발행
```java
// 장점: 구현 단순, 직관적, 빠른 개발
// 특징: 실용적 DDD 접근법
AccessGranted event = new AccessGranted(this);
event.publishAfterCommit(); // 도메인에서 직접 발행
```

### 이벤트 흐름
```
Domain Logic Execution
         ↓
Event Creation & Publishing
         ↓
Kafka Event Bus
         ↓
Other Microservices
```

## 🧪 테스트 전략

### 계층별 테스트 접근
```java
// Domain Logic Test (단위 테스트)
@Test
void testAccessToContentLogic() {
    UserAccessProfile profile = createTestProfile();
    profile.accessToContent(command, true);
    // 비즈니스 로직만 검증
}

// Service Layer Test (통합 테스트)
@Test  
void testAccessToContentService() {
    // Mock Repository 사용
    when(repository.findById(1L)).thenReturn(profile);
    service.accessToContent(1L, command);
    // 조율 로직 검증
}
```

## 📈 확장 가능성

### Domain Service 분리 (복잡한 로직 시)
```java
// 복잡한 비즈니스 로직이 증가할 경우
Controller → Application Service → Domain Service → Domain Entity
```

### Pure Domain 패턴 (이론적 DDD)
```java
// 엄격한 계층 분리가 필요한 경우
Domain: 이벤트 수집만
Application Service: 이벤트 발행
```

## 🎯 Best Practices

### 1. 단일 책임 원칙
- 각 계층이 명확한 하나의 책임만 수행

### 2. 의존성 방향 준수
```
Controller → Service → Domain ← Repository
     ✅          ✅        ✅
```

### 3. 도메인 중심 설계
- 비즈니스 로직은 Domain Layer에 집중
- 기술적 관심사는 Infrastructure Layer에 분리

### 4. 이벤트 기반 통신
- 마이크로서비스 간 느슨한 결합
- 도메인 상태 변경을 이벤트로 전파

## 🚀 개발 가이드라인

### 새로운 기능 추가 시
1. **Command 정의**: 요청 데이터 구조화
2. **Domain Logic 구현**: 비즈니스 규칙 적용
3. **Event 정의**: 상태 변경 알림
4. **Service 조율**: 인프라 의존성 처리
5. **Controller 연결**: HTTP API 노출

### 코드 품질 유지
- Domain Entity는 순수 비즈니스 로직만 포함
- Application Service에서 복잡한 조율 로직 관리
- Repository는 단순한 데이터 접근만 담당
- 이벤트는 불변 객체로 설계

---

## 📚 참고 자료

- **DDD (Domain-Driven Design)**: Eric Evans
- **Clean Architecture**: Robert C. Martin  
- **Microservices Patterns**: Chris Richardson
- **Event-Driven Architecture**: Hugh McKee

---

*이 문서는 Creator Platform View 서비스의 Domain Layer 구현을 기준으로 작성되었습니다.*
*궁금한 점이나 개선 사항이 있다면 팀 내 DDD 전문가와 상의하세요.* 