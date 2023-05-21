package haohanyang.springchat.models;

import haohanyang.springchat.dtos.UserDto;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "app", name = "user", indexes = @Index(name = "username_index", columnList = "username", unique = true))
public class User {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", unique = true, nullable = false, length = 20)
    private String username;

    @Column(name = "password", nullable = false, columnDefinition = "char(60)")
    private String password;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "first_name", nullable = false, length = 20)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 20)
    private String lastName;

    @Column(name = "avatar_url", length = 50)
    private String avatarUrl;

    @OneToMany(mappedBy = "member")
    private Set<Membership> memberships = new HashSet<>();

    @OneToMany(mappedBy = "sender")
    private Set<UserMessage> userMessagesSent;

    @OneToMany(mappedBy = "receiver")
    private Set<UserMessage> userMessagesReceived;

    @OneToMany(mappedBy = "sender")
    private Set<GroupMessage> groupMessagesSent;

    @OneToMany(mappedBy = "creator")
    private Set<Group> createdGroups = new HashSet<>();

    public User() {
    }

    public User(String username, String password, String email, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.avatarUrl = "https://api.dicebear.com/6.x/initials/svg?seed=" + firstName.charAt(0) + lastName.charAt(0);
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

    public String getAvatarUrl() {
        return avatarUrl;
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

    public String getEmail() {
        return email;
    }

    public Set<Group> getCreatedGroups() {
        return createdGroups;
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

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
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

    public void setCreatedGroups(Set<Group> createdGroups) {
        this.createdGroups = createdGroups;
    }

    public UserDto toDto() {
        return new UserDto(this.id, this.username, this.firstName + " " + this.lastName, this.email, this.avatarUrl);
    }


}