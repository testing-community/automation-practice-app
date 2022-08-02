package testing.community.automation.practice.app.domain.model.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import testing.community.automation.practice.app.domain.model.models.Skill;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MySkillsResponse {
    private String username;
    private List<Skill> skills;
}
