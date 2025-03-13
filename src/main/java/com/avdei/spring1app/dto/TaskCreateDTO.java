package com.avdei.spring1app.dto;

import com.avdei.spring1app.model.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;


@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class TaskCreateDTO {

    @Column(nullable = false, unique = true)
    @Size(min = 3, max = 30, message = "Description should be between 3 and 30 characters")
    @Pattern(regexp = "^[^\\d\\t ].*", message = "Description shouldn't start from digits or spaces")
    String description;
    @Column(columnDefinition = "TEXT")
    String text;
    @Enumerated(EnumType.ORDINAL)
    Status status;
}