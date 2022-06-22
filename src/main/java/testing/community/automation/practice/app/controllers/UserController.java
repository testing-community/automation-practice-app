package testing.community.automation.practice.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import testing.community.automation.practice.app.db.enumerable.RoleEnum;
import testing.community.automation.practice.app.domain.model.models.User;
import testing.community.automation.practice.app.domain.model.models.UserRole;
import testing.community.automation.practice.app.domain.model.payload.response.ErrorResponse;
import testing.community.automation.practice.app.shared.exceptions.AlreadyExistException;
import testing.community.automation.practice.app.shared.services.IRoleService;
import testing.community.automation.practice.app.shared.services.IUserRoleService;
import testing.community.automation.practice.app.shared.services.IUserService;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private PasswordEncoder encoder;

    @GetMapping
    public ResponseEntity<?> getUsers() {
        var users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("{username}")
    public ResponseEntity<?> getUser(@PathVariable("username") String username) {
        var user = userService.getUser(username);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            //For the next line we need to ensure Role table already have the values
            var role = roleService.getRoleByName(RoleEnum.MODERATOR.getValue()).get(0);
            var userCreated = userService.createUser(user);
            userRoleService.createUserRole(new UserRole(0L, userCreated.getId(), role.getId()));
            return new ResponseEntity<>(userCreated, HttpStatus.CREATED);
        } catch (Exception e) {
            if (e instanceof AlreadyExistException) {
                return new ResponseEntity<>(new ErrorResponse(AlreadyExistException.class.getSimpleName(), e.getMessage()), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(new ErrorResponse(AlreadyExistException.class.getSimpleName(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("{username}")
    public ResponseEntity<?> updateUser(@PathVariable("username") String username, @RequestBody User user) {
        var userUpdated = userService.updateUser(username, user);
        if (userUpdated != null) {
            return new ResponseEntity<>(userUpdated, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("{username}")
    public ResponseEntity<?> deleteUser(@PathVariable("username") String username) {
        var userDeleted = userService.deleteUser(username);
        if (userDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
