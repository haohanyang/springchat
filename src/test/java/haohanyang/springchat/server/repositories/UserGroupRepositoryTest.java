package haohanyang.springchat.server.repositories;

import haohanyang.springchat.server.models.Group;
import haohanyang.springchat.server.models.Membership;
import haohanyang.springchat.server.models.User;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;


import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.sql.init.mode=always",
        "spring.jpa.hibernate.ddl-auto=validate",
        "spring.datasource.url=${TEST_DATASOURCE_URL}",
        "spring.datasource.username=${TEST_DATASOURCE_USERNAME}",
        "spring.datasource.password=${TEST_DATASOURCE_PASSWORD}",
})
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
    @Transactional(readOnly = true)
    public void test_find_user() {
        var username = "user1";
        var dbUser = userRepository.findByUsername(username);
        assertTrue(dbUser.isPresent());
        assertEquals(username, dbUser.get().getUsername());
        assertTrue(dbUser.get().getMemberships().size() > 0);
    }

    @Test
    @Transactional(readOnly = true)
    public void test_find_group() {
        var groupName = "group1";
        var dbGroup = groupRepository.findByGroupName(groupName);
        assertTrue(dbGroup.isPresent());
        assertEquals(groupName, dbGroup.get().getGroupName());
        assertTrue(dbGroup.get().getMemberships().size() > 0);
    }

    @Test
    @Transactional(readOnly = true)
    public void test_user_groups() {
        var memberships = membershipRepository.findJoinedGroups("user1");
        var groupNames = memberships.stream().map(e -> e.getGroup().getGroupName()).collect(Collectors.toSet());
        assertTrue(groupNames.containsAll(Set.of("group1", "group2")));
    }

    @Test
    @Transactional(readOnly = true)
    public void group_members() {
        var memberships = membershipRepository.findGroupMembers("group1");
        var memberNames = memberships.stream().map(e -> e.getMember().getUsername()).collect(Collectors.toSet());
        assertTrue(memberNames.containsAll(Set.of("user1", "user3")));
    }


    @Test
    @Transactional
    public void test_create_new_user() {
        var username = "new_user";
        var user = new User(username, "my-password");
        userRepository.save(user);

        testEntityManager.getEntityManager().getTransaction().commit();

        var dbUser = userRepository.findByUsername(username);
        assertTrue(dbUser.isPresent());
    }

    @Test
    @Transactional
    public void test_create_new_group() {
        var groupName = "new_group";
        var group = new Group(groupName);
        groupRepository.save(group);

        testEntityManager.getEntityManager().getTransaction().commit();
        var dbGroup = groupRepository.findByGroupName(groupName);
        assertTrue(dbGroup.isPresent());
    }

    @Test
    @Transactional
    public void test_user_join_group() {
        // user1 joined group4
        var username = "user1";
        var groupName = "group4";

        var user = userRepository.findByUsername(username);
        var group = groupRepository.findByGroupName(groupName);
        assertTrue(user.isPresent());
        assertTrue(group.isPresent());

        var membership = new Membership(user.get(), group.get());
        membershipRepository.save(membership);

        testEntityManager.getEntityManager().getTransaction().commit();

        var user_ = userRepository.findByUsername(username);
        assertTrue(user_.isPresent());
        var dbUserMembership = user_.get().getMemberships();
        assertTrue(dbUserMembership.contains(membership));

        var group_ = groupRepository.findByGroupName(groupName);
        assertTrue(group_.isPresent());
        var dbGroupMembership = group_.get().getMemberships();
        assertTrue(dbGroupMembership.contains(membership));
    }
}