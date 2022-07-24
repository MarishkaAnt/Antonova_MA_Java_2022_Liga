package org.liga.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    @NotBlank
    String firstName;

    @NotBlank
    String lastName;

    @OneToMany(mappedBy= "tasks")
    Set<Task> tasks;

    public String toString() {
        return    this.getId() + ". "
                + this.getFirstName() + " "
                + this.getLastName();
    }

}
