package bg.softuni.sweatsmartproject.services;

import bg.softuni.sweatsmartproject.domain.dto.model.CategoryModel;
import bg.softuni.sweatsmartproject.domain.dto.view.CategoryViewDto;
import bg.softuni.sweatsmartproject.domain.entity.Category;
import bg.softuni.sweatsmartproject.domain.enums.CategoryEnum;
import bg.softuni.sweatsmartproject.repository.CategoryRepo;
import bg.softuni.sweatsmartproject.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepo categoryRepo;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        categoryService = new CategoryService(categoryRepo, modelMapper);
    }

    @Test
    public void testIsDbInitWhenEmpty() {
        when(categoryRepo.count()).thenReturn(0L);
        boolean result = categoryService.isDbInit();

        assertFalse(result);
    }

    @Test
    public void testIsDbInitWhenNotEmpty() {

        when(categoryRepo.count()).thenReturn(3L);

        boolean result = categoryService.isDbInit();

        assertTrue(result);
    }

    @Test
    public void testDbInitWhenNotInitialized() {
        when(categoryRepo.count()).thenReturn(0L);
        when(categoryRepo.saveAllAndFlush(anyList())).thenReturn(null);

        categoryService.dbInit();

        verify(categoryRepo, times(1)).saveAllAndFlush(anyList());
    }

    @Test
    public void testDbInitWhenAlreadyInitialized() {
        when(categoryRepo.count()).thenReturn(3L);

        categoryService.dbInit();

        verify(categoryRepo, never()).saveAllAndFlush(anyList());
    }

    @Test
    public void testGetAllCategories() {
        when(categoryRepo.findAll()).thenReturn(Arrays.asList(
                createCategory(CategoryEnum.HOME_WORKOUTS),
                createCategory(CategoryEnum.FITNESS_WORKOUTS)
        ));

        when(modelMapper.map(any(Category.class), eq(CategoryViewDto.class)))
                .thenReturn(new CategoryViewDto(), new CategoryViewDto());

        List<CategoryViewDto> result = categoryService.getAll();

        assertEquals(2, result.size());
    }

    @Test
    public void testGetCategoryByName() {
        Category category = createCategory(CategoryEnum.HOME_WORKOUTS);

        when(categoryRepo.findCategoryByName(CategoryEnum.HOME_WORKOUTS)).thenReturn(category);

        CategoryModel categoryModel = CategoryModel.builder()
                .id(UUID.randomUUID())
                .name(CategoryEnum.HOME_WORKOUTS)
                .build();

        when(modelMapper.map(category, CategoryModel.class)).thenReturn(categoryModel);

        CategoryModel result = categoryService.getCategory("HOME_WORKOUTS");

        assertEquals(CategoryEnum.HOME_WORKOUTS, result.getName());
    }

    private Category createCategory(CategoryEnum categoryEnum) {
        return new Category().setName(categoryEnum);
    }
}