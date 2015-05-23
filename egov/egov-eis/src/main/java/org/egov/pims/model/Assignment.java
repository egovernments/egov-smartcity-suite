/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.pims.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.eis.entity.Employee;
import org.egov.eis.entity.HeadOfDepartments;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.Position;
import org.springframework.context.annotation.Bean;

@Entity
@Table(name = "egeis_assignment")
public class Assignment extends AbstractAuditable<User, Long> {

    private static final long serialVersionUID = -2720951718725134740L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position")
    private Position position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "functionary")
    private Functionary functionary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fund")
    private Fund fund;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "function")
    private CFunction function;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "designation")
    private DesignationMaster designation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department")
    private Department department;

    @Column(name="isprimary")
    private boolean primary;

    private Date fromDate;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oldemployee")
    private PersonalInformation oldEmployee;

    private Date toDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade")
    private GradeMaster grade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee")
    private Employee employee;
    
    @Transient
    private List<Long> hodDeptIds = new ArrayList<Long>();

    @OneToMany(mappedBy = "assignment")
    private Set<HeadOfDepartments> deptSet = new HashSet<HeadOfDepartments>(0);

    public List<Long> getHodDeptIds() {
        for (final HeadOfDepartments dep : deptSet)
            hodDeptIds.add(dep.getHod().getId());
        return hodDeptIds;
    }

    public DesignationMaster getDesignation() {
        return designation;
    }

    public void setDesignation(final DesignationMaster designation) {
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

    public PersonalInformation getOldEmployee() {
        return oldEmployee;
    }

    public void setOldEmployee(final PersonalInformation employee) {
        oldEmployee = employee;
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

    public Set<HeadOfDepartments> getDeptSet() {
        return deptSet;
    }

    public void setDeptSet(final Set<HeadOfDepartments> deptSet) {
        this.deptSet = deptSet;
    }

}
