package global.inventory.controller;

import global.inventory.exception.TokenNotFoundException;
import global.inventory.payload.request.AuthenticationRequest;
import global.inventory.payload.request.RefreshTokenRequest;
import global.inventory.payload.request.UserRegistrationRequest;
import global.inventory.payload.response.AuthenticationResponse;
import global.inventory.payload.response.RefreshTokenResponse;
import global.inventory.payload.response.RegistrationResponse;
import global.inventory.payload.response.generic.InventoryResponse;
import global.inventory.service.security.AuthenticationService;
import global.inventory.service.security.JwtService;
import global.inventory.service.security.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<InventoryResponse<AuthenticationResponse>> login(@RequestBody AuthenticationRequest authenticationRequest) {
        var response = authenticationService.authenticate(authenticationRequest);
        var generatedCookies = constructCookies(response);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, generatedCookies.getLeft())
                .header(HttpHeaders.SET_COOKIE, generatedCookies.getRight())
                .body(new InventoryResponse<>(response));
    }

    @PostMapping("/registration")
    public ResponseEntity<InventoryResponse<RegistrationResponse>> userRegistration(@RequestBody @Validated UserRegistrationRequest userRegistrationRequest) {
        var register = authenticationService.userRegistration(userRegistrationRequest);
        log.info("user registration response: {}", register);
        return ResponseEntity.ok(new InventoryResponse<>(register));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<InventoryResponse<RefreshTokenResponse>> refreshToken(@RequestBody RefreshTokenRequest request) {
        var response = refreshTokenService.generateNewToken(request);
        return ResponseEntity.ok(new InventoryResponse<>(response));
    }

    @PostMapping("/logout")
    public ResponseEntity<InventoryResponse<String>> logout(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String refreshToken
    ) {
            if (refreshToken != null) {
            refreshTokenService.deleteByToken(refreshToken);

            var cookies = clearCookies();
            log.info("user logged out.............");
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookies.getLeft())
                    .header(HttpHeaders.SET_COOKIE, cookies.getRight())
                    .body(new InventoryResponse<>("Logged out successfully"));
        }
        throw new TokenNotFoundException("Logged out  unsuccessful");
    }

    private Pair<String, String> constructCookies(AuthenticationResponse response) {
        var jwtCookie = jwtService.generateJwtCookie(response.getAccessToken()).toString();
        var refreshTokenCookie = refreshTokenService.generateRefreshTokenCookie(response.getRefreshToken()).toString();
        return Pair.of(jwtCookie, refreshTokenCookie);
    }

    private Pair<String, String> clearCookies() {
        var jwtCookie = jwtService.getCleanJwtCookie().toString();
        var refreshTokenCookie = refreshTokenService.getCleanRefreshTokenCookie().toString();
        return Pair.of(jwtCookie, refreshTokenCookie);
    }
}
