//RequestPublicationCommand.java
package creatorplatform.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class RequestPublicationCommand {

    private Long id;
    private Long authorId;
    private String authorNickname;
    private String title;
    private String content;
    private Date createdAt;
    private Date lastUpdatedAt;
}
