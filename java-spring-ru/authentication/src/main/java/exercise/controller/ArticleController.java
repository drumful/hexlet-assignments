package exercise.controller;

import java.util.List;

import exercise.dto.ArticleCreateDTO;
import exercise.dto.ArticleDTO;
import exercise.dto.ArticleUpdateDTO;
import exercise.mapper.ArticleMapper;
import exercise.repository.UserRepository;
import exercise.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import exercise.exception.ResourceNotFoundException;
import exercise.repository.ArticleRepository;
import jakarta.validation.Valid;
import exercise.dto.*;
import org.springframework.security.authentication.AuthenticationManager;
import exercise.utils.JWTUtils;

@RestController
@RequestMapping("/articles")
public class ArticleController {
  @Autowired
  private ArticleRepository articleRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ArticleMapper articleMapper;

  @Autowired
  private UserUtils userUtils;

  // BEGIN
  @PostMapping(path = "")
  @ResponseStatus(HttpStatus.CREATED)
  public ArticleDTO create(@Valid @RequestBody ArticleCreateDTO articleData) {
    var article = articleMapper.map(articleData);
    var author = userUtils.getCurrentUser();
    article.setAuthor(author);
    articleRepository.save(article);
    var articleDto = articleMapper.map(article);
    return articleDto;
  }
  // END

  @GetMapping("")
  public List<ArticleDTO> index() {
    var tasks = articleRepository.findAll();

    return tasks.stream()
      .map(t -> articleMapper.map(t))
      .toList();
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ArticleDTO show(@PathVariable Long id) {
    var article = articleRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Not Found: " + id));
    return articleMapper.map(article);
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ArticleDTO update(@RequestBody @Valid ArticleUpdateDTO articleData, @PathVariable Long id) {
    var article = articleRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Not Found"));

    articleMapper.update(articleData, article);
    articleRepository.save(article);
    return articleMapper.map(article);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void destroy(@PathVariable Long id) {
    articleRepository.deleteById(id);
  }
}
