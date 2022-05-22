package testing.community.automation.practice.app.controllers.security.services;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import testing.community.automation.practice.app.domain.model.models.Role;
import testing.community.automation.practice.app.domain.model.models.User;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.equals("admin")) {
            User user = new User(
                    username,
                    "admin@automation.com",
                    "$2a$10$HkkcaMM6MsOp0lob/iDGIuibyve6wUKU5JQRhzCCOH45JoGSlsN9i");

            Set<Role> roles = new HashSet<>();
            Role role = new Role("User");
            roles.add(role);
            user.setRoles(roles);
            return UserDetailsServiceImpl.build(user);

        }
        return null;
    }

    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());


        return new UserDetailsImpl(
                new Random().nextLong(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authorities);
    }
}
