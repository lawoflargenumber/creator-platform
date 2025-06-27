package creatorplatform.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class SaveDraftCommand {

    private Long id;
    private Long authorId;
    private String title;
    private String content;
}
