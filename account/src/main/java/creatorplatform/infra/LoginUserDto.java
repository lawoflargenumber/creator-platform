package creatorplatform.infra;

import creatorplatform.domain.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserDto {
    private Long id;
    private String nickname;
    private String authorshipStatus;
    private Date createdAt;
    private Boolean subscriber;
    private String accountId;
    private String authorsProfile;
    private String authorNickname;
    private Boolean agreedToMarketing;

    public static LoginUserDto fromEntity(Users user) {
        return new LoginUserDto(
            user.getId(),
            user.getNickname(),
            user.getAuthorshipStatus(),
            user.getCreatedAt(),
            user.getSubscriber(),
            user.getAccountId(),
            user.getAuthorsProfile(),
            user.getAuthorNickname(),
            user.getAgreedToMarketing()
        );
    }
} 