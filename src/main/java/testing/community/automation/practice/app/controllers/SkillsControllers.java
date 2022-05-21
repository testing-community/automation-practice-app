package testing.community.automation.practice.app.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import testing.community.automation.practice.app.controllers.dto.Skill;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.*;

@RestController
@RequestMapping("skills")
public class SkillsControllers {
    private HashMap<String, String> skills;

    public SkillsControllers() {
        skills = new HashMap<>() {{
            put("671645b5-306f-4328-9b32-0c19873373e5", "Java");
            put("9269d596-8e54-43c4-93c8-d8ee438abbe1", "Selenium");
            put("5cb5c0a9-f2be-4daa-9cf0-3d69caba5590", "Automation API Testing");
        }};
    }

    @GetMapping()
    public List<Skill> getSkillsPaginated(
            @RequestParam(name ="page", defaultValue = "0") @Min(0) final Optional<Integer> page,
            @RequestParam(name = "offset", defaultValue = "10") @Min(1) @Max(10) final Optional<Integer> offset) {
        final int size = offset.get() > 10 ? 10 : offset.get();
        final int start = page.get() * size;

        var skills = mapSkills();
        final var totalElements = skills.size();

        if (totalElements < start) {
            return Collections.emptyList();
        }

        if (totalElements < start + size) {
            return  skills.subList(start, totalElements);
        }

        return skills.subList(start, size);
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

    private List<Skill> mapSkills() {
        return skills.entrySet().stream().map(item -> new Skill(item.getKey(), item.getValue())).toList();
    }
}
