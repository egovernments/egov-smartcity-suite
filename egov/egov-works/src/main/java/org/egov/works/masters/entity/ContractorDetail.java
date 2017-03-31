/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */
package org.egov.works.masters.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;

import org.egov.commons.ContractorGrade;
import org.egov.commons.EgwStatus;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.entity.component.Period;
import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.works.utils.WorksConstants;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "EGW_CONTRACTOR_DETAIL")
@SequenceGenerator(name = ContractorDetail.SEQ_EGW_CONTRACTOR_DETAIL, sequenceName = ContractorDetail.SEQ_EGW_CONTRACTOR_DETAIL, allocationSize = 1)
public class ContractorDetail extends AbstractAuditable {

    private static final long serialVersionUID = -3375445155375225162L;
    public static final String SEQ_EGW_CONTRACTOR_DETAIL = "SEQ_EGW_CONTRACTOR_DETAIL";

    @Id
    @GeneratedValue(generator = SEQ_EGW_CONTRACTOR_DETAIL, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONTRACTOR_ID", nullable = false)
    private Contractor contractor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPARTMENT_ID")
    @Required(message = "contractorDetails.department.required")
    private Department department;

    @Column(name = "REGISTRATION_NUMBER")
    @Length(max = 50, message = "contractorDetail.registrationNumber.length")
    @OptionalPattern(regex = WorksConstants.ALPHANUMERICWITHSPECIALCHAR, message = "contractorDetail.registrationNumber.alphaNumeric")
    private String registrationNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STATUS_ID")
    @Required(message = "contractorDetails.status.required")
    private EgwStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONTRACTOR_GRADE_ID")
    private ContractorGrade grade;

    @Valid
    private Period validity;

    private String category;

    public Contractor getContractor() {
        return contractor;
    }

    public void setContractor(final Contractor contractor) {
        this.contractor = contractor;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(final Department department) {
        this.department = department;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(final String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public EgwStatus getStatus() {
        return status;
    }

    public void setStatus(final EgwStatus status) {
        this.status = status;
    }

    public ContractorGrade getGrade() {
        return grade;
    }

    public void setGrade(final ContractorGrade grade) {
        this.grade = grade;
    }

    public Period getValidity() {
        return validity;
    }

    public void setValidity(final Period validity) {
        this.validity = validity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(final String category) {
        this.category = category;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

}