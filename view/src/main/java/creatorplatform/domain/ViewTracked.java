package creatorplatform.domain;

import creatorplatform.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class ViewTracked extends AbstractEvent {

    private Long id;
    private Long userId;
    private Date createdAt;
}
