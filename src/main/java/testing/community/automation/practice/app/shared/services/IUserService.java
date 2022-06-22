package testing.community.automation.practice.app.shared.services;

import java.util.List;

import testing.community.automation.practice.app.domain.model.models.User;

public interface IUserService {

    User getUser(String username);
    List<User> getAllUsers();
    User createUser(User user);
    User updateUser(String username, User user);
    Boolean deleteUser(String username);
}
