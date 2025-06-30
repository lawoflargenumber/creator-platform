package creatorplatform.domain.aggregate;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nickname;
    private String password;
    private String authorshipStatus;
    private boolean isSubscribed;
    private boolean agreedToMarketing;
}
