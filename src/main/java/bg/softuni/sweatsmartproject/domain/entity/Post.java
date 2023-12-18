package bg.softuni.sweatsmartproject.domain.entity;

import bg.softuni.sweatsmartproject.domain.dto.view.PostViewDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "posts")
public class Post extends BaseEntity {

    @Column
    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String text;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Like> likes = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "post_likes",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> likedByUsers = new HashSet<>();

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

    public Post setLikes(Set<Like> likes) {
        this.likes = likes;
        return this;
    }

    public Set<Like> getLikes() {
        return likes;
    }

    public int getLikeCount() {
        return likes.size();
    }

    public void addLike(User user) {
        if (!userLikedPost(user)) {
            Like like = new Like(user, this);
            likes.add(like);
            user.getLikedPosts().add(like);
        }
    }

    public void removeLike(User user) {
        if (userLikedPost(user)) {
            Like likeToRemove = likes.stream()
                    .filter(like -> like.getUser().equals(user))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Like not found for user"));

            likes.remove(likeToRemove);
            user.getLikedPosts().remove(likeToRemove);
        }
    }

    private boolean userLikedPost(User user) {
        return likes.stream().anyMatch(like -> like.getUser().equals(user));
    }

    public Set<User> getLikedByUsers() {
        return likedByUsers;
    }

    public void setLikedByUsers(Set<User> likedByUsers) {
        this.likedByUsers = likedByUsers;
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