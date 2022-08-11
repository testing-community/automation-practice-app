package testing.community.automation.practice.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import testing.community.automation.practice.app.domain.model.models.Role;
import testing.community.automation.practice.app.domain.model.payload.response.ErrorResponse;
import testing.community.automation.practice.app.shared.exceptions.AlreadyExistException;
import testing.community.automation.practice.app.shared.services.IRoleService;

import java.util.List;

@RestController
@RequestMapping("api/test/roles")
public class RoleTestController {

    @Autowired
    private IRoleService roleService;

    @GetMapping
    public ResponseEntity<?> getRoles() {
        List<Role> roles = roleService.getAllRoles();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getRole(@PathVariable("id") Long id) {
        Role role = roleService.getRole(id);
        if (role != null) {
            return new ResponseEntity<>(role, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<?> createRole(@RequestBody Role role) {
        try {
            Role roleCreated = roleService.createRole(role);
            return new ResponseEntity<>(roleCreated, HttpStatus.CREATED);
        } catch (Exception e) {
            if (e instanceof AlreadyExistException) {
                return new ResponseEntity<>(new ErrorResponse(AlreadyExistException.class.getSimpleName(), e.getMessage()), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(new ErrorResponse(e.getClass().getSimpleName(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateRole(@PathVariable("id") Long id, @RequestBody Role role) {
        Role roleUpdated = roleService.updateRole(id, role);
        if (roleUpdated != null) {
            return new ResponseEntity<>(roleUpdated, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteRole(@PathVariable("id") Long id) {
        Boolean roleDeleted = roleService.deleteRole(id);
        if (roleDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
