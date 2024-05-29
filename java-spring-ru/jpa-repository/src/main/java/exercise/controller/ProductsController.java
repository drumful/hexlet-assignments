package exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Limit;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.data.domain.PageRequest;

import exercise.model.Product;
import exercise.repository.ProductRepository;
import exercise.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/products")
public class ProductsController {

  @Autowired
  private ProductRepository productRepository;

  // BEGIN
  @GetMapping(path = "")
  public List<Product> show(@RequestParam(required = false) Integer min, @RequestParam(required = false) Integer max) {
    List<Product> products;
    var sort = Sort.by(Sort.Order.asc("price"));
    var limit = Limit.unlimited();
    if (min != null && max != null) {
      products = productRepository.findAllByPriceBetweenOrderByPrice(min, max);
    } else if (min != null) {
      products = productRepository.findAllByPriceGreaterThanEqualOrderByPrice(min);
    } else if (max != null) {
      products = productRepository.findAllByPriceLessThanEqualOrderByPrice(max);
    } else {
      products = productRepository.findAll(sort);
    }
    return products;
  }
  // END

  @GetMapping(path = "/{id}")
  public Product show(@PathVariable long id) {

    var product = productRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));

    return product;
  }
}
