package rw.heritier.jwt.auth;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    public AuthenticationResponseDTO register(RegisterRequestDTO request) {
        return null;
    }

    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request) {
        return null;
    }
}
