package haohanyang.springchat.server.repositories;

import haohanyang.springchat.server.models.Group;
import haohanyang.springchat.server.models.Membership;
import haohanyang.springchat.server.models.User;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;


import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserGroupRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private MembershipRepository membershipRepository;

    @Test
    public void should_find_user() {
        var username = "user1";
        var dbUser = userRepository.findByUsername(username);
        assertTrue(dbUser.isPresent());
        assertEquals(username, dbUser.get().getUsername());
    }

    @Test
    public void should_find_group() {
        var groupName = "group1";
        var dbGroup = groupRepository.findByGroupName(groupName);
        assertTrue(dbGroup.isPresent());
        assertEquals(groupName, dbGroup.get().getGroupName());
    }

    @Test
    public void should_find_joined_groups() {
        var username = "user2";
        var joinedGroups = membershipRepository.findJoinedGroups(username);
        assertEquals(2, joinedGroups.size());
    }

    @Test
    public void should_find_group_members() {
        var groupName = "group1";
        var members = membershipRepository.findGroupMembers(groupName);
        assertEquals(2, members.size());
    }

    @Test
    public void should_create_new_user() {
        var username = "user-repo-test";
        var user = new User(username, "my-password");
        userRepository.save(user);

        testEntityManager.getEntityManager().getTransaction().commit();

        var dbUser = userRepository.findByUsername(username);
        assertTrue(dbUser.isPresent());
    }

    @Test
    public void should_create_new_group() {
        var groupName = "group-repo-test";
        var group = new Group(groupName);
        groupRepository.save(group);

        testEntityManager.getEntityManager().getTransaction().commit();
        var dbGroup = groupRepository.findByGroupName(groupName);
        assertTrue(dbGroup.isPresent());
    }

    @Test
    public void should_user_join_group() {
        var username = "user1";
        var groupName = "group3";

        var user = userRepository.findByUsername(username);
        var group = groupRepository.findByGroupName(groupName);
        assertTrue(user.isPresent());
        assertTrue(group.isPresent());

        var membership = new Membership(user.get(), group.get());
        membershipRepository.save(membership);

        testEntityManager.getEntityManager().getTransaction().commit();

        var dbUser = userRepository.findByUsername(username);
        assertTrue(dbUser.isPresent());
        var dbUserMembership = dbUser.get().getMemberships();
        assertTrue(dbUserMembership.contains(membership));

        var dbGroup = groupRepository.findByGroupName(groupName);
        assertTrue(dbGroup.isPresent());
        var dbGroupMembership = dbGroup.get().getMemberships();
        assertTrue(dbGroupMembership.contains(membership));
    }
}