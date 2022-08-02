package testing.community.automation.practice.app.controllers.security.services;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import testing.community.automation.practice.app.domain.model.models.User;
import testing.community.automation.practice.app.shared.services.IRoleService;
import testing.community.automation.practice.app.shared.services.IUserRoleService;
import testing.community.automation.practice.app.shared.services.IUserService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private IUserService userService;

    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private IRoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var userFound = userService.getUser(username);
        if (userFound == null) {
            return null;
        }
        var user = new User(userFound.getId(), userFound.getUsername(), userFound.getEmail(), userFound.getPassword(), null, null);
        var roles = userRoleService.getRolesByUserId(user.getId());
        user.setRoles(new HashSet<>(roles));
        return build(user);
    }

    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());


        return new UserDetailsImpl(
                UUID.randomUUID().toString(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authorities);
    }
}
