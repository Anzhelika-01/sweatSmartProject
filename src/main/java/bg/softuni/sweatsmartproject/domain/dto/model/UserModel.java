package bg.softuni.sweatsmartproject.domain.dto.model;

import lombok.*;

import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserModel {

    private UUID id;

    private String username;

    private String password;

    private String email;

    private Set<UserRoleModel> role;
}