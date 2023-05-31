package rw.heritier.jwt.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import rw.heritier.jwt.model.Token;
import rw.heritier.jwt.repository.TokenRepository;
import rw.heritier.jwt.service.TokenService;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final TokenRepository repository;

    @Override
    public List<Token> findAllValidTokenByUser(Integer id) {
        return repository.findAllValidTokenByUser(id);
    }

    @Override
    public Optional<Token> findByToken(String token) {
        return repository.findByToken(token);
    }

    @Override
    public Token save(Token token) {
        return repository.save(token);
    }

    @Override
    public void saveAll(List<Token> tokens) {
        repository.saveAll(tokens);
    }

}
