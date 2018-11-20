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

import static org.egov.eis.entity.EmployeeGrievance.SEQ_EMPLOYEEGRIEVANCE;

import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.egov.eis.entity.enums.EmployeeGrievanceStatus;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.pims.commons.Position;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Unique(fields = { "grievanceNumber" }, enableDfltMsg = true)
@Table(name = "egeis_grievance")
@SequenceGenerator(name = SEQ_EMPLOYEEGRIEVANCE, sequenceName = SEQ_EMPLOYEEGRIEVANCE, allocationSize = 1)
public class EmployeeGrievance extends StateAware<Position> {

    private static final long serialVersionUID = 9177150353790687499L;

    public static final String SEQ_EMPLOYEEGRIEVANCE = "SEQ_EGEIS_GRIEVANCE";

    @Id
    @GeneratedValue(generator = SEQ_EMPLOYEEGRIEVANCE, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank
    @Length(max = 50)
    @SafeHtml
    @Column(name = "grievanceNumber", unique = true)
    private String grievanceNumber;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "grievanceType", nullable = false)
    private EmployeeGrievanceType employeeGrievanceType;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "employee", nullable = false)
    private Employee employee = new Employee();

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    @JoinColumn(name = "status")
    private EmployeeGrievanceStatus status;

    @Length(min = 10, max = 500)
    @SafeHtml
    @NotNull
    private String details;

    @Length(max = 500)
    @SafeHtml
    private String grievanceResolution;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinTable(name = "egeis_grievancedocs", joinColumns = @JoinColumn(name = "grievanceid"), inverseJoinColumns = @JoinColumn(name = "filestoreid"))
    private Set<FileStoreMapper> grievanceDocs = Collections.emptySet();

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    @Override
    public String getStateDetails() {
        return "Employee Grievance Number : " + getGrievanceNumber();
    }

    public Set<FileStoreMapper> grievanceDocsOrderById() {
        return grievanceDocs
                .stream()
                .sorted(Comparator.comparing(FileStoreMapper::getId))
                .collect(Collectors.toSet());
    }

    @Override
    public String myLinkId() {
        return id.toString();
    }

    public String getGrievanceNumber() {
        return grievanceNumber;
    }

    public void setGrievanceNumber(String grievanceNumber) {
        this.grievanceNumber = grievanceNumber;
    }

    public EmployeeGrievanceType getEmployeeGrievanceType() {
        return employeeGrievanceType;
    }

    public void setEmployeeGrievanceType(EmployeeGrievanceType employeeGrievanceType) {
        this.employeeGrievanceType = employeeGrievanceType;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public EmployeeGrievanceStatus getStatus() {
        return status;
    }

    public void setStatus(EmployeeGrievanceStatus status) {
        this.status = status;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getGrievanceResolution() {
        return grievanceResolution;
    }

    public void setGrievanceResolution(String grievanceResolution) {
        this.grievanceResolution = grievanceResolution;
    }

    public Set<FileStoreMapper> getGrievanceDocs() {
        return grievanceDocs;
    }

    public void setGrievanceDocs(Set<FileStoreMapper> grievanceDocs) {
        this.grievanceDocs = grievanceDocs;
    }
}
