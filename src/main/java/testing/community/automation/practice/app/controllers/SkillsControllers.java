package testing.community.automation.practice.app.controllers;

import org.springframework.web.bind.annotation.*;
import testing.community.automation.practice.app.controllers.dto.Skill;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("skills")
public class SkillsControllers {
    private HashMap<String, String> skills;

    public SkillsControllers() {
        skills = new HashMap<>() {{
            put(UUID.randomUUID().toString(), "Java");
            put(UUID.randomUUID().toString(), "Selenium");
            put(UUID.randomUUID().toString(), "Automation API Testing");
        }};
    }

    @GetMapping()
    public List<Skill> getSkills() {
        return skills.entrySet().stream().map(entry -> new Skill(entry.getKey(), entry.getValue())).toList();
    }

    @PostMapping
    public void saveSkill(String name) {
        skills.put(UUID.randomUUID().toString(), name);
    }

    @PutMapping
    public void forceUpdate(Skill skill) {
        skills.put(skill.getId(), skill.getName());
    }

    @DeleteMapping
    public void deleteSkill(String id) {
        skills.remove(id);
    }

    @PatchMapping
    public void update(Skill skill) {
        skills.replace(skill.getId(), skill.getName());
    }
}
