package bg.softuni.sweatsmartproject.controller;

import bg.softuni.sweatsmartproject.service.PostService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testViewPostWithoutLoggedUser() throws Exception {

        final UUID postId = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.get("/post/{postId}", postId))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));
    }

    @Test
    public void testGetAllPosts() throws Exception {

        mockMvc.perform(get("/all-posts"))
                .andExpect(status().isOk())
                .andExpect(view().name("all-posts"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    public void testGetMakePostPageAuthenticatedUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/make-post"))
                .andExpect(status().isOk())
                .andExpect(view().name("make-post"));
    }

    @Test
    public void testGetMakePostPageUnauthenticatedUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/make-post"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }
}