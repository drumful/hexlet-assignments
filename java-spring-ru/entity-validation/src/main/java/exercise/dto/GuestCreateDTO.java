package exercise.dto;

// BEGIN
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
public class GuestCreateDTO {
  private long id;

  @NonNull
  private String name;

  @Email
  private String email;

  @Pattern(regexp = "^\\+(\\d){11,13}$")
  private String phoneNumber;

  @Size(min = 4, max = 4)
  private String clubCard;

  @Future
  private LocalDate cardValidUntil;

  private LocalDate createdAt;
}
// END
