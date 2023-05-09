package haohanyang.springchat.repositories;

import haohanyang.springchat.models.GroupDao;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GroupRepository extends CrudRepository<GroupDao, Integer> {
    Optional<GroupDao> findByGroupName(String groupName);
}
