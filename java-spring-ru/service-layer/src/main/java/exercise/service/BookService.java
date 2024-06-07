package exercise.service;

import exercise.dto.BookCreateDTO;
import exercise.dto.BookDTO;
import exercise.dto.BookUpdateDTO;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.BookMapper;
import exercise.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import exercise.model.Book;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class BookService {
  // BEGIN
  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private BookMapper bookMapper;

  @Autowired
  private AuthorService authorService;

  public List<BookDTO> findAll() {
    var books = bookRepository.findAll();
    return books.stream()
      .map(p -> bookMapper.map(p))
      .toList();
  }

  public BookDTO findById(Long id) {
    var book = bookRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Book with id " + id + " not found"));
    var bookDto = bookMapper.map(book);
    return bookDto;
  }

  public BookDTO create(BookCreateDTO dto) {
    var authorDTO = authorService.findById(dto.getAuthorId());
    if (authorDTO == null) {
      return null;
    }
    var book = bookMapper.map(dto);
    bookRepository.save(book);
    var bookDto = bookMapper.map(book);
    return bookDto;
  }

  public BookDTO update(Long id, BookUpdateDTO dto) {
    var book = bookRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Book with id " + id + " not found"));
    bookMapper.update(dto, book);
    bookRepository.save(book);
    var bookDto = bookMapper.map(book);
    return bookDto;
  }

  public void deleteById(Long id) {
    bookRepository.deleteById(id);
  }
  // END
}
