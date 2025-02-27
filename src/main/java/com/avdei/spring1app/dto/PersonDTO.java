package com.avdei.spring1app.dto;

import com.avdei.spring1app.model.Role;
import com.avdei.spring1app.model.Task;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PersonDTO {
    Integer id;
    String userName;
    String password;
    Short age;
    String email;
    Role role;
    Date createdAt;
    Date updatedAt;
    List<Task> tasks;
}
