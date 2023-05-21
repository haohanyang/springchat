package haohanyang.springchat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import haohanyang.springchat.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String name);
}
