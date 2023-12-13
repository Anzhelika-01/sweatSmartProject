package bg.softuni.sweatsmartproject.domain.entity;

import bg.softuni.sweatsmartproject.domain.dto.view.PostViewDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "posts")
public class Post extends BaseEntity{

    @Column
    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String text;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.DETACH})
    private Category category;

    @ManyToOne
    private User author;

    public String getText() {
        return text;
    }

    public Post setText(String text) {
        this.text = text;
        return this;
    }

    public Category getCategory() {
        return category;
    }

    public Post setCategory(Category category) {
        this.category = category;
        return this;
    }

    public User getAuthor() {
        return author;
    }

    public Post setAuthor(User author) {
        this.author = author;
        return this;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public String getTitle() {
        return title;
    }

    public Post setTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public UUID getId() {
        return super.getId();
    }

    public PostViewDto toDto() {
        return PostViewDto.builder()
                .id(this.getId())
                .title(this.title)
                .category(this.category.getName().toString())
                .text(this.text)
                .creationDate(this.creationDate)
                .author(this.author.getUsername())
                .build();
    }
}