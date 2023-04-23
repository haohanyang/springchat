package haohanyang.springchat.server.repositories;

import haohanyang.springchat.server.models.GroupMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMessageRepository extends JpaRepository<GroupMessage, Integer> {
}
