package com.avdei.spring1app.dto;

import com.avdei.spring1app.model.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class PersonUpdateDTO {
    Integer id;
    @Column(name = "username", nullable = false)
    @Size(min = 3, max = 100, message = "Name should be between 3 and 100 characters")
    @Pattern(regexp = "^[^\\d\\t ].*", message = "Name shouldn't start from digits or spaces")
    String userName;
    @Column(name = "password", nullable = false)
    @Size(min = 5, max = 100, message = "Password should be between 5 and 20 characters")
    String password;
    @Column(name = "age")
    @Min(value = 0, message = "Age should be at least zero")
    Short age;
    @Column(name = "email", nullable = false)
    @Email(message = "Invalid format of email")
    String email;
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    Role role;

    public PersonUpdateDTO() {
    }

    public PersonUpdateDTO(Integer id, String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;
    }

    public PersonUpdateDTO(Integer id, String userName, String password, String email) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.email = email;
    }
}
