package haohanyang.springchat.models;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(schema = "app", name = "membership")
public class Membership {
    private static final long serialVersionUID = 1L;
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

    @Column(name = "created_at")
    private Timestamp createdAt;

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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setMember(User member) {
        this.member = member;
    }

    public void setGroup(Group group) {
        this.group = group;
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