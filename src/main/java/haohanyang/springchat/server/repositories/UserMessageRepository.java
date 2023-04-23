package haohanyang.springchat.server.repositories;

import haohanyang.springchat.server.models.UserMessage;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

public interface UserMessageRepository extends JpaRepository<UserMessage, Integer> {
}
