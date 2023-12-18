package bg.softuni.sweatsmartproject.domain.dto.model;

import bg.softuni.sweatsmartproject.domain.enums.CategoryEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryModel {

    private UUID id;

    private CategoryEnum name;
}