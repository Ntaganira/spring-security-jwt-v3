package rw.heritier.jwt.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.heritier.jwt.model.Role;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role;
}
