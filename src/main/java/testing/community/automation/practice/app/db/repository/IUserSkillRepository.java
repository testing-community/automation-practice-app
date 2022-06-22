package testing.community.automation.practice.app.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import testing.community.automation.practice.app.db.model.UserSkillEntity;

import java.util.List;

@Repository
public interface IUserSkillRepository extends JpaRepository<UserSkillEntity, Long> {

    List<UserSkillEntity> findByUserId(Long userid);
    List<UserSkillEntity> findBySkillId(Long roleid);
    List<UserSkillEntity> findByUserIdAndSkillId(Long userid, Long skillid);
}
