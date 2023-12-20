package bg.softuni.sweatsmartproject.service;

import bg.softuni.sweatsmartproject.domain.dto.model.UserModel;
import bg.softuni.sweatsmartproject.domain.dto.model.UserRoleModel;
import bg.softuni.sweatsmartproject.domain.dto.view.UserInfoWithoutPassDto;
import bg.softuni.sweatsmartproject.domain.dto.wrapper.UserRegisterForm;
import bg.softuni.sweatsmartproject.domain.entity.User;
import bg.softuni.sweatsmartproject.repository.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final UserRoleService userRoleService;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    @Autowired
    public UserService(UserRepo userRepo, ModelMapper modelMapper, UserRoleService userRoleService, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
        this.userRoleService = userRoleService;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    public void registerUser(UserRegisterForm registerForm, Consumer<Authentication> successfulLoginProcessor) {

        final UserModel userModel = this.mapUser(registerForm);
        final User user = this.modelMapper.map(userModel, User.class);
        this.userRepo.saveAndFlush(user);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(registerForm.getUsername());

        final Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );

        successfulLoginProcessor.accept(authentication);
    }

    public boolean changeUsername(String username, UserModel userModel) {
        if (this.userRepo.findUserByUsername(username).isPresent() || username.trim().isEmpty() || username.length() < 3) {
            return false;
        }

        final User userToChange = this.modelMapper.map(userModel, User.class);
        userToChange.setUsername(username);
        this.userRepo.saveAndFlush(userToChange);

        UserDetails updatedUserDetails = this.userDetailsService.loadUserByUsername(username);
        Authentication newAuthentication = new UsernamePasswordAuthenticationToken(
                updatedUserDetails, updatedUserDetails.getPassword(), updatedUserDetails.getAuthorities());

        SecurityContextHolder.clearContext();
        SecurityContextHolder.getContext().setAuthentication(newAuthentication);

        return true;
    }

    public void changeRoles(UserModel userModel, String role, boolean addRole) {
        final User userToChange;
        final Set<UserRoleModel> userRoles = userModel.getRole();

        if (addRole) {
            if (userRoles.stream().noneMatch(r -> role.equalsIgnoreCase(r.getRole()))) {
                final UserRoleModel newRole = this.userRoleService.findRoleByName(role);
                userRoles.add(newRole);
                userModel.setRole(userRoles);
                userToChange = this.modelMapper.map(userModel, User.class);
                this.userRepo.saveAndFlush(userToChange);
            }
        } else {
            userRoles.removeIf(r -> role.equalsIgnoreCase(r.getRole()));
            userModel.setRole(userRoles);
            userToChange = this.modelMapper.map(userModel, User.class);
            this.userRepo.saveAndFlush(userToChange);
        }
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public Authentication getAuthenticationToken(String username) {
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );
    }

    private UserModel mapUser(UserRegisterForm userRegister) {
        final UserRoleModel roleModel = this.userRoleService.setToUser();

        return UserModel.builder()
                .username(userRegister.getUsername())
                .email(userRegister.getEmail())
                .password(this.passwordEncoder.encode(userRegister.getPassword()))
                .role(Set.of(roleModel))
                .build();
    }

    public UserModel findById(UUID id) {
        final User user = this.userRepo.findById(id).orElseThrow(NoSuchElementException::new);
        return this.modelMapper.map(user, UserModel.class);
    }

    public List<UserInfoWithoutPassDto> getAllUsersWithInfo() {
        final List<User> users = userRepo.findAll();

        return users.stream().map(User::toDto).collect(Collectors.toList());
    }
}