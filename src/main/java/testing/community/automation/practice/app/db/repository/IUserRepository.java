package testing.community.automation.practice.app.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import testing.community.automation.practice.app.db.model.UserEntity;

@Repository
public interface IUserRepository extends JpaRepository<UserEntity, Long> {

    List<UserEntity> findByUsername(String username);
    List<UserEntity> findByEmail(String email);
}
