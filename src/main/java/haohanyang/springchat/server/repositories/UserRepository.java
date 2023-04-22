package haohanyang.springchat.server.repositories;

import org.springframework.data.repository.CrudRepository;
import haohanyang.springchat.server.models.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByUsername(String name);
}
