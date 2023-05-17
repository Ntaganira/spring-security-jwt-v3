package rw.heritier.jwt.service.impl;

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
    public User findUserByEmail(String email) {
        return repository.findUserByEmail(email);
    }

}
