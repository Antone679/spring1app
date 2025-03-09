package com.avdei.spring1app.dto;

import com.avdei.spring1app.model.Person;
import com.avdei.spring1app.model.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskDTO {
    Integer id;
    String description;
    String text;
    Status status;
    long duration;
    String currentShow;
    Date createdAt;
    Date updatedAt;
    Person author;
    LocalDate formattedCreationDate;
    List<CommentDTO> comments;
}
