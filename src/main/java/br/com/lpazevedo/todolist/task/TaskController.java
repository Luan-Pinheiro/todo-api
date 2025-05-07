package br.com.lpazevedo.todolist.task;

import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RestController;

import br.com.lpazevedo.todolist.utils.Utils;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;





@RestController
@RequestMapping("/tasks")
public class TaskController {
  @Autowired
  private ITaskRespository taskRespository;

  @PostMapping("/")
  public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
    var idUser = request.getAttribute("idUser");
    taskModel.setIdUser((UUID)idUser);
    LocalDateTime currentDate = LocalDateTime.now();

    if(currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndtAt())){
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data inserida deve ser válida");
    }
    if(taskModel.getEndtAt().isBefore(taskModel.getStartAt())){
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de término deve ser posterior a data de início");
    }

    var task = this.taskRespository.save(taskModel);
    return ResponseEntity.status(HttpStatus.OK).body(task);
  }

  @GetMapping("/")
  public List<TaskModel> list(HttpServletRequest request){
    var idUser = request.getAttribute("idUser");
    var tasks = this.taskRespository.findByIdUser((UUID)idUser);

    return tasks;
  }

  //                            |     Variavel do path    |
  //http://localhost:8080/tasks/892347823-oowkapt-897456123
  @PutMapping("/{id}")
  public TaskModel update(@RequestBody TaskModel taskModel, @PathVariable  UUID id, HttpServletRequest request){
    var idUser = request.getAttribute("idUser");

    var task = this.taskRespository.findById(id).orElse(null);

    Utils.copyNonNullProperties(taskModel, task);

    return this.taskRespository.save(task);
  }

  
}

