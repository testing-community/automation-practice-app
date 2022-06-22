package testing.community.automation.practice.app.shared.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import testing.community.automation.practice.app.db.model.RoleEntity;
import testing.community.automation.practice.app.db.repository.IRoleRepository;
import testing.community.automation.practice.app.domain.model.models.Role;
import testing.community.automation.practice.app.shared.exceptions.AlreadyExistException;

@Service("roleService")
@Slf4j
public class RoleService implements IRoleService {

    @Autowired
    private IRoleRepository roleRepository;

    @Override
    public Role getRole(Long id) {
        var roleFound = roleRepository.findById(id);
        if (roleFound.isPresent()) {
            var role = roleFound.get();
            return  new Role(role.getId(), role.getName());
        }
        return null;
    }

    @Override
    public List<Role> getRoleByName(String name) {
        return roleRepository.findByName(name).stream().map((role) -> new Role(role.getId(), role.getName())).collect(Collectors.toList());
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll().stream().map((role) -> new Role(role.getId(), role.getName())).collect(Collectors.toList());
    }

    @Override
    @SneakyThrows
    public Role createRole(Role role) {
        try {
            if (!roleRepository.findByName(role.getName()).isEmpty()) {
                throw new AlreadyExistException(String.format("Name %s already exist", role.getName()));
            }
            roleRepository.save(new RoleEntity(role.getName()));
            return mapperToDomain(roleRepository.findByName(role.getName()).get(0));
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public Role updateRole(Long id, Role role) {
        var userData = roleRepository.findById(id).stream().findFirst();
        if (!userData.isPresent()) {
            var updatedRole = userData.get();
            updatedRole.setName(role.getName());
            return mapperToDomain(roleRepository.save(updatedRole));
        }
        return null;
    }

    @Override
    public Boolean deleteRole(Long id) {
        if (id < 4) {
            return false;
        }
        var roleFound = roleRepository.findById(id).stream().findFirst();
        if (roleFound.isPresent()) {
            var role = roleFound.get();
            roleRepository.deleteById(role.getId());
            return roleRepository.existsById(role.getId());
        }
        return false;
    }

    private Role mapperToDomain(RoleEntity roleEntity){
        return new Role(roleEntity.getId(), roleEntity.getName());
    }
}
