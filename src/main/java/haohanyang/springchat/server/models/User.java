package haohanyang.springchat.server.models;

import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(schema = "app", name = "\"user\"")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", unique = true, nullable = false, length = 20)
    private String username;

    @Column(name = "password", nullable = false, length = 32)
    private String password;

    @OneToMany(mappedBy = "member")
    private Set<Membership> memberships = new HashSet<>();

    @OneToMany(mappedBy = "sender")
    private Set<UserMessage> userMessagesSent;

    @OneToMany(mappedBy = "receiver")
    private Set<UserMessage> userMessagesReceived;

    @OneToMany(mappedBy = "sender")
    private Set<GroupMessage> groupMessagesSent;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public int hashCode() {
        return id.hashCode();
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Set<Membership> getMemberships() {
        return memberships;
    }

    public Set<UserMessage> getUserMessagesSent() {
        return userMessagesSent;
    }

    public Set<UserMessage> getUserMessagesReceived() {
        return userMessagesReceived;
    }

    public Set<GroupMessage> getGroupMessagesSent() {
        return groupMessagesSent;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMemberships(Set<Membership> memberships) {
        this.memberships = memberships;
    }

    public void setGroupMessagesSent(Set<GroupMessage> groupMessagesSent) {
        this.groupMessagesSent = groupMessagesSent;
    }

    public void setUserMessagesReceived(Set<UserMessage> userMessagesReceived) {
        this.userMessagesReceived = userMessagesReceived;
    }

    public void setUserMessagesSent(Set<UserMessage> userMessagesSent) {
        this.userMessagesSent = userMessagesSent;
    }

    public boolean isMemberOf(Group group) {
        return memberships.stream().map(Membership::getGroup).collect(Collectors.toSet()).contains(group);
    }

    public boolean isMemberOf(String groupName) {
        return memberships.stream().map(e -> e.getGroup().getGroupName()).collect(Collectors.toSet()).contains(groupName);
    }

}