package creatorplatform.domain.aggregate;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;

@Entity
@Data
public class RegisteredUser {

    @Id
    private String id;
    private String nickname;
    private String password;
    private String authorshipStatus;
    private String authorsProfile;
    private boolean isSubscribed;
    private boolean agreedToMarketing;

    // Getters and setters omitted for brevity
}
