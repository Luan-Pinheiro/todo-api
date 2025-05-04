package br.com.lpazevedo.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;


@RestController
@RequestMapping("/users")
public class UserController {
  @Autowired //Gerencia o ciclo de vida
  private IUserRepository userRepository;

  @PostMapping("/")
  public ResponseEntity create(@RequestBody UserModel userModel){

    UserModel user_test = this.userRepository.findByUsername(userModel.getUsername());

    //Validando unicidade da entidade -> retorna codigo de erro
    if (user_test != null) 
     return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já existe!");

     //Criptografando a senha do usuario
    String passwordHashed = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
    userModel.setPassword(passwordHashed);

    //Salvando no banco
    var user = this.userRepository.save(userModel);
    
    //Retorn o status code de  sucesso
    return ResponseEntity.status(HttpStatus.CREATED).body(user);

  }

}
