package creatorplatform.domain;
import javax.persistence.*;
import lombok.Data;
import java.util.Date;

/**
 * Domain Entity (Aggregate Root)
 * 역할: 이벤트 발행 + 데이터 모델링
 * vs Application Service: 복잡한 비즈니스 프로세스는 Service Layer에서 처리
 */
@Entity
@Table(name="UserAccessProfile_table")
@Data

//<<< DDD / Aggregate Root
public class UserAccessProfile  {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    

private Long id;    

private String accountId;

private String nickname;
    
private Boolean isSubscribed;    
    
private Integer points;    
    
private Date subscribtionDue;

private String authorshipStatus;

private String autornickname;

private String authorsProfile;

//<<< Clean Arch / Port Method
    /**
     * 접근 허용 도메인 이벤트 발행
     * 이벤트: AccessGranted (View → 다른 서비스)
     * 트리거: 구매완료, 구독자 접근, 포인트구매 완료
     */
    public void publishAccessGranted(Long productId, Integer price) {
        AccessGranted accessGranted = new AccessGranted(this);
        accessGranted.setProductId(productId);
        accessGranted.setPrice(price);
        accessGranted.publishAfterCommit();
        System.out.println(" AccessGranted 이벤트 발행: userId=" + this.id + 
                          ", productId=" + productId + ", price=" + price);
    }
    
    /**
     * 접근 거부 도메인 이벤트 발행  
     * 이벤트: AccessDenied (View → 다른 서비스)
     * 트리거: 미구매 + 비구독자 접근
     */
    public void publishAccessDenied(Long productId, Integer price) {
        AccessDenied accessDenied = new AccessDenied(this);
        
        // 포인트 충분 여부 계산 (현재 포인트 vs 상품 가격)
        accessDenied.setHasSufficientPoints(
            this.points != null && price != null && this.points >= price
        );
        
        accessDenied.publishAfterCommit();
        System.out.println("AccessDenied 이벤트 발행: userId=" + this.id + 
                          ", productId=" + productId + 
                          ", hasSufficientPoints=" + accessDenied.getHasSufficientPoints());
    }
}
