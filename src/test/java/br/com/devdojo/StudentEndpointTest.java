package br.com.devdojo;

import br.com.devdojo.model.Student;
import br.com.devdojo.repository.StudentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * Created by Thiago Rodrigues on 03/12/2019
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class StudentEndpointTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private  int port;

    @MockBean
    private StudentRepository studentRepository;

    @Autowired
    private MockMvc mockMvc; // para usar o MockMvc é necessario anotar a classe com @AutoConfigurationMockMvc


    @TestConfiguration
    static class Config {
        @Bean
        public RestTemplateBuilder restTemplateBuilder(){
            return new RestTemplateBuilder().basicAuthentication("toyo", "devdojo");
        }

    }

    @Test
    public void listStudentsWhenUsernameAndPasswordAreIncorrectShouldReturnStatusCode401(){
        System.out.println(port);
        restTemplate = restTemplate.withBasicAuth("1", "1");
        ResponseEntity<String> response = restTemplate.getForEntity("/v1/protected/students/", String.class);
        System.out.println(response.getBody());
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(401);

    }

    @Test
    public void getStudentByIdWhenUsernameAndPasswordAreIncorrectShouldReturnStatusCode401(){
        System.out.println(port);
        restTemplate = restTemplate.withBasicAuth("1", "1");
        ResponseEntity<String> response = restTemplate.getForEntity("/v1/protected/students/20", String.class);
        System.out.println(response.getBody());
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(401);

    }

    @Test
    public void listStudentsWhenUsernameAndPasswordAreCorrecttShouldReturnStatusCode200(){
        List<Student> students = asList(
                new Student(1L, "Legolas", "legolas@lotr.com"),
                new Student(2L, "Aragorn", "aragorn@lotr.com")
        );
        BDDMockito.when(studentRepository.findAll()).thenReturn(students);
        ResponseEntity<String> response = restTemplate.getForEntity("/v1/protected/students/", String.class);
        System.out.println(response.getBody());
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);

    }

    @Test
    public void getStudentByIdWhenUsernameAndPasswordAreCorrectShouldReturnStatusCode200(){
        Student student = new Student(1L, "Legolas", "legolas@lotr.com");
        BDDMockito.when(studentRepository.findOneById(student.getId())).thenReturn(student);

        ResponseEntity<Student> response = restTemplate
                        .getForEntity("/v1/protected/students/{id}", Student.class, student.getId());
        System.out.println(response.getBody());
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);

    }

    @Test
    public void getStudentByIdWhenUsernameAndPasswordAreCorrectAndStudentDoesExistsShouldReturnStatusCode404(){
        ResponseEntity<Student> response = restTemplate
                .getForEntity("/v1/protected/students/{id}", Student.class, -1);
        System.out.println(response.getBody());
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(404);

    }

}
