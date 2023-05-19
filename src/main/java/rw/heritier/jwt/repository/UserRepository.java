package rw.heritier.jwt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import rw.heritier.jwt.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findUserByEmail(String email);
}
