package bg.softuni.sweatsmartproject.domain.dto.model;

import bg.softuni.sweatsmartproject.domain.entity.Post;
import bg.softuni.sweatsmartproject.domain.entity.User;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentModel {
    private UUID id;

    public LocalDate creationDate;

    public String text;

    public User author;

    public Post post;
}