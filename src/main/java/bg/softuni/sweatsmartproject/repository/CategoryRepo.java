package bg.softuni.sweatsmartproject.repository;

import bg.softuni.sweatsmartproject.domain.entity.Category;
import bg.softuni.sweatsmartproject.domain.enums.CategoryEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepo extends JpaRepository<Category, UUID> {
    Category findCategoryByName(CategoryEnum name);
}