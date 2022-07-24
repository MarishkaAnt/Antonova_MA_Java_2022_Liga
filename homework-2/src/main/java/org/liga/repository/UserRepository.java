package org.liga.repository;

import org.liga.model.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier("UserRepository")
public interface UserRepository extends CrudRepository<User, Integer> {

}
