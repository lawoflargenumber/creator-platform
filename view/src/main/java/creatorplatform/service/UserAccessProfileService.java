package creatorplatform.service;

import creatorplatform.domain.*;
import creatorplatform.infra.CheckIfBoughtRepository;
import creatorplatform.infra.CheckPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.Calendar;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestHeader;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Application Service Layer
 * 역할: 유스케이스 조율, 인프라 조정, 트랜잭션 관리
 * vs Domain Service: 순수 비즈니스 규칙과 도메인 로직만 담당
 */
@Service
public class UserAccessProfileService {
    
    @Autowired
    private CheckIfBoughtRepository checkIfBoughtRepository;
    
    @Autowired
    private UserAccessProfileRepository userAccessProfileRepository;
    
    @Autowired
    private CheckPriceRepository checkPriceRepository;
    
    /**
     * 콘텐츠 접근 권한 확인 비즈니스 프로세스
     * 비즈니스 규칙: 구매 이력 → 구독 상태 → 접근 거부 순서로 검증
     * @Transactional 필수: 조회 + 이벤트 발행 일관성 보장
     */
    @Transactional
    public Map<String, Object> accessToContent(Long userId, Long productId) {
        Map<String, Object> result = new HashMap<>();
        
        // 가격 조회
        Integer productPrice = getProductPrice(productId);
        
        // 1. 이미 구매했는지 확인
        boolean isPurchased = checkIfBoughtRepository.existsByIdAndProductId(userId, productId);
        if (isPurchased) {
            UserAccessProfile user = userAccessProfileRepository.findById(userId).orElse(null);
            if (user != null) {
                user.publishAccessGranted(productId, productPrice); 
            }
            
            result.put("hasAccess", true);
            result.put("reason", "PURCHASED");
            result.put("message", "이미 구매한 책입니다");
            return result;
        }
        
        // 2. 구독자인지 확인
        Optional<UserAccessProfile> userOpt = userAccessProfileRepository.findById(userId);
        if (userOpt.isPresent()) {
            UserAccessProfile user = userOpt.get();
            if (user.getIsSubscribed() != null && user.getIsSubscribed()) {
                user.publishAccessGranted(productId, productPrice); 
                
                result.put("hasAccess", true);
                result.put("reason", "SUBSCRIBED");
                result.put("message", "구독자입니다");
                return result;
            }
        }
        
        // 3. 접근 권한 없음
        if (userOpt.isPresent()) {
            UserAccessProfile user = userOpt.get();
            user.publishAccessDenied(productId, productPrice); 
        }
        
        result.put("hasAccess", false);
        result.put("reason", "NO_ACCESS");
        result.put("message", "구매하거나 구독해야 합니다");
        return result;
    }
    
    /**
     * 포인트 구매 가능성 검증 (조회 전용)
     * @Transactional 없음: 순수 조회 작업으로 성능 최적화
     */
    public Map<String, Object> checkPurchaseability(Long userId, Long productId) {
        Map<String, Object> result = new HashMap<>();
        
        // 1. 사용자 포인트 조회
        Optional<UserAccessProfile> userOpt = userAccessProfileRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("사용자를 찾을 수 없습니다: " + userId);
        }
        
        UserAccessProfile user = userOpt.get();
        Integer userPoints = user.getPoints() != null ? user.getPoints() : 0;
        
        // 2. 상품 가격 조회
        Optional<CheckPrice> priceOpt = checkPriceRepository.findById(productId);
        if (!priceOpt.isPresent()) {
            throw new RuntimeException("상품 가격을 찾을 수 없습니다: " + productId);
        }
        
        CheckPrice priceInfo = priceOpt.get();
        Integer productPrice = priceInfo.getPrice();
        
        // 3. 구매 가능 여부 판단
        boolean canPurchase = userPoints >= productPrice;
        Integer remainingPoints = userPoints - productPrice;
        
        result.put("canPurchase", canPurchase); // 구매 가능 여부
        result.put("userPoints", userPoints); // 내 포인트
        result.put("productPrice", productPrice); // 구매 포인트 
        result.put("remainingPoints", remainingPoints); // 구매 후 포인트
        
        if (canPurchase) {
            result.put("message", "구매 가능합니다");
        } else {
            result.put("message", "포인트가 부족합니다");
        }
        
