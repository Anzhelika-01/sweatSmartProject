package bg.softuni.sweatsmartproject.controller;

import bg.softuni.sweatsmartproject.domain.dto.model.UserModel;
import bg.softuni.sweatsmartproject.service.UserService;
import bg.softuni.sweatsmartproject.web.AdminsController;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminsControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @InjectMocks
    private AdminsController adminsController;

    private MockMvc mockMvc;

    @Test
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminsController).build();
    }

    @Test
    void getAdmins_ShouldReturnAdminsView() {

        when(userService.getAllUsersWithInfo()).thenReturn(Collections.emptyList());

        final ModelAndView modelAndView = adminsController.getAdmins(model);

        assertEquals("admins", modelAndView.getViewName());
    }

    @Test
    public void testPostChangeUsername() throws Exception {
        setup();

        final UUID userId = UUID.randomUUID();
        final String newUsername = "newUsername";

        when(userService.findById(userId)).thenReturn(createDummyUserModel());
        when(userService.changeUsername(newUsername, createDummyUserModel())).thenReturn(true);

        mockMvc.perform(post("/admins/change-username")
                        .param("userId", userId.toString())
                        .param("username", newUsername))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admins"));
    }

    @Test
    public void testAddRole() throws Exception {
        setup();

        UUID userId = UUID.randomUUID();
        String role = "ROLE_ADMIN";

        UserModel dummyUserModel = createDummyUserModel();

        when(userService.findById(userId)).thenReturn(dummyUserModel);

        mockMvc.perform(post("/admins/add-role")
                        .param("userId", userId.toString())
                        .param("role", role))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admins"));

        verify(userService).changeRoles(dummyUserModel, role, true);
    }

    @Test
    public void testDeleteRole() throws Exception {
        setup();

        final UUID userId = UUID.randomUUID();
        final String role = "ROLE_USER";

        final UserModel dummyUserModel = createDummyUserModel();

        when(userService.findById(userId)).thenReturn(dummyUserModel);

        mockMvc.perform(post("/admins/delete-role")
                        .param("userId", userId.toString())
                        .param("role", role))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admins"));

        verify(userService).changeRoles(dummyUserModel, role, false);
    }

    private UserModel createDummyUserModel() {
        final UserModel dummyUser = new UserModel();
        dummyUser.setId(UUID.randomUUID());
        dummyUser.setUsername("dummyUsername");
        return dummyUser;
    }
}
