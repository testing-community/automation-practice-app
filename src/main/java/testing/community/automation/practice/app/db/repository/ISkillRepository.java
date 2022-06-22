package testing.community.automation.practice.app.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import testing.community.automation.practice.app.db.model.SkillEntity;

@Repository
public interface ISkillRepository extends JpaRepository<SkillEntity, Long> {

    List<SkillEntity> findByName(String name);
}
