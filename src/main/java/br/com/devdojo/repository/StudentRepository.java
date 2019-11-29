package br.com.devdojo.repository;

import br.com.devdojo.model.Student;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Thiago Rodrigues on 28/11/2019
 */
public interface StudentRepository  extends CrudRepository<Student, Long> {
    List<Student> findByNameIgnoreCaseContaining(String name);
    Student findOneById(Long id);
}
