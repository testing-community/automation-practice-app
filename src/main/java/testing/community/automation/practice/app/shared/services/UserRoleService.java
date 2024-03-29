package testing.community.automation.practice.app.shared.services;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import testing.community.automation.practice.app.db.model.RoleEntity;
import testing.community.automation.practice.app.db.model.UserRoleEntity;
import testing.community.automation.practice.app.db.repository.IRoleRepository;
import testing.community.automation.practice.app.db.repository.IUserRoleRepository;
import testing.community.automation.practice.app.domain.model.models.Role;
import testing.community.automation.practice.app.domain.model.models.UserRole;
import testing.community.automation.practice.app.shared.exceptions.AlreadyExistException;

@Service("userRoleService")
@Slf4j
public class UserRoleService implements IUserRoleService {

    @Autowired
    private IUserRoleRepository userRoleRepository;

    @Autowired
    private IRoleRepository roleRepository;

    @Override
    public List<UserRole> getUserRoleByUserId(Long userid) {
        return userRoleRepository.findByUserId(userid).stream().map((userRole) -> mapperToDomain(userRole)).collect(Collectors.toList());
    }

    @Override
    public List<UserRole> getUserRoleByRoleId(Long roleid) {
        return userRoleRepository.findByRoleId(roleid).stream().map((userRole) -> mapperToDomain(userRole)).collect(Collectors.toList());
    }

    @Override
    public List<Role> getRolesByUserId(Long userid) {
        ArrayList<Role> roles = new ArrayList<>();
        userRoleRepository.findByUserId(userid).forEach((userRole) -> {
            Optional<RoleEntity> roleFound = roleRepository.findById(userRole.getRoleId());
            if (roleFound.isPresent()) {
                RoleEntity role = roleFound.get();
                roles.add(new Role(role.getId(), role.getName()));
            }
        });

        return roles;
    }

    @Override
    @SneakyThrows
    public UserRole createUserRole(UserRole userRole) {
        try {
            List<UserRoleEntity> userRoleFound = userRoleRepository.findByUserIdAndRoleId(userRole.getUserId(), userRole.getRoleId());
            if (!userRoleFound.isEmpty()) {
                throw new AlreadyExistException("User already have the role");
            }
            userRoleRepository.save(new UserRoleEntity(userRole.getUserId(), userRole.getRoleId()));
            return mapperToDomain(userRoleRepository.findByUserIdAndRoleId(userRole.getUserId(), userRole.getRoleId()).get(0));
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public UserRole updateUserRole(Long id, UserRole userRole) {
        Optional<UserRoleEntity> userRoleData = userRoleRepository.findById(id);
        if (!userRoleData.isPresent()) {
            UserRoleEntity updatedUserRole = userRoleData.get();
            updatedUserRole.setRoleId(userRole.getRoleId());
            updatedUserRole.setUserId(userRole.getUserId());
            return mapperToDomain(userRoleRepository.save(updatedUserRole));
        }
        return null;
    }

    @Override
    public Boolean deleteUserRole(Long id) {
        Optional<UserRoleEntity> userData = userRoleRepository.findById(id);
        if (userData.isPresent()) {
            UserRoleEntity userRole = userData.get();
            userRoleRepository.deleteById(userRole.getId());
            return !userRoleRepository.existsById(userRole.getId());
        }
        return false;
    }

    @Override
    public Set<UserRole> createUserRoles(Long userId, Set<Role> roles) {
        Set<UserRole> userRolesUpdated = new HashSet<>();
        roles.forEach(role -> {
            userRolesUpdated.add(createUserRole(new UserRole(0L, userId, role.getId())));
        });
        return userRolesUpdated;
    }

    public static List<UserRole> updateUserRoles(IUserRoleRepository userRoleRepository, IRoleRepository roleRepository, Long userId, Set<Role> roles) {
        List<UserRole> userRoles = new ArrayList<>();
        if (!roles.isEmpty()) {
            List<UserRoleEntity> userRolesFound = userRoleRepository.findByUserId(userId);
            userRolesFound.forEach(userRoleFound -> userRoleRepository.deleteById(userRoleFound.getId()));
            roles.forEach(roleUpdate -> {
                RoleEntity role = roleRepository.findByName(roleUpdate.getName()).get(0);
                if (role != null) {
                    userRoleRepository.save(new UserRoleEntity(userId, role.getId()));
                }
            });
        }
        return userRoles;
    }

    private UserRole mapperToDomain(UserRoleEntity userRoleEntity){
        return new UserRole(userRoleEntity.getId(), userRoleEntity.getUserId(), userRoleEntity.getRoleId());
    }
}
