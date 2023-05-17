package rw.heritier.jwt.service;

import rw.heritier.jwt.model.User;

public interface UserService {
    User findUserByEmail(String email);
}
