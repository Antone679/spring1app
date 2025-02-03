package com.avdei.spring1app.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(nullable = false, unique = true)
    @Size(min = 3, max = 100, message = "Description should be between 3 and 100 characters")
    @Pattern(regexp = "^[^\\d\\t ].*", message = "Description shouldn't start from digits or spaces")
    String description;
    @Enumerated(EnumType.ORDINAL)
    Status status;
}
