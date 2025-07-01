package creatorplatform.infra;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductDetailDto {
    private Long id;
    private String coverImageUrl;
    private String category;
    private Integer price;
    private String authorNickname;
    private Date publishedAt;
    private String title;
    private String summary;
    private Boolean isBestseller;
}
