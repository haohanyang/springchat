package haohanyang.springchat.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(schema = "app", name = "\"group\"")
public class Group {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "group_name", unique = true, nullable = false, length = 20)
    private String groupName;

    @OneToMany(mappedBy = "group")
    private Set<Membership> memberships = new HashSet<>();

    @OneToMany(mappedBy = "group")
    private Set<GroupMessage> messages;

    public Group() {
    }

    public Group(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public Integer getId() {
        return id;
    }

    public String getGroupName() {
        return groupName;
    }

    public Set<Membership> getMemberships() {
        return memberships;
    }

    public Set<GroupMessage> getMessages() {
        return messages;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setMemberships(Set<Membership> memberships) {
        this.memberships = memberships;
    }

    public void setMessages(Set<GroupMessage> messages) {
        this.messages = messages;
    }

    public boolean hasMember(User user) {
        return memberships.stream().map(Membership::getMember).collect(Collectors.toSet()).contains(user);
    }

    public boolean hasMember(String username) {
        return memberships.stream().map(e -> e.getMember().getUsername()).collect(Collectors.toSet())
                .contains(username);
    }
}
