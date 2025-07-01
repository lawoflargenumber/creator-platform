package creatorplatform.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    LITERATURE("문학"),
    ECONOMY("경제"),
    SELF_DEVELOPMENT("자기계발"),
    LIFESTYLE("라이프스타일"),
    OTHERS("기타");

    private final String displayName;

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    public static Category fromDisplayName(String displayName) {
        for (Category category : Category.values()) {
            if (category.getDisplayName().equalsIgnoreCase(displayName)) {
                return category;
            }
        }
        return OTHERS;
    }
}
