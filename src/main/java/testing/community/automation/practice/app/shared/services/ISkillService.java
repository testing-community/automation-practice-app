package testing.community.automation.practice.app.shared.services;

import java.util.List;

import testing.community.automation.practice.app.domain.model.models.Skill;

public interface ISkillService {

    Skill getSkill(Long id);
    List<Skill> getAllSkills();
    Skill createSkill(Skill skill);
    Skill updateSkill(Long id, Skill skill);
    Boolean deleteSkill(Long id);
}
