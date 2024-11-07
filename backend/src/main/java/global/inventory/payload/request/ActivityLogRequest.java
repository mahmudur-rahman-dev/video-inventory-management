package global.inventory.payload.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ActivityLogRequest {
    private String action;
    private LocalDateTime timestamp;
    private Long userId;
    private Long videoId;
}
