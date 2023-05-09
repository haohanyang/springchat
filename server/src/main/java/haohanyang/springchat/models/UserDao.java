package haohanyang.springchat.models;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(schema = "app", name = "user")
public class UserDao {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Integer id;

    @Column(name = "username", unique = true, nullable = false, length = 20)
    private String username;

    @Column(name = "password", nullable = false, columnDefinition = "char(32)")
    private String password;

    @Column(name = "email", nullable = false, unique = true, length = 50)
    private String email;

    @Column(name = "first_name", nullable = false, length = 20)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 20)
    private String lastName;

    @OneToMany(mappedBy = "member")
    private Set<MembershipDao> memberships = new HashSet<>();

    @OneToMany(mappedBy = "sender")
    private Set<UserMessageDao> userMessagesSent;

    @OneToMany(mappedBy = "receiver")
    private Set<UserMessageDao> userMessagesReceived;

    @OneToMany(mappedBy = "sender")
    private Set<GroupMessageDao> groupMessagesSent;

    public UserDao() {
    }

    public UserDao(String username, String password, String email, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
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

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Set<MembershipDao> getMemberships() {
        return memberships;
    }

    public Set<UserMessageDao> getUserMessagesSent() {
        return userMessagesSent;
    }

    public Set<UserMessageDao> getUserMessagesReceived() {
        return userMessagesReceived;
    }

    public Set<GroupMessageDao> getGroupMessagesSent() {
        return groupMessagesSent;
    }

    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setMemberships(Set<MembershipDao> memberships) {
        this.memberships = memberships;
    }

    public void setGroupMessagesSent(Set<GroupMessageDao> groupMessagesSent) {
        this.groupMessagesSent = groupMessagesSent;
    }

    public void setUserMessagesReceived(Set<UserMessageDao> userMessagesReceived) {
        this.userMessagesReceived = userMessagesReceived;
    }

    public void setUserMessagesSent(Set<UserMessageDao> userMessagesSent) {
        this.userMessagesSent = userMessagesSent;
    }


    public boolean isMemberOf(GroupDao group) {
        return memberships.stream().map(MembershipDao::getGroup).collect(Collectors.toSet()).contains(group);
    }

    public boolean isMemberOf(String groupName) {
        return memberships.stream().map(e -> e.getGroup().getGroupName()).collect(Collectors.toSet())
                .contains(groupName);
    }

}