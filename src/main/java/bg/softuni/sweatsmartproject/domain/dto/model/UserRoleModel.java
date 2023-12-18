package bg.softuni.sweatsmartproject.domain.dto.model;

import lombok.*;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRoleModel {
    private UUID id;

    private String role;
}