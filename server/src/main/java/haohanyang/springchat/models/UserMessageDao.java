package haohanyang.springchat.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(schema = "app", name = "user_message")
public class UserMessageDao {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private UserDao sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private UserDao receiver;

    @Column(name = "sent_time")
    @Temporal(TemporalType.TIME)
    private Date sentTime;

    @Column(name = "content", length = 200)
    private String content;

    public UserMessageDao() {
    }

    public UserMessageDao(UserDao sender, UserDao receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.sentTime = new Date();
    }

    public Integer getId() {
        return id;
    }

    public Date getSentTime() {
        return sentTime;
    }

    public UserDao getSender() {
        return sender;
    }

    public UserDao getReceiver() {
        return receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSender(UserDao sender) {
        this.sender = sender;
    }

    public void setReceiver(UserDao receiver) {
        this.receiver = receiver;
    }

}
