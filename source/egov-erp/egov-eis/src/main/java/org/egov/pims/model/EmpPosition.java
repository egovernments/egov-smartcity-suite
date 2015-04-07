package org.egov.pims.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.egov.commons.EgwStatus;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.admin.master.entity.Department;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.Position;

@Entity
@Table(name="EGEIS_POST_CREATION")
public class EmpPosition extends StateAware {

    private static final long serialVersionUID = 9220002621595085170L;

    @NotNull(message = "postname.required")
    @Column(name="POST_NAME",nullable=false)
    private String postName;
    
    @NotNull(message = "desig.required")
    @ManyToOne(fetch=FetchType.LAZY,optional=false)
    @JoinColumn(name="DESIG_ID")
    private DesignationMaster desigId;
    
    @NotNull(message = "dept.required")
    @ManyToOne(fetch=FetchType.LAZY,optional=false)
    @JoinColumn(name="DEPT_ID")
    private Department deptId;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="STATUS")
    private EgwStatus status;
    
    @Column(name="QUALIFY_DETAILS")
    private String qualificationDetails;
    
    private String remarks;
    
    @ManyToOne(fetch=FetchType.LAZY,cascade=CascadeType.PERSIST)
    @JoinColumn(name="POSITION_ID")
    private Position position;

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public DesignationMaster getDesigId() {
        return desigId;
    }

    public void setDesigId(DesignationMaster desigId) {
        this.desigId = desigId;
    }

    public Department getDeptId() {
        return deptId;
    }

    public void setDeptId(Department deptId) {
        this.deptId = deptId;
    }

    public EgwStatus getStatus() {
        return status;
    }

    public void setStatus(EgwStatus status) {
        this.status = status;
    }

    public String getQualificationDetails() {
        return qualificationDetails;
    }

    public void setQualificationDetails(String qualificationDetails) {
        this.qualificationDetails = qualificationDetails;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public String getStateDetails() {

        return "" + getDeptId().getName() + "-" + getDesigId().getDesignationName() + "-" + getPostName();
    }

}
