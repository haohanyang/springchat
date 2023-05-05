package haohanyang.springchat.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(schema = "app", name = "user_message")
public class UserMessage {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @Column(name = "sent_time")
    @Temporal(TemporalType.TIME)
    private Date sentTime;

    @Column(name = "content", length = 200)
    private String content;

    public UserMessage() {
    }

    public UserMessage(User sender, User receiver, String content) {
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

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

}
