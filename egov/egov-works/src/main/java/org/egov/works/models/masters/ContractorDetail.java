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
package org.egov.works.models.masters;

import org.egov.commons.ContractorGrade;
import org.egov.commons.EgwStatus;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.persistence.entity.component.Period;
import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infstr.models.BaseModel;
import org.egov.works.utils.WorksConstants;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

public class ContractorDetail extends BaseModel {

    /**
     *
     */
    private static final long serialVersionUID = -3375445155375225162L;

    private Contractor contractor;

    private Department department;

    @Length(max = 50, message = "contractorDetail.registrationNumber.length")
    @OptionalPattern(regex = WorksConstants.alphaNumericwithspecialchar, message = "contractorDetail.registrationNumber.alphaNumeric")
    private String registrationNumber;

    private EgwStatus status;

    private ContractorGrade grade;

    private List<ValidationError> errorList;
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

    public List<ValidationError> getErrorList() {
        if (errorList != null)
            return errorList;
        else
            return new ArrayList<ValidationError>();
    }

    public void setErrorList(final List<ValidationError> errorList) {
        this.errorList = errorList;
    }
    
    
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public List<ValidationError> validate() {
        final List<ValidationError> validationErrors = getErrorList();
        if (department == null || department.getId() == null)
            validationErrors.add(new ValidationError("department", "contractorDetails.department.required"));
        if (status == null || status.getId() == null)
            validationErrors.add(new ValidationError("status", "contractorDetails.status.required"));
        if (validity == null || validity != null && validity.getStartDate() == null)
            validationErrors.add(new ValidationError("validity", "contractorDetails.fromDate_empty"));
        else if (validity == null || validity != null && !compareDates(validity.getStartDate(), validity.getEndDate()))
            validationErrors.add(new ValidationError("validity", "contractorDetails.invalid_fromdate_range"));
        
        if (validationErrors.isEmpty())
            return null;
        else
            return validationErrors;
    }

    public static boolean compareDates(final java.util.Date startDate, final java.util.Date endDate) {
        if (startDate == null)
            return false;

        if (endDate == null)
            return true;

        if (endDate.before(startDate))
            return false;
        return true;
    }

}
