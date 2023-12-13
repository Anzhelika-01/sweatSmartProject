package bg.softuni.sweatsmartproject.service;

import bg.softuni.sweatsmartproject.domain.dto.model.CategoryModel;
import bg.softuni.sweatsmartproject.domain.dto.view.CategoryViewDto;
import bg.softuni.sweatsmartproject.domain.entity.Category;
import bg.softuni.sweatsmartproject.domain.enums.CategoryEnum;
import bg.softuni.sweatsmartproject.repository.CategoryRepo;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepo categoryRepo;
    private final ModelMapper modelMapper;


    @Autowired
    public CategoryService(CategoryRepo categoryRepo, ModelMapper modelMapper) {
        this.categoryRepo = categoryRepo;
        this.modelMapper = modelMapper;
    }

    public boolean isDbInit() {
        return categoryRepo.count() > 0;
    }

    @PostConstruct
    public void dbInit() {
        if (!isDbInit()) {
            List<Category> categories = new ArrayList<>();
            categories.add(new Category().setName(CategoryEnum.HOME_WORKOUTS));
            categories.add(new Category().setName(CategoryEnum.FITNESS_WORKOUTS));
            categories.add(new Category().setName(CategoryEnum.DIETS_AND_NUTRITIOUS));
            categories.add(new Category().setName(CategoryEnum.WEIGHT_MANAGEMENT));
            categories.add(new Category().setName(CategoryEnum.MENTAL_HEALTH));
            categories.add(new Category().setName(CategoryEnum.HACKS));
            this.categoryRepo.saveAllAndFlush(categories);
        }
    }

    public List<CategoryViewDto> getAll() {
        return this.categoryRepo.findAll()
                .stream()
                .map(c -> this.modelMapper.map(c, CategoryViewDto.class))
                .collect(Collectors.toList());
    }

    public CategoryModel getCategory(String categoryName) {
        final Category category = this.categoryRepo.findCategoryByName(CategoryEnum.valueOf(categoryName));
        return this.modelMapper.map(category, CategoryModel.class);
    }
}