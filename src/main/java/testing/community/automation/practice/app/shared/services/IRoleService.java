package testing.community.automation.practice.app.shared.services;

import java.util.List;

import testing.community.automation.practice.app.domain.model.models.Role;

public interface IRoleService {

    Role getRole(Long id);
    List<Role> getRoleByName(String name);
    List<Role> getAllRoles();
    Role createRole(Role role);
    Role updateRole(Long id, Role role);
    Boolean deleteRole(Long id);
}
