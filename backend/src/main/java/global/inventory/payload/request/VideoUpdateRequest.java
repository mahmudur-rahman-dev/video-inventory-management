package global.inventory.payload.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VideoUpdateRequest {
    private String title;
    private String description;
}
