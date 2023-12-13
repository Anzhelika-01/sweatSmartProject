package bg.softuni.sweatsmartproject.services;

import bg.softuni.sweatsmartproject.service.CalorieCalculatorService;
import bg.softuni.sweatsmartproject.service.UserService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class CalorieCalculatorTest {

    private CalorieCalculatorService calorieCalculatorService = new CalorieCalculatorService();

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculateCaloriesForMale() {
        double result = calorieCalculatorService.calculateCalories("M", 70.0, 175.0, 30, "Moderately active");
        assertEquals(2628, result); // You might need to adjust the expected value based on your specific calculations.
    }

    @Test
    void testCalculateCaloriesForFemale() {
        double result = calorieCalculatorService.calculateCalories("F", 60.0, 160.0, 25, "Very active");
        assertEquals(2397, result); // Adjust the expected value based on your specific calculations.
    }
}