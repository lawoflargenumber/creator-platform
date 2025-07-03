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
        // UserAccessProfile 임시 데이터 생성 비활성화
        // (UserRegistered 이벤트로 실제 사용자가 생성되도록 함)
        System.out.println("✅ UserAccessProfile 임시 데이터 생성 건너뜀 (이벤트 기반 생성 사용)");
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
        // 실제 사용자와 충돌하지 않도록 높은 ID 사용 (9900번대)
        CheckIfBought purchase1 = new CheckIfBought();
        purchase1.setId(9901L);        // 테스트 사용자 ID
        purchase1.setProductId(200L); // 상품 ID
        checkIfBoughtRepository.save(purchase1);
    
        CheckIfBought purchase2 = new CheckIfBought();
        purchase2.setId(9902L);        // 테스트 사용자 ID
        purchase2.setProductId(300L); // 상품 ID
        checkIfBoughtRepository.save(purchase2);
    
        CheckIfBought purchase3 = new CheckIfBought();
        purchase3.setId(9904L);        // 테스트 사용자 ID  
        purchase3.setProductId(100L); // 상품 ID
        checkIfBoughtRepository.save(purchase3);
    
        CheckIfBought purchase4 = new CheckIfBought();
        purchase4.setId(9904L);        // 테스트 사용자 ID
        purchase4.setProductId(200L); // 상품 ID
        checkIfBoughtRepository.save(purchase4);
    
        System.out.println("   ✅ CheckIfBought 테스트 데이터 4개 생성 완료 (ID: 9900번대 사용)");
    }
}
