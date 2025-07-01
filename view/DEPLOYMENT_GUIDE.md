# 🚀 Creator Platform View 서비스 배포 가이드

## 📋 **개요**

이 가이드는 Docker 환경에서 Kafka 이벤트 스트리밍 아키텍처와 함께 Creator Platform View 서비스를 배포하고 실행하는 단계별 지침을 제공합니다.

## 🎯 **아키텍처 개요**

```
┌─────────────────────────────────────────────────────────────┐
│                     Docker 환경                            │
│  ┌─────────────────────────────────────────────────────────┐ │
│  │              Docker 네트워크                           │ │
│  │                                                         │ │
│  │  ┌─────────────────┐          ┌─────────────────────────┐│ │
│  │  │   View 서비스   │─────────▶│        Kafka            ││ │
│  │  │ (포트 8085)     │          │  my-kafka:29092 ◀──────┤│ │
│  │  │ Spring Boot     │          │  포트: 9092:9092        ││ │
│  │  │ + Event Stream  │          │  토픽: creatorplatform  ││ │
│  │  └─────────────────┘          └─────────────────────────┘│ │
│  │           │                            │                 │ │
│  │           │                    ┌─────────────────────────┐│ │
│  │           │                    │      Zookeeper         ││ │
│  │           │                    │  포트: 2181:2181       ││ │
│  │           │                    │  Kafka 코디네이션      ││ │
│  │           │                    └─────────────────────────┘│ │
│  └─────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
```

## 🛠️ **사전 요구사항**

### **시스템 요구사항**
- Windows 10/11 with WSL2
- Docker Desktop 4.0+ 
- Git Bash 또는 PowerShell
- 최소 4GB RAM 사용 가능
- 최소 10GB 디스크 여유 공간

### **소프트웨어 설치 확인**
```bash
# Docker 설치 확인
docker --version
# 예상 결과: Docker version 28.2.2 이상

# Docker Compose 확인
docker compose version
# 예상 결과: Docker Compose version v2.37.1 이상
```

## 🚀 **빠른 시작**

### **1단계: Docker Desktop 시작**
```bash
# Windows에서 Docker Desktop 애플리케이션 시작
# Docker Desktop이 완전히 초기화될 때까지 대기 (녹색 상태)

# Docker 실행 확인
docker ps
# 오류 없이 빈 목록이 반환되어야 함
```

### **2단계: 프로젝트 루트로 이동**
```bash
cd /c/creator-platform
# 또는
cd C:/creator-platform
```

### **3단계: 인프라 서비스 시작**
```bash
# Zookeeper 먼저 시작
docker compose -f build-docker-compose.yml up -d zookeeper

# Zookeeper 완전 시작 대기 (30초)
sleep 30

# Kafka 시작
docker compose -f build-docker-compose.yml up -d kafka

# Kafka 완전 시작 대기 (60초)
sleep 60
```

### **4단계: View 서비스 시작**
```bash
# View 서비스 시작 (포그라운드 - 로그 모니터링용)
docker compose -f build-docker-compose.yml up view

# 또는 백그라운드 실행
docker compose -f build-docker-compose.yml up -d view
```

### **5단계: 배포 확인**
```bash
# Health 엔드포인트 확인
curl http://localhost:8085/actuator/health
# 예상 결과: {"status":"UP"}

# 서비스 로그 확인
docker logs creator-platform-view-1 | grep "Started ViewApplication"
# 예상 결과: Started ViewApplication in XX.XXX seconds
```

## 🔧 **상세 설정**

### **Kafka 설정**

**이미지 버전**: `confluentinc/cp-kafka:7.4.0`
- ✅ **안정적인 버전** (latest의 KRaft 모드 문제 회피)
- ✅ **Spring Cloud Stream과 호환**

**포트 설정**:
```yaml
ports:
  - "9092:9092"     # 외부 접근 포트
environment:
  KAFKA_ADVERTISED_LISTENERS: 
    - PLAINTEXT://my-kafka:29092      # 내부 컨테이너 통신
    - PLAINTEXT_HOST://my-kafka:9092  # 외부 호스트 접근
```

**토픽 설정**:
- **토픽 이름**: `creatorplatform`
- **파티션**: 1 (자동 생성)
- **복제 인수**: 1

### **View 서비스 설정**

