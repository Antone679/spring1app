package com.avdei.spring1app.dto;

import com.avdei.spring1app.model.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class PersonCreateDTO {
    @Column(name = "username", nullable = false)
    @Size(min = 3, max = 100, message = "{reg.con.name.length}")
    @Pattern(regexp = "^[^\\d\\t ].*", message = "{reg.con.name.char}")
    String userName;
    @Column(name = "password", nullable = false)
    @Size(min = 5, max = 100, message = "{reg.con.password}")
    String password;
    @Column(name = "age")
    @Min(value = 0, message = "{reg.con.age}")
    @Max(value = 120, message = "{reg.con.max.age}")
    Short age;
    @Column(name = "email", nullable = false)
    @Email(message = "{reg.con.email}")
    String email;
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    Role role;
}
