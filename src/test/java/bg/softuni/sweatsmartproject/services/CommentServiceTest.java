package bg.softuni.sweatsmartproject.services;

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
        final CommentForm commentForm = new CommentForm();
        final String username = "testUser";
        final Post post = new Post();

        final User mockedUser = mock(User.class);
        when(userRepo.findUserByUsername(username)).thenReturn(Optional.of(mockedUser));

        when(modelMapper.map(any(), eq(Comment.class))).thenReturn(new Comment());

        commentService.addComment(commentForm, username, post);

        verify(commentRepo).saveAndFlush(any(Comment.class));
    }
}