package global.inventory.service.security;

import global.inventory.model.User;
import global.inventory.payload.request.AuthenticationRequest;
import global.inventory.payload.request.UserRegistrationRequest;
import global.inventory.payload.response.AuthenticationResponse;
import global.inventory.payload.response.RegistrationResponse;
import global.inventory.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationProvider authenticationProvider;
    private final UserService userService;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        var user = userService.getUserByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Username or password."));

        var roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        var jwt = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return AuthenticationResponse.builder()
                .accessToken(jwt)
                .roles(roles)
                .username(user.getUsername())
                .userId(user.getId())
                .refreshToken(refreshToken.getToken())
                .build();
    }

    @Override
    public RegistrationResponse userRegistration(UserRegistrationRequest request) {
        User user = userService.createUser(request);

        return RegistrationResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .build();
    }
}
