package testing.community.automation.practice.app.shared.services;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import testing.community.automation.practice.app.db.model.SkillEntity;
import testing.community.automation.practice.app.db.model.UserEntity;
import testing.community.automation.practice.app.db.model.UserSkillEntity;
import testing.community.automation.practice.app.db.repository.ISkillRepository;
import testing.community.automation.practice.app.db.repository.IUserRepository;
import testing.community.automation.practice.app.db.repository.IUserSkillRepository;
import testing.community.automation.practice.app.domain.model.models.Skill;
import testing.community.automation.practice.app.domain.model.models.UserSkill;
import testing.community.automation.practice.app.shared.exceptions.AlreadyExistException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
        List<UserEntity> userFound = userRepository.findByUsername(username);
        if (userFound.isEmpty() || userFound.size() > 1) {
            return null;
        }
        List<Skill> skills = new ArrayList<>();
        userSkillRepository.findByUserId(userFound.get(0).getId()).forEach((userSkill) -> {
            Optional<SkillEntity> skillFound = skillRepository.findById(userSkill.getSkillId());
            if (skillFound.isPresent()) {
                SkillEntity skill = skillFound.get();
                skills.add(new Skill(skill.getId(), skill.getName()));
            }
        });

        return skills;
    }

    @Override
    @SneakyThrows
    public UserSkill createUserSkill(UserSkill userSkill) {
        try {
            List<UserSkillEntity> userSkillFound = userSkillRepository.findByUserIdAndSkillId(userSkill.getUserId(), userSkill.getSkillId());
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
        Optional<UserSkillEntity> userSkillData = userSkillRepository.findById(id);
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
        Optional<UserSkillEntity> userData = userSkillRepository.findById(id);
        if (userData.isPresent()) {
            UserSkillEntity userSkill = userData.get();
            userSkillRepository.deleteById(userSkill.getId());
            return !userSkillRepository.existsById(userSkill.getId());
        }
        return false;
    }

    public static List<UserSkill> updateUserSkills(IUserSkillRepository userSkillRepository, ISkillRepository skillRepository, Long userId, Set<Skill> skills) {
        List<UserSkill> userSkills = new ArrayList<>();
        if (!skills.isEmpty()) {
            List<UserSkillEntity> userSkillsFound = userSkillRepository.findByUserId(userId);
            userSkillsFound.forEach(userSkillFound -> userSkillRepository.deleteById(userSkillFound.getId()));
            skills.forEach(skillUpdate -> {
                SkillEntity skill = skillRepository.findByName(skillUpdate.getName()).get(0);
                if (skill != null) {
                    userSkillRepository.save(new UserSkillEntity(userId, skill.getId()));
                }
            });
        }
        return userSkills;
    }

    private UserSkill mapperToDomain(UserSkillEntity userSkillEntity){
        return new UserSkill(0L, userSkillEntity.getUserId(), userSkillEntity.getSkillId());
    }
}
