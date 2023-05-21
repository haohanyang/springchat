package haohanyang.springchat.models;

import jakarta.persistence.*;

import java.sql.Timestamp;


@Entity
@Table(schema = "app", name = "group_message")
public class GroupMessage {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Group group;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "content", length = 255)
    private String content;

    public GroupMessage() {
    }

    public GroupMessage(User sender, Group group, String content) {
        this.sender = sender;
        this.group = group;
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public Group getGroup() {
        return group;
    }

    public User getSender() {
        return sender;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj == this) {
            return true;
        } else if (obj instanceof GroupMessage other) {
            return this.id.equals(other.id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
