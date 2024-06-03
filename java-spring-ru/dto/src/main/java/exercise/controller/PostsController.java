package exercise.controller;

import exercise.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import exercise.model.Post;
import exercise.model.Comment;
import exercise.repository.PostRepository;
import exercise.exception.ResourceNotFoundException;
import exercise.dto.PostDTO;
import exercise.dto.CommentDTO;

// BEGIN
@RequestMapping("/posts")
@RestController
public class PostsController {

  @Autowired
  private PostRepository postRepository;
  @Autowired
  private CommentRepository commentRepository;

  @GetMapping("")
  public List<PostDTO> index(@RequestParam(defaultValue = "100") Integer limit) {
    var posts = postRepository.findAll();
    var result = posts.stream()
      .map(this::toDTO)
      .toList();
    return result;
  }

  @GetMapping("/{id}")
  public PostDTO show(@PathVariable Long id) {
    var post = postRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found"));
    var result = toDTO(post);
    return result;
  }

  private PostDTO toDTO(Post post) {
    var dto = new PostDTO();
    dto.setId(post.getId());
    dto.setBody(post.getBody());
    dto.setTitle(post.getTitle());
    var comments = commentRepository.findByPostId(post.getId())
      .stream()
      .map(this::toDTO)
      .toList();
    dto.setComments(comments);
    return dto;
  }

  private CommentDTO toDTO(Comment comment) {
    var dto = new CommentDTO();
    dto.setId(comment.getId());
    dto.setBody(comment.getBody());
    return dto;
  }
}
// END
