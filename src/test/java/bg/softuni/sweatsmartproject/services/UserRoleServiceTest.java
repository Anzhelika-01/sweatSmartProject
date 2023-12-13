package bg.softuni.sweatsmartproject.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import bg.softuni.sweatsmartproject.domain.dto.model.UserRoleModel;
import bg.softuni.sweatsmartproject.domain.dto.view.UserRoleViewDto;
import bg.softuni.sweatsmartproject.domain.entity.UserRole;
import bg.softuni.sweatsmartproject.domain.enums.RoleEnum;
import bg.softuni.sweatsmartproject.repository.UserRoleRepo;
import bg.softuni.sweatsmartproject.service.UserRoleService;
import bg.softuni.sweatsmartproject.service.UserService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UserRoleServiceTest {

    @Mock
    private UserRoleRepo userRoleRepo;

    @InjectMocks
    private UserRoleService userRoleService;

    @Mock
    private ModelMapper modelMapper;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        userRoleService = new UserRoleService(userRoleRepo, modelMapper);
    }

    @Test
    public void testIsDbInitWhenCountGreaterThanZero() {
        // Arrange
        when(userRoleRepo.count()).thenReturn(1L); // Set the desired count value

        // Act
        boolean result = userRoleService.isDbInit();

        // Assert
        assertTrue(result);
        verify(userRoleRepo, times(1)).count(); // Verify that the count method is called once
    }

    @Test
    public void testIsDbInitWhenCountIsZero() {
        // Arrange
        when(userRoleRepo.count()).thenReturn(0L); // Set the desired count value

        // Act
        boolean result = userRoleService.isDbInit();

        // Assert
        assertFalse(result);
        verify(userRoleRepo, times(1)).count(); // Verify that the count method is called once
    }

    @Test
    public void testDbInitWhenDatabaseNotInitialized() {
        // Arrange
        when(userRoleRepo.count()).thenReturn(0L); // Simulate database not initialized

        // Act
        userRoleService.dbInit();

        // Assert
        verify(userRoleRepo, times(1)).saveAllAndFlush(anyList()); // Verify that saveAllAndFlush is called
    }

    @Test
    public void testDbInitWhenDatabaseAlreadyInitialized() {
        // Arrange
        when(userRoleRepo.count()).thenReturn(2L); // Simulate database already initialized

        // Act
        userRoleService.dbInit();

        // Assert
        verify(userRoleRepo, never()).saveAllAndFlush(anyList()); // Verify that saveAllAndFlush is not called
    }


    @Test
    public void testGetAllUserRolesAfterInitialization() {
        // Arrange
        UserRole userRole1 = new UserRole().setRole(RoleEnum.USER);
        UserRole userRole2 = new UserRole().setRole(RoleEnum.ADMIN);

        when(userRoleRepo.findAll()).thenReturn(Arrays.asList(userRole1, userRole2));

        UserRoleViewDto dto1 = new UserRoleViewDto();
        UserRoleViewDto dto2 = new UserRoleViewDto();

        when(modelMapper.map(userRole1, UserRoleViewDto.class)).thenReturn(dto1);
        when(modelMapper.map(userRole2, UserRoleViewDto.class)).thenReturn(dto2);

        List<UserRoleViewDto> result = userRoleService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertSame(dto1, result.get(0));
        assertSame(dto2, result.get(1));
    }

    @Test
    public void testFindRoleByNameWithUser() {
        // Arrange
        String roleName = "USER";
        UserRole userRole = new UserRole().setRole(RoleEnum.USER);

        when(userRoleRepo.findByRole(RoleEnum.USER)).thenReturn(userRole);

        UserRoleModel expectedModel = new UserRoleModel(); // Assuming you have a proper UserRoleModel setup
        when(modelMapper.map(userRole, UserRoleModel.class)).thenReturn(expectedModel);

        // Act
        UserRoleModel result = userRoleService.findRoleByName(roleName);

        // Assert
        assertNotNull(result);
        assertSame(expectedModel, result); // Verify that the expected UserRoleModel is returned
    }

    @Test
    public void testFindRoleByNameWithAdmin() {
        // Arrange
        String roleName = "ADMIN";
        UserRole adminRole = new UserRole().setRole(RoleEnum.ADMIN);

        when(userRoleRepo.findByRole(RoleEnum.ADMIN)).thenReturn(adminRole);

        UserRoleModel expectedModel = new UserRoleModel(); // Assuming you have a proper UserRoleModel setup
        when(modelMapper.map(adminRole, UserRoleModel.class)).thenReturn(expectedModel);

        // Act
        UserRoleModel result = userRoleService.findRoleByName(roleName);

        // Assert
        assertNotNull(result);
        assertSame(expectedModel, result); // Verify that the expected UserRoleModel is returned
    }

    @Test
    public void testSetToUser() {
        // Arrange
        UserRole userRole = new UserRole().setRole(RoleEnum.USER);
        when(userRoleRepo.findByRole(RoleEnum.USER)).thenReturn(userRole);

        UserRoleModel expectedModel = new UserRoleModel(); // Assuming you have a proper UserRoleModel setup
        when(modelMapper.map(userRole, UserRoleModel.class)).thenReturn(expectedModel);

        // Act
        UserRoleModel result = userRoleService.setToUser();

        // Assert
        assertNotNull(result);
        assertSame(expectedModel, result); // Verify that the expected UserRoleModel is returned
    }
}