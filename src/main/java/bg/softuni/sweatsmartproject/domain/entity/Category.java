package bg.softuni.sweatsmartproject.domain.entity;

import bg.softuni.sweatsmartproject.domain.enums.CategoryEnum;
import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class Category extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoryEnum name;

    public Category setName(CategoryEnum name) {
        this.name = name;
        return this;
    }

    public CategoryEnum getName() {
        return name;
    }
}