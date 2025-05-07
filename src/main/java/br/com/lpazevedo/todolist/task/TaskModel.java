package br.com.lpazevedo.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "db_task")
public class TaskModel {
  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  private String description;

  @Column(length = 50)
  private String title;
  private LocalDateTime startAt;
  private LocalDateTime endtAt;
  private String priority;
  private UUID idUser;

  @CreationTimestamp
  private LocalDateTime createdAt; //db tem a infromacao do instante de criacao do dado

  public void setTitle(String title) throws Exception{
    if(title.length() > 50){
      throw new Exception("O título deve contar até 50 caracteres");
    }
    this.title = title;
  }

  
}