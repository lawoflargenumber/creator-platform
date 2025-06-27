package creatorplatform.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class TrackViewCommand {

    private Long id;
    private Long userId;
    private Date createdAt;
}
