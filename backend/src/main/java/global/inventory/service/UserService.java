package global.inventory.service;

import global.inventory.model.User;
import global.inventory.payload.request.UserRegistrationRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    Optional<User> getUserByUsername(String username);

    User createUser(UserRegistrationRequest userRegistrationRequest);

    Boolean existsByUsername(String username);

    Page<User> getAllNonAdminUsers(Pageable pageable);

    User findById(Long userId);
}
