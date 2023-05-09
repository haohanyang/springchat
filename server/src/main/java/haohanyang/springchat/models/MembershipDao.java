package haohanyang.springchat.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(schema = "app", name = "membership")
public class MembershipDao {
    @EmbeddedId
    private MembershipId id;

    @ManyToOne
    @MapsId("member_id")
    @JoinColumn(name = "member_id")
    private UserDao member;

    @ManyToOne
    @MapsId("group_id")
    @JoinColumn(name = "group_id")
    private GroupDao group;

    @Column(name = "joined_time")
    @Temporal(TemporalType.TIME)
    private Date joinedTime = new Date();

    public MembershipDao() {
    }

    public MembershipDao(UserDao member, GroupDao group) {
        id = new MembershipId(member.getId(), group.getId());
        this.member = member;
        this.group = group;
    }

    public MembershipId getId() {
        return id;
    }

    public UserDao getMember() {
        return member;
    }

    public GroupDao getGroup() {
        return group;
    }

    public Date getJoinedTime() {
        return joinedTime;
    }

    public void setMember(UserDao member) {
        this.member = member;
    }

    public void setGroup(GroupDao group) {
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
        if (obj instanceof MembershipDao) {
            return ((MembershipDao) obj).getId().equals(this.id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}