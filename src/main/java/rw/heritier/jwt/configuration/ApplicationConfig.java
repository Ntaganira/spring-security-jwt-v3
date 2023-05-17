package rw.heritier.jwt.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

import lombok.RequiredArgsConstructor;
import rw.heritier.jwt.service.UserService;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    // @Autowired is replace by @RequiredArgsConstructor
    private final UserService userService;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userService.findUserByEmail(username);
        // .orElseThrow(()-> new UsernameNotFoundException("User not fund"));
    }
}
