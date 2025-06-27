package creatorplatform.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class RegisterUserCommand {

    private String accountId;
    private String password;
    private String nickname;
    private Boolean agreedToMarketing;
}
