package creatorplatform.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class AccessToContentCommand {

    private Long id;
    private Long productId;
}
