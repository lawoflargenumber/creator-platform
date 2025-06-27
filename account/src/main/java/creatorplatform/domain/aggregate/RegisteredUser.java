package creatorplatform.domain.aggregate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
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
