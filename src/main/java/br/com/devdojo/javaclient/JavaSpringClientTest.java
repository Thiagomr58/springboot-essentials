package br.com.devdojo.javaclient;

import br.com.devdojo.model.PageableResponse;
import br.com.devdojo.model.Student;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Thiago Rodrigues on 02/12/2019
 */
public class JavaSpringClientTest {

    public static void main(String[] args) {

        Student studentPost = new Student();
        studentPost.setName("John Wick 2");
        studentPost.setEmail("john@pensil.com");
//        studentPost.setId(21L);
        JavaClientDAO dao = new JavaClientDAO();
//        System.out.println(dao.findById(5));
//        List<Student> students = dao.listAll();
//        System.out.println(students);
//        System.out.println(dao.save(studentPost));

//        dao.update(studentPost);
        dao.delete(21);



    }


}
