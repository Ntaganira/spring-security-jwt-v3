package rw.heritier.jwt.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor // Autowired //Automatically injects
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDTO> register(
            @RequestBody RegisterRequestDTO registerRequest) {
        return ResponseEntity.ok(service.register(registerRequest));

    }

    @PostMapping("/authenticate")
    public ResponseEntity<Map<String, Object>> authenticateRequest(
            @RequestBody AuthenticationRequestDTO request) {
        Map<String, Object> rtn = new HashMap<>();
        AuthenticationResponseDTO tokenResp = service.authenticate(request);
        if (tokenResp == null) {
            rtn.put("code", HttpStatus.BAD_REQUEST);
            rtn.put("message", "Invalid username or password !");
            return new ResponseEntity<>(rtn, HttpStatus.BAD_REQUEST);
        }

        rtn.put("code", HttpStatus.ACCEPTED);
        rtn.put("message", "Access granted !");
        rtn.put("result", tokenResp);
        return new ResponseEntity<>(rtn, HttpStatus.ACCEPTED);
    }
}
