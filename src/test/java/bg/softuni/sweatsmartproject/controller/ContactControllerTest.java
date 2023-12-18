package bg.softuni.sweatsmartproject.controller;

import bg.softuni.sweatsmartproject.service.MessageService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
public class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getContact_ShouldReturnContactView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/contact"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("contact"));
    }

    @Test
    public void postMessage_ShouldAddMessageAndReturnContactViewWithSuccessMessage() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/contact")
                        .param("name", "John Doe")
                        .param("email", "john@example.com")
                        .param("subject", "Test subject")
                        .param("message", "Test message")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("successMessage", "Your message has been sent successfully, and you will receive a response very soon."))
                .andReturn();

        Mockito.verify(messageService).addMessage(Mockito.any());
    }
}