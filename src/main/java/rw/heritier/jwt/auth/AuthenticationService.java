package rw.heritier.jwt.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import rw.heritier.jwt.configuration.JwtService;
import rw.heritier.jwt.model.Role;
import rw.heritier.jwt.model.User;
import rw.heritier.jwt.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

        private final UserRepository repository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final AuthenticationManager authManager;

        public AuthenticationResponseDTO register(RegisterRequestDTO request) {
                User user = User.builder()
                                .firstName(request.getFirstName())
                                .lastName(request.getLastName())
                                .email(request.getEmail())
                                .password(passwordEncoder.encode(request.getPassword()))
                                .role(Role.USER)
                                .build();

                repository.save(user);
                var jwtToken = jwtService.generateToken(user);
                var jwtRefreshToken = jwtService.generateRefreshToken(user);
                return AuthenticationResponseDTO.builder()
                                .accessToken(jwtToken)
                                .refreshToken(jwtRefreshToken)
                                .build();
        }

        public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request) {
                authManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getEmail(),
                                                request.getPassword()));

                var user = repository.findUserByEmail(request.getEmail()).orElseThrow();
                var jwtToken = jwtService.generateToken(user);
                var jwtRefreshToken = jwtService.generateRefreshToken(user);
                return AuthenticationResponseDTO.builder()
                                .accessToken(jwtToken)
                                .refreshToken(jwtRefreshToken)
                                .build();
        }
}
