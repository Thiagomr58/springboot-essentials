package br.com.devdojo.model;

import javax.persistence.Entity;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;


/**
 * Created by Thiago Rodrigues on 28/11/2019
 */

@Entity
public class Student extends AbstractEntity{

    @NotEmpty(message = "O campo nome do estudante é obrigatório")
    private String name;

    @NotEmpty(message = "O campo email do estudante é obrigatório")
    @Email(message = "Insira um email válido")
    private String email;

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
