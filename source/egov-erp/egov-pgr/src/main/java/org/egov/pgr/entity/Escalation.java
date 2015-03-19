/**
 * 
 */
package org.egov.pgr.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.egov.infra.admin.master.entity.UserImpl;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.pims.commons.DesignationMaster;
import org.egov.search.domain.Searchable;
import org.egov.search.util.Serializer;
import org.joda.time.DateTime;
import org.json.simple.JSONObject;

/**
 * @author Vaibhav.K
 *
 */
@Entity
@Table(name = "pgr_escalation")
@Searchable
public class Escalation extends AbstractAuditable<UserImpl,Long> {

    private static final long serialVersionUID = -1317277378596990014L;
    
    @Valid
    @NotNull
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "complaint_type_id")
    @Searchable
    private ComplaintType complaintType;
    
    @Valid
    @NotNull
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "designation_id")
    @Searchable
    private DesignationMaster designation;    
    
    @Column(name = "no_of_hrs")
    private Integer noOfHrs;

    public ComplaintType getComplaintType() {
        return complaintType;
    }

    public void setComplaintType(ComplaintType complaintType) {
        this.complaintType = complaintType;
    }

    public DesignationMaster getDesignation() {
        return designation;
    }

    public void setDesignation(DesignationMaster designation) {
        this.designation = designation;
    }

    public Integer getNoOfHrs() {
        return noOfHrs;
    }

    public void setNoOfHrs(Integer noOfHrs) {
        this.noOfHrs = noOfHrs;
    }

    public JSONObject toJsonObject() {
        return Serializer.fromJson(Serializer.toJson(this), JSONObject.class);
    }
}
