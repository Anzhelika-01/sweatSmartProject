package bg.softuni.sweatsmartproject.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "messages")
public class Message extends BaseEntity {

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String subject;

    @Column
    private String message;
}
