package creatorplatform.domain.model;

import lombok.*;
import javax.persistence.*;

@Entity @Data @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(unique=true,nullable=false) private String email;
    @Column(nullable=false) private String password;
    private String nickname;
    private boolean isAuthor;
    private int points;
    private boolean marketingConsent;
}