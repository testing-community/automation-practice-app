package testing.community.automation.practice.app.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import testing.community.automation.practice.app.db.model.UserRoleEntity;

@Repository
public interface IUserRoleRepository extends JpaRepository<UserRoleEntity, Long> {

    List<UserRoleEntity> findByUserId(Long userid);
    List<UserRoleEntity> findByRoleId(Long roleid);
    List<UserRoleEntity> findByUserIdAndRoleId(Long userid, Long roleid);
}
