package br.com.lpazevedo.todolist.erros;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//  Anotacao que define classes globais no tratamento de excecoes -> todas exceptions passam por aqui
//  e caso estiver descrita no metodo tera o tratamento especial
@ControllerAdvice
public class ExcpetionHandlerController {

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e){
    String errorMessage = e.getMostSpecificCause().getMessage();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
  }
}
