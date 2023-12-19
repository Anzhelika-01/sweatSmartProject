package bg.softuni.sweatsmartproject.controller;

import bg.softuni.sweatsmartproject.domain.dto.wrapper.UserRegisterForm;
import bg.softuni.sweatsmartproject.web.UserController;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Mock
    private BindingResult bindingResult;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private UserController userController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetRegister() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/register"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("register"));
    }

    @Test
    void testPostRegisterWithNoValidationErrors() throws Exception {
        UserRegisterForm registerForm = new UserRegisterForm();
        when(bindingResult.hasErrors()).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .flashAttr("registerForm", registerForm)
                        .param("username", "anzhelika")
                        .param("email", "test@abv.bg")
                        .param("password", "12345")
                        .param("confirmPassword", "12345"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("login"));
    }

    @Test
    void testPostRegisterWithValidationErrors() throws Exception {
        UserRegisterForm registerForm = new UserRegisterForm();
        when(bindingResult.hasErrors()).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .flashAttr("registerForm", registerForm)
                        .param("username", "anz")
                        .param("email", "test@abv.bg")
                        .param("password", "12345")
                        .param("confirmPassword", "12345"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("register"));
    }

    @Test
    void testGetLogin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("login"));
    }

    @Test
    void testOnFailedLogin() {
        String username = "testUser";
        ModelAndView modelAndView = userController.onFailedLogin(username, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY, username);
        verify(redirectAttributes).addFlashAttribute("bad_credentials", true);
        assertEquals("redirect:login", modelAndView.getViewName());
    }
}