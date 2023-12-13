package bg.softuni.sweatsmartproject.domain.dto.model;

import bg.softuni.sweatsmartproject.domain.entity.User;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostModel {

    private UUID id;

    private String title;

    private CategoryModel category;

    private String text;

    private LocalDate creationDate;

    private User author;
}
