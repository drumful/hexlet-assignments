package exercise;

import exercise.annotation.Inspect;
import exercise.model.Address;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.Arrays;

// BEGIN
@SpringBootApplication
public class Application {
  public static void main(String[] args) {
    var service = new Address();

    // Итерируем все методы класса
    for (Method method : Address.class.getDeclaredMethods()) {

      // Проверяем, есть ли у метода аннотация @Inspect
      if (method.isAnnotationPresent(Inspect.class)) {

        String regex = "\\.";
        var splitType = method.getReturnType().toString().split(regex);
        System.out.println("Method " + method.getName() + " returns a value of type " + splitType[2]);
      }
    }
  }
}
// END
