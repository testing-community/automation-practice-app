package testing.community.automation.practice.app.shared.services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
        Optional<RoleEntity> roleFound = roleRepository.findById(id);
        if (roleFound.isPresent()) {
            RoleEntity role = roleFound.get();
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
        RoleEntity roleFound = roleRepository.findById(id).get();
        if (roleFound != null) {
            roleFound.setName(role.getName());
            return mapperToDomain(roleRepository.save(roleFound));
        }
        return null;
    }

    @Override
    public Boolean deleteRole(Long id) {
        if (id < 4) {
            return false;
        }
        RoleEntity role = roleRepository.findById(id).get();
        if (role != null) {
            roleRepository.deleteById(role.getId());
            return !roleRepository.existsById(role.getId());
        }
        return false;
    }

    @Override
    public Set<Role> updateRoles(Set<String> roles) {
        Set<Role> rolesUpdated = new HashSet<>();
        roles.forEach(role -> {
            List<Role> rolesFound = getRoleByName(role);
            if (!rolesFound.isEmpty()) {
                rolesUpdated.add(rolesFound.get(0));
            }
        });
        return rolesUpdated;
    }

    private Role mapperToDomain(RoleEntity roleEntity){
        return new Role(roleEntity.getId(), roleEntity.getName());
    }
}
