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
package org.egov.works.web.controller.masters;

import org.apache.commons.lang3.StringUtils;
import org.egov.infra.persistence.entity.component.Period;
import org.egov.infra.validation.regex.Constants;
import org.egov.works.masters.entity.Contractor;
import org.egov.works.masters.entity.ContractorDetail;
import org.egov.works.masters.service.ContractorService;
import org.egov.works.utils.WorksConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

@Controller
public abstract class BaseContractorController {

    protected static final String CONTRACTOR = "contractor";
    private static final String TEMPCONTRACTORDETAILS = "tempContractorDetails[";

    @Autowired
    private ContractorService contractorService;

    protected void validateContractor(final Contractor contractor, final BindingResult resultBinder) {
        final String[] contractorMasterMandatoryFields = contractorService.getContractorMasterMandatoryFields();
        final Long contractorId = contractor.getId();
        if (contractorId != null) {
            final Contractor existingContractor = contractorService.getContractorById(contractorId);
            if (existingContractor != null && !existingContractor.getId().equals(contractorId))
                resultBinder.reject("error.contractor.exists", "error.contractor.exists");
        }
        checkValidationForNameAndPatternValidations(contractor, resultBinder);
        checkValidationForPatterns(contractor, resultBinder);
        checkValidationForCode(contractor, resultBinder);
        checkValidationForConfigurableContractorMasterMandatoryFields(contractor, resultBinder,
                contractorMasterMandatoryFields);
    }

    private void checkValidationForNameAndPatternValidations(final Contractor contractor,
            final BindingResult resultBinder) {
        final String contractorName = contractor.getName();
        if (contractorName == null)
            resultBinder.reject("error.contractor.name", "error.contractor.name");
        if (contractorName != null && !contractorName.matches(WorksConstants.ALPHANUMERICWITHSPECIALCHAR))
            resultBinder.reject("error.name.invalid", "error.name.invalid");
        if (contractor.getPwdApprovalCode() != null
                && !contractor.getPwdApprovalCode().matches(WorksConstants.ALPHANUMERICWITHHYPHENSLASH))
            resultBinder.reject("error.contractor.pwdapprovalcode.invalid", "error.contractor.pwdapprovalcode.invalid");
        if (contractor.getIfscCode() != null && !contractor.getIfscCode().matches(Constants.ALPHANUMERIC))
            resultBinder.reject("error.contractor.ifsccode.alphanumeric", "error.contractor.ifsccode.alphanumeric");
    }

    private void checkValidationForPatterns(final Contractor contractor, final BindingResult resultBinder) {
        if (contractor.getPanNumber() != null && !contractor.getPanNumber().matches(Constants.PANNUMBER))
            resultBinder.reject("error.contractor.pannumber.alphanumeric", "error.contractor.pannumber.alphanumeric");
        if (contractor.getTinNumber() != null && !contractor.getTinNumber().matches(Constants.ALPHANUMERIC))
            resultBinder.reject("error.contractor.tinnumber.alphanumeric", "error.contractor.tinnumber.alphanumeric");
        if (contractor.getBankAccount() != null && !contractor.getBankAccount().matches(Constants.ALPHANUMERIC))
            resultBinder.reject("error.contractor.bankaccount.alphanumeric",
                    "error.contractor.bankaccount.alphanumeric");
        checkPatternValidationForContractPersonAndEmailAndMobileNumber(contractor, resultBinder);
    }

    private void checkPatternValidationForContractPersonAndEmailAndMobileNumber(final Contractor contractor,
            final BindingResult resultBinder) {
        if (contractor.getContactPerson() != null
                && !contractor.getContactPerson().matches(Constants.ALPHANUMERIC_WITHSPACE))
            resultBinder.reject("error.contractor.contactperson.alphanumeric",
                    "error.contractor.contactperson.alphanumeric");
        if (contractor.getEmail() != null && !contractor.getEmail().matches(Constants.EMAIL))
            resultBinder.reject("error.contractor.email.invalid", "error.contractor.email.invalid");
        if (contractor.getMobileNumber() != null && contractor.getMobileNumber().length() != 10)
            resultBinder.reject("error.contractor.mobileno.length", "error.contractor.mobileno.length");
    }

    private void checkValidationForCode(final Contractor contractor, final BindingResult resultBinder) {
        final String codeAutoGeneration = contractorService.getContractorMasterAutoCodeGenerateValue();
        if (!WorksConstants.YES.equalsIgnoreCase(codeAutoGeneration.toLowerCase()) && contractor.getCode() == null)
            resultBinder.reject("error.contractor.code", "error.contractor.code");

        if (!WorksConstants.YES.equalsIgnoreCase(codeAutoGeneration.toLowerCase()) && contractor.getCode() != null
                && !contractor.getCode().matches(WorksConstants.ALPHANUMERICWITHHYPHENSLASH))
            resultBinder.reject("error.contractor.code.invalid", "error.contractor.code.invalid");

        if (WorksConstants.YES.equalsIgnoreCase(codeAutoGeneration.toLowerCase()) && contractor.getName() != null
                && contractor.getName().length() < 4)
            resultBinder.reject("error.contractor.name.length", "error.contractor.name.length");
    }

