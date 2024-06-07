package exercise.service;

import exercise.dto.AuthorCreateDTO;
import exercise.dto.AuthorDTO;
import exercise.dto.AuthorUpdateDTO;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.AuthorMapper;
import exercise.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import exercise.model.Author;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class AuthorService {
  // BEGIN
  @Autowired
  private AuthorRepository authorRepository;

  @Autowired
  private AuthorMapper authorMapper;

  public List<AuthorDTO> findAll() {
    var authors = authorRepository.findAll();
    return authors.stream()
      .map(p -> authorMapper.map(p))
      .toList();
  }

  public AuthorDTO findById(Long id) {
    var author = authorRepository.findById(id);
    return author.map(value -> authorMapper.map(value)).orElse(null);
  }

  public AuthorDTO create(AuthorCreateDTO dto) {
    var author = authorMapper.map(dto);
    authorRepository.save(author);
    var authorDto = authorMapper.map(author);
    return authorDto;
  }

  public AuthorDTO update(Long id, AuthorUpdateDTO dto) {
    var author2 = authorRepository.findById(id);
    if (author2.isPresent()) {
      var author = author2.get();
      authorMapper.update(dto, author);
      authorRepository.save(author);
      var authorDto = authorMapper.map(author);
      return authorDto;
    } else {
      return null;
    }
  }

  public void deleteById(Long id) {
    authorRepository.deleteById(id);
  }
  // END
}
