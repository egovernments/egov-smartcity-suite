/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
package org.egov.eis.entity;

import com.google.common.base.Objects;
import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.pims.model.GradeMaster;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.egov.eis.entity.Assignment.SEQ_ASSIGNMENT;

@Entity
@Table(name = "egeis_assignment")
@SequenceGenerator(name = SEQ_ASSIGNMENT, sequenceName = SEQ_ASSIGNMENT, allocationSize = 1)
@AuditOverrides({@AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedBy"),
        @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedDate")})
@NamedQuery(name = "getDesignationForActiveAssignmentsByListOfDesgNames", query = "select distinct A.designation from  Assignment A where A.fromDate<=current_date and A.toDate>=current_date and trim(upper(A.designation.name)) in(:param_0)")
public class Assignment extends AbstractAuditable {

    public static final String SEQ_ASSIGNMENT = "SEQ_EGEIS_ASSIGNMENT";
    private static final long serialVersionUID = -2720951718725134740L;
    @Id
    @GeneratedValue(generator = SEQ_ASSIGNMENT, strategy = GenerationType.SEQUENCE)
    @Audited
    private Long id;
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "position")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Position position;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "functionary")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Functionary functionary;
    @ManyToOne(fetch = FetchType.LAZY)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @JoinColumn(name = "fund")
    private Fund fund;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "function")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private CFunction function;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "designation")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Designation designation;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Department department;
    @Column(name = "isprimary")
    @Audited
    private boolean primary;
    @NotNull
    @Audited
    private Date fromDate;
    @NotNull
    @Audited
    private Date toDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private GradeMaster grade;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Employee employee;
    @OneToMany(mappedBy = "assignment", orphanRemoval = true, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<HeadOfDepartments> deptSet = new ArrayList<>(0);
    @Transient
    private List<HeadOfDepartments> hodList = new ArrayList<>(0);

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public List<HeadOfDepartments> getHodList() {
        return hodList;
    }

    public void setHodList(final List<HeadOfDepartments> hodLists) {
        hodList = hodLists;
    }

    public Designation getDesignation() {
        return designation;
    }

    public void setDesignation(final Designation designation) {
        this.designation = designation;
    }

    public Functionary getFunctionary() {
        return functionary;
    }

    public void setFunctionary(final Functionary functionary) {
        this.functionary = functionary;
    }

    public CFunction getFunction() {
        return function;
    }

    public void setFunction(final CFunction function) {
        this.function = function;
    }

    public Fund getFund() {
        return fund;
    }

    public void setFund(final Fund fund) {
        this.fund = fund;
    }

    public GradeMaster getGrade() {
        return grade;
    }

    public void setGrade(final GradeMaster gradeId) {
        grade = gradeId;
    }

    public boolean getPrimary() {
        return primary;
    }

    public void setPrimary(final boolean primary) {
        this.primary = primary;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(final Position position) {
        this.position = position;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(final Department department) {
        this.department = department;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(final Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(final Date toDate) {
        this.toDate = toDate;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(final Employee employee) {
        this.employee = employee;
    }

    public List<HeadOfDepartments> getDeptSet() {
        return deptSet;
    }

    public void setDeptSet(final List<HeadOfDepartments> deptSet) {
        this.deptSet.clear();
        if (deptSet != null)
            this.deptSet.addAll(deptSet);
    }

    public boolean isExpired() {
        return toDate.before(new Date());
    }

    @Override
    public boolean equals(Object o) {
        //Removing this will break assignment comparison across erp
        if (this == o)
            return true;
        if (!(o instanceof Assignment))
            return false;
        Assignment that = (Assignment) o;
        return Objects.equal(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
