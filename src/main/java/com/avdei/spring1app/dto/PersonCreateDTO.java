package com.avdei.spring1app.dto;

import com.avdei.spring1app.model.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class PersonCreateDTO {
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
}
