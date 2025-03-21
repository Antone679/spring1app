package com.avdei.spring1app.dto;

import com.avdei.spring1app.model.Person;
import com.avdei.spring1app.model.Status;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class TaskUpdateDTO {
    Integer id;
    @Column(nullable = false, unique = true)
    @Size(min = 3, max = 30, message = "Description should be between 3 and 30 characters")
    @Pattern(regexp = "^[^\\d\\t ].*", message = "Description shouldn't start from digits or spaces")
    String description;
    @Column(columnDefinition = "TEXT")
    String text;
    @Enumerated(EnumType.ORDINAL)
    Status status;
    Person author;
}
