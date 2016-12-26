package org.egov.council.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

@Entity
@Unique(id = "id", tableName = "egcncl_committee_members", enableDfltMsg = true)
@Table(name = "egcncl_committee_members")
@SequenceGenerator(name = CommitteeMembers.SEQ_COMMITTEE_MEMBERS, sequenceName = CommitteeMembers.SEQ_COMMITTEE_MEMBERS, allocationSize = 1)
@AuditOverrides({ @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedBy"),
    @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedDate") })
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
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private CommitteeType committeeType;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "councilMember", nullable = false)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
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
