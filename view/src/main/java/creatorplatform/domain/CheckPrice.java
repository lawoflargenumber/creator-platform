package creatorplatform.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

//<<< EDA / CQRS
@Entity
@Table(name = "CheckPrice_table")
@Data
public class CheckPrice {

    @Id
    //@GeneratedValue(strategy=GenerationType.AUTO)
    private Long productId;

    private Integer price;
}
