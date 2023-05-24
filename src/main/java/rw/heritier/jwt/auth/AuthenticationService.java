package rw.heritier.jwt.auth;

import java.io.IOException;
import java.net.http.HttpRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

        public void refreshToken(
                        HttpServletRequest request,
                        HttpServletResponse response) throws IOException {

                final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

                final String refreshToken;
                final String userEmail;
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                        return;
                }
                // Extraction token from header
                refreshToken = authHeader.substring(7);
                // Extracting user email / username
                userEmail = jwtService.extractUsername(refreshToken);
                if (userEmail != null) {
                        var user = this.repository.findUserByEmail(userEmail).orElseThrow();
                        if (jwtService.isTokenValid(refreshToken, user)) {
                                var accessToken = jwtService.generateToken(user);
                                var authResponse = AuthenticationResponseDTO.builder()
                                                .accessToken(accessToken)
                                                .refreshToken(refreshToken)
                                                .build();
                                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
                        }
                }
        }
}
