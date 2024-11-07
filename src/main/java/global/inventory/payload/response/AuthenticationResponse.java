package global.inventory.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    private  Long userId;
    private String username;
    private String accessToken;
    private String refreshToken;
    private List<String> roles;
}