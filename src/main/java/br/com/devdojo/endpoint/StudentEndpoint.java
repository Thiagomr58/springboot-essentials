package br.com.devdojo.endpoint;

import br.com.devdojo.error.CustomErrorType;
import br.com.devdojo.error.ResourceNotFoundException;
import br.com.devdojo.model.Student;
import br.com.devdojo.repository.StudentRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by Thiago Rodrigues on 28/11/2019
 */
@RestController
@RequestMapping("v1")
public class StudentEndpoint {

    private final StudentRepository studantDAO;

    public StudentEndpoint(StudentRepository studantDAO) {
        this.studantDAO = studantDAO;
    }

    @GetMapping(path = "protected/students")
    public ResponseEntity<?> listAll(Pageable pageable){
        return new ResponseEntity<>(studantDAO.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "protected/students/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable("id") Long id,
                                            Authentication authentication){
        System.out.println(authentication);
        verifyIfStudentExists(id);
        Student student = studantDAO.findOneById(id);

        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @GetMapping(path = "protected/students/findByName/{name}")
    public ResponseEntity<?> findStudentsByName(@PathVariable String name){
        return new ResponseEntity<>(studantDAO.findByNameIgnoreCaseContaining(name), HttpStatus.OK);
    }

    @PostMapping(path = "admin/students")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> save(@Valid @RequestBody Student student){
        return new ResponseEntity<>(studantDAO.save(student), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "admin/students/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        verifyIfStudentExists(id);
        studantDAO.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "admin/students")
    public ResponseEntity<?> update(@RequestBody Student student){
        verifyIfStudentExists(student.getId());
        studantDAO.save(student);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void verifyIfStudentExists(Long id){
        if (studantDAO.findOneById(id) == null)
            throw new ResourceNotFoundException("Student not found for ID: "+id);
    }





























}
