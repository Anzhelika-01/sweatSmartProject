package bg.softuni.sweatsmartproject.domain.dto.wrapper;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentForm {
    private UUID id;

    @NotNull
    @Size(max = 5000)
    private String text;

    public UUID getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public CommentForm setText(String text) {
        this.text = text;
        return this;
    }
}
