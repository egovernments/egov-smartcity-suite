package org.egov.infra.config.process.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.egov.infra.admin.master.entity.User;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "eg_group")
public class Group {

    @Id
    private Long id;
    
	
    @Length(max=20)
    private String name;
    
	
    @Length(max=20)
    private String type;
    @Version
    private Long version;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "eg_usergroup",
            joinColumns = @JoinColumn(name = "GROUP_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID")
    )
    private List<User> users;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(final List<User> users) {
        this.users = users;
    }

}
