package creatorplatform.config;

import creatorplatform.domain.Users;
import creatorplatform.domain.UsersRepository;
import creatorplatform.domain.command.RegisterUserCommand;
import creatorplatform.domain.service.UserCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

@Component
public class DataInitializer {

    @Autowired
    private UsersRepository usersRepository;
    
    @Autowired
    private UserCommandService userCommandService;

    @PostConstruct
    public void initializeData() {
        // 기존 데이터가 있는지 확인
        if (usersRepository.count() == 0) {
            createTestUsers();
        }
    }

    private void createTestUsers() {
        // 테스트용 사용자 10명 기본 데이터
        String[][] userData = {
            {"user1@example.com", "김철수", "true"},
            {"user2@example.com", "이영희", "false"},
            {"author1@example.com", "박민수", "true"},
            {"author2@example.com", "정지수", "true"},
            {"user3@example.com", "최민경", "false"},
            {"user4@example.com", "강호동", "true"},
            {"author3@example.com", "윤서준", "true"},
            {"subscriber1@example.com", "김영수", "true"},
            {"activeuser@example.com", "송지현", "false"},
            {"veteran@example.com", "문성호", "true"},
            {"newbie@example.com", "이현아", "true"}
        };
        
        for (String[] user : userData) {
            RegisterUserCommand cmd = new RegisterUserCommand();
            cmd.id = user[0];
            cmd.password = "password123";
            cmd.nickname = user[1];
            cmd.agreedToMarketing = Boolean.parseBoolean(user[2]);
            userCommandService.handleRegisterUser(cmd);
        }

        System.out.println("✅ Users 기본 데이터 11개 생성 완료");
        System.out.println("   📊 생성된 사용자:");
        System.out.println("   - 김철수 (user1@example.com) - 마케팅 동의");
        System.out.println("   - 이영희 (user2@example.com) - 마케팅 미동의");
        System.out.println("   - 박민수 (author1@example.com) - 마케팅 동의");
        System.out.println("   - 정지수 (author2@example.com) - 마케팅 동의");
        System.out.println("   - 최민경 (user3@example.com) - 마케팅 미동의");
        System.out.println("   - 강호동 (user4@example.com) - 마케팅 동의");
        System.out.println("   - 윤서준 (author3@example.com) - 마케팅 동의");
        System.out.println("   - 김영수 (subscriber1@example.com) - 마케팅 동의");
        System.out.println("   - 송지현 (activeuser@example.com) - 마케팅 미동의");
        System.out.println("   - 문성호 (veteran@example.com) - 마케팅 동의");
        System.out.println("   - 이현아 (newbie@example.com) - 마케팅 동의");
        System.out.println("🚀 각 사용자 생성 시 UserRegistered 이벤트가 Kafka로 전송됩니다!");
        System.out.println("   구독신청/작가신청/승인은 직접 처리하시면 됩니다.");
    }
} 