package bg.softuni.sweatsmartproject.domain.entity;

import bg.softuni.sweatsmartproject.domain.dto.view.CommentsViewDto;
import bg.softuni.sweatsmartproject.domain.dto.wrapper.CommentForm;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "comments")
public class Comment extends BaseEntity{

    @Column
    public LocalDate creationDate;

    @Column(columnDefinition = "LONGTEXT")
    public String text;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.DETACH})
    public User author;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.DETACH})
    public Post post;

    public Comment setText(String text) {
        this.text = text;
        return this;
    }

    public String getText() {
        return text;
    }

    public Comment setAuthor(User author) {
        this.author = author;
        return this;
    }

    public Comment setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public Comment setPost(Post post) {
        this.post = post;
        return this;
    }

    public User getAuthor() {
        return author;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public Post getPost() {
        return post;
    }

    @Override
    public UUID getId() {
        return super.getId();
    }

    public CommentsViewDto toDto(){
        return CommentsViewDto.builder()
                .id(this.getId())
                .author(this.author.getUsername())
                .text(this.text)
                .creationDate(this.creationDate)
                .build();
    }
}