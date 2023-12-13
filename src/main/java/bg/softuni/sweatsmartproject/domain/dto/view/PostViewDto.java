package bg.softuni.sweatsmartproject.domain.dto.view;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostViewDto {

    private UUID id;

    private String title;

    private String category;

    private String text;

    private LocalDate creationDate;

    private String author;

    public UUID getId() {
        return id;
    }
}