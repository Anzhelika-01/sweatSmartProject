package bg.softuni.sweatsmartproject.web;

import bg.softuni.sweatsmartproject.domain.dto.model.AppUserDetails;
import bg.softuni.sweatsmartproject.domain.dto.model.MessageModel;
import bg.softuni.sweatsmartproject.domain.dto.view.CommentsViewDto;
import bg.softuni.sweatsmartproject.domain.dto.view.PostViewDto;
import bg.softuni.sweatsmartproject.domain.dto.wrapper.CommentForm;
import bg.softuni.sweatsmartproject.domain.dto.wrapper.PostForm;
import bg.softuni.sweatsmartproject.domain.entity.Post;
import bg.softuni.sweatsmartproject.repository.CommentRepo;
import bg.softuni.sweatsmartproject.repository.PostRepo;
import bg.softuni.sweatsmartproject.service.CommentService;
import bg.softuni.sweatsmartproject.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping
public class PostController extends BaseController {

    private final PostService postService;

    private final PostRepo postRepo;

    private final CommentService commentService;

    private final CommentRepo commentRepo;
    public static final String BINDING_RESULT_PATH = "org.springframework.validation.BindingResult.";


    @Autowired
    public PostController(PostService postService, PostRepo postRepo, CommentService commentService, CommentRepo commentRepo) {
        this.postService = postService;
        this.postRepo = postRepo;
        this.commentService = commentService;
        this.commentRepo = commentRepo;
    }

    @GetMapping("/post/{postId}")
    public ModelAndView viewPost(@PathVariable UUID postId, Model model) {

        final Post post = postRepo.getPostById(postId);
        model.addAttribute("post", post);

        final List<CommentsViewDto> comments = commentService.getAllComments(post.getTitle());
        model.addAttribute("comments", comments);

        return super.view("individual-post");
    }

    @GetMapping("/all-posts")
    public ModelAndView getAllPosts(Model model) {

        final List<PostViewDto> posts = this.postService.getAllPosts();
        model.addAttribute("posts", posts);

        return super.view("all-posts");
    }

    @GetMapping("/make-post")
    public ModelAndView getMakePostPage() {

        return super.view("make-post");
    }

    @PostMapping("/make-post")
    public ModelAndView postMakingPost(@Valid @ModelAttribute(name = "postForm") PostForm postForm,
                                       BindingResult bindingResult, RedirectAttributes redirectAttributes,
                                       @AuthenticationPrincipal AppUserDetails userDetails) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("postForm", postForm)
                    .addFlashAttribute(BINDING_RESULT_PATH + "postForm", bindingResult);

            return super.view("make-post");
        }

        final String username = userDetails.getUsername();

        this.postService.makePost(postForm, username);
        return super.view("index");
    }

    @PostMapping("/post/comment")
    public ModelAndView postWritingComment(@Valid @ModelAttribute(name = "commentForm") CommentForm commentForm,
                                           BindingResult bindingResult, RedirectAttributes redirectAttributes,
                                           @AuthenticationPrincipal AppUserDetails userDetails,
                                           @ModelAttribute("postId") UUID postId) {

        final Post post = postRepo.getPostById(postId);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("commentForm", commentForm)
                    .addFlashAttribute(BINDING_RESULT_PATH + "commentForm", bindingResult);
            return super.redirect("/post/" + postId);
        }

        final String username = userDetails.getUsername();

        this.commentService.addComment(commentForm, username, post);
        return super.redirect("/post/" + postId);
    }

    @PostMapping("/post/like")
    public ModelAndView likePost(@RequestParam UUID postId) {
        postService.incrementLikeCount(postId);
        System.out.println("Post liked successfully");
        return new ModelAndView("redirect:/post/" + postId);
    }

    @ModelAttribute(name = "postForm")
    public PostForm getPostForm() {
        return new PostForm();
    }

    @ModelAttribute(name = "commentForm")
    public CommentForm getCommentForm() {
        return new CommentForm();
    }
}