package creatorplatform.domain;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductSummaryDto {
    private Long id;
    private String coverImageUrl;
    private String category;
    private String authorNickname;
    private String title;
    private Date publishedAt;
    private Boolean isBestseller;
}
