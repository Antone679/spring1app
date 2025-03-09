package com.avdei.spring1app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(nullable = false, columnDefinition = "TEXT")
    @Size(min = 3, max = 300, message = "Comment should be between 3 and 300 characters")
    @Pattern(regexp = "^[^\\d\\t ].*", message = "Comment shouldn't start from digits or spaces")
    String text;
    @JoinColumn(name = "author_id")
    @ManyToOne(fetch = FetchType.EAGER)
    Person author;
    @JoinColumn(name = "task_id")
    @ManyToOne(fetch = FetchType.LAZY)
    Task task;
    @CreationTimestamp
    @Temporal(TemporalType.TIME)
    Date createdAt;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    Date updatedAt;

}
