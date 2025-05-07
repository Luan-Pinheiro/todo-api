package br.com.lpazevedo.todolist.task;

import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;




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
  
}

