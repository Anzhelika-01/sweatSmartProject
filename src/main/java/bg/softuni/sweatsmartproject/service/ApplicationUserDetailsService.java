package bg.softuni.sweatsmartproject.service;

import java.util.List;

import bg.softuni.sweatsmartproject.domain.dto.model.AppUserDetails;
import bg.softuni.sweatsmartproject.domain.entity.User;
import bg.softuni.sweatsmartproject.domain.entity.UserRole;
import bg.softuni.sweatsmartproject.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

public class ApplicationUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    @Autowired
    public ApplicationUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return
                userRepo.
                        findUserByUsername(username).
                        map(this::map).
                        orElseThrow(() -> new UsernameNotFoundException("User with name " + username + " not found!"));
    }

    private UserDetails map(User user) {
        return new AppUserDetails(
                user.getUsername(),
                user.getPassword(),
                extractAuthorities(user)
        ).
                setEmail(user.getEmail());
    }

    private List<GrantedAuthority> extractAuthorities(User user) {
        return user.
                getRole().
                stream().
                map(this::mapRole).
                toList();
    }

    private GrantedAuthority mapRole(UserRole role) {
        return new SimpleGrantedAuthority("ROLE_" + role.getRole().name());
    }
}