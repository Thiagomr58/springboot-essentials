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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static java.util.Arrays.asList;
import static org.springframework.http.HttpMethod.*;

/**
 * Created by Thiago Rodrigues on 03/12/2019
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class StudentEndpointTokenTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private  int port;

    @MockBean
    private StudentRepository studentRepository;

    @Autowired
    private MockMvc mockMvc; // para usar o MockMvc é necessario anotar a classe com @AutoConfigurationMockMvc

    private HttpEntity<Void> protectedHeader;
    private HttpEntity<Void> adminHeader;
    private HttpEntity<Void> wrongHeader;




    @org.junit.jupiter.api.BeforeEach
    public void configProtectedHeaders() {
        String str = "{\"username\": \"oda\", \"password\":\"devdojo\"}";
        HttpHeaders headers = restTemplate.postForEntity("/login", str, String.class).getHeaders();
        this.protectedHeader = new HttpEntity<>(headers);
    }

    @org.junit.jupiter.api.BeforeEach
    public void configAdminHeaders() {
        String str = "{\"username\": \"toyo\", \"password\":\"devdojo\"}";
        HttpHeaders headers = restTemplate.postForEntity("/login", str, String.class).getHeaders();
        this.adminHeader = new HttpEntity<>(headers);
    }

    @org.junit.jupiter.api.BeforeEach
    public void configWrongHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "11111");
        this.wrongHeader = new HttpEntity<>(headers);
    }

    @org.junit.jupiter.api.BeforeEach
    void config() {
        Student student = new Student(1L, "Legolas", "legolas@lotr.com");
        BDDMockito.when(studentRepository.findOneById(student.getId())).thenReturn(student);
    }



   // o @Before, executa esse metodo setup antes de executar os tests. Mas não está funcionando, então desativei
    @BeforeTestClass
    public void setup(){
        Student student = new Student(1L, "Legolas", "legolas@lotr.com");
        BDDMockito.when(studentRepository.findOneById(student.getId())).thenReturn(student);
    }

    @Test
    public void listStudentsWhenTokenIsIncorrectShouldReturnStatusCode403(){
        ResponseEntity<String> response = restTemplate
                .exchange("/v1/protected/students/", GET, wrongHeader, String.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(403);

    }

    @Test
    public void getStudentByIdWhenTokenIsIncorrectShouldReturnStatusCode403(){
        ResponseEntity<String> response = restTemplate
                .exchange("/v1/protected/students/1", GET, wrongHeader, String.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(403);

    }

    @Test
    public void listStudentsWhenTokenIsCorrectShouldReturnStatusCode200(){

        ResponseEntity<String> response = restTemplate
                .exchange("/v1/protected/students/1", GET, protectedHeader, String.class);
        System.out.println(response.getBody());
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);

    }

    @Test
    public void getStudentByIdWhenTokenISCorrectShouldReturnStatusCode200(){

        ResponseEntity<Student> response = restTemplate
                .exchange("/v1/protected/students/1", GET, protectedHeader, Student.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);

    }

    @Test
    public void getStudentByIdWhenTokenIsCorrectAreCorrectAndStudentDoesExistsShouldReturnStatusCode404(){
        ResponseEntity<Student> response = restTemplate
                .exchange("/v1/protected/students/-1", GET, protectedHeader, Student.class);
        System.out.println(response.getBody());
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(404);

    }

    @Test
    public void deleteWhenUserHasRoleAdminAndStudentExistShouldReturnStatusCode200(){

        BDDMockito.doNothing().when(studentRepository).deleteById(1L);
        ResponseEntity<String> exchange = restTemplate
                .exchange("/v1/admin/students/1", DELETE, adminHeader, String.class);
        Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(200);

    }

    @Test
    public void deleteWhenUserHasRoleAdminAndStudentDoesNotExistShouldReturnStatusCode404() throws Exception {
        String token = adminHeader.getHeaders().get("Authorization").get(0);
        BDDMockito.doNothing().when(studentRepository).deleteById(1L);

        mockMvc.perform(MockMvcRequestBuilders
            .delete("/v1/admin/students/{id}", -1L)
                .header("Authorization", token))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void deleteWhenUserDoesNotHaveRoleAdminShouldReturnStatusCode404() throws Exception {
        String token = protectedHeader.getHeaders().get("Authorization").get(0);

        BDDMockito.doNothing().when(studentRepository).deleteById(1L);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/v1/admin/students/{id}", 1L)
                .header("Authorization", token))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    /*@Test
    public void createWhenNameIsNullShouldReturnStatusCode400BadRequest() throws Exception {
        Student student = new Student(3L, "oi", "sam@lotr.com");
        BDDMockito.when(studentRepository.save(student)).thenReturn(student);
        ResponseEntity<String> response = restTemplate
                .exchange("/v1/admin/students/", POST, new HttpEntity<>(student, adminHeader.getHeaders()), String.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(400);
        Assertions.assertThat(response.getBody()).contains("fieldMessage", "O campo nome é obrigatório");

    }*/

    @Test
    public void createShouldPersitDataAndReturnStatusCode201() throws Exception {
        Student student = new Student(3L, "sam", "sam@lotr.com");
        BDDMockito.when(studentRepository.save(student)).thenReturn(student);
        ResponseEntity<Student> response = restTemplate
                .exchange("/v1/admin/students/", POST, new HttpEntity<>(student, adminHeader.getHeaders()), Student.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(201);
        Assertions.assertThat(response.getBody().getId()).isNotNull();

    }

}
