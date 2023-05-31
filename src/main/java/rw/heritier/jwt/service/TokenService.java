package rw.heritier.jwt.service;

import java.util.List;
import java.util.Optional;

import rw.heritier.jwt.model.Token;

public interface TokenService {
    List<Token> findAllValidTokenByUser(Integer id);

    Optional<Token> findByToken(String token);

    Token save(Token token);

    void saveAll(List<Token> tokens);
}
