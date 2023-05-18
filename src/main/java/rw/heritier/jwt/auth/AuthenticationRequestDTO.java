package rw.heritier.jwt.auth;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequestDTO {
    private String email;
    private String password;
}
