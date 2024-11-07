package global.inventory.repository;

import global.inventory.enums.Role;
import global.inventory.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Page<User> findAllByRole(Role role, Pageable pageable);
}
