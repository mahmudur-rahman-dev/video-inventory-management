package global.inventory.payload.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserVideoResponse {
    private Long id;
    private String title;
    private String description;
    private String videoUrl;
    private LocalDateTime assignedAt;
    private LocalDateTime lastViewed;
}