**애플리케이션 프로필**:
```yaml
# 기본 프로필 (개발환경)
server:
  port: 8080
spring:
  cloud:
    stream:
      kafka:
        binder:
          brokers: localhost:9092

# Docker 프로필 (컨테이너 환경)  
server:
  port: 8085
spring:
  profiles: docker
  cloud:
    stream:
      kafka:
        binder:
          brokers: my-kafka:29092    # 내부 컨테이너 통신
```

**포트 매핑**:
```yaml
ports:
  - "8085:8085"    # 호스트:컨테이너
```

## 🔍 **검증 및 테스트**

### **1. 인프라 상태 확인**

**Zookeeper 확인**:
```bash
# Zookeeper 상태 확인
docker ps | grep zookeeper
# 예상 결과: creator-platform-zookeeper-1 Up X minutes

# Zookeeper 로그 확인
docker logs creator-platform-zookeeper-1 | tail -5
# 오류가 없어야 함
```

**Kafka 확인**:
```bash
# Kafka 상태 확인
docker ps | grep kafka
# 예상 결과: my-kafka Up X minutes

# Kafka 토픽 목록 확인
docker exec my-kafka kafka-topics --list --bootstrap-server localhost:9092
# 예상 결과: creatorplatform (자동 생성됨)

# Kafka 시작 완료 로그 확인
docker logs my-kafka | grep "started"
# 예상 결과: [KafkaServer id=1] started
```

### **2. View 서비스 상태 확인**

**서비스 상태**:
```bash
# 컨테이너 상태 확인
docker ps | grep view
# 예상 결과: creator-platform-view-1 Up X minutes

# 애플리케이션 시작 확인
docker logs creator-platform-view-1 | grep "Tomcat started"
# 예상 결과: Tomcat started on port(s): 8085 (http)
```

**API 엔드포인트 테스트**:
```bash
# Health Check
curl -X GET http://localhost:8085/actuator/health
# 예상 결과: {"status":"UP"}

# AccessToContent 엔드포인트 테스트
curl -X PUT http://localhost:8085/userAccessProfiles/1/accesstocontent \
     -H "Content-Type: application/json" \
     -d '{"id": 1, "productId": 123, "userId": "test-user"}'
# 예상 결과: 500 에러 (정상 - 테스트 데이터 없음), 하지만 엔드포인트 접근 가능
```

### **3. Kafka 이벤트 스트리밍 확인**

**Consumer 테스트**:
```bash
# Kafka 컨슈머 시작 (새 터미널)
docker exec my-kafka kafka-console-consumer \
  --topic creatorplatform \
  --bootstrap-server localhost:9092 \
  --from-beginning

# 성공적으로 연결되고 메시지 대기 상태가 되어야 함
```

**이벤트 발행 테스트**:
```bash
# 이벤트 발행자 테스트 (테스트 데이터 사용 가능할 때)
docker exec my-kafka kafka-console-producer \
  --topic creatorplatform \
  --bootstrap-server localhost:9092

# 테스트 메시지 입력 후 Enter:
{"eventType":"TestEvent","userId":"test-user","timestamp":"2025-06-29T14:00:00Z"}

# 컨슈머 터미널에 표시되어야 함
```

## 🚨 **문제 해결**

### **일반적인 문제 및 해결책**

**1. Docker Desktop이 실행되지 않음**
```bash
# 증상: "Cannot connect to the Docker daemon"
# 해결책: Docker Desktop 시작 후 완전 초기화 대기
```

**2. 포트가 이미 사용 중**
```bash
# 증상: "Port 8085 is already allocated"
# 해결책: 충돌하는 서비스 중단
docker compose -f build-docker-compose.yml down
netstat -ano | findstr :8085  # 포트 사용 프로세스 찾기
taskkill /PID <PID> /F        # 필요시 프로세스 종료
```

**3. Kafka 연결 실패**
```bash
# 증상: "No resolvable bootstrap urls"
# 확인: Kafka 컨테이너 상태
docker logs my-kafka | grep ERROR

# 해결책: 적절한 순서로 Kafka 재시작
docker compose -f build-docker-compose.yml stop kafka
docker compose -f build-docker-compose.yml up -d kafka
```

**4. View 서비스 500 에러**
```bash
# 증상: "UserAccessProfile not found"
# 원인: 데이터베이스에 테스트 데이터 없음
# 해결책: 정상적인 동작 - 테스트 데이터 생성 또는 기존 ID 사용
```

