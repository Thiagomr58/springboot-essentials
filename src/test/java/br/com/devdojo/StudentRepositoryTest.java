package br.com.devdojo;

/**
 * Created by Thiago Rodrigues on 02/12/2019
 */

import br.com.devdojo.model.Student;
import br.com.devdojo.repository.StudentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolationException;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    /*@Test
    public void createShouldPersistData() {
        Student student = new Student("Thiago", "thiago@devdojo.com.br");
        this.studentRepository.save(student);
        org.assertj.core.api.Assertions.assertThat(student.getId()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(student.getName()).isEqualTo("Thiago");
        org.assertj.core.api.Assertions.assertThat(student.getEmail()).isEqualTo("thiago@devdojo.com.br");
    }

    @Test
    public void deleteShouldRemoveData() {
        Student student = new Student( "Thiago", "thiago@devdojo.com.br");
        this.studentRepository.save(student);
        studentRepository.delete(student);
        org.assertj.core.api.Assertions.assertThat(!studentRepository.findById(student.getId()).isPresent());

    }

    @Test
    public void updateShouldChangeAndPersistData() {
        Student student = new Student("Thiago", "thiago@devdojo.com.br");
        this.studentRepository.save(student);
        student.setName("Thiago222");
        student.setEmail("thiago222@devdojo.com.br");
        this.studentRepository.save(student);
        student = this.studentRepository.findOneById(student.getId());
        org.assertj.core.api.Assertions.assertThat(student.getName()).isEqualTo("Thiago222");
        org.assertj.core.api.Assertions.assertThat(student.getEmail()).isEqualTo("thiago222@devdojo.com.br");

    }


    @Test
    public void createWhenEmailIsNullShouldThrowConstranintViolationException() {
        Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> studentRepository.save(new Student("luiz", null)));
    }


    @Test
    public void createWhenEmailINotValidShouldThrowConstranintViolationException() {
        Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> studentRepository.save(new Student("luiz", "INVALID")));
    }*/
}

