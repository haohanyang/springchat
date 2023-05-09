package haohanyang.springchat.repositories;

import org.springframework.data.repository.CrudRepository;
import haohanyang.springchat.models.UserDao;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserDao, Integer> {
    Optional<UserDao> findByUsername(String name);
    Optional<UserDao> findByEmail(String email);
}
