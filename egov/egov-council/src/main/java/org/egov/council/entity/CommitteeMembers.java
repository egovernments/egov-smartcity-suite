package org.egov.council.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.search.domain.Searchable;

@Entity
@Unique(id = "id", tableName = "egcncl_committee_members", enableDfltMsg = true)
@Table(name = "egcncl_committee_members")
@Searchable
@SequenceGenerator(name = CommitteeMembers.SEQ_COMMITTEE_MEMBERS, sequenceName = CommitteeMembers.SEQ_COMMITTEE_MEMBERS, allocationSize = 1)
public class CommitteeMembers extends AbstractAuditable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String SEQ_COMMITTEE_MEMBERS = "seq_egcncl_committee_members";

    @Id
    @GeneratedValue(generator = SEQ_COMMITTEE_MEMBERS, strategy = GenerationType.SEQUENCE)
    private Long id;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "committeeType", nullable = false)
    private CommitteeType committeeType;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "councilMember", nullable = false)
    private CouncilMember councilMember;
    
    @Transient
    private Boolean checked;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CommitteeType getCommitteeType() {
        return committeeType;
    }

    public void setCommitteeType(CommitteeType committeeType) {
        this.committeeType = committeeType;
    }

    public CouncilMember getCouncilMember() {
        return councilMember;
    }

    public void setCouncilMember(CouncilMember councilMember) {
        this.councilMember = councilMember;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    

    

}
