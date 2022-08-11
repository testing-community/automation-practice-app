package testing.community.automation.practice.app.shared.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import testing.community.automation.practice.app.db.model.SkillEntity;
import testing.community.automation.practice.app.db.repository.ISkillRepository;
import testing.community.automation.practice.app.domain.model.models.Skill;
import testing.community.automation.practice.app.shared.exceptions.AlreadyExistException;

@Service("skillService")
@Slf4j
public class SkillService implements ISkillService {

    @Autowired
    private ISkillRepository skillRepository;

    @Override
    public Skill getSkill(Long id) {
        Optional<SkillEntity> skill = skillRepository.findById(id);
        if (skill.isPresent()) {
            return  mapperToDomain(skill.get());
        }
        return null;
    }

    @Override
    public List<Skill> getAllSkills() {
        return skillRepository.findAll().stream().map((skill) -> mapperToDomain(skill)).collect(Collectors.toList());
    }

    @Override
    @SneakyThrows
    public Skill createSkill(Skill skill) {
        try {
            if (!skillRepository.findByName(skill.getName()).isEmpty()) {
                throw new AlreadyExistException(String.format("Name %s already exist", skill.getName()));
            }
            skillRepository.save(new SkillEntity(skill.getName()));
            return mapperToDomain(skillRepository.findByName(skill.getName()).get(0));
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public Skill updateSkill(Long id, Skill skill) {
        SkillEntity skillFound = skillRepository.findById(id).get();
        if (skillFound != null) {
            skillFound.setName(skill.getName());
            return mapperToDomain(skillRepository.save(skillFound));
        }
        return null;
    }

    @Override
    public Boolean deleteSkill(Long id) {
        SkillEntity skill = skillRepository.findById(id).get();
        if (skill != null) {
            skillRepository.deleteById(skill.getId());
            return !skillRepository.existsById(skill.getId());
        }
        return false;
    }

    private Skill mapperToDomain(SkillEntity skillEntity){
        return new Skill(skillEntity.getId(), skillEntity.getName());
    }
}
