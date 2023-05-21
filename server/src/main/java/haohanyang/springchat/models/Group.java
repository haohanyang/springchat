package haohanyang.springchat.models;

import haohanyang.springchat.dtos.GroupDto;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(schema = "app", name = "group")
public class Group {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, length = 20)
    private String Name;

    @OneToMany(mappedBy = "group")
    private Set<Membership> memberships = new HashSet<>();

    @OneToMany(mappedBy = "group")
    private Set<GroupMessage> messages;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "avatar_url", length = 50)
    private String avatarUrl;

    @ManyToOne
    private User creator;

    public Group() {
    }

    public Group(User creator, String groupName) {
        this.creator = creator;
        this.Name = groupName;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return Name;
    }

    public Set<Membership> getMemberships() {
        return memberships;
    }

    public Set<GroupMessage> getMessages() {
        return messages;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public User getCreator() {
        return creator;
    }

    public void setName(String groupName) {
        this.Name = groupName;
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

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        else if (obj == this) return true;
        else if (obj instanceof Group) {
            return ((Group) obj).getId().equals(this.getId());
        }
        return false;
    }

    public GroupDto toDto() {
        return new GroupDto(
                getId(),
                getName(),
                getAvatarUrl(),
                getCreator().toDto(),
                getMemberships().stream().map(Membership::getMember).map(User::toDto).collect(Collectors.toSet())
        );
    }
}
