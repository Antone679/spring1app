package com.avdei.spring1app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "person")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Temporal(TemporalType.DATE)
    @CreationTimestamp
    Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    Date updatedAt;
    @OneToMany (mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Task> tasks;
    @OneToMany (mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Comment> comments;

}
