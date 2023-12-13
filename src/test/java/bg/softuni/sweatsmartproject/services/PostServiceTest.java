package bg.softuni.sweatsmartproject.services;

import bg.softuni.sweatsmartproject.domain.dto.model.CategoryModel;
import bg.softuni.sweatsmartproject.domain.dto.view.PostViewDto;
import bg.softuni.sweatsmartproject.domain.dto.wrapper.PostForm;
import bg.softuni.sweatsmartproject.domain.entity.Category;
import bg.softuni.sweatsmartproject.domain.entity.Post;
import bg.softuni.sweatsmartproject.domain.entity.User;
import bg.softuni.sweatsmartproject.domain.enums.CategoryEnum;
import bg.softuni.sweatsmartproject.repository.PostRepo;
import bg.softuni.sweatsmartproject.repository.UserRepo;

import bg.softuni.sweatsmartproject.service.CategoryService;
import bg.softuni.sweatsmartproject.service.PostService;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostRepo postRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private CategoryService categoryService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PostService postService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        postService = new PostService(postRepo, modelMapper, userRepo, categoryService);
    }

    @Test
    void testMakePost() {
        // Arrange
        UUID postId = UUID.randomUUID();

        PostForm postForm = new PostForm()
                .setId(postId)
                .setTitle("Test Title")
                .setCategory("FITNESS_WORKOUTS")
                .setText("Test Text 123");

        String username = "testUser";

        User mockUser = new User();
        when(userRepo.findUserByUsername(username)).thenReturn(Optional.of(mockUser));

        UUID categoryId = UUID.randomUUID();
        when(categoryService.getCategory(any())).thenReturn(CategoryModel.builder().id(categoryId).name(CategoryEnum.FITNESS_WORKOUTS).build());

        when(modelMapper.map(any(), eq(Post.class))).thenReturn(new Post());

        postService.makePost(postForm, username);

        verify(userRepo, times(1)).findUserByUsername(username);
        verify(categoryService, times(1)).getCategory(any());
        verify(postRepo, times(1)).saveAndFlush(any(Post.class));
    }

    @Test
    void testGetAllPosts() {

        Category mockCategory = new Category();
        mockCategory.setId(UUID.randomUUID());
        mockCategory.setName(CategoryEnum.FITNESS_WORKOUTS);

        User mockUser = new User();
        mockUser.setId(UUID.randomUUID());
        mockUser.setUsername("MockUser");

        Post mockPost = new Post();
        mockPost.setId(UUID.randomUUID());
        mockPost.setTitle("Mock Title");
        mockPost.setCategory(mockCategory);
        mockPost.setText("Mock Text");
        mockPost.setAuthor(mockUser);
        mockPost.setCreationDate(LocalDate.now());

        when(postRepo.findAll()).thenReturn(Collections.singletonList(mockPost));

        PostViewDto expectedPostViewDto = new PostViewDto();
        expectedPostViewDto.setId(mockPost.getId());
        expectedPostViewDto.setTitle(mockPost.getTitle());
        expectedPostViewDto.setCategory(mockCategory.getName().toString());
        expectedPostViewDto.setText(mockPost.getText());
        expectedPostViewDto.setCreationDate(mockPost.getCreationDate());
        expectedPostViewDto.setAuthor(mockUser.getUsername());

        List<PostViewDto> result = postService.getAllPosts();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        System.out.println("Result: " + result);
        System.out.println("Expected: " + expectedPostViewDto);

        assertTrue(result.stream().anyMatch(dto ->
                Objects.equals(dto.getId(), expectedPostViewDto.getId()) &&
                        Objects.equals(dto.getTitle(), expectedPostViewDto.getTitle()) &&
                        Objects.equals(dto.getCategory(), expectedPostViewDto.getCategory()) &&
                        Objects.equals(dto.getText(), expectedPostViewDto.getText()) &&
                        Objects.equals(dto.getCreationDate(), expectedPostViewDto.getCreationDate()) &&
                        Objects.equals(dto.getAuthor(), expectedPostViewDto.getAuthor())
        ));
    }
}