package exercise.controller;

import exercise.exception.ResourceNotFoundException;
import exercise.model.Post;
import exercise.repository.CommentRepository;
import exercise.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// BEGIN
@RequestMapping("/posts")
@RestController
public class PostsController {

  @Autowired
  private PostRepository postRepository;
  @Autowired
  private CommentRepository commentRepository;

  @GetMapping()
  public List<Post> index(@RequestParam(defaultValue = "100") Integer limit) {
    return postRepository.findAll();
  }

  @GetMapping("/{id}")
  public Post show(@PathVariable Long id) {
    return postRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found"));
  }

  @PostMapping()
  @ResponseStatus(HttpStatus.CREATED)
  public Post create(@RequestBody Post post) {
    postRepository.save(post);
    return post;
  }

  @PutMapping("/{id}")
  public Post update(@PathVariable Long id, @RequestBody Post data) {
    var post = postRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found"));
    post.setTitle(data.getTitle());
    post.setBody(data.getBody());
    postRepository.save(post);
    return post;
  }

  @DeleteMapping("/{id}")
  public void destroy(@PathVariable Long id) {
    commentRepository.deleteByPostId(id);
    postRepository.deleteById(id);
  }
}
// END
