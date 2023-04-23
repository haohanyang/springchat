package haohanyang.springchat.server.repositories;

import haohanyang.springchat.server.models.Membership;
import haohanyang.springchat.server.models.MembershipId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface MembershipRepository extends CrudRepository<Membership, MembershipId> {

    @Query("SELECT m FROM Membership m JOIN User u ON m.member.id = u.id  WHERE u.username = :username")
    Set<Membership> findByUsername(@Param("username") String username);

    @Query("SELECT m FROM Membership m JOIN Group g ON m.group.id = g.id WHERE g.groupName = :group_name")
    Set<Membership> findByGroupName(@Param("group_name") String groupName);
}
