package haohanyang.springchat.repositories;

import haohanyang.springchat.models.GroupMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface GroupMessageRepository extends JpaRepository<GroupMessage, Integer> {
}
