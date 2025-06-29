package creatorplatform.service;

import creatorplatform.domain.*;
import creatorplatform.infra.CheckIfBoughtRepository;
import creatorplatform.infra.CheckPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserAccessProfileService {
    
    @Autowired
    private CheckIfBoughtRepository checkIfBoughtRepository;
    
    @Autowired
    private UserAccessProfileRepository userAccessProfileRepository;
    
    @Autowired
    private CheckPriceRepository checkPriceRepository;
    
    // 기존 메소드 주석처리 (필요시 나중에 삭제)
    /*
    public UserAccessProfile accessToContent(Long userAccessProfileId, AccessToContentCommand command) {
        // 1. 인프라에서 데이터 조회
        boolean isBought = checkIfBoughtRepository.existsByIdAndProductId(
            userAccessProfileId, command.getProductId()
        );
        
        Optional<UserAccessProfile> optionalProfile = userAccessProfileRepository.findById(userAccessProfileId);
        if (!optionalProfile.isPresent()) {
            throw new RuntimeException("UserAccessProfile not found: " + userAccessProfileId);
        }
        
        UserAccessProfile profile = optionalProfile.get();
        
        // 2. 도메인 비즈니스 로직 실행 (순수!)
        profile.accessToContent(command, isBought);
        
        // 3. 인프라에 저장
        userAccessProfileRepository.save(profile);
        
        return profile;
    }
    */
    
    // 📖 책 접근 권한 확인 (메소드명 변경)
    public Map<String, Object> accessToContent(Long userId, Long productId) {
        Map<String, Object> result = new HashMap<>();
        
        // 1. 이미 구매했는지 확인
        boolean isPurchased = checkIfBoughtRepository.existsByIdAndProductId(userId, productId);
        if (isPurchased) {
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
                result.put("hasAccess", true);
                result.put("reason", "SUBSCRIBED");
                result.put("message", "구독자입니다");
                return result;
            }
        }
        
        // 3. 접근 권한 없음
        result.put("hasAccess", false);
        result.put("reason", "NO_ACCESS");
        result.put("message", "구매하거나 구독해야 합니다");
        return result;
    }
    
    // 💰 포인트 구매 가능 여부 확인
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
    
    // 🛒 포인트로 구매 실행
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
        
        result.put("success", true);
        result.put("message", "구매가 완료되었습니다");
        result.put("remainingPoints", user.getPoints());
        
        return result;
    }
} 