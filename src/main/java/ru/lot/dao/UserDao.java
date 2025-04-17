package ru.lot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.lot.entity.User;
import ru.lot.enums.RoleType;

import java.util.Optional;

public interface UserDao extends JpaRepository<User, Long> {

    Optional<User> findFirstByRole(RoleType role);

    Optional<User> findByEmail(String email);
}
