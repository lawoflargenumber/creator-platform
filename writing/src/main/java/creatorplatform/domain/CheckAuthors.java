package creatorplatform.domain;

import javax.persistence.*;
import lombok.Data;

//<<< EDA / CQRS
@Entity
@Table(name = "CheckAuthors_table")
@Data
public class CheckAuthors {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)  // ✅ ID 자동 생성
    private Long id;

    private String nickname;
}
