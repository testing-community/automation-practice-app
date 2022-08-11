package testing.community.automation.practice.app.domain.model.payload.request;

import lombok.Getter;
import lombok.Setter;
import testing.community.automation.practice.app.domain.model.models.Role;
import testing.community.automation.practice.app.domain.model.models.Skill;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UpdateUserRequest {
    @Size(max = 255)
    private String username;

    @Size(max = 255)
    @Email
    private String email;

    @Size(max = 255)
    private String password;

    private Set<Role> roles = new HashSet<>();

    private Set<Skill> skills = new HashSet<>();
}
