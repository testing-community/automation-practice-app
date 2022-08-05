package testing.community.automation.practice.app.controllers.security;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import testing.community.automation.practice.app.controllers.security.jwt.JwtUtils;
import testing.community.automation.practice.app.controllers.security.services.UserDetailsImpl;
import testing.community.automation.practice.app.db.enumerable.RoleEnum;
import testing.community.automation.practice.app.domain.model.models.Role;
import testing.community.automation.practice.app.domain.model.models.User;
import testing.community.automation.practice.app.domain.model.models.UserRole;
import testing.community.automation.practice.app.domain.model.payload.request.LoginRequest;
import testing.community.automation.practice.app.domain.model.payload.request.SignupRequest;
import testing.community.automation.practice.app.domain.model.payload.response.ErrorResponse;
import testing.community.automation.practice.app.domain.model.payload.response.JwtResponse;
import testing.community.automation.practice.app.shared.exceptions.AlreadyExistException;
import testing.community.automation.practice.app.shared.services.IRoleService;
import testing.community.automation.practice.app.shared.services.IUserRoleService;
import testing.community.automation.practice.app.shared.services.IUserService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private IUserService userService;

    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private IRoleService roleService;

    @PostMapping("login")
    public ResponseEntity<?> authenticate(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                "Bearer",
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        User user = new User(0L, signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()), null, null);

        try {
            //For the next line we need to ensure Role table already have the values
            Role role = roleService.getRoleByName(RoleEnum.USER.getValue()).get(0);
            User userCreated = userService.createUser(user);
            userRoleService.createUserRole(new UserRole(0L, userCreated.getId(), role.getId()));
            return new ResponseEntity<>(userCreated, HttpStatus.CREATED);
        } catch (Exception e) {
            if (e instanceof AlreadyExistException) {
                return new ResponseEntity<>(new ErrorResponse(AlreadyExistException.class.getSimpleName(), e.getMessage()), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(new ErrorResponse(e.getClass().getSimpleName(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
