package global.inventory.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VideoAssignRequest {
    @NotNull
    private Long userId;
}
