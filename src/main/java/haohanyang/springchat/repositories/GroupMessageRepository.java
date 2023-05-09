package haohanyang.springchat.repositories;

import haohanyang.springchat.models.GroupMessageDao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMessageRepository extends JpaRepository<GroupMessageDao, Integer> {
}
