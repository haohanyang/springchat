package haohanyang.springchat.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(schema = "app", name = "group_message")
public class GroupMessageDao {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private UserDao sender;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private GroupDao group;

    @Column(name = "sent_time")
    @Temporal(TemporalType.TIME)
    private Date sentTime;

    @Column(name = "content", length = 200)
    private String content;

    public GroupMessageDao() {
    }

    public GroupMessageDao(UserDao sender, GroupDao group, String content) {
        this.sender = sender;
        this.group = group;
        this.content = content;
        this.sentTime = new Date();
    }

    public Integer getId() {
        return id;
    }

    public GroupDao getGroup() {
        return group;
    }

    public UserDao getSender() {
        return sender;
    }

    public Date getSentTime() {
        return sentTime;
    }

    public void setSender(UserDao sender) {
        this.sender = sender;
    }

    public void setGroup(GroupDao group) {
        this.group = group;
    }

    public void setSentTime(Date sentTime) {
        this.sentTime = sentTime;
    }
}
