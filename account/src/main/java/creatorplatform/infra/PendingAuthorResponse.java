package creatorplatform.infra;

import creatorplatform.domain.Users;
import lombok.Getter;
@Getter
public class PendingAuthorResponse {
    private final Long userId;
    private final String accountId;
    private final String AuthorNickname;
    private final String authorProfile;

    private PendingAuthorResponse(Users entity) {
        this.userId = entity.getId();
        this.accountId = entity.getAccountId();
        this.AuthorNickname = entity.getAuthorNickname();
        this.authorProfile = entity.getAuthorsProfile();
    }

    public static PendingAuthorResponse fromEntity(Users entity) {
        return new PendingAuthorResponse(entity);
    }
}
