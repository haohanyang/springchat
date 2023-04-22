package haohanyang.springchat.server.models;

import jakarta.persistence.*;

import java.util.Date;


@Entity
@Table(schema = "app", name = "membership")
public class Membership {
    @EmbeddedId
    private MembershipId id;

    @ManyToOne
    @MapsId("member_id")
    @JoinColumn(name = "member_id")
    private User member;

    @ManyToOne
    @MapsId("group_id")
    @JoinColumn(name = "group_id")
    private Group group;

    @Column(name = "joined_time")
    @Temporal(TemporalType.TIME)
    private Date joinedTime = new Date();

    public Membership() {
    }

    public Membership(User member, Group group) {
        id = new MembershipId(member.getId(), group.getId());
        this.member = member;
        this.group = group;
    }


    public MembershipId getId() {
        return id;
    }

    public User getMember() {
        return member;
    }

    public Group getGroup() {
        return group;
    }

    public Date getJoinedTime() {
        return joinedTime;
    }

    public void setMember(User member) {
        this.member = member;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void setJoinedTime(Date joinedTime) {
        this.joinedTime = joinedTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (obj instanceof Membership) {
            return ((Membership) obj).getId().equals(this.id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}