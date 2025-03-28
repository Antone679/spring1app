package com.avdei.spring1app.dto;

import com.avdei.spring1app.model.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class PersonUpdateDTO {
    Integer id;
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
