package exercise.repository;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import exercise.model.Product;

import org.springframework.data.domain.Sort;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
  // BEGIN
  List<Product> findAllByPriceBetweenOrderByPrice(Integer min, Integer max);
  List<Product> findAllByPriceGreaterThanEqualOrderByPrice(Integer price);
  List<Product> findAllByPriceLessThanEqualOrderByPrice(Integer price);
  //  List<Product> findAllOrderByPrice();
  // END
}
