package global.inventory.service.security;

import global.inventory.model.RefreshToken;
import global.inventory.payload.request.RefreshTokenRequest;
import global.inventory.payload.response.RefreshTokenResponse;
import org.springframework.http.ResponseCookie;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(Long userId);

    RefreshToken verifyExpiration(RefreshToken token);

    ResponseCookie generateRefreshTokenCookie(String token);

    void deleteByToken(String token);

    ResponseCookie getCleanRefreshTokenCookie();

    RefreshTokenResponse generateNewToken(RefreshTokenRequest request);
}