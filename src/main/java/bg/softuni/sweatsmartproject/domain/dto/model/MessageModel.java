package bg.softuni.sweatsmartproject.domain.dto.model;

import lombok.*;

import java.util.UUID;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageModel {

    private UUID id;

    private String name;

    private String email;

    private String subject;

    private String message;

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }
}