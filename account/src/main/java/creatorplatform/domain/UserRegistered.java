package creatorplatform.domain;

import creatorplatform.domain.*;
import creatorplatform.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class UserRegistered extends AbstractEvent {

    private Long id;
    private String nickname;
    private String accountId;
    private String authorshipStatus;
    private String authorsProfile;
    private String authorNickname;
    private Integer points;
    
    public UserRegistered(Users aggregate) {
        super(aggregate);
        
        // 디버깅: Users 엔티티의 실제 값 확인
        System.out.println("🔍 [Account] Users 엔티티의 agreedToMarketing: " + aggregate.getAgreedToMarketing());
        
        // Users 데이터를 이벤트 필드에 설정
        this.id = aggregate.getId();
        this.nickname = aggregate.getNickname();
        this.accountId = aggregate.getAccountId();
        this.authorshipStatus = aggregate.getAuthorshipStatus();
        this.authorsProfile = aggregate.getAuthorsProfile();
        this.authorNickname = aggregate.getAuthorNickname();
        
        // 📝 이벤트 데이터만 설정 (비즈니스 로직은 Service에서 처리됨)
        this.points = null; // Service에서 setPoints()로 설정됨
        
        System.out.println("📤 [Event] UserRegistered 이벤트 생성 완료 - agreedToMarketing: " + aggregate.getAgreedToMarketing());
    }

    public UserRegistered() {
        super();
    }
}
//>>> DDD / Domain Event
