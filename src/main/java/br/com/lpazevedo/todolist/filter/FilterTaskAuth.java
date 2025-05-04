package br.com.lpazevedo.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.lpazevedo.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter{
  @Autowired
  private IUserRepository userRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)throws ServletException, IOException {

        //Configurando a rota especifica para a validacao (nesse caso task)
        var servletPath = request.getServletPath();
        if(servletPath.equals("/tasks/")){
          // Obtem a autenticacao (usuario e senha)
          var authorization =  request.getHeader("Authorization");
          var authEncoded = authorization.substring("Basic".length()).trim();
          byte[] authDecoded = Base64.getDecoder().decode(authEncoded);
          var authString = new String(authDecoded);
  
          String[] credentials = authString.split(":");
          String username  = credentials[0];
          String password  = credentials[1];
  
          // Valida usuario
          var user = this.userRepository.findByUsername(username);
          if(user == null){
            response.sendError(401);
          }else{
            // Valida senha
            var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(),user.getPassword());
            if(passwordVerify.verified){
              // Continue 
              /* Manipula a id do usuario que fez a requisiao: evita a necessidade de inserir id na autenticacao
                 garante que o usuario somente gerencie tarefas caso esteja autenticado */
              request.setAttribute("idUser", user.getId());
              filterChain.doFilter(request, response);
            }else{
            response.sendError(401);
            }
          }
        }else{
          filterChain.doFilter(request, response);
        }
  }


  /*
   * @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)throws IOException, ServletException {
    chain.doFilter(request, response);
  }
   */
  
}