**5. Kafka 최신 버전 문제**
```bash
# 증상: "KAFKA_PROCESS_ROLES is required"
# 해결책: docker-compose에서 버전 7.4.0으로 이미 수정됨
image: confluentinc/cp-kafka:7.4.0  # ✅ 안정적인 버전
```

### **로그 모니터링 명령어**

```bash
# 모든 서비스 로그 보기
docker compose -f build-docker-compose.yml logs -f

# 특정 서비스 로그 보기
docker logs -f creator-platform-view-1
docker logs -f my-kafka
docker logs -f creator-platform-zookeeper-1

# 특정 이벤트 로그 필터링
docker logs creator-platform-view-1 | grep -E "(ERROR|Exception|Started)"
```

## 📊 **성능 모니터링**

### **리소스 사용량 확인**
```bash
# 컨테이너 리소스 사용량 확인
docker stats

# 예상 사용량:
# view:      ~200-400MB RAM, 시작 시 ~10-20% CPU
# kafka:     ~150-300MB RAM, ~5-15% CPU  
# zookeeper: ~50-100MB RAM, ~2-5% CPU
```

### **애플리케이션 메트릭**
```bash
# Spring Boot Actuator 엔드포인트
curl http://localhost:8085/actuator/metrics
curl http://localhost:8085/actuator/info
curl http://localhost:8085/actuator/env
```

## 🛑 **종료 절차**

### **정상 종료**
```bash
# View 서비스 먼저 중단
docker compose -f build-docker-compose.yml stop view

# Kafka 중단
docker compose -f build-docker-compose.yml stop kafka

# Zookeeper 마지막에 중단
docker compose -f build-docker-compose.yml stop zookeeper
```

### **완전 정리**
```bash
# 모든 컨테이너 중단 및 제거
docker compose -f build-docker-compose.yml down

# 볼륨 제거 (선택사항 - 데이터 손실됨)
docker compose -f build-docker-compose.yml down -v

# Docker 리소스 정리
docker system prune -f
```

## 🔄 **개발 워크플로우**

### **코드 변경 후 배포**
```bash
# View 서비스 코드 변경 후:

# 1. View 서비스만 재시작
docker compose -f build-docker-compose.yml restart view

# 2. 시작 로그 모니터링
docker logs -f creator-platform-view-1

# 3. 상태 확인
curl http://localhost:8085/actuator/health
```

### **설정 변경**
```bash
# application.yml 변경 후:

# 1. 필요시 리빌드 (주요 변경사항의 경우)
docker compose -f build-docker-compose.yml build view

# 2. 서비스 재시작
docker compose -f build-docker-compose.yml up -d view
```

## 📈 **다음 단계**

### **완전한 테스트를 위해**
1. **테스트 데이터 설정**: 데이터베이스에 UserAccessProfile 엔티티 생성
2. **이벤트 검증**: AccessGranted/AccessDenied 이벤트 발행 테스트
3. **부하 테스트**: 다중 동시 요청 스트레스 테스트 수행
4. **통합 테스트**: 다른 마이크로서비스와의 테스트

### **프로덕션 배포를 위해**
1. **환경 설정**: 프로덕션 프로필 설정
2. **리소스 스케일링**: 적절한 리소스 제한 구성
3. **모니터링 설정**: 포괄적인 로깅 및 모니터링 구현
4. **Gateway 통합**: Spring Cloud Gateway 라우팅 구성

## 📝 **설정 파일 참조**

### **수정된 주요 파일**
- `view/src/main/resources/application.yml` - 포트 설정 추가
- `build-docker-compose.yml` - Kafka 버전을 7.4.0으로 변경

### **환경 변수**
```bash
# Docker 환경용 설정
SPRING_PROFILES_ACTIVE=docker

# 사용 가능한 프로필:
# - default: localhost를 사용하는 개발환경
# - docker: 내부 네트워킹을 사용하는 컨테이너 환경
```

---

**📋 문서 정보**
- **작성일**: 2025년 6월 29일
- **버전**: 1.0
- **상태**: 프로덕션 준비 완료
- **최종 업데이트**: 포트 설정 해결 후

**🎯 성공 기준**
- ✅ 모든 서비스가 오류 없이 실행
- ✅ Health Check 통과
- ✅ Kafka 이벤트 스트리밍 연결
- ✅ 8085 포트에서 API 엔드포인트 접근 가능

**📞 지원**
문제나 질문이 있으면 위의 문제 해결 섹션을 확인하거나 컨테이너 로그에서 구체적인 오류 메시지를 검토하세요. 