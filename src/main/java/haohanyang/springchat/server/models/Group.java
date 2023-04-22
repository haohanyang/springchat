package haohanyang.springchat.server.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "app", name = "\"group\"")
public class Group {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "group_name", unique = true, nullable = false)
    private String groupName;

    @OneToMany(mappedBy = "group")
    private Set<Membership> memberships = new HashSet<>();

    public Group() {

    }

    public Group(String groupName) {
        this.groupName = groupName;
    }

    public Integer getId() {
        return id;
    }

    public String getGroupName() {
        return groupName;
    }

    public Set<Membership> getMemberships() {
        return memberships;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setMemberships(Set<Membership> memberships) {
        this.memberships = memberships;
    }
}

//@Entity
//@Table(name = "GROUP_")
//public class Group {
//    @Id
//    @Column(name = "ID_")
//    private String id;
//
//    @OneToMany(mappedBy = "group")
//    private Set<Membership> memberships = new HashSet<>();
//
//
//    public Group() {
//    }
//
//    public Group(String id) {
//        this.id = id;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public Set<Membership> getMemberships() {
//        return memberships;
//    }
//
//    public void setMemberships(Set<Membership> memberships) {
//        this.memberships = memberships;
//    }
//
//    @Override
//    public String toString() {
//        return getId();
//    }
//}