package testing.community.automation.practice.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import testing.community.automation.practice.app.db.enumerable.RoleEnum;
import testing.community.automation.practice.app.domain.model.models.Role;
import testing.community.automation.practice.app.domain.model.models.User;
import testing.community.automation.practice.app.domain.model.payload.request.SignupRequest;
import testing.community.automation.practice.app.domain.model.payload.request.UpdateUserRequest;
import testing.community.automation.practice.app.domain.model.payload.response.ErrorResponse;
import testing.community.automation.practice.app.shared.exceptions.AlreadyExistException;
import testing.community.automation.practice.app.shared.services.IRoleService;
import testing.community.automation.practice.app.shared.services.IUserRoleService;
import testing.community.automation.practice.app.shared.services.IUserService;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IUserRoleService userRoleService;

    @GetMapping
    public ResponseEntity<?> getUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("{username}")
    public ResponseEntity<?> getUser(@PathVariable("username") String username) {
        User user = userService.getUser(username);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody SignupRequest signUpRequest) {
        try {
            Role defaultRole = roleService.getRoleByName(RoleEnum.MODERATOR.getValue()).get(0);
            Set<Role> roles = roleService.updateRoles(signUpRequest.getRole());
            if (roles.isEmpty()) {
                roles.add(defaultRole);
            }
            User userCreated = userService.createUser(new User(signUpRequest.getUsername(), signUpRequest.getPassword(), signUpRequest.getEmail(), roles));
            userRoleService.createUserRoles(userCreated.getId(), roles);
            return new ResponseEntity<>(userCreated, HttpStatus.CREATED);
        } catch (Exception e) {
            if (e instanceof AlreadyExistException) {
                return new ResponseEntity<>(new ErrorResponse(AlreadyExistException.class.getSimpleName(), e.getMessage()), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(new ErrorResponse(e.getClass().getSimpleName(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("{username}")
    public ResponseEntity<?> updateUser(@PathVariable("username") String username, @RequestBody UpdateUserRequest updateUserRequest) {
        User userUpdated = userService.updateUser(username, updateUserRequest);
        if (userUpdated != null) {
            return new ResponseEntity<>(userUpdated, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("{username}")
    public ResponseEntity<?> deleteUser(@PathVariable("username") String username) {
        Boolean userDeleted = userService.deleteUser(username);
        if (userDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
