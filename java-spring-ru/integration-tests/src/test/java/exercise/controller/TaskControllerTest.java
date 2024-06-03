package exercise.controller;

import org.junit.jupiter.api.Test;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

import org.instancio.Instancio;
import org.instancio.Select;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import exercise.repository.TaskRepository;
import exercise.model.Task;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

// BEGIN
@SpringBootTest
@AutoConfigureMockMvc
// END
class YtApplicationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private Faker faker;

  @Autowired
  private ObjectMapper om;

  @Autowired
  private TaskRepository taskRepository;


  @Test
  public void testWelcomePage() throws Exception {
    var result = mockMvc.perform(get("/"))
      .andExpect(status().isOk())
      .andReturn();

    var body = result.getResponse().getContentAsString();
    assertThat(body).contains("Welcome to Spring!");
  }

  @Test
  public void testIndex() throws Exception {
    var result = mockMvc.perform(get("/tasks"))
      .andExpect(status().isOk())
      .andReturn();

    var body = result.getResponse().getContentAsString();
    assertThatJson(body).isArray();
  }

  // BEGIN
  @Test
  @Transactional(rollbackFor = Exception.class)
  public void testShow() throws Exception {
    var task = Instancio.of(Task.class)
      .supply(Select.field(Task::getId), () -> 1)
      .supply(Select.field(Task::getCreatedAt), () -> LocalDate.now())
      .supply(Select.field(Task::getUpdatedAt), () -> LocalDate.now())
      .supply(Select.field(Task::getTitle), () -> faker.lorem().word())
      .supply(Select.field(Task::getDescription), () -> faker.lorem().paragraph())
      .create();
    taskRepository.save(task);

    var result = mockMvc.perform(get("/tasks/1"))
      .andExpect(status().isOk())
      .andReturn();

    var body = result.getResponse().getContentAsString();
    assertThatJson(body).isNotNull();
    assertThatJson(body).isObject();
    assertThatJson(body).and(
      a -> a.node("id").isEqualTo(1),
      a -> a.node("title").isEqualTo(task.getTitle()),
      a -> a.node("description").isEqualTo(task.getDescription()),
      a -> a.node("createdAt").isEqualTo(task.getCreatedAt().toString()),
      a -> a.node("updatedAt").isEqualTo(task.getUpdatedAt().toString())
    );
  }

  @Test
  @Transactional(rollbackFor = Exception.class)
  public void testCreate() throws Exception {
    var task = Instancio.of(Task.class)
      .supply(Select.field(Task::getId), () -> 1)
      .supply(Select.field(Task::getCreatedAt), () -> LocalDate.now())
      .supply(Select.field(Task::getUpdatedAt), () -> LocalDate.now())
      .supply(Select.field(Task::getTitle), () -> faker.lorem().word())
      .supply(Select.field(Task::getDescription), () -> faker.lorem().paragraph())
      .create();

    var result = mockMvc.perform(post("/tasks")
        .contentType(MediaType.APPLICATION_JSON)
        .content(om.writeValueAsString(task)))
      .andExpect(status().isCreated())
      .andReturn();

    var body = result.getResponse().getContentAsString();
    assertThatJson(body).isNotNull();
    assertThatJson(body).isObject();
    assertThatJson(body).and(
//      a -> a.node("id").isEqualTo(task.getId()),
      a -> a.node("title").isEqualTo(task.getTitle()),
      a -> a.node("description").isEqualTo(task.getDescription()),
      a -> a.node("createdAt").isEqualTo(task.getCreatedAt().toString()),
      a -> a.node("updatedAt").isEqualTo(task.getUpdatedAt().toString())
    );
  }

  @Test
  @Transactional(rollbackFor = Exception.class)
  public void testUpdate() throws Exception {
    var task = Instancio.of(Task.class)
      .ignore(Select.field(Task::getId))
      .supply(Select.field(Task::getCreatedAt), () -> LocalDate.now())
      .supply(Select.field(Task::getUpdatedAt), () -> LocalDate.now())
      .supply(Select.field(Task::getTitle), () -> faker.lorem().word())
      .supply(Select.field(Task::getDescription), () -> faker.lorem().paragraph())
      .create();

    var task2 = taskRepository.save(task);
    task2.setTitle(faker.lorem().word());
    task2.setDescription(faker.lorem().paragraph());

    var result = mockMvc.perform(put("/tasks/" + task2.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(om.writeValueAsString(task2)))
      .andExpect(status().isOk())
      .andReturn();

    var body = result.getResponse().getContentAsString();
    assertThatJson(body).isNotNull();
    assertThatJson(body).isObject();
    assertThatJson(body).and(
      a -> a.node("id").isEqualTo(task2.getId()),
      a -> a.node("title").isEqualTo(task2.getTitle()),
      a -> a.node("description").isEqualTo(task2.getDescription()),
      a -> a.node("createdAt").isEqualTo(task2.getCreatedAt().toString()),
      a -> a.node("updatedAt").isEqualTo(task2.getUpdatedAt().toString())
    );
  }

  @Test
  @Transactional(rollbackFor = Exception.class)
  public void testDelete() throws Exception {
    var task = Instancio.of(Task.class)
      .ignore(Select.field(Task::getId))
      .ignore(Select.field(Task::getCreatedAt))
      .ignore(Select.field(Task::getUpdatedAt))
      .supply(Select.field(Task::getTitle), () -> faker.lorem().word())
      .supply(Select.field(Task::getDescription), () -> faker.lorem().paragraph())
      .create();
    var task2 = taskRepository.save(task);

    var result = mockMvc.perform(delete("/tasks/" + task2.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(om.writeValueAsString(task2)))
      .andExpect(status().isOk());

    var result2 = mockMvc.perform(get("/tasks/" + task2.getId()))
      .andExpect(status().isNotFound());
  }
  // END
}
