package haohanyang.springchat.server.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(schema = "app", name = "group_message")
public class GroupMessage {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Column(name = "sent_time")
    @Temporal(TemporalType.TIME)
    private Date sentTime;

    @Column(name = "content", length = 200)
    private String content;

    public GroupMessage() {
    }

    public GroupMessage(User sender, Group group, String content) {
        this.sender = sender;
        this.group = group;
        this.content = content;
        this.sentTime = new Date();
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

    public Date getSentTime() {
        return sentTime;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void setSentTime(Date sentTime) {
        this.sentTime = sentTime;
    }
}
