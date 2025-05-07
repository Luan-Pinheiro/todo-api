package br.com.lpazevedo.todolist.utils;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class Utils {


  //Copia de um objeto para o outro ignorando as propriedades nulas
  public static void copyNonNullProperties(Object source, Object target){
    BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
  }

  //Idenfica todas as propriedades nulas do objeto em questao
  public static String[] getNullPropertyNames(Object source){
    //BeanWrapper interface que viabiliza o acesso das propriedades dentro de um objeto
    final BeanWrapper src = new BeanWrapperImpl(source);

    PropertyDescriptor[] pds = src.getPropertyDescriptors();

    Set<String> emptyNames = new HashSet<>();

    for(PropertyDescriptor pd : pds){
      Object sourceValue = src.getPropertyValue(pd.getName());
      if(sourceValue == null){
        //Insere no hashset todas as propriedades nulas
        emptyNames.add(pd.getName());
      }
    }

    String[] resObject = new String[emptyNames.size()];
    return emptyNames.toArray(resObject);
  }
}
