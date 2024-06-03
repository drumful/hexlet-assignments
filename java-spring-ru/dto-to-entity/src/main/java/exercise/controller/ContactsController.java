package exercise.controller;

import exercise.dto.ContactCreateDTO;
import exercise.dto.ContactDTO;
import exercise.model.Contact;
import exercise.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contacts")
public class ContactsController {

  @Autowired
  private ContactRepository contactRepository;

  // BEGIN
  @PostMapping("")
  @ResponseStatus(HttpStatus.CREATED)
  public ContactDTO create(@RequestBody ContactCreateDTO data) {
    var contact = contactRepository.save(toEntity(data));
    return toDTO(contact);
  }

  private ContactDTO toDTO(Contact contact) {
    var dto = new ContactDTO();
    dto.setId(contact.getId());
    dto.setFirstName(contact.getFirstName());
    dto.setLastName(contact.getLastName());
    dto.setPhone(contact.getPhone());
    dto.setCreatedAt(contact.getCreatedAt());
    dto.setUpdatedAt(contact.getUpdatedAt());
    return dto;
  }

  private Contact toEntity(ContactCreateDTO contactDto) {
    var contact = new Contact();
    contact.setFirstName(contactDto.getFirstName());
    contact.setLastName(contactDto.getLastName());
    contact.setPhone(contactDto.getPhone());
    return contact;
  }  // END
}
