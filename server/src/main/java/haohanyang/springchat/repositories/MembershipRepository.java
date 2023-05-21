package haohanyang.springchat.repositories;

import haohanyang.springchat.models.Group;
import haohanyang.springchat.models.Membership;
import haohanyang.springchat.models.MembershipId;
import haohanyang.springchat.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface MembershipRepository extends CrudRepository<Membership, MembershipId> {

    @Query("SELECT m FROM Membership m JOIN User u ON m.member.id = u.id  WHERE u.username = :username")
    Set<Membership> findByUsername(@Param("username") String username);

    @Query("SELECT m FROM Membership m JOIN Group g ON m.group.id = g.id WHERE g.id = :group_id")
    Set<Membership> findByGroupId(@Param("group_id") Integer groupId);

    Set<Membership> findByMember(User member);

    Optional<Membership> findByMemberAndGroup(User member, Group group);

    void deleteByMemberAndGroup(User member, Group group);
}
