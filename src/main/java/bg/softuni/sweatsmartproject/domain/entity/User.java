package bg.softuni.sweatsmartproject.domain.entity;

import bg.softuni.sweatsmartproject.domain.dto.view.UserInfoWithoutPassDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @NotEmpty(message = "Name could not be empty or null.")
    @Size(min = 6, max = 100, message = "Name must contains min 36 or max 100 characters.")
    @Column
    private String username;

    @Column
    @NotEmpty(message = "Password could not be empty or null.")
    private String password;

    @Column
    @NotEmpty(message = "Email could not be empty or null.")
    private String email;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Post> posts = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.DETACH})
    private List<UserRole> role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Like> likedPosts = new HashSet<>();

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public User setRole(List<UserRole> role) {
        this.role = role;
        return this;
    }

    public User setPosts(List<Post> posts) {
        this.posts = posts;
        return this;
    }

    public User setLikedPosts(Set<Like> likedPosts) {
        this.likedPosts = likedPosts;
        return this;
    }

    @Override
    public UUID getId() {
        return super.getId();
    }

    public UserInfoWithoutPassDto toDto() {
        return UserInfoWithoutPassDto.builder()
                .id(this.getId())
                .username(this.username)
                .email(this.email)
                .posts(this.posts)
                .role(this.role)
                .build();
    }
}