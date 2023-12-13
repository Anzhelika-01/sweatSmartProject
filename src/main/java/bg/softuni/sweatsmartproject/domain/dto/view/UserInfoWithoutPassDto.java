package bg.softuni.sweatsmartproject.domain.dto.view;

import bg.softuni.sweatsmartproject.domain.entity.Post;
import bg.softuni.sweatsmartproject.domain.entity.UserRole;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoWithoutPassDto {

    private UUID id;

    private String username;

    private String email;

    private List<Post> posts;

    private List<UserRole> role;

    public List<Post> getPosts() {
        return posts;
    }

    public UUID getId() {
        return id;
    }
}