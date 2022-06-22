package testing.community.automation.practice.app.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import testing.community.automation.practice.app.db.model.RoleEntity;

@Repository
public interface IRoleRepository extends JpaRepository<RoleEntity, Long> {

    List<RoleEntity> findByName(String name);
}
