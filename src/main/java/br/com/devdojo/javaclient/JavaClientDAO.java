package br.com.devdojo.javaclient;

import br.com.devdojo.handler.RestResponseExceptionHandler;
import br.com.devdojo.model.PageableResponse;
import br.com.devdojo.model.Student;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by Thiago Rodrigues on 02/12/2019
 */
public class JavaClientDAO {
    private RestTemplate restTemplate = new RestTemplateBuilder()
            .rootUri("http://localhost:8080/v1/protected/students")
            .basicAuthentication("toyo", "devdojo")
            .errorHandler(new RestResponseExceptionHandler())
            .build();

    private RestTemplate restTemplateAdmin = new RestTemplateBuilder()
            .rootUri("http://localhost:8080/v1/admin/students")
            .basicAuthentication("toyo", "devdojo")
            .errorHandler(new RestResponseExceptionHandler())
            .build();

    public Student findById(long id){
       return restTemplate.getForObject("/{id}", Student.class, id);
        //ResponseEntity<Student> forEntity = restTemplate.getForEntity("/{id}", Student.class, 5);

    }

    public List<Student> listAll() {
        ResponseEntity<PageableResponse<Student>> exchange = restTemplate
                .exchange("/", HttpMethod.GET, null,
                        new ParameterizedTypeReference<PageableResponse<Student>>() {});

        return exchange.getBody().getContent();

    }

    public  Student save(Student student){
        ResponseEntity<Student> exchangePost = restTemplateAdmin
                .exchange("/", HttpMethod.POST,
                        new HttpEntity<>(student, createJSONHeaders()),
                        Student.class);

        return exchangePost.getBody();
    }

    public void update(Student student){
        restTemplateAdmin.put("/", student);
    }

    public void delete(long id) {
        restTemplateAdmin.delete("/{id}", id);
    }


    private static HttpHeaders createJSONHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

}
