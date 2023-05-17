package rw.heritier.jwt.service;

import java.util.List;

import rw.heritier.jwt.model.User;

public interface UserService {
    List<User> findUserByEmail(String email);
}
