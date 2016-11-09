package org.egov.council.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.hibernate.validator.constraints.Length;

@Entity
@Unique(id = "id", tableName = "egcncl_committeeType", fields = { "code", "name" }, columnName = { "code", "name" }, enableDfltMsg = true)
@Table(name = "egcncl_committeeType")
@SequenceGenerator(name = CommitteeType.SEQ_COMMITTEETYPE, sequenceName = CommitteeType.SEQ_COMMITTEETYPE, allocationSize = 1)
public class CommitteeType extends AbstractAuditable {

    private static final long serialVersionUID = 5920779296207990727L;

    public static final String SEQ_COMMITTEETYPE = "seq_egcncl_committeeType";

    @Id
    @GeneratedValue(generator = SEQ_COMMITTEETYPE, strategy = GenerationType.SEQUENCE)
    Long id;

    @NotNull
    @Length(min = 2, max = 100)
    private String name;

    @NotNull
    @Length(max = 20)
    @Column(name = "code", updatable = false)
    private String code;

    @NotNull
    private Boolean isActive;
    
    @OneToMany(mappedBy = "committeeType", cascade = CascadeType.ALL)
    private List<CommitteeMembers> commiteemembers= new ArrayList<CommitteeMembers>();
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public List<CommitteeMembers> getCommiteemembers() {
        return commiteemembers;
    }

    public void setCommiteemembers(List<CommitteeMembers> commiteemembers) {
        this.commiteemembers = commiteemembers;
    }
    
    public void deleteCommiteemembers(CommitteeMembers commiteemembers) {
        if(this.commiteemembers != null ){
            this.commiteemembers.remove(commiteemembers);
        }
    }
    
    public void addCommiteemembers(CommitteeMembers commiteemembers) {
        if(this.commiteemembers != null ){
            this.commiteemembers.add(commiteemembers);
        }
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CommitteeType other = (CommitteeType) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
    
    
    
}
