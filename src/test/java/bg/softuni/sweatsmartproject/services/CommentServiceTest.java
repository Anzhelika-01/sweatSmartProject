package bg.softuni.sweatsmartproject.services;

import bg.softuni.sweatsmartproject.domain.dto.model.CommentModel;
import bg.softuni.sweatsmartproject.domain.dto.wrapper.CommentForm;
import bg.softuni.sweatsmartproject.domain.entity.Comment;
import bg.softuni.sweatsmartproject.domain.entity.Post;
import bg.softuni.sweatsmartproject.domain.entity.User;
import bg.softuni.sweatsmartproject.repository.CommentRepo;
import bg.softuni.sweatsmartproject.repository.PostRepo;
import bg.softuni.sweatsmartproject.repository.UserRepo;
import bg.softuni.sweatsmartproject.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepo commentRepo;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UserRepo userRepo;

    @Mock
    private PostRepo postRepo;

    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    public void setup() {
        commentService = new CommentService(commentRepo, modelMapper, userRepo, postRepo);
    }

    @Test
    void testAddComment() {
        // Given
        CommentForm commentForm = new CommentForm();
        String username = "testUser";
        Post post = new Post();

        User mockedUser = mock(User.class);
        when(userRepo.findUserByUsername(username)).thenReturn(Optional.of(mockedUser));

        CommentModel commentModel = CommentModel.builder()
                .id(commentForm.getId())
                .creationDate(LocalDate.now())
                .author(mockedUser)
                .text(commentForm.getText())
                .post(post)
                .build();
        when(modelMapper.map(any(), eq(Comment.class))).thenReturn(new Comment());

        commentService.addComment(commentForm, username, post);

        verify(commentRepo).saveAndFlush(any(Comment.class));
    }
}