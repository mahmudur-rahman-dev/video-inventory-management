package global.inventory.payload.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class VideoResponse {
    private Long id;
    private String title;
    private String description;
    private String videoUrl;
    private LocalDateTime createdAt;
    private LocalDateTime modificationDate;
}
