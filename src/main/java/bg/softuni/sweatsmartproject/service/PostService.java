package bg.softuni.sweatsmartproject.service;

import bg.softuni.sweatsmartproject.domain.dto.model.CategoryModel;
import bg.softuni.sweatsmartproject.domain.dto.model.PostModel;
import bg.softuni.sweatsmartproject.domain.dto.view.PostViewDto;
import bg.softuni.sweatsmartproject.domain.dto.wrapper.PostForm;
import bg.softuni.sweatsmartproject.domain.entity.Post;
import bg.softuni.sweatsmartproject.domain.entity.User;
import bg.softuni.sweatsmartproject.repository.PostRepo;
import bg.softuni.sweatsmartproject.repository.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepo postRepo;

    private final ModelMapper modelMapper;

    private final UserRepo userRepo;
    private final CategoryService categoryService;

    @Autowired
    public PostService(PostRepo postRepo, ModelMapper modelMapper, UserRepo userRepo, CategoryService categoryService) {
        this.postRepo = postRepo;
        this.modelMapper = modelMapper;
        this.userRepo = userRepo;
        this.categoryService = categoryService;
    }

    public void makePost(PostForm postForm, String username) {
        final User user = this.userRepo.findUserByUsername(username)
                .orElseThrow(NoSuchElementException::new);

        final CategoryModel categoryModel = categoryService.getCategory(postForm.getCategory());

        final PostModel postModel = PostModel.builder()
                .id(postForm.getId())
                .title(postForm.getTitle())
                .category(categoryModel)
                .text(postForm.getText())
                .creationDate(LocalDate.now())
                .author(user)
                .build();

        final Post post = this.modelMapper.map(postModel, Post.class);

        this.postRepo.saveAndFlush(post);
    }

    public List<PostViewDto> getAllPosts() {
        List<Post> posts = postRepo.findAll();

        return posts.stream().map(Post::toDto).collect(Collectors.toList());
    }
}