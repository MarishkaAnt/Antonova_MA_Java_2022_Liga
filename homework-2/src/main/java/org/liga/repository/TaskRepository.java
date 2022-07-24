package org.liga.repository;

import org.liga.enums.Status;
import org.liga.model.Task;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Qualifier("TaskRepository")
public interface TaskRepository extends CrudRepository<Task, Integer> {

    List<Task> findByStatus(Status status);

}
