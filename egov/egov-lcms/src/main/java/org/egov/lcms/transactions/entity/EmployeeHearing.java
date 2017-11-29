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
package org.egov.lcms.transactions.entity;

import com.google.gson.annotations.Expose;
import org.egov.eis.entity.Employee;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;

import static org.egov.lcms.transactions.entity.EmployeeHearing.SEQ_EGLC_EMPHEARING;

@Entity
@Table(name = "eglc_employeehearing")
@SequenceGenerator(name = SEQ_EGLC_EMPHEARING, sequenceName = SEQ_EGLC_EMPHEARING, allocationSize = 1)
@AuditOverrides({@AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedBy"),
        @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedDate")})
public class EmployeeHearing extends AbstractAuditable {

    public static final String SEQ_EGLC_EMPHEARING = "seq_eglc_employeehearing";
    private static final long serialVersionUID = 1517694643078084884L;
    @Expose
    @Id
    @GeneratedValue(generator = SEQ_EGLC_EMPHEARING, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @Valid
    @JoinColumn(name = "employee")
    @Audited
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @Valid
    @JoinColumn(name = "hearing")
    @Audited
    private Hearings hearing;

    @Transient
    private String empPosName;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(final Employee employee) {
        this.employee = employee;
    }

    public Hearings getHearing() {
        return hearing;
    }

    public void setHearing(final Hearings hearing) {
        this.hearing = hearing;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getEmpPosName() {
        return empPosName;
    }

    public void setEmpPosName(final String empPosName) {
        this.empPosName = empPosName;
    }

}