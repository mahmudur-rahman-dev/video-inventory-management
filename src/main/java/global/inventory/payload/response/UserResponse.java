package global.inventory.payload.response;

import global.inventory.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String name;
    private String username;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime modificationDate;
}
