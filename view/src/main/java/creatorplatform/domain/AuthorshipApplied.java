package creatorplatform.domain;

import creatorplatform.infra.AbstractEvent;
import java.util.*;
import lombok.*;

@Data
@ToString
public class AuthorshipApplied extends AbstractEvent {
    private Long id;
    private String authorshipStatus;
} 