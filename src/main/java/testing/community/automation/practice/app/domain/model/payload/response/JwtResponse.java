package testing.community.automation.practice.app.domain.model.payload.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String type;
    private String id;
    private String username;
    private String email;
    private List<String> roles;
}
