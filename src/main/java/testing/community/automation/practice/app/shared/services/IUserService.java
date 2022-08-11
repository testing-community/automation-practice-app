package testing.community.automation.practice.app.shared.services;

import java.util.List;

import testing.community.automation.practice.app.domain.model.models.User;
import testing.community.automation.practice.app.domain.model.payload.request.UpdateUserRequest;

public interface IUserService {

    User getUser(String username);
    List<User> getAllUsers();
    User createUser(User user);
    User updateUser(String username, UpdateUserRequest updateUserRequest);
    Boolean deleteUser(String username);
}
