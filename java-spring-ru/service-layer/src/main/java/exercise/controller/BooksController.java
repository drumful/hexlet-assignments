package exercise.controller;

import exercise.dto.BookDTO;
import exercise.dto.BookCreateDTO;
import exercise.dto.BookUpdateDTO;
import exercise.service.AuthorService;
import exercise.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/books")
public class BooksController {

  @Autowired
  private BookService bookService;

  // BEGIN
  @GetMapping()
  public List<BookDTO> index() {
    var books = bookService.findAll();
    return books;
  }

  @GetMapping("/{id}")
  public BookDTO show(@PathVariable Long id) {
    var bookDto = bookService.findById(id);
    return bookDto;
  }

  @PostMapping()
  public ResponseEntity<BookDTO> create(@RequestBody BookCreateDTO dto) {
    var bookDto = bookService.create(dto);
    if (bookDto == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } else {
      return new ResponseEntity<>(bookDto, HttpStatus.CREATED);
    }
  }

  @PutMapping("/{id}")
  public BookDTO update(@PathVariable Long id, @RequestBody BookUpdateDTO dto) {
    var bookDto = bookService.update(id, dto);
    return bookDto;
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void destroy(@PathVariable Long id) {
    bookService.deleteById(id);
  }
  // END
}
