package rw.heritier.jwt.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import rw.heritier.jwt.model.User;
import rw.heritier.jwt.repository.UserRepository;
import rw.heritier.jwt.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public Optional<User> findUserByEmail(String email) {
        return repository.findUserByEmail(email);
    }

    @Override
    public User save(User user) {
        return repository.save(user);
    }

}
