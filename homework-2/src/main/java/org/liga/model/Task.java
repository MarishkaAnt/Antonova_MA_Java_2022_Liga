package org.liga.model;

import lombok.*;
import org.liga.enums.Status;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Entity
@Table(name = "tasks")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    @NotBlank
    String name;

    @NotBlank
    String description;

    @ManyToOne
    @JoinTable(name = "users")
    User user;

    @Enumerated(EnumType.STRING)
    Status status;

    @NotNull
    @FutureOrPresent
    LocalDate deadline;

    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return this.getUser() + " - "
                + this.getId() + ". "
                + this.getName() + ": "
                + this.getDescription() + " - ("
                + this.getStatus() + ") - "
                + this.getDeadline().format(formatter);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task task)) return false;
        return getId().equals(task.getId()) &&
                getName().equals(task.getName()) &&
                getDescription().equals(task.getDescription()) &&
                getUser().equals(task.getUser()) &&
                getStatus() == task.getStatus() &&
                getDeadline().equals(task.getDeadline());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(),
                getUser(), getStatus(), getDeadline());
    }
}
