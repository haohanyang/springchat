package haohanyang.springchat.repositories;

import haohanyang.springchat.models.GroupDao;
import haohanyang.springchat.models.MembershipDao;
import haohanyang.springchat.models.MembershipId;
import haohanyang.springchat.models.UserDao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface MembershipRepository extends CrudRepository<MembershipDao, MembershipId> {

    @Query("SELECT m FROM MembershipDao m JOIN UserDao u ON m.member.id = u.id  WHERE u.username = :username")
    Set<MembershipDao> findByUsername(@Param("username") String username);

    @Query("SELECT m FROM MembershipDao m JOIN GroupDao g ON m.group.id = g.id WHERE g.groupName = :group_name")
    Set<MembershipDao> findByGroupName(@Param("group_name") String groupName);

    void deleteByMemberAndGroup(UserDao member, GroupDao group);
}
