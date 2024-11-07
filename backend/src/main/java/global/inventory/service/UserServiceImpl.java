package global.inventory.service;

import global.inventory.enums.Role;
import global.inventory.model.User;
import global.inventory.payload.request.UserRegistrationRequest;
import global.inventory.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        userRepository.findByName("admin").ifPresentOrElse(
                user -> {
                },
                () -> userRepository.save(User.builder()
                        .name("admin")
                        .username("admin")
                        .role(Role.ADMIN)
                        .password(passwordEncoder.encode("pass"))
                        .build())
        );
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User createUser(UserRegistrationRequest request) {
        if (existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("User already exists");
        }

        var newUser = User.builder()
                .name(request.getUsername())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        return userRepository.save(newUser);
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public Page<User> getAllNonAdminUsers(Pageable pageable) {
        return userRepository.findAllByRole(Role.USER, pageable);
    }

    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
