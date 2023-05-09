package haohanyang.springchat.repositories;

import haohanyang.springchat.models.UserMessageDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface UserMessageRepository extends JpaRepository<UserMessageDao, Integer> {
    @Query("SELECT m FROM UserMessageDao m INNER JOIN UserDao u ON m.sender.id = u.id WHERE u.username = :username")
    Set<UserMessageDao> findUserMessageBySenderUsername(@Param("username") String username);

    @Query("SELECT m FROM UserMessageDao m INNER JOIN UserDao u ON m.receiver.id = u.id WHERE u.username = :username")
    Set<UserMessageDao> findUserMessageByReceiverUsername(@Param("username") String username);
}
