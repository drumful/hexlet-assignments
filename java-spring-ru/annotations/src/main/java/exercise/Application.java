package exercise;

import exercise.model.Address;
import exercise.annotation.Inspect;

import java.lang.reflect.Method;

public class Application {
  public static void main(String[] args) {
    var address = new Address("London", 12345678);

    // BEGIN
    for (Method method : Address.class.getDeclaredMethods()) {
      if (method.isAnnotationPresent(Inspect.class)) {
        String regex = "\\.";
        String[] splitType = method.getReturnType().toString().split(regex);
        System.out.println("Method " + method.getName() + " returns a value of type " + splitType[splitType.length - 1]);
      }
    }
    // END
  }
}
