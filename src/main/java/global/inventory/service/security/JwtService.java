package global.inventory.service.security;

import global.inventory.model.User;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String extractUserName(String token);

    String generateToken(User user);

    boolean isTokenValid(String token, UserDetails userDetails);

    ResponseCookie generateJwtCookie(String jwt);

    String getJwtFromCookies(HttpServletRequest request);

    ResponseCookie getCleanJwtCookie();

    String generateRefreshToken(User user);

    Claims extractAllClaims(String token);
}