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
        when(userRoleRepo.count()).thenReturn(1L);

        boolean result = userRoleService.isDbInit();

        assertTrue(result);
        verify(userRoleRepo, times(1)).count();
    }

    @Test
    public void testIsDbInitWhenCountIsZero() {
        when(userRoleRepo.count()).thenReturn(0L);

        boolean result = userRoleService.isDbInit();

        assertFalse(result);
        verify(userRoleRepo, times(1)).count();
    }

    @Test
    public void testDbInitWhenDatabaseNotInitialized() {
        when(userRoleRepo.count()).thenReturn(0L);

        userRoleService.dbInit();

        verify(userRoleRepo, times(1)).saveAllAndFlush(anyList());
    }

    @Test
    public void testDbInitWhenDatabaseAlreadyInitialized() {
        when(userRoleRepo.count()).thenReturn(2L);

        userRoleService.dbInit();

        verify(userRoleRepo, never()).saveAllAndFlush(anyList());
    }


    @Test
    public void testGetAllUserRolesAfterInitialization() {
        final UserRole userRole1 = new UserRole().setRole(RoleEnum.USER);
        final UserRole userRole2 = new UserRole().setRole(RoleEnum.ADMIN);

        when(userRoleRepo.findAll()).thenReturn(Arrays.asList(userRole1, userRole2));

        final UserRoleViewDto dto1 = new UserRoleViewDto();
        final UserRoleViewDto dto2 = new UserRoleViewDto();

        when(modelMapper.map(userRole1, UserRoleViewDto.class)).thenReturn(dto1);
        when(modelMapper.map(userRole2, UserRoleViewDto.class)).thenReturn(dto2);

        final List<UserRoleViewDto> result = userRoleService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertSame(dto1, result.get(0));
        assertSame(dto2, result.get(1));
    }

    @Test
    public void testFindRoleByNameWithUser() {
        final String roleName = "USER";
        final UserRole userRole = new UserRole().setRole(RoleEnum.USER);

        when(userRoleRepo.findByRole(RoleEnum.USER)).thenReturn(userRole);

        final UserRoleModel expectedModel = new UserRoleModel();
        when(modelMapper.map(userRole, UserRoleModel.class)).thenReturn(expectedModel);

        final UserRoleModel result = userRoleService.findRoleByName(roleName);

        assertNotNull(result);
        assertSame(expectedModel, result);
    }

    @Test
    public void testFindRoleByNameWithAdmin() {
        final String roleName = "ADMIN";
        final UserRole adminRole = new UserRole().setRole(RoleEnum.ADMIN);

        when(userRoleRepo.findByRole(RoleEnum.ADMIN)).thenReturn(adminRole);

        final UserRoleModel expectedModel = new UserRoleModel();
        when(modelMapper.map(adminRole, UserRoleModel.class)).thenReturn(expectedModel);

        final UserRoleModel result = userRoleService.findRoleByName(roleName);

        assertNotNull(result);
        assertSame(expectedModel, result);
    }

    @Test
    public void testSetToUser() {
        final UserRole userRole = new UserRole().setRole(RoleEnum.USER);
        when(userRoleRepo.findByRole(RoleEnum.USER)).thenReturn(userRole);

        final UserRoleModel expectedModel = new UserRoleModel();
        when(modelMapper.map(userRole, UserRoleModel.class)).thenReturn(expectedModel);

        final UserRoleModel result = userRoleService.setToUser();

        assertNotNull(result);
        assertSame(expectedModel, result);
    }
}