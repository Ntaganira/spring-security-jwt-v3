package rw.heritier.jwt.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
        http
            .csrf()
            .disable()
            .authorizeRequests()
            .requestMatchers("")
            .permitAll()
            .anyRequest()
            .authenticated()
            .and()

            // https://www.youtube.com/watch?v=KxqlJblhzfI 1:31:34
        return http.build();
    }
}
