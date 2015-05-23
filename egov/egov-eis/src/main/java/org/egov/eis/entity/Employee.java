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
package org.egov.eis.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.egov.eis.entity.enums.EmployeeStatus;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.validation.regex.Constants;
import org.egov.search.domain.Searchable;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Table(name = "egeis_employee")
@DiscriminatorValue("EMPLOYEE")
public class Employee extends User {

    private static final long serialVersionUID = -1105585841211211215L;
    
    @NotNull
    @SafeHtml
    @Column(name = "code", unique = true)
    @Pattern(regexp = Constants.ALPHANUMERIC)
    private String code;
    
    @NotNull
    @SafeHtml
    private LocalDate dateOfAppointment;
    
    @NotNull
    @SafeHtml
    private LocalDate dateOfRetirement;
    
    @SafeHtml
    @Column(name = "pannumber", unique = true)
    @Pattern(regexp = Constants.ALPHANUMERIC)
    private String panNumber;
    
    @Enumerated
    @NotNull
    @Searchable(group = Searchable.Group.CLAUSES)
    private EmployeeStatus employeeStatus;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeetype")
    private EmployeeType employeeType;
    
    @OneToMany(mappedBy = "employee",orphanRemoval=true,fetch=FetchType.LAZY,cascade = CascadeType.ALL)
    private final Set<Assignment> assignments = new HashSet<Assignment>(0);

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public LocalDate getDateOfAppointment() {
        return dateOfAppointment;
    }

    public void setDateOfAppointment(final LocalDate dateOfAppointment) {
        this.dateOfAppointment = dateOfAppointment;
    }

    public LocalDate getDateOfRetirement() {
        return dateOfRetirement;
    }

    public void setDateOfRetirement(final LocalDate dateOfRetirement) {
        this.dateOfRetirement = dateOfRetirement;
    }

}
