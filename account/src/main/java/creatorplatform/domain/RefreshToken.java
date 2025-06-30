package creatorplatform.domain;

import lombok.*;
import javax.persistence.*;
import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;
    @Column(nullable = false, unique = true)
    private String token;
    private Instant issuedAt;
    private Instant expiresAt;
    private boolean revoked;
}
