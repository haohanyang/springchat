package haohanyang.springchat.repositories;

import haohanyang.springchat.models.GroupMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMessageRepository extends JpaRepository<GroupMessage, Integer> {
}
