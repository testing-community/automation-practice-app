package testing.community.automation.practice.app.shared.services;

import testing.community.automation.practice.app.domain.model.models.Skill;
import testing.community.automation.practice.app.domain.model.models.UserSkill;

import java.util.List;

public interface IUserSkillService {

    List<UserSkill> getUserSkillByUserId(Long userid);
    List<UserSkill> getUserSkillBySkillId(Long skillid);
    List<Skill> getSkillsByUsername(String username);
    UserSkill createUserSkill(UserSkill userSkill);
    UserSkill updateUserSkill(Long id, UserSkill userSkill);
    Boolean deleteUserSkill(Long id);
}
