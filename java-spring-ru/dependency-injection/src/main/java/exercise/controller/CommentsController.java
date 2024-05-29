package exercise.controller;

import exercise.exception.ResourceNotFoundException;
import exercise.model.Comment;
import exercise.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// BEGIN
@RequestMapping("/comments")
@RestController
public class CommentsController {

  @Autowired
  private CommentRepository commentRepository;

  @GetMapping()
  public List<Comment> index(@RequestParam(defaultValue = "100") Integer limit) {
    return commentRepository.findAll();
  }

  @GetMapping("/{id}")
  public Comment show(@PathVariable Long id) {
    return commentRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Comment with id " + id + " not found"));
  }

  @PostMapping()
  @ResponseStatus(HttpStatus.CREATED)
  public Comment create(@RequestBody Comment comment) {
    commentRepository.save(comment);
    return comment;
  }

  @PutMapping("/{id}")
  public Comment update(@PathVariable Long id, @RequestBody Comment data) {
    var comment = commentRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Comment with id " + id + " not found"));
    comment.setBody(data.getBody());
    commentRepository.save(comment);
    return comment;
  }

  @DeleteMapping("/{id}")
  public void destroy(@PathVariable Long id) {
    commentRepository.deleteById(id);
  }
}
// END
