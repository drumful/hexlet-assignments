package exercise.controller;

import java.util.List;

import exercise.dto.TaskCreateDTO;
import exercise.dto.TaskDTO;
import exercise.dto.TaskUpdateDTO;
import exercise.mapper.TaskMapper;
import exercise.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import exercise.exception.ResourceNotFoundException;
import exercise.repository.TaskRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/tasks")
public class TasksController {

  @Autowired
  private TaskRepository taskRepository;

  @Autowired
  private TaskMapper taskMapper;

  @Autowired
  private UserRepository userRepository;

  // BEGIN
  @GetMapping()
  public List<TaskDTO> index() {
    var tasks = taskRepository.findAll();
    return tasks.stream()
      .map(p -> taskMapper.map(p))
      .toList();
  }

  @GetMapping("/{id}")
  public TaskDTO show(@PathVariable Long id) {
    var task = taskRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
    var taskDto = taskMapper.map(task);
    return taskDto;
  }

  @PostMapping()
  @ResponseStatus(HttpStatus.CREATED)
  public TaskDTO create(@RequestBody TaskCreateDTO dto) {
    var task = taskMapper.map(dto);
    taskRepository.save(task);
    var taskDto = taskMapper.map(task);
    return taskDto;
  }

  @PutMapping("/{id}")
  public TaskDTO update(@PathVariable Long id, @RequestBody TaskUpdateDTO data) {
    var task = taskRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
    taskMapper.update(data, task);
    var user= userRepository.findById(data.getAssigneeId())
      .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
    task.setAssignee(user);
    taskRepository.save(task);
    var taskDto = taskMapper.map(task);
    return taskDto;
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void destroy(@PathVariable Long id) {
    taskRepository.deleteById(id);
  }
  // END
}
