package rw.heritier.jwt.service;

import java.util.Optional;

import rw.heritier.jwt.model.User;

public interface UserService {
    Optional<User> findUserByEmail(String email);
}
