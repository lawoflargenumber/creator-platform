package creatorplatform.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class ApplyForAuthorshipCommand {

    private Long id;
    private String authorsProfile;
    private String autornickname;
}
