package haohanyang.springchat.repositories;

import org.springframework.data.repository.CrudRepository;
import haohanyang.springchat.models.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByUsername(String name);
}
