package bg.softuni.sweatsmartproject.controller;

import bg.softuni.sweatsmartproject.service.CalorieCalculatorService;
import bg.softuni.sweatsmartproject.web.CalorieCalculatorController;
import bg.softuni.sweatsmartproject.repository.UserRepo;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CalorieCalculatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CalorieCalculatorService calorieCalculatorService;

    @InjectMocks
    private CalorieCalculatorController calorieCalculatorController;

    @MockBean
    private UserRepo userRepo;  // Mocking UserRepo

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getCalculator_ShouldReturnCalorieCalculatorView() throws Exception {
        mockMvc.perform(get("/calorie-calculator"))
                .andExpect(status().isOk())
                .andExpect(view().name("calorie-calculator"));
    }

    @Test
    public void calculateCalories_ShouldReturnCalculatedCalories() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/calorie-calculator")
                        .with(csrf())
                        .param("sex", "M")
                        .param("weight", "70")
                        .param("height", "175")
                        .param("age", "25")
                        .param("activityLevel", "Sedentary"))  // Include CSRF token
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("caloriesPerDay"))
                .andReturn();
    }
}