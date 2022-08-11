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

import testing.community.automation.practice.app.db.model.UserEntity;
import testing.community.automation.practice.app.db.repository.IUserRepository;
import testing.community.automation.practice.app.domain.model.models.Role;
import testing.community.automation.practice.app.domain.model.models.User;
import testing.community.automation.practice.app.shared.services.IUserRoleService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IUserRoleService userRoleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserEntity> usersFound = userRepository.findByUsername(username);
        if (usersFound.isEmpty()) {
            return null;
        }
        UserEntity userFound = usersFound.get(0);
        User user = new User(userFound.getId(), userFound.getUsername(), userFound.getEmail(), userFound.getPassword(), null, null);
        List<Role> roles = userRoleService.getRolesByUserId(user.getId());
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
