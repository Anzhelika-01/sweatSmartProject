package bg.softuni.sweatsmartproject.services;

import bg.softuni.sweatsmartproject.domain.entity.User;
import bg.softuni.sweatsmartproject.domain.entity.UserRole;
import bg.softuni.sweatsmartproject.domain.enums.RoleEnum;
import bg.softuni.sweatsmartproject.repository.UserRepo;
import bg.softuni.sweatsmartproject.repository.UserRoleRepo;
import bg.softuni.sweatsmartproject.service.DataInitializerService;
import bg.softuni.sweatsmartproject.service.UserRoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataInitializerServiceTest {

    @Mock
    private DataInitializerService dataInitializerService;

    @Mock
    private UserRoleService userRoleService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRoleRepo userRoleRepo;

    @Mock
    private UserRepo userRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dataInitializerService = new DataInitializerService(userRoleService, passwordEncoder, userRoleRepo, userRepo);
    }

    @Test
    void testInitAdmin() {
        // Arrange
        String adminEmail = "kotevaanzhelika@gmail.com";
        String adminUsername = "akoteva";
        String adminPassword = "1234";

        when(userRoleRepo.findAll()).thenReturn(Collections.singletonList(new UserRole().setRole(RoleEnum.ADMIN)));
        when(passwordEncoder.encode(adminPassword)).thenReturn("encodedPassword");

        // Act
        dataInitializerService.initAdmin();

        // Assert
        verify(userRoleRepo, times(1)).findAll();
        verify(userRepo, times(1)).saveAndFlush(any(User.class));

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepo).saveAndFlush(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals(adminEmail, savedUser.getEmail());
        assertEquals(adminUsername, savedUser.getUsername());
        assertEquals("encodedPassword", savedUser.getPassword());

        assertTrue(savedUser.getRole().stream().anyMatch(role -> role.getRole().equals(RoleEnum.ADMIN)));
    }

    @Test
    void testDbInit() {
        List<UserRole> userRoles = Arrays.asList(
                new UserRole().setRole(RoleEnum.USER),
                new UserRole().setRole(RoleEnum.ADMIN)
        );
        when(userRoleRepo.findAll()).thenReturn(userRoles);

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        when(userRepo.count()).thenReturn(0L);

        // Act
        dataInitializerService.dbInit();

        // Assert
        verify(userRoleService, times(1)).dbInit();
        verify(userRepo, times(1)).count();
        verify(userRepo, times(1)).saveAndFlush(any(User.class));

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepo).saveAndFlush(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertTrue(savedUser.getRole().stream().anyMatch(role -> role.getRole().equals(RoleEnum.USER)));
        assertTrue(savedUser.getRole().stream().anyMatch(role -> role.getRole().equals(RoleEnum.ADMIN)));
    }
}