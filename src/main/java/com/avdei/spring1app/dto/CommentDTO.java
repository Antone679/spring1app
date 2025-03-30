package com.avdei.spring1app.dto;

import com.avdei.spring1app.model.Person;
import com.avdei.spring1app.model.Task;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDTO {
    Integer id;
    @Column(nullable = false, columnDefinition = "TEXT")
    @Size(min = 3, max = 300, message = "{comment.size}")
    @Pattern(regexp = "^[^\\d\\t ].*", message = "{comment.pattern}")
    String commentText;
    Person author;
    Task task;
    Date createdAt;
    Date updatedAt;
}
