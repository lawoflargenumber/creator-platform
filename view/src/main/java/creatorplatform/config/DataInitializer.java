package creatorplatform.config;

import creatorplatform.domain.*;
import creatorplatform.infra.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.Calendar;

@Component
public class DataInitializer {

    @Autowired
    private UserAccessProfileRepository userAccessProfileRepository;

    @Autowired
    private CheckPriceRepository checkPriceRepository;

    @Autowired
    private CheckIfBoughtRepository checkIfBoughtRepository;

    @PostConstruct
    public void initializeData() {
        // 테스트용 초기 데이터 생성 (SubscriptionStarted 이벤트 테스트용 유저 1명)
        createTestUserProfile();
        createTestPriceData();
        createTestPurchaseHistory(); 
    }

    private void createTestUserProfile() {
        // id=1 구독 안된 유저 (SubscriptionStarted 이벤트 테스트용)
        UserAccessProfile user1 = new UserAccessProfile();
        user1.setId(1L);
        user1.setIsSubscribed(false);
        user1.setPoints(100);
        user1.setSubscribtionDue(null);
        userAccessProfileRepository.save(user1);

        System.out.println("✅ SubscriptionStarted 이벤트 테스트용 UserAccessProfile id=1 생성 완료");
    }

    private void createTestPriceData() {
        // 테스트용 상품 가격 데이터
        CheckPrice product1 = new CheckPrice();
        product1.setProductId(100L);
        product1.setPrice(1000);
        checkPriceRepository.save(product1);

        CheckPrice product2 = new CheckPrice();
        product2.setProductId(200L);
        product2.setPrice(2000);
        checkPriceRepository.save(product2);

        System.out.println("✅ CheckPrice 테스트 데이터 2개 생성 완료");
    }
    private void createTestPurchaseHistory() {
        // 사용자 1: 상품 200을 구매한 이력 (하지만 상품 100은 구매하지 않음)
        CheckIfBought purchase1 = new CheckIfBought();
        purchase1.setId(1L);        // 사용자 ID
        purchase1.setProductId(200L); // 상품 ID
        checkIfBoughtRepository.save(purchase1);
    
        // 사용자 2: 상품 300을 구매한 이력 (구독자이지만 추가 구매도 함)
        CheckIfBought purchase2 = new CheckIfBought();
        purchase2.setId(2L);        // 사용자 ID
        purchase2.setProductId(300L); // 상품 ID
        checkIfBoughtRepository.save(purchase2);
    
        // 사용자 4: 상품 100을 구매한 이력 (포인트 부족하지만 구매로 접근 가능)
        CheckIfBought purchase3 = new CheckIfBought();
        purchase3.setId(4L);        // 사용자 ID  
        purchase3.setProductId(100L); // 상품 ID
        checkIfBoughtRepository.save(purchase3);
    
        // 사용자 4: 상품 200도 추가로 구매한 이력
        CheckIfBought purchase4 = new CheckIfBought();
        purchase4.setId(4L);        // 사용자 ID
        purchase4.setProductId(200L); // 상품 ID
        checkIfBoughtRepository.save(purchase4);
    
        System.out.println("   ✅ CheckIfBought 테스트 데이터 4개 생성 완료");
    }
}
