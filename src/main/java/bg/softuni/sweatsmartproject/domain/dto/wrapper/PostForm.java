package bg.softuni.sweatsmartproject.domain.dto.wrapper;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostForm {

    private UUID id;

    @NotNull
    @Size(min = 3, max = 50)
    private String title;
    private String category;

    @NotNull
    @Size(min = 10, max = 5000)
    private String text;

    public String getCategory() {
        return category;
    }

    public PostForm setCategory(String category) {
        this.category = category;
        return this;
    }

    public UUID getId() {
        return id;
    }

    public PostForm setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getText() {
        return text;
    }

    public PostForm setText(String text) {
        this.text = text;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public PostForm setTitle(String title) {
        this.title = title;
        return this;
    }
}