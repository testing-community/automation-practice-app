package testing.community.automation.practice.app.shared.services;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import testing.community.automation.practice.app.db.model.UserSkillEntity;
import testing.community.automation.practice.app.db.repository.ISkillRepository;
import testing.community.automation.practice.app.db.repository.IUserRepository;
import testing.community.automation.practice.app.db.repository.IUserSkillRepository;
import testing.community.automation.practice.app.domain.model.models.Skill;
import testing.community.automation.practice.app.domain.model.models.UserSkill;
import testing.community.automation.practice.app.shared.exceptions.AlreadyExistException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("userSkillService")
@Slf4j
public class UserSkillService implements IUserSkillService {

    @Autowired
    private IUserSkillRepository userSkillRepository;

    @Autowired
    private ISkillRepository skillRepository;

    @Autowired
    private IUserRepository userRepository;

    @Override
    public List<UserSkill> getUserSkillByUserId(Long userid) {
        return userSkillRepository.findByUserId(userid).stream().map((userSkill) -> mapperToDomain(userSkill)).collect(Collectors.toList());
    }

    @Override
    public List<UserSkill> getUserSkillBySkillId(Long skillid) {
        return userSkillRepository.findBySkillId(skillid).stream().map((userSkill) -> mapperToDomain(userSkill)).collect(Collectors.toList());
    }

    @Override
    public List<Skill> getSkillsByUsername(String username) {
        var userFound = userRepository.findByUsername(username);
        if (userFound.isEmpty() || userFound.size() > 1) {
            return null;
        }
        List<Skill> skills = new ArrayList<>();
        userSkillRepository.findByUserId(userFound.get(0).getId()).forEach((userSkill) -> {
            var skillFound = skillRepository.findById(userSkill.getSkillId());
            if (skillFound.isPresent()) {
                var skill = skillFound.get();
                skills.add(new Skill(skill.getId(), skill.getName()));
            }
        });

        return skills;
    }

    @Override
    @SneakyThrows
    public UserSkill createUserSkill(UserSkill userSkill) {
        try {
            var userSkillFound = userSkillRepository.findByUserIdAndSkillId(userSkill.getUserId(), userSkill.getSkillId());
            if (!userSkillFound.isEmpty()) {
                throw new AlreadyExistException("User already have the role");
            }
            userSkillRepository.save(new UserSkillEntity(userSkill.getUserId(), userSkill.getSkillId()));
            return mapperToDomain(userSkillRepository.findByUserIdAndSkillId(userSkill.getUserId(), userSkill.getSkillId()).get(0));
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public UserSkill updateUserSkill(Long id, UserSkill userSkill) {
        var userSkillData = userSkillRepository.findById(id);
        if (!userSkillData.isPresent()) {
            UserSkillEntity updatedUserRole = userSkillData.get();
            updatedUserRole.setSkillId(userSkill.getSkillId());
            updatedUserRole.setUserId(userSkill.getUserId());
            return mapperToDomain(userSkillRepository.save(updatedUserRole));
        }
        return null;
    }

    @Override
    public Boolean deleteUserSkill(Long id) {
        var userData = userSkillRepository.findById(id);
        if (userData.isPresent()) {
            UserSkillEntity userSkill = userData.get();
            userSkillRepository.deleteById(userSkill.getId());
            return userSkillRepository.existsById(userSkill.getId());
        }
        return false;
    }

    private UserSkill mapperToDomain(UserSkillEntity userSkillEntity){
        return new UserSkill(0L, userSkillEntity.getUserId(), userSkillEntity.getSkillId());
    }
}
