# View Test Scenarios

## 🎯 개요

Creator Platform의 **View 서비스**는 책 접근 권한 관리를 담당하는 MSA 컴포넌트입니다.

### 주요 기능
- 📖 **접근 권한 확인**: 구매이력/구독상태 기반 접근 판단
- 💰 **포인트 구매 가능성**: 사용자 포인트와 상품 가격 비교  
- 🛒 **포인트 구매 실행**: 포인트 차감 및 구매이력 생성

### 기술 스택
- **Backend**: Spring Boot 2.3.1 + JPA + H2
- **Infrastructure**: Docker + Kafka + Zookeeper
- **Port**: `8085`

---

## 🚀 서버 실행 방법

### 1. 필수 요구사항
```bash
# Docker가 설치되어 있어야 함
docker --version
docker-compose --version
```

### 2. View 서비스 실행
```bash
# 프로젝트 루트 디렉토리로 이동
cd creator-platform

# View 서비스만 실행 (Kafka, Zookeeper 자동 포함)
docker-compose -f build-docker-compose.yml up view

# 백그라운드 실행 (선택사항)
docker-compose -f build-docker-compose.yml up view -d
```

### 3. 실행 상태 확인
```bash
# 컨테이너 상태 확인
docker ps

# 로그 확인
docker-compose -f build-docker-compose.yml logs view --tail=20

# 서버 응답 확인
curl http://localhost:8085/actuator/health
```

### 4. 서버 종료
```bash
# 서비스 중지
docker-compose -f build-docker-compose.yml down
```

---

## 👥 테스트 시나리오

### 자동 생성 테스트 데이터

**View 서비스 시작시 `DataInitializer`가 자동으로 다음 데이터를 생성합니다:**

#### 사용자 프로필 (UserAccessProfile)
| 사용자ID | 구독상태 | 포인트 | 시나리오 설명 |
|----------|----------|--------|---------------|
| 1 | ❌ 비구독 | 50 | 포인트 부족 사용자 |
| 2 | ✅ 구독중 | 30 | 활성 구독자 |
| 3 | ❌ 비구독 | 150 | 포인트 충분하지만 비구독 |
| 4 | ❌ 비구독 | 20 | 포인트 부족하지만 구매이력 있음 |

#### 상품 가격 정보 (CheckPrice)
| 상품ID | 가격 |
|--------|------|
| 100 | 1000 포인트 |
| 200 | 2000 포인트 |

#### 구매 이력 (CheckIfBought)
| 사용자ID | 구매한 상품ID |
|----------|---------------|
| 1 | 200 |
| 2 | 300 |  
| 4 | 100, 200 |

---

## 🧪 API 테스트 방법

### API 엔드포인트 목록

#### 1. 📖 접근 권한 확인
```http
GET /userAccessProfiles/{userId}/accesstocontent?productId={productId}
```

#### 2. 💰 포인트 구매 가능성 확인  
```http
GET /userAccessProfiles/{userId}/checkpurchaseability?productId={productId}
```

#### 3. 🛒 포인트로 구매 실행
```http
POST /userAccessProfiles/{userId}/purchasewithpoints?productId={productId}
```

---

## 📋 테스트 케이스별 시나리오

### 🔥 핵심 테스트 케이스

#### **케이스 1: 구독자 접근** ✅
```bash
curl "http://localhost:8085/userAccessProfiles/2/accesstocontent?productId=100"
```
**예상 결과:**
```json
{
  "hasAccess": true,
  "reason": "SUBSCRIBED", 
  "message": "구독자입니다"
}
```

#### **케이스 2: 구매이력 기반 접근** ✅  
```bash
curl "http://localhost:8085/userAccessProfiles/4/accesstocontent?productId=100"
```
**예상 결과:**
```json
{
  "hasAccess": true,
  "reason": "PURCHASED",
  "message": "이미 구매한 책입니다"
}
```

#### **케이스 3: 접근 권한 없음** ❌
```bash
curl "http://localhost:8085/userAccessProfiles/1/accesstocontent?productId=100"
curl "http://localhost:8085/userAccessProfiles/3/accesstocontent?productId=100"
```
**예상 결과:**
```json
{
  "hasAccess": false,
  "reason": "NO_ACCESS",
  "message": "구매하거나 구독해야 합니다"
}
```

