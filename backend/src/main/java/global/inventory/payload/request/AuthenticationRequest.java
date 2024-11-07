package global.inventory.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {
    @Schema(description = "Username", example = "admin", defaultValue = "admin")
    private String username;
    @Schema(description = "Password", example = "pass", defaultValue = "pass")
    private String password;
}
