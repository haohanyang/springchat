package haohanyang.springchat.repositories;

import haohanyang.springchat.models.Group;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GroupRepository extends CrudRepository<Group, Integer> {
    Optional<Group> findByGroupName(String groupName);
}
