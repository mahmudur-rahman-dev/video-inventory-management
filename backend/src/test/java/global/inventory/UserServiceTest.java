package global.inventory;

import global.inventory.enums.Role;
import global.inventory.model.User;
import global.inventory.payload.request.UserRegistrationRequest;
import global.inventory.repository.UserRepository;
import global.inventory.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private UserRegistrationRequest registrationRequest;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .name("Test User")
                .username("testuser")
                .password("encoded_password")
                .role(Role.USER)
                .build();

        registrationRequest = UserRegistrationRequest.builder()
                .name("Test User")
                .username("testuser")
                .password("password")
                .build();
    }

    @Test
    @DisplayName("Should create user successfully")
    void createUserSuccess() {
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.createUser(registrationRequest);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals(Role.USER, result.getRole());
        verify(passwordEncoder).encode("password");
    }

    @Test
    @DisplayName("Should throw exception when username already exists")
    void createUserUsernameExists() {
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> userService.createUser(registrationRequest));
    }
}
