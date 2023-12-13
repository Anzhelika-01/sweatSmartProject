package bg.softuni.sweatsmartproject.domain.dto.wrapper;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jdk.jfr.Name;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CalorieCalculatorForm {

    @NotNull
    public String sex;

    @Min(10)
    @Max(300)
    public double weight;

    @Min(20)
    @Max(250)
    public double height;

    @Min(7)
    @Max(100)
    public int age;

    @NotNull
    public String activityLevel;
}