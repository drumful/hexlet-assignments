package exercise.model;

import exercise.annotation.Inspect;

public class Address {
  @Inspect
  public String getCity() throws InterruptedException {
    return "Жень Шень";
  }

  @Inspect
  public String getPostalCode() {
    return "2024-05";
  }
}