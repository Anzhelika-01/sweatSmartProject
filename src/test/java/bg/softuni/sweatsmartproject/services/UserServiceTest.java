package bg.softuni.sweatsmartproject.services;

import bg.softuni.sweatsmartproject.domain.dto.model.AppUserDetails;
import bg.softuni.sweatsmartproject.domain.dto.model.UserModel;
import bg.softuni.sweatsmartproject.domain.dto.model.UserRoleModel;
import bg.softuni.sweatsmartproject.domain.dto.wrapper.UserRegisterForm;
import bg.softuni.sweatsmartproject.domain.entity.User;
import bg.softuni.sweatsmartproject.domain.entity.UserRole;
import bg.softuni.sweatsmartproject.domain.enums.RoleEnum;
import bg.softuni.sweatsmartproject.repository.UserRepo;
import bg.softuni.sweatsmartproject.repository.UserRoleRepo;
import bg.softuni.sweatsmartproject.service.ApplicationUserDetailsService;
import bg.softuni.sweatsmartproject.service.DataInitializerService;
import bg.softuni.sweatsmartproject.service.UserRoleService;
import bg.softuni.sweatsmartproject.service.UserService;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UserRoleService userRoleService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private UserDetails userDetails;
    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @InjectMocks
    private UserService userService;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepo, modelMapper, userRoleService, passwordEncoder, userDetailsService);
    }

    @Test
    public void testRegister() {
        final UserRegisterForm registerForm = new UserRegisterForm();
        registerForm.setUsername("testUser");
        registerForm.setEmail("test@example.com");
        registerForm.setPassword("testPassword");

        final UserRoleModel userRoleModel = new UserRoleModel();
        userRoleModel.setId(UUID.randomUUID());
        userRoleModel.setRole("USER");

        final User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        user.setPassword("testPassword");
        final UserRoleModel roleModel = new UserRoleModel();
        roleModel.setId(UUID.randomUUID());
        roleModel.setRole(RoleEnum.USER.name());

        when(userRoleService.setToUser()).thenReturn(roleModel);
        when(modelMapper.map(any(UserModel.class),eq(User.class))).thenReturn(user);
        when(passwordEncoder.encode(any(String.class))).thenReturn("testPassword");

        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(
                new org.springframework.security.core.userdetails.User("testUser", "encodedPassword", Collections.emptyList())
        );

        userService.registerUser(registerForm, authentication -> {});

        verify(userRoleService, times(1)).setToUser();
        verify(userDetailsService, times(1)).loadUserByUsername(registerForm.getUsername());
        verify(userRepo, times(1)).saveAndFlush(userArgumentCaptor.capture());

        final User savedUser =  userArgumentCaptor.getValue();
        assertEquals(registerForm.getUsername(), savedUser.getUsername());
        assertEquals(registerForm.getEmail(), savedUser.getEmail());
        assertEquals(registerForm.getPassword(), savedUser.getPassword());
    }

    @Test
    public void testChangeUsername() {
        final String username = "newUsername";
        final UserModel userModel = new UserModel();
        final User user = new User();
        Authentication authentication = mock(Authentication.class);
        when(userRepo.findUserByUsername(username)).thenReturn(Optional.empty());
        when(userDetailsService.loadUserByUsername("newUsername")).thenReturn(userDetails);
        when(userDetails.getPassword()).thenReturn("password");
        when(userDetails.getAuthorities()).thenReturn(Collections.emptyList());
        when(modelMapper.map(userModel, User.class)).thenReturn(user);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        boolean result = userService.changeUsername(username, userModel);

        Assertions.assertTrue(result);
        verify(userRepo).findUserByUsername(username);
        verify(userRepo).saveAndFlush(userArgumentCaptor.capture());
        assertEquals(username, userArgumentCaptor.getValue().getUsername());
        verify(modelMapper).map(userModel, User.class);
        verify(userDetailsService).loadUserByUsername(username);
        verify(userDetails).getAuthorities();
        verify(userDetails).getPassword();
    }

    @Test
    void testAddAdminRole() {
        final User user = new User();
        final UserRoleModel newRole = new UserRoleModel();
        newRole.setRole(RoleEnum.USER.name());

        final Set<UserRoleModel> userRoles = new HashSet<>();
        userRoles.add(newRole);

        final UserModel userModel = new UserModel();
        userModel.setRole(userRoles);

        when(userRepo.saveAndFlush(any())).thenReturn(user);
        when(userRoleService.findRoleByName(any())).thenReturn(newRole);

        // Act
        userService.changeRoles(userModel, RoleEnum.ADMIN.name(), true);

// Print or log a message to confirm that the method was called
        System.out.println("changeRoles method called");

// Assert
        verify(userRepo, times(1)).saveAndFlush(any());
    }

    @Test
    void testRemoveAdminRole() {

        final User user = new User();
        final UserRoleModel existingRole = new UserRoleModel();
        existingRole.setRole(RoleEnum.ADMIN.name());

        final Set<UserRoleModel> roles = new HashSet<>();
        roles.add(existingRole);

        final UserModel userModel = new UserModel();
        userModel.setRole(roles);

        when(userRepo.saveAndFlush(any())).thenReturn(user);

        userService.changeRoles(userModel, RoleEnum.ADMIN.name(), false);

        verify(userRepo, times(1)).saveAndFlush(any());

    }

    @Test
    public void testGetAllUsers() {
        // Arrange
        final User user1 = new User().setUsername("user1").setEmail("user1@example.com").setPassword("password1");
        final User user2 = new User().setUsername("user2").setEmail("user2@example.com").setPassword("password2");
        final List<User> userList = Arrays.asList(user1, user2);

        when(userRepo.findAll()).thenReturn(userList);

        final List<User> result = userService.getAllUsers();

        assertEquals(userList.size(), result.size());
        assertEquals(user1.getUsername(), result.get(0).getUsername());
        assertEquals(user1.getEmail(), result.get(0).getEmail());
        assertEquals(user1.getPassword(), result.get(0).getPassword());

        assertEquals(user2.getUsername(), result.get(1).getUsername());
        assertEquals(user2.getEmail(), result.get(1).getEmail());
        assertEquals(user2.getPassword(), result.get(1).getPassword());
    }

    @Test
    public void testGetAuthenticationToken() {
        final String username = "testUser";
        final String password = "testPassword";

        final UserRole userRole = UserRole.builder()
                .role(RoleEnum.USER)
                .build();

        final User user = new User().setUsername(username).setPassword(password).setRole(Collections.singletonList(userRole));

        Collection<? extends GrantedAuthority> authorities = user.getRole().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole().name()))
                .collect(Collectors.toList());

        final UserDetails userDetails = new AppUserDetails(username, password, authorities);

        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        final Authentication result = userService.getAuthenticationToken(username);

        assertEquals(username, result.getName());
        assertEquals(password, result.getCredentials());
        assertEquals(authorities, result.getAuthorities());
        assertEquals(UsernamePasswordAuthenticationToken.class, result.getClass());
    }

    @Test
    void testFindById() {
        // Arrange
        final UUID userId = UUID.randomUUID();
        final User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setUsername("testUser");

        final UserModel expectedUserModel = new UserModel();
        expectedUserModel.setId(userId);
        expectedUserModel.setUsername("testUser");

        when(userRepo.findById(userId)).thenReturn(Optional.of(mockUser));
        when(modelMapper.map(mockUser, UserModel.class)).thenReturn(expectedUserModel);

        final UserModel resultUserModel = userService.findById(userId);

        // Assert
        assertEquals(expectedUserModel.getId(), resultUserModel.getId());
        assertEquals(expectedUserModel.getUsername(), resultUserModel.getUsername());
    }
}