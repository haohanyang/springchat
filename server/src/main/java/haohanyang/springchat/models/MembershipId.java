package haohanyang.springchat.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MembershipId implements Serializable {
    @Serial
    private static final long serialVersionUID = 9283737204L;

    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "group_id")
    private Integer groupId;

    public MembershipId() {
    }

    public MembershipId(Integer memberId, Integer groupId) {
        this.memberId = memberId;
        this.groupId = groupId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    @Override
    public int hashCode() {
        return memberId + groupId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (obj instanceof MembershipId) {
            return Objects.equals(((MembershipId) obj).memberId, this.memberId)
                    && Objects.equals(((MembershipId) obj).groupId, this.groupId);
        }
        return false;
    }
}
