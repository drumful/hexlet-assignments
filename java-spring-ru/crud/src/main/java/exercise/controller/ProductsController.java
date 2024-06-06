package exercise.controller;

import exercise.dto.ProductCreateDTO;
import exercise.dto.ProductDTO;
import exercise.dto.ProductUpdateDTO;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.CategoryMapper;
import exercise.mapper.ProductMapper;
import exercise.repository.CategoryRepository;
import exercise.repository.ProductRepository;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashSet;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductsController {
  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private ProductMapper productMapper;

  @Autowired
  private CategoryRepository categoryRepository;

  @Autowired
  private CategoryMapper categoryMapper;

  // BEGIN
  @GetMapping()
  public List<ProductDTO> index() {
    var products = productRepository.findAll();
    return products.stream()
      .map(p -> productMapper.map(p))
      .toList();
  }

  @GetMapping("/{id}")
  public ProductDTO show(@PathVariable Long id) {
    var product = productRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));
    var productDto = productMapper.map(product);
    return productDto;
  }

  @PostMapping()
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<ProductDTO> create(@RequestBody ProductCreateDTO dto) {
    var category = categoryRepository.findById(dto.getCategoryId());
    if (category.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    var product = productMapper.map(dto);
    productRepository.save(product);
    var productDto = productMapper.map(product);
    return new ResponseEntity<>(productDto, HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProductDTO> update(@PathVariable Long id, @RequestBody ProductUpdateDTO dto) {
    var product = productRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));
    productMapper.update(dto, product);
    var category = categoryRepository.findById(dto.getCategoryId().get());
    if (category.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    product.setCategory(category.get());
    productRepository.save(product);
    var productDto = productMapper.map(product);
    return ResponseEntity.ok(productDto);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void destroy(@PathVariable Long id) {
    productRepository.deleteById(id);
  }
  // END
}
