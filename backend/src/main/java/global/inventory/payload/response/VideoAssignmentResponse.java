package global.inventory.payload.response;

import global.inventory.payload.request.UserDetailsResponse;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ToString
public class VideoAssignmentResponse {
    private Long id;
    private UserDetailsResponse user;
    private VideoResponse video;
    private LocalDateTime assignedAt;
}
