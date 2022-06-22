package testing.community.automation.practice.app.shared.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import testing.community.automation.practice.app.db.model.UserEntity;
import testing.community.automation.practice.app.db.repository.IUserRepository;
import testing.community.automation.practice.app.domain.model.models.User;
import testing.community.automation.practice.app.shared.exceptions.AlreadyExistException;

@Service("userService")
@Slf4j
public class UserService implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    @Override
    public User getUser(String username) {
        var user = userRepository.findByUsername(username).stream().findFirst();
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
        try {
            if (!userRepository.findByUsername(user.getUsername()).isEmpty()) {
                throw new AlreadyExistException(String.format("Username %s already in use", user.getUsername()));
            } else if (!userRepository.findByEmail(user.getEmail()).isEmpty()) {
                throw new AlreadyExistException(String.format("Email %s already in use", user.getEmail()));
            }
            userRepository.save(new UserEntity(user.getEmail(), user.getUsername(), user.getPassword()));
            return mapperToDomain(userRepository.findByUsername(user.getUsername()).get(0));
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public User updateUser(String username, User user) {
        var userData = userRepository.findByUsername(username).stream().findFirst();
        if (!userData.isPresent()) {
            UserEntity updatedUser = userData.get();
            updatedUser.setEmail(user.getEmail());
            updatedUser.setPassword(user.getPassword());
            updatedUser.setUsername(user.getUsername());
            return mapperToDomain(userRepository.save(updatedUser));
        }
        return null;
    }

    @Override
    public Boolean deleteUser(String username) {
        if (username.equals("admin")) {
            return false;
        }
        var userData = userRepository.findByUsername(username).stream().findFirst();
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
