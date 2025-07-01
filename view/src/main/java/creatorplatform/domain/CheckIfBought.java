package creatorplatform.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

//<<< EDA / CQRS
@Entity
@Table(name = "CheckIfBought_table")
@Data
public class CheckIfBought {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long checkId;
    
    private Long id;
    private Long productId;
}
