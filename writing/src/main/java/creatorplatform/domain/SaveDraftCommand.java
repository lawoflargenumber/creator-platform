package creatorplatform.domain;

import lombok.Data;
import java.util.Date;

@Data
public class SaveDraftCommand {

    private Long id;          // (자동 생성 JPA ID가 아니라면 사실 필요 없지만 남겨도 무방)
    private Long authorId;
    private String authorNickname;   // ⭐ 추가
    private String title;
    private String content;
}
