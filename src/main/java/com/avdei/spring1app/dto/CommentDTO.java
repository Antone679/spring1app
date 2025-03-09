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
    @Column(nullable = false, columnDefinition = "TEXT")
    @Size(min = 3, max = 300, message = "Comment should be between 3 and 300 characters")
    @Pattern(regexp = "^[^\\d\\t ].*", message = "Comment shouldn't start from digits or spaces")
    String text;
    Person author;
    Task task;
    Date createdAt;
    Date updatedAt;
}
