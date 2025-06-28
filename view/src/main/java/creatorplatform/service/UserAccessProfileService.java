package creatorplatform.service;

import creatorplatform.domain.*;
import creatorplatform.infra.CheckIfBoughtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserAccessProfileService {
    
    @Autowired
    private CheckIfBoughtRepository checkIfBoughtRepository;
    
    @Autowired
    private UserAccessProfileRepository userAccessProfileRepository;
    
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
} 