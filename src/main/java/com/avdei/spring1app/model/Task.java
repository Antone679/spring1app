package com.avdei.spring1app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

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
    @Column(nullable = false, unique = true, columnDefinition = "TEXT")
    @Size(min = 3, max = 30, message = "Description should be between 3 and 30 characters")
    @Pattern(regexp = "^[^\\d\\t ].*", message = "Description shouldn't start from digits or spaces")
    String description;
    @Column(columnDefinition = "TEXT")
    String text;
    @Enumerated(EnumType.ORDINAL)
    Status status;
    @Column(nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    long duration;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    Date createdAt;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    Date updatedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    Person author;
    boolean isActive;
    @OneToMany (mappedBy = "task", orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Comment> comments;
}
