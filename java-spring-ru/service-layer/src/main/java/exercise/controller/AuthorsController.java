package exercise.controller;

import exercise.dto.AuthorDTO;
import exercise.dto.AuthorCreateDTO;
import exercise.dto.AuthorUpdateDTO;
import exercise.service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorsController {

  @Autowired
  private AuthorService authorService;

  // BEGIN
  @GetMapping()
  public List<AuthorDTO> index() {
    var authors = authorService.findAll();
    return authors;
  }

  @GetMapping("/{id}")
  public AuthorDTO show(@PathVariable Long id) {
    var authorDto = authorService.findById(id);
    return authorDto;
  }

  @PostMapping()
  @ResponseStatus(HttpStatus.CREATED)
  public AuthorDTO create(@RequestBody AuthorCreateDTO dto) {
    var authorDto = authorService.create(dto);
    return authorDto;
  }

  @PutMapping("/{id}")
  public AuthorDTO update(@PathVariable Long id, @RequestBody AuthorUpdateDTO dto) {
    var authorDto = authorService.update(id, dto);
    return authorDto;
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void destroy(@PathVariable Long id) {
    authorService.deleteById(id);
  }
  // END
}
