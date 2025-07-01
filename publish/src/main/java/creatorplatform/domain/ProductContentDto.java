package creatorplatform.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductContentDto {
    private Long id;
    private String title;
    private String authorNickname;
    private String content;
    private Boolean isBestseller;
}
