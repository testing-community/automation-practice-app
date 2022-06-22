package testing.community.automation.practice.app.controllers;

import java.util.Collections;
import java.util.Optional;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import testing.community.automation.practice.app.controllers.security.jwt.JwtUtils;
import testing.community.automation.practice.app.domain.model.models.Skill;
import testing.community.automation.practice.app.domain.model.payload.response.ErrorResponse;
import testing.community.automation.practice.app.domain.model.payload.response.MySkillsResponse;
import testing.community.automation.practice.app.shared.exceptions.AlreadyExistException;
import testing.community.automation.practice.app.shared.services.ISkillService;
import testing.community.automation.practice.app.shared.services.IUserService;
import testing.community.automation.practice.app.shared.services.IUserSkillService;

@RestController
@RequestMapping("skills")
public class SkillController {

    @Autowired
    private ISkillService skillService;

    @Autowired
    private IUserSkillService userSkillService;

    @Autowired
    private IUserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping
    public ResponseEntity<?> getSkills(
            @RequestParam(name ="page", defaultValue = "0") @Min(0) final Optional<Integer> page,
            @RequestParam(name = "offset", defaultValue = "10") @Min(1) @Max(10) final Optional<Integer> offset) {
        final int size = offset.get() > 10 ? 10 : offset.get();
        final int start = page.get() * size;

        var skills = skillService.getAllSkills();

        final var totalElements = skills.size();

        if (totalElements < start) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        }

        if (totalElements < start + size) {
            return new ResponseEntity<>(skills.subList(start, totalElements), HttpStatus.OK);
        }

        return new ResponseEntity<>(skills.subList(start, size), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getSkill(@PathVariable("id") Long id) {
        var skill = skillService.getSkill(id);
        if (skill != null) {
            return new ResponseEntity<>(skill, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("mine")
    public ResponseEntity<?> getMySkills(@RequestHeader("Authorization") String bearerToken) {
        var token = bearerToken.split(" ")[1];
        var username = jwtUtils.getUserNameFromJwtToken(token);
        var skills = userSkillService.getSkillsByUsername(username);

        return new ResponseEntity<>(new MySkillsResponse(username, skills), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createSkill(@RequestBody Skill skill) {
        try {
            var skillCreated = skillService.createSkill(skill);
            return new ResponseEntity<>(skillCreated, HttpStatus.CREATED);
        } catch (Exception e) {
            if (e instanceof AlreadyExistException) {
                return new ResponseEntity<>(new ErrorResponse(AlreadyExistException.class.getSimpleName(), e.getMessage()), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(new ErrorResponse(AlreadyExistException.class.getSimpleName(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateSkill(@PathVariable("id") Long id, @RequestBody Skill skill) {
        var skillUpdated = skillService.updateSkill(id, skill);
        if (skillUpdated != null) {
            return new ResponseEntity<>(skillUpdated, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteSkill(@PathVariable("id") Long id) {
        var skillDeleted = skillService.deleteSkill(id);
        if (skillDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
