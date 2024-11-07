package global.inventory.service.security;

import global.inventory.payload.request.AuthenticationRequest;
import global.inventory.payload.request.UserRegistrationRequest;
import global.inventory.payload.response.AuthenticationResponse;
import global.inventory.payload.response.RegistrationResponse;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);

    RegistrationResponse userRegistration(UserRegistrationRequest registerRequest);
}