### 💰 포인트 구매 관련 테스트

#### **포인트 부족 - 구매 불가**
```bash
curl "http://localhost:8085/userAccessProfiles/1/checkpurchaseability?productId=100"
curl "http://localhost:8085/userAccessProfiles/3/checkpurchaseability?productId=100"
```
**예상 결과:**
```json
{
  "canPurchase": false,
  "userPoints": 50,  // 또는 150
  "productPrice": 1000,
  "remainingPoints": -950,  // 또는 -850
  "message": "포인트가 부족합니다"
}
```

### 🛒 구매 실행 테스트

#### **실제 포인트 구매 (충분한 포인트가 있다면)**
```bash
# 먼저 사용자4를 4000포인트로 수정한 후 테스트 
curl -X POST "http://localhost:8085/userAccessProfiles/4/purchasewithpoints?productId=200"
```

---

## 📊 전체 테스트 시나리오 실행

### 순차 테스트 스크립트
```bash
#!/bin/bash
echo "=== Creator Platform View 서비스 테스트 ==="

echo "1. 구독자 접근 테스트"
curl "http://localhost:8085/userAccessProfiles/2/accesstocontent?productId=100"
echo -e "\n"

echo "2. 구매이력 접근 테스트"  
curl "http://localhost:8085/userAccessProfiles/4/accesstocontent?productId=100"
echo -e "\n"

echo "3. 접근 권한 없음 테스트"
curl "http://localhost:8085/userAccessProfiles/1/accesstocontent?productId=100"
echo -e "\n"

echo "4. 포인트 부족 테스트"
curl "http://localhost:8085/userAccessProfiles/1/checkpurchaseability?productId=100"
echo -e "\n"

echo "=== 테스트 완료 ==="
```

---

## 🔍 비즈니스 로직 상세

### View 서비스의 역할 범위
- ✅ **판단**: 구매이력 존재 여부
- ✅ **판단**: 구독 상태 확인  
- ✅ **정보 제공**: 포인트 구매 가능성
- ❌ **판단하지 않음**: 포인트 충분 여부로 접근 허용

### 클라이언트 측 처리 로직
```
📖 책읽기 버튼 클릭
    ↓
🔍 accessToContent API 호출
    ↓
┌─ hasAccess: true → 책 내용 페이지로 이동
└─ hasAccess: false → 선택 페이지 표시
    ├─ "구독하기" 버튼 (Account 서비스)
    └─ "포인트로 구매하기" 버튼 (checkPurchaseability 확인 후)
```

---

## 🛠 트러블슈팅

### 일반적인 문제들

#### 1. **서버 시작 실패**
```bash
# 포트 충돌 확인
netstat -an | grep 8085

# 기존 컨테이너 정리
docker-compose -f build-docker-compose.yml down
docker system prune -f
```

#### 2. **컴파일 에러**
```bash
# 강제 리빌드
docker-compose -f build-docker-compose.yml up view --build
```

#### 3. **API 응답 없음**
```bash
# 서버 완전 시작 대기 (보통 1-2분)
docker-compose -f build-docker-compose.yml logs view --tail=50

# "Started ViewApplication" 메시지 확인
```

#### 4. **테스트 데이터 없음**
```bash
# DataInitializer 로그 확인
docker-compose -f build-docker-compose.yml logs view | grep "테스트 데이터"

# "✅ UserAccessProfile 테스트 데이터 3개 생성 완료" 메시지 확인
```

---

## 📝 참고사항

### 개발 환경 정보
- **Java**: OpenJDK 11
- **Spring Boot**: 2.3.1.RELEASE  
- **Database**: H2 In-Memory
- **Profile**: docker

### 포트 정보
- **View Service**: 8085
- **Kafka**: 9092
- **Zookeeper**: 2181

### 데이터베이스 초기화
- 서비스 재시작 시마다 **H2 In-Memory DB 초기화**
- **DataInitializer가 자동으로 테스트 데이터 재생성**

---

**📅 마지막 업데이트**: 2025-06-29  
**📋 테스트 완료**: View 서비스 접근 권한 관리 로직 