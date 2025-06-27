package creatorplatform.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

//<<< EDA / CQRS
@Entity
@Table(name = "CheckAuthors_table")
@Data
public class CheckAuthors {

    @Id
    //@GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private String nickname;
}
