package rw.heritier.jwt.auth;

import java.io.IOException;

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
import rw.heritier.jwt.model.Token;
import rw.heritier.jwt.model.TokenType;
import rw.heritier.jwt.model.User;
import rw.heritier.jwt.service.TokenService;
import rw.heritier.jwt.service.UserService;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

        private final UserService userService;
        private final TokenService tokenService;
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

                var savedUser = userService.save(user);
                var jwtToken = jwtService.generateToken(user);
                var jwtRefreshToken = jwtService.generateRefreshToken(user);
                saveUserToken(savedUser, jwtToken);
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

                var user = userService.findUserByEmail(request.getEmail()).orElseThrow();
                var jwtToken = jwtService.generateToken(user);
                var jwtRefreshToken = jwtService.generateRefreshToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, jwtToken);
                return AuthenticationResponseDTO.builder()
                                .accessToken(jwtToken)
                                .refreshToken(jwtRefreshToken)
                                .build();
        }

        private void saveUserToken(User user, String jwtToken) {
                var token = Token.builder()
                                .user(user)
                                .token(jwtToken)
                                .tokenType(TokenType.BEARER)
                                .expired(false)
                                .revoked(false)
                                .build();
                tokenService.save(token);
        }

        private void revokeAllUserTokens(User user) {
                var validUserTokens = tokenService.findAllValidTokenByUser(user.getId());
                if (validUserTokens.isEmpty())
                        return;
                validUserTokens.forEach(token -> {
                        token.setExpired(true);
                        token.setRevoked(true);
                });
                tokenService.saveAll(validUserTokens);
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
                        var user = this.userService.findUserByEmail(userEmail).orElseThrow();
                        if (jwtService.isTokenValid(refreshToken, user)) {
                                var accessToken = jwtService.generateToken(user);
                                revokeAllUserTokens(user);
                                saveUserToken(user, accessToken);
                                var authResponse = AuthenticationResponseDTO.builder()
                                                .accessToken(accessToken)
                                                .refreshToken(refreshToken)
                                                .build();
                                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
                        }
                }
        }
}