    private void checkValidationForConfigurableContractorMasterMandatoryFields(final Contractor contractor,
            final BindingResult resultBinder, final String[] contractorMasterMandatoryFields) {
        for (final String val : contractorMasterMandatoryFields) {
            if ("exemptionForm".equals(val) && StringUtils.isBlank(contractor.getExemptionForm().toString()))
                resultBinder.reject("error.contractor.exemptionform", "error.contractor.exemptionform");
            if ("pwdApprovalCode".equals(val) && StringUtils.isBlank(contractor.getPwdApprovalCode()))
                resultBinder.reject("error.contractor.pwdapprovalcode", "error.contractor.pwdapprovalcode");
            checkValidationForContactPersonAndNarrationAndEmailAndMobileNumber(contractor, resultBinder, val);
            checkValidationForBank(contractor, resultBinder, val);
            checkValidationForPanAndTinNumberAndAdress(contractor, resultBinder, val);
        }
    }

    private void checkValidationForContactPersonAndNarrationAndEmailAndMobileNumber(final Contractor contractor,
            final BindingResult resultBinder, final String val) {
        if ("contactPerson".equals(val) && StringUtils.isBlank(contractor.getContactPerson()))
            resultBinder.reject("error.contractor.contactperson", "error.contractor.contactperson");
        if ("narration".equals(val) && StringUtils.isBlank(contractor.getNarration()))
            resultBinder.reject("error.contractor.narration", "error.contractor.narration");
        if ("email".equals(val) && StringUtils.isBlank(contractor.getEmail()))
            resultBinder.reject("error.contractor.email", "error.contractor.email");

        if ("mobileNumber".equals(val) && StringUtils.isBlank(contractor.getMobileNumber()))
            resultBinder.reject("error.contractor.mobileno", "error.contractor.mobileno");
    }

    private void checkValidationForBank(final Contractor contractor, final BindingResult resultBinder,
            final String val) {
        if ("bankAccount".equals(val) && StringUtils.isBlank(contractor.getBankAccount()))
            resultBinder.reject("error.contractor.bankaccount", "error.contractor.bankaccount");
        if ("bank".equals(val) && contractor.getBank() == null)
            resultBinder.reject("error.bank.name", "error.bank.name");
        if ("ifscCode".equals(val) && StringUtils.isBlank(contractor.getIfscCode()))
            resultBinder.reject("error.contractor.ifsccode", "error.contractor.ifsccode");
    }

    private void checkValidationForPanAndTinNumberAndAdress(final Contractor contractor,
            final BindingResult resultBinder, final String val) {
        if ("tinNumber".equals(val) && StringUtils.isBlank(contractor.getTinNumber()))
            resultBinder.reject("error.contractor.tinnumber", "error.contractor.tinnumber");
        if ("panNumber".equals(val) && StringUtils.isBlank(contractor.getPanNumber()))
            resultBinder.reject("error.contractor.pannumber", "error.contractor.pannumber");
        if ("correspondenceAddress".equals(val) && StringUtils.isBlank(contractor.getCorrespondenceAddress()))
            resultBinder.reject("error.contractor.correspondenceaddress", "error.contractor.correspondenceaddress");
        if ("paymentAddress".equals(val) && StringUtils.isBlank(contractor.getPaymentAddress()))
            resultBinder.reject("error.contractor.paymentaddress", "error.contractor.paymentaddress");

    }

    protected void validateContractorDetails(final Contractor contractor, final BindingResult resultBinder) {
        int index = 0;
        if (contractor.getTempContractorDetails() == null)
            resultBinder.reject("error.contractor.details.altleastone_details_needed",
                    "error.contractor.details.altleastone_details_needed");
        else
            for (final ContractorDetail contractorDetail : contractor.getTempContractorDetails()) {
                if (contractorDetail.getDepartment() == null)
                    resultBinder.rejectValue(TEMPCONTRACTORDETAILS + index + "].department",
                            "error.contractordetail.department");
                if (contractorDetail.getStatus() == null)
                    resultBinder.rejectValue(TEMPCONTRACTORDETAILS + index + "].status",
                            "error.contractordetail.status");
                if (contractorDetail.getValidity().getStartDate() == null)
                    resultBinder.rejectValue(TEMPCONTRACTORDETAILS + index + "].validity.startDate",
                            "error.contractordetail.fromDate");
                compareStartDateAndEndDate(resultBinder, contractorDetail);
                checkValidationForConfigurableContractorDetailsMandatoryFields(contractorDetail, resultBinder, index);
                index++;
            }
    }

    private void compareStartDateAndEndDate(final BindingResult resultBinder, final ContractorDetail contractorDetail) {
        final Period validity = contractorDetail.getValidity();
        if (validity != null && validity.getStartDate() != null && validity.getEndDate() != null
                && validity.getStartDate().compareTo(validity.getEndDate()) > 0)
            resultBinder.reject("error.fromdate.cannot.be.grater.then.todate",
                    "error.fromdate.cannot.be.grater.then.todate");
    }

    private void checkValidationForConfigurableContractorDetailsMandatoryFields(final ContractorDetail contractorDetail,
            final BindingResult resultBinder, final int index) {
        final String[] contractorMasterMandatoryFields = contractorService.getContractorMasterMandatoryFields();
        for (final String val : contractorMasterMandatoryFields) {
            if ("registrationNumber".equals(val) && StringUtils.isBlank(contractorDetail.getRegistrationNumber()))
                resultBinder.rejectValue(TEMPCONTRACTORDETAILS + index + "].registrationNumber",
                        "error.contractordetail.registrationnumber.required");
            if ("grade".equals(val)
                    && (contractorDetail.getGrade() == null || contractorDetail.getGrade().getId() == null))
                resultBinder.rejectValue(TEMPCONTRACTORDETAILS + index + "].grade",
                        "error.contractordetail.grade.required");
            if ("category".equals(val) && StringUtils.isBlank(contractorDetail.getCategory()))
                resultBinder.rejectValue(TEMPCONTRACTORDETAILS + index + "].category",
                        "error.contractordetail.category.required");
        }
    }

}