        return result;
    }
    
    /**
     * 포인트 구매 트랜잭션 비즈니스 프로세스
     * 비즈니스 규칙: 포인트 검증 → 차감 → 구매 이력 생성 → 이벤트 발행
     * @Transactional 필수: 다중 Repository 작업으로 원자성 보장
     */
    @Transactional
    public Map<String, Object> purchaseWithPoints(Long userId, Long productId) {
        Map<String, Object> result = new HashMap<>();
        
        // 1. 구매 가능 여부 재확인
        Map<String, Object> purchaseCheck = checkPurchaseability(userId, productId);
        if (!(Boolean) purchaseCheck.get("canPurchase")) {
            throw new RuntimeException("구매할 수 없습니다: " + purchaseCheck.get("message"));
        }
        
        // 2. 포인트 차감
        UserAccessProfile user = userAccessProfileRepository.findById(userId).get();
        Integer productPrice = (Integer) purchaseCheck.get("productPrice");
        user.setPoints(user.getPoints() - productPrice);
        userAccessProfileRepository.save(user);
        
        // 3. 구매 이력 추가
        CheckIfBought purchase = new CheckIfBought();
        purchase.setId(userId);
        purchase.setProductId(productId);
        checkIfBoughtRepository.save(purchase);
        
        // 4. 구매 완료 후 접근 허용 이벤트 발행
        user.publishAccessGranted(productId, productPrice);
        
        result.put("success", true);
        result.put("message", "구매가 완료되었습니다");
        result.put("remainingPoints", user.getPoints());
        
        return result;
    }
    
    /**
     * 신규 사용자 등록 비즈니스 프로세스
     * 이벤트: UserRegistered (Account → View)
     * 비즈니스 규칙: 신규 가입자에게 100 포인트 지급
     * @Transactional 없음: 단순 저장 작업으로 Repository 자체 트랜잭션 사용
     */
    public void processUserRegistration(UserRegistered userRegistered) {
        System.out.println("🎉 UserRegistered 이벤트 수신! userId: " + userRegistered.getId() + ", nickname: " + userRegistered.getNickname() + ", accountId: " + userRegistered.getAccountId());
        System.out.println("💰 받은 포인트: " + userRegistered.getPoints());
        
        UserAccessProfile userAccessProfile = new UserAccessProfile();
        userAccessProfile.setId(userRegistered.getId());
        userAccessProfile.setAccountId(userRegistered.getAccountId());
        userAccessProfile.setNickname(userRegistered.getNickname());
        
        // 🎯 계산 없이 받은 포인트 그대로 사용
        userAccessProfile.setPoints(userRegistered.getPoints());
        userAccessProfile.setIsSubscribed(false);
        userAccessProfile.setSubscribtionDue(null);
        
        userAccessProfileRepository.save(userAccessProfile);
        System.out.println("✅ 신규 사용자 생성 완료! userId: " + userRegistered.getId() + ", accountId: " + userRegistered.getAccountId() + ", nickname: " + userRegistered.getNickname() + ", 포인트: " + userRegistered.getPoints());
    }
    
    /**
     * 구독 활성화 비즈니스 프로세스
     * 이벤트: SubscriptionStarted (Account → View)
     * 비즈니스 규칙: 구독 상태 활성화 + 만료일 설정 (이벤트 시작일 기준 1개월)
     * @Transactional 필수: findById + save 조합으로 데이터 일관성 보장
     */
    @Transactional
    public void processSubscriptionActivation(SubscriptionStarted subscriptionStarted) {
        // 해당 사용자의 구독 상태 업데이트
        userAccessProfileRepository.findById(subscriptionStarted.getId()).ifPresent(userAccessProfile -> {
            // 비즈니스 규칙: 구독 상태 활성화
            userAccessProfile.setIsSubscribed(true);
            
            // 비즈니스 규칙: 이벤트의 시작일 기준으로 구독 만료일 설정 (한 달 후)
            LocalDateTime subscriptionStart = subscriptionStarted.getSubscribtionStartedAt();
            LocalDateTime subscriptionEnd = subscriptionStart.plusMonths(1);
            
            // LocalDateTime을 Date로 변환하여 저장
            java.util.Date subscriptionDueDate = java.sql.Timestamp.valueOf(subscriptionEnd);
            userAccessProfile.setSubscribtionDue(subscriptionDueDate);
            
            userAccessProfileRepository.save(userAccessProfile);
            
            System.out.println("✅ 구독 활성화 완료! userId: " + subscriptionStarted.getId() + 
                              ", 시작일: " + subscriptionStart + 
                              ", 만료일: " + subscriptionEnd);
        });
    }

    /**
     * 작가 신청 이벤트 비즈니스 프로세스  
     * 이벤트: AuthorshipApplied (Account → View)
     * 비즈니스 규칙: 작가 신청 정보를 View에 반영
     */
    public void processAuthorshipApplication(AuthorshipApplied authorshipApplied) {
        System.out.println("AuthorshipApplied 이벤트 수신! userId: " + authorshipApplied.getId() + 
                          ", authorshipStatus: " + authorshipApplied.getAuthorshipStatus() + 
                          ", authorsProfile: " + authorshipApplied.getAuthorsProfile());
        
        // 해당 사용자의 작가 신청 정보 업데이트
        userAccessProfileRepository.findById(authorshipApplied.getId()).ifPresent(userAccessProfile -> {
            // 작가 신청 상태와 프로필을 View에 반영
            userAccessProfile.setAuthorshipStatus(authorshipApplied.getAuthorshipStatus());
            userAccessProfile.setAuthorsProfile(authorshipApplied.getAuthorsProfile());
            
            userAccessProfileRepository.save(userAccessProfile);
            System.out.println("작가 신청 정보 업데이트 완료! userId: " + authorshipApplied.getId() + 
                             ", status: " + authorshipApplied.getAuthorshipStatus());
        });
    }

    /**
     * 작가 승인 이벤트 비즈니스 프로세스
     * 이벤트: AuthorshipAccepted (Account → View)
     * 비즈니스 규칙: 작가 신청 승인 처리 - authorshipStatus를 "ACCEPTED"로 변경
     * @Transactional 필수: findById + save 조합으로 데이터 일관성 보장
     */
    @Transactional
    public void processAuthorshipAcceptance(AuthorshipAccepted authorshipAccepted) {
        System.out.println("AuthorshipAccepted 이벤트 수신! userId: " + authorshipAccepted.getId() + 
                          ", authorshipStatus: " + authorshipAccepted.getAuthorshipStatus());
        
        // 해당 사용자의 작가 승인 상태 업데이트
        userAccessProfileRepository.findById(authorshipAccepted.getId()).ifPresent(userAccessProfile -> {
            // 비즈니스 규칙: 작가 신청 승인 처리
            userAccessProfile.setAuthorshipStatus("ACCEPTED");
            
            userAccessProfileRepository.save(userAccessProfile);
            System.out.println("✅ 작가 승인 처리 완료! userId: " + authorshipAccepted.getId() + 
                             ", 새로운 status: ACCEPTED");
        });
    }

    // 가격 조회 헬퍼 메소드
    private Integer getProductPrice(Long productId) {
        return checkPriceRepository.findById(productId)
            .map(CheckPrice::getPrice)
            .orElse(0);
    }

    /**
     * 내 정보 조회 비즈니스 프로세스
     * 비즈니스 규칙: authorshipStatus가 "accepted"면 isAuthor = true, 아니면 false
     * @Transactional 없음: 순수 조회 작업으로 성능 최적화
     */
    public Map<String, Object> getUserInfo(Long userId) {
        Map<String, Object> result = new HashMap<>();
        
        // 사용자 정보 조회
        Optional<UserAccessProfile> userOpt = userAccessProfileRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("사용자를 찾을 수 없습니다: " + userId);
        }
        
        UserAccessProfile user = userOpt.get();
        
        // isAuthor 판단: authorshipStatus가 "accepted"면 true, 아니면 false
        boolean isAuthor = "accepted".equals(user.getAuthorshipStatus());
        
        // 응답 데이터 구성
        result.put("id", user.getId());
        result.put("accountId", user.getAccountId());
        result.put("nickname", user.getNickname());
        result.put("isAuthor", isAuthor);
        result.put("points", user.getPoints() != null ? user.getPoints() : 0);
        result.put("authorNickname", user.getAuthorNickname());
        result.put("authorsProfile", user.getAuthorsProfile());
        
        return result;
    }
} 