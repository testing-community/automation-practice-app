package testing.community.automation.practice.app.shared.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import testing.community.automation.practice.app.db.model.UserEntity;
import testing.community.automation.practice.app.db.repository.*;
import testing.community.automation.practice.app.domain.model.models.*;
import testing.community.automation.practice.app.domain.model.payload.request.UpdateUserRequest;
import testing.community.automation.practice.app.shared.exceptions.AlreadyExistException;

@Service("userService")
@Slf4j
public class UserService implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IUserRoleRepository userRoleRepository;

    @Autowired
    private IUserSkillRepository userSkillRepository;

    @Autowired
    private ISkillRepository skillRepository;

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public User getUser(String username) {
        Optional<UserEntity> user = userRepository.findByUsername(username).stream().findFirst();
        if (user.isPresent()) {
            return  mapperToDomain(user.get());
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll().stream().map((user) -> mapperToDomain(user)).collect(Collectors.toList());
    }

    @Override
    @SneakyThrows
    public User createUser(User user) {
        String passwordEncoded = encoder.encode(user.getPassword());
        try {
            if (!userRepository.findByUsername(user.getUsername()).isEmpty()) {
                throw new AlreadyExistException(String.format("Username %s already in use", user.getUsername()));
            } else if (!userRepository.findByEmail(user.getEmail()).isEmpty()) {
                throw new AlreadyExistException(String.format("Email %s already in use", user.getEmail()));
            }
            userRepository.save(new UserEntity(user.getEmail(), user.getUsername(), passwordEncoded));
            return mapperToDomain(userRepository.findByUsername(user.getUsername()).get(0));
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public User updateUser(String username, UpdateUserRequest updateUserRequest) {
        String passwordEncoded = updateUserRequest.getPassword() == null || updateUserRequest.getPassword().isEmpty() ?
                "" : encoder.encode(updateUserRequest.getPassword());
        Optional<UserEntity> userData = userRepository.findByUsername(username).stream().findFirst();
        if (userData.isPresent()) {
            UserEntity updatedUser = userData.get();
            updatedUser.setEmail(updateUserRequest.getEmail());
            updatedUser.setPassword(passwordEncoded.isEmpty() ? updatedUser.getPassword() : passwordEncoded);
            updatedUser.setUsername(updateUserRequest.getUsername());
            UserRoleService.updateUserRoles(userRoleRepository, roleRepository, updatedUser.getId(), updateUserRequest.getRoles());
            UserSkillService.updateUserSkills(userSkillRepository, skillRepository, updatedUser.getId(), updateUserRequest.getSkills());
            return mapperToDomain(userRepository.save(updatedUser));
        }
        return null;
    }

    @Override
    public Boolean deleteUser(String username) {
        if (username.equals("admin")) {
            return false;
        }
        Optional<UserEntity> userData = userRepository.findByUsername(username).stream().findFirst();
        if (userData.isPresent()) {
            UserEntity user = userData.get();
            userRepository.deleteById(user.getId());
            return !userRepository.existsById(user.getId());
        }
        return false;
    }

    private User mapperToDomain(UserEntity userEntity){
        return new User(userEntity.getId(), userEntity.getUsername(), userEntity.getEmail(), userEntity.getPassword(), null, null);
    }
}
