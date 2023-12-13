package bg.softuni.sweatsmartproject.domain.dto.view;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentsViewDto {

    private UUID id;

    public LocalDate creationDate;

    public String text;

    public String author;

    public UUID getId() {
        return id;
    }
}