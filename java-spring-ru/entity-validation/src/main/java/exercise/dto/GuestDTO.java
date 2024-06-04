package exercise.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class GuestDTO {
  private long id;

  private String name;

  private String email;

  private String phoneNumber;

  private String clubCard;

  private LocalDate cardValidUntil;

  private LocalDate createdAt;
}
