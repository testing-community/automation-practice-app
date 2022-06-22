package testing.community.automation.practice.app.shared.services;

import java.util.List;

import testing.community.automation.practice.app.domain.model.models.Role;
import testing.community.automation.practice.app.domain.model.models.UserRole;

public interface IUserRoleService {

    List<UserRole> getUserRoleByUserId(Long userid);
    List<UserRole> getUserRoleByRoleId(Long roleid);
    List<Role> getRolesByUserId(Long userid);
    UserRole createUserRole(UserRole userRole);
    UserRole updateUserRole(Long id, UserRole userRole);
    Boolean deleteUserRole(Long id);
}
