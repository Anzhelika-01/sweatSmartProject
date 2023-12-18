package bg.softuni.sweatsmartproject.service;

import bg.softuni.sweatsmartproject.domain.dto.model.CommentModel;
import bg.softuni.sweatsmartproject.domain.dto.view.CommentsViewDto;
import bg.softuni.sweatsmartproject.domain.dto.wrapper.CommentForm;
import bg.softuni.sweatsmartproject.domain.entity.Comment;
import bg.softuni.sweatsmartproject.domain.entity.Post;
import bg.softuni.sweatsmartproject.domain.entity.User;
import bg.softuni.sweatsmartproject.repository.CommentRepo;
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
public class CommentService {

    public final CommentRepo commentRepo;

    private final ModelMapper modelMapper;

    private final UserRepo userRepo;

    private final PostRepo postRepo;

    @Autowired
    public CommentService(CommentRepo commentRepo, ModelMapper modelMapper, UserRepo userRepo, PostRepo postRepo) {
        this.commentRepo = commentRepo;
        this.modelMapper = modelMapper;
        this.userRepo = userRepo;
        this.postRepo = postRepo;
    }

    public void addComment(CommentForm commentForm, String username, Post post) {

        final User user = this.userRepo.findUserByUsername(username)
                .orElseThrow(NoSuchElementException::new);

        final CommentModel commentModel = CommentModel.builder()
                .id(commentForm.getId())
                .creationDate(LocalDate.now())
                .author(user)
                .text(commentForm.getText())
                .post(post)
                .build();

        final Comment comment = this.modelMapper.map(commentModel, Comment.class);

        this.commentRepo.saveAndFlush(comment);
    }

    public List<CommentsViewDto> getAllComments(String title) {

        List<Comment> comments = this.commentRepo.getCommentsByPost_Title(title);

        return comments.stream().map(Comment::toDto).collect(Collectors.toList());
    }
}