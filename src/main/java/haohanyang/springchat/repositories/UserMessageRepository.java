package haohanyang.springchat.repositories;

import haohanyang.springchat.models.UserMessage;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

public interface UserMessageRepository extends JpaRepository<UserMessage, Integer> {
    @Query("SELECT m FROM UserMessage m INNER JOIN User u ON m.sender.id = u.id WHERE u.username = :username")
    Set<UserMessage> findUserMessageBySenderUsername(@Param("username") String username);

    @Query("SELECT m FROM UserMessage m INNER JOIN User u ON m.receiver.id = u.id WHERE u.username = :username")
    Set<UserMessage> findUserMessageByReceiverUsername(@Param("username") String username);
}
