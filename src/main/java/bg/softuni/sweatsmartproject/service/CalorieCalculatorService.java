package bg.softuni.sweatsmartproject.service;

import org.springframework.stereotype.Service;

@Service
public class CalorieCalculatorService {

    public double calculateCalories(String sex, double weight, double height, int age, String activityLevel) {

        double bmr;

        if (sex.equals("M")) {
            bmr = 88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age);
        } else {
            bmr = 447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age);
        }

        double pal;
        switch (activityLevel) {
            case "Lightly active":
                pal = 1.375;
                break;
            case "Moderately active":
                pal = 1.55;
                break;
            case "Very active":
                pal = 1.725;
                break;
            case "Extra active":
                pal = 1.9;
                break;
            default:
                pal = 1.2;
        }

        double caloriePerDay = bmr * pal;
        return Math.round(caloriePerDay);
    }
}