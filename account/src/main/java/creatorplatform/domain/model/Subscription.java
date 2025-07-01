package creatorplatform.domain.model;

import creatorplatform.domain.Users;
import lombok.*;
import javax.persistence.*;
import java.time.Instant;

@Entity @Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Subscription {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne @JoinColumn(name="user_id") private Users user;
    private String plan;
    private Instant expiresAt;
}