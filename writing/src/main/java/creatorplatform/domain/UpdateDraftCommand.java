
package creatorplatform.domain;

import lombok.Data;

@Data
public class UpdateDraftCommand {
    private String title;
    private String content;
}
