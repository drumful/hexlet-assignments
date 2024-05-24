package exercise;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import exercise.model.Post;

@SpringBootApplication
@RestController
public class Application {
  // Хранилище добавленных постов
  private List<Post> posts = Data.getPosts();

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  // BEGIN
  @GetMapping("/posts") // Список постов
  public ResponseEntity<List<Post>> index(@RequestParam(defaultValue = "10") Integer limit) {
    var result = posts.stream().limit(limit).toList();
    return ResponseEntity.ok()
      .header("X-Total-Count", String.valueOf(posts.size()))
      .body(result);
  }

  @GetMapping("/posts/{id}") // Вывод постов
  public ResponseEntity<Post> show(@PathVariable String id) {
    var post = posts.stream()
      .filter(p -> p.getId().equals(id))
      .findFirst();
    return post.isPresent()
      ? ResponseEntity.of(post)
      : new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @PostMapping("/posts") // Создание поста
  public ResponseEntity<Post> create(@RequestBody Post post) {
    posts.add(post);
    return new ResponseEntity<>(post, HttpStatus.CREATED);
  }

  @PutMapping("/posts/{id}") // Обновление поста
  public ResponseEntity<Post> update(@PathVariable String id, @RequestBody Post data) {
    var maybePost = posts.stream()
      .filter(p -> p.getId().equals(id))
      .findFirst();
    if (maybePost.isPresent()) {
      var post = maybePost.get();
      post.setId(data.getId());
      post.setTitle(data.getTitle());
      post.setBody(data.getBody());
      return ResponseEntity.ok(post);
    } else {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    // END
  }

  @DeleteMapping("/posts/{id}")
  public void destroy(@PathVariable String id) {
    posts.removeIf(p -> p.getId().equals(id));
  }
}
