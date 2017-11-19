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

import org.egov.commons.EgwStatus;
import org.egov.commons.utils.EntityType;
import org.egov.eis.entity.enums.EmployeeStatus;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.persistence.entity.enums.UserType;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infra.validation.regex.Constants;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.hibernate.validator.constraints.SafeHtml;
import org.joda.time.DateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "egeis_employee")
@Unique(fields = { "code" }, enableDfltMsg = true)
@AuditOverrides({
        @AuditOverride(forClass = User.class, name = "name"),
        @AuditOverride(forClass = User.class, name = "mobileNumber"),
        @AuditOverride(forClass = User.class, name = "emailId")
})
public class Employee extends User implements EntityType {

    private static final long serialVersionUID = -1105585841211211215L;
    @NotNull
    @SafeHtml
    @Column(name = "code", unique = true)
    @Pattern(regexp = Constants.ALPHANUMERIC)
    @Audited
    private String code;

    @Temporal(value = TemporalType.DATE)
    @Audited
    private Date dateOfAppointment;

    @Temporal(value = TemporalType.DATE)
    @Audited
    private Date dateOfRetirement;

    @Enumerated(EnumType.STRING)
    @Audited
    private EmployeeStatus employeeStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeetype")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private EmployeeType employeeType;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy(" primary desc,toDate DESC ")
    @NotAudited
    private final List<Assignment> assignments = new ArrayList<Assignment>(0);

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id DESC ")
    @NotAudited
    private final List<Jurisdiction> jurisdictions = new ArrayList<Jurisdiction>(0);

    public Employee() {
        setType(UserType.EMPLOYEE);
    }

    @Override
    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public DateTime getDateOfAppointment() {
        return null == dateOfAppointment ? null : new DateTime(dateOfAppointment);
    }

    public void setDateOfAppointment(final DateTime dateOfAppointment) {
        this.dateOfAppointment = null == dateOfAppointment ? null : dateOfAppointment.toDate();
    }

    public DateTime getDateOfRetirement() {
        return null == dateOfRetirement ? null : new DateTime(dateOfRetirement);
    }

    public void setDateOfRetirement(final DateTime dateOfRetirement) {
        this.dateOfRetirement = null == dateOfRetirement ? null : dateOfRetirement.toDate();
    }

    public EmployeeStatus getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(final EmployeeStatus employeeStatus) {
        this.employeeStatus = employeeStatus;
    }

    public EmployeeType getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(final EmployeeType employeeType) {
        this.employeeType = employeeType;
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(final List<Assignment> assignments) {
        this.assignments.clear();
        if (assignments != null)
            this.assignments.addAll(assignments);
    }

    public List<Jurisdiction> getJurisdictions() {
        return jurisdictions;
    }

    public void setJurisdictions(final List<Jurisdiction> jurisdictions) {
        this.jurisdictions.clear();
        if (jurisdictions != null)
            this.jurisdictions.addAll(jurisdictions);
    }

    @Override
    public String getBankname() {
        return null;
    }

    @Override
    public String getBankaccount() {
        return null;
    }

    @Override
    public String getPanno() {
        return null;
    }

    @Override
    public String getTinno() {
        return null;
    }

    @Override
    public String getIfsccode() {
        return null;
    }

    @Override
    public String getModeofpay() {
        return null;
    }

    @Override
    public Integer getEntityId() {
        return getId().intValue();
    }

    @Override
    public String getEntityDescription() {
        return getName();
    }

    @Override
    public EgwStatus getEgwStatus() {
        return null;
    }

}
