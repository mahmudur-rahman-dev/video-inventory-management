package global.inventory.payload.response;

import global.inventory.enums.ActivityAction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityLogResponse {
    private Long id;
    private UserResponse user;
    private VideoResponse video;
    private ActivityAction action;
    private LocalDateTime timestamp;
}
