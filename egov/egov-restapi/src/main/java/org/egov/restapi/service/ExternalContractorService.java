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
package org.egov.restapi.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.egov.commons.dao.BankHibernateDAO;
import org.egov.ptis.domain.model.ErrorDetails;
import org.egov.restapi.constants.RestApiConstants;
import org.egov.restapi.model.ContractorHelper;
import org.egov.works.master.service.ContractorService;
import org.egov.works.models.masters.Contractor;
import org.egov.works.models.masters.ExemptionForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ExternalContractorService {

    protected static final String ALPHANUMERICWITHSPECIALCHAR = "[0-9a-zA-Z-& :,/.()@]+";

    protected static final String ALPHANUMERICWITHALLSPECIALCHAR = "[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+";

    protected static final String EMAIL = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

    protected static final String ALPHANUMERIC_WITHSPACE = "[0-9a-zA-Z ]+";

    protected static final String WORKS_CONTRACTOR_STATUS = "Contractor";

    protected static final String PANNUMBER = "[a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}";

    protected static final String ALPHANUMERIC = "[0-9a-zA-Z]+";

    protected static final String MOBILE_NUM = "^((\\+)?(\\d{2}[-]))?(\\d{10}){1}?$";

    @Autowired
    private ContractorService contractorService;

    @Autowired
    private BankHibernateDAO bankHibernateDAO;

    public ContractorHelper populateContractorData(final Contractor contractor) {
        final ContractorHelper cont = new ContractorHelper();
        cont.setCode(contractor.getCode());
        cont.setName(contractor.getName());
        cont.setBankName(contractor.getBank() != null ? contractor.getBank().getName() : StringUtils.EMPTY);
        cont.setBankAccount(contractor.getBankAccount());
        cont.setContactPerson(contractor.getContactPerson());
        cont.setCorrespondenceAddress(contractor.getCorrespondenceAddress());
        cont.setPaymentAddress(contractor.getPaymentAddress());
        cont.setEmail(contractor.getEmail());
        cont.setExemptionName(
                contractor.getExemptionForm() != null ? contractor.getExemptionForm().toString() : StringUtils.EMPTY);
        cont.setIfscCode(contractor.getIfscCode());
        cont.setPanNumber(contractor.getPanNumber());
        cont.setTinNumber(contractor.getTinNumber());
        cont.setPwdApprovalCode(contractor.getPwdApprovalCode());
        cont.setNarration(contractor.getNarration());
        cont.setMobileNumber(contractor.getMobileNumber());

        if (!contractor.getContractorDetails().isEmpty()) {
            cont.setContractorCategory(StringUtils.isNotBlank(contractor.getContractorDetails().get(0).getCategory())
                    ? contractor.getContractorDetails().get(0).getCategory()
                    : StringUtils.EMPTY);
            cont.setContractorClass(
                    contractor.getContractorDetails().get(0).getGrade() != null
                            ? contractor.getContractorDetails().get(0).getGrade().getGrade() : StringUtils.EMPTY);
            cont.setStatus(contractor.getContractorDetails().get(0).getStatus().getCode());
        }
        return cont;

    }

    public List<ErrorDetails> validateContactorToCreate(final ContractorHelper contractorHelper) {
        List<ErrorDetails> errors = new ArrayList<ErrorDetails>();
        ErrorDetails errorDetails = null;
        errors = validateMandatoryFields(contractorHelper, errors);
        if (contractorHelper.getCode() != null) {
            final Contractor existingContractor = contractorService.getContractorByCode(contractorHelper.getCode());
            if (existingContractor != null) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_EXIST_CONTRACTOR);
                errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_EXIST_CONTRACTOR);
                errors.add(errorDetails);
            }
        }

        return errors;
    }

    private List<ErrorDetails> validateMandatoryFields(final ContractorHelper contractorHelper, List<ErrorDetails> errors) {
        ErrorDetails errorDetails = null;
        if (StringUtils.isBlank(contractorHelper.getCode())) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CONTRACTOR_NO_CODE);
            errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_NO_CONTRACTOR_CODE);
            errors.add(errorDetails);
        }
        if (StringUtils.isBlank(contractorHelper.getName())) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CONTRACTOR_NO_NAME);
            errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_NO_CONTRACTOR_NAME);
            errors.add(errorDetails);
        }
        if (contractorHelper.getCode() != null && !contractorHelper.getCode().matches(ALPHANUMERICWITHSPECIALCHAR)) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CONTRACTOR_CODE_SPECIAL);
            errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_CONTRACTOR_CODE_SPECIAL);
            errors.add(errorDetails);
        }
        if (contractorHelper.getName() != null && !contractorHelper.getName().matches(ALPHANUMERICWITHSPECIALCHAR)) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CONTRACTOR_NAME_SPECIAL);
            errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_CONTRACTOR_NAME_SPECIAL);
            errors.add(errorDetails);
        }
        if (contractorHelper.getCode() != null && contractorHelper.getCode().length() > 50) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CONTRACTOR_CODE_MAXLENGTH);
            errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_CONTRACTOR_CODE_MAXLENGTH);
            errors.add(errorDetails);
        }
        if (contractorHelper.getName() != null && contractorHelper.getName().length() > 100) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CONTRACTOR_NAME_MAXLENGTH);
            errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_CONTRACTOR_NAME_MAXLENGTH);
            errors.add(errorDetails);
        }
        errors = validateNonMandatorFields(contractorHelper, errors, errorDetails);

        return errors;
    }

    private List<ErrorDetails> validateNonMandatorFields(final ContractorHelper contractorHelper, final List<ErrorDetails> errors,
            ErrorDetails errorDetails) {
        if (StringUtils.isNotBlank(contractorHelper.getCorrespondenceAddress())
                && !contractorHelper.getCorrespondenceAddress().matches(ALPHANUMERICWITHALLSPECIALCHAR)) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CONTRACTOR_CORRESPONDENCEADDRESS_INVALID);
            errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_CONTRACTOR_CORRESPONDENCEADDRESS_INVALID);
            errors.add(errorDetails);
        }
        if (StringUtils.isNotBlank(contractorHelper.getPaymentAddress())
                && !contractorHelper.getPaymentAddress().matches(ALPHANUMERICWITHALLSPECIALCHAR)) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CONTRACTOR_PAYMENTADDRESS_INVALID);
            errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_CONTRACTOR_PAYMENTADDRESS_INVALID);
            errors.add(errorDetails);
        }
        if (StringUtils.isNotBlank(contractorHelper.getNarration())
                && !contractorHelper.getNarration().matches(ALPHANUMERICWITHALLSPECIALCHAR)) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CONTRACTOR_NARRATION_INVALID);
            errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_CONTRACTOR_NARRATION_INVALID);
            errors.add(errorDetails);
        }
        if (StringUtils.isNotBlank(contractorHelper.getContactPerson())
                && !contractorHelper.getContactPerson().matches(ALPHANUMERIC_WITHSPACE)) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CONTRACTOR_CONTACTPERSON_INVALID);
            errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_CONTRACTOR_CONTACTPERSON_INVALID);
            errors.add(errorDetails);
        }
        if (StringUtils.isNotBlank(contractorHelper.getEmail()) && !contractorHelper.getEmail().matches(EMAIL)) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CONTRACTOR_EMAIL_INVALID);
            errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_CONTRACTOR_EMAIL_INVALID);
            errors.add(errorDetails);
        }
        if (StringUtils.isNotBlank(contractorHelper.getPanNumber())
                && !contractorHelper.getPanNumber().matches(PANNUMBER)) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CONTRACTOR_PANNUMBER_INVALID);
            errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_CONTRACTOR_PANNUMBER_INVALID);
            errors.add(errorDetails);
        }
        if (StringUtils.isNotBlank(contractorHelper.getTinNumber())
                && !contractorHelper.getTinNumber().matches(ALPHANUMERIC)) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CONTRACTOR_TINNUMBER_INVALID);
            errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_CONTRACTOR_TINNUMBER_INVALID);
            errors.add(errorDetails);
        }
        if (StringUtils.isNotBlank(contractorHelper.getIfscCode())
                && !contractorHelper.getIfscCode().matches(ALPHANUMERIC)) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CONTRACTOR_IFSCCODE_INVALID);
            errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_CONTRACTOR_IFSCCODE_INVALID);
        }
        if (StringUtils.isNotBlank(contractorHelper.getBankAccount())
                && !contractorHelper.getBankAccount().matches(ALPHANUMERIC)) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CONTRACTOR_BANKACCOUNT_INVALID);
            errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_CONTRACTOR_BANKACCOUNT_INVALID);
            errors.add(errorDetails);
        }
        if (StringUtils.isNotBlank(contractorHelper.getPwdApprovalCode())
                && !contractorHelper.getPwdApprovalCode().matches(ALPHANUMERIC)) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CONTRACTOR_PWDAPPROVALCODE_INVALID);
            errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_CONTRACTOR_PWDAPPROVALCODE_INVALID);
            errors.add(errorDetails);
        }
        if (StringUtils.isNotBlank(contractorHelper.getMobileNumber())
                && !contractorHelper.getMobileNumber().matches(MOBILE_NUM)) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CONTRACTOR_MOBILENUMBER_INVALID);
            errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_CONTRACTOR_MOBILENUMBER_INVALID);
            errors.add(errorDetails);
        }
        return errors;
    }

    public List<ErrorDetails> validateContactorToUpdate(final ContractorHelper contractorHelper) {
        List<ErrorDetails> errors = new ArrayList<>();
        ErrorDetails errorDetails = null;
        if (contractorHelper.getCode() != null) {
            final Contractor existingContractor = contractorService.getContractorByCode(contractorHelper.getCode());
            if (existingContractor == null) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_NOT_EXIST_CONTRACTOR);
                errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_NOT_EXIST_CONTRACTOR);
                errors.add(errorDetails);
            }
        }
        errors = validateMandatoryFields(contractorHelper, errors);

        return errors;
    }

    public Contractor populateContractorToCreate(final ContractorHelper contractorHelper) {
        final Contractor contractor = new Contractor();
        populateContractor(contractorHelper, contractor);
        return contractor;
    }

    public Contractor populateContractorToUpdate(final ContractorHelper contractorHelper) {
        final Contractor contractor = contractorService.getContractorByCode(contractorHelper.getCode());
        populateContractor(contractorHelper, contractor);
        return contractor;
    }

    private void populateContractor(final ContractorHelper contractorHelper, final Contractor contractor) {
        contractor.setCode(contractorHelper.getCode());
        contractor.setName(contractorHelper.getName());
        if (StringUtils.isNotBlank(contractorHelper.getBankName()))
            contractor.setBank(bankHibernateDAO.getBankByName(contractorHelper.getBankName()));
        contractor.setBankAccount(contractorHelper.getBankAccount());
        contractor.setContactPerson(contractorHelper.getContactPerson());
        contractor.setCorrespondenceAddress(contractorHelper.getCorrespondenceAddress());
        contractor.setPaymentAddress(contractorHelper.getPaymentAddress());
        contractor.setEmail(contractorHelper.getEmail());
        contractor.setMobileNumber(contractorHelper.getMobileNumber());

        if (contractorHelper.getExemptionName() != null
                && contractorHelper.getExemptionName().equalsIgnoreCase(ExemptionForm.EARNEST_MONEY_DEPOSIT.toString()))
            contractor.setExemptionForm(ExemptionForm.EARNEST_MONEY_DEPOSIT);
        else if (contractorHelper.getExemptionName() != null
                && contractorHelper.getExemptionName().equalsIgnoreCase(ExemptionForm.INCOME_TAX.toString()))
            contractor.setExemptionForm(ExemptionForm.INCOME_TAX);
        else if (contractorHelper.getExemptionName() != null
                && contractorHelper.getExemptionName().equalsIgnoreCase(ExemptionForm.VAT.toString()))
            contractor.setExemptionForm(ExemptionForm.VAT);

        contractor.setIfscCode(contractorHelper.getIfscCode());
        contractor.setPanNumber(contractorHelper.getPanNumber());
        contractor.setTinNumber(contractorHelper.getTinNumber());
        contractor.setPwdApprovalCode(contractorHelper.getPwdApprovalCode());
        contractor.setNarration(contractorHelper.getNarration());
    }

    public Contractor saveContractor(final Contractor contractor) {
        return contractorService.createContractor(contractor);
    }

    public Contractor updateContractor(final Contractor contractor) {
        return contractorService.updateContractor(contractor);
    }

    public List<ContractorHelper> populateContractor() {
        final List<Contractor> contractors = contractorService.getAllContractors();

        final List<ContractorHelper> contractorHelpers = new ArrayList<>();

        for (final Contractor contractor : contractors)
            createContractorData(contractorHelpers, contractor);
        return contractorHelpers;
    }

    private void createContractorData(final List<ContractorHelper> contractorHelpers, final Contractor contractor) {
        final ContractorHelper contractorHelper = new ContractorHelper();
        contractorHelper.setCode(contractor.getCode());
        contractorHelper.setName(contractor.getName());
        contractorHelper.setBankName(contractor.getBank() != null ? contractor.getBank().getName() : StringUtils.EMPTY);
        contractorHelper.setBankAccount(contractor.getBankAccount());
        contractorHelper.setContactPerson(contractor.getContactPerson());
        contractorHelper.setCorrespondenceAddress(contractor.getCorrespondenceAddress());
        contractorHelper.setPaymentAddress(contractor.getPaymentAddress());
        contractorHelper.setEmail(contractor.getEmail());
        contractorHelper.setExemptionName(
                contractor.getExemptionForm() != null ? contractor.getExemptionForm().toString() : StringUtils.EMPTY);
        contractorHelper.setIfscCode(contractor.getIfscCode());
        contractorHelper.setPanNumber(contractor.getPanNumber());
        contractorHelper.setTinNumber(contractor.getTinNumber());
        contractorHelper.setPwdApprovalCode(contractor.getPwdApprovalCode());
        contractorHelper.setNarration(contractor.getNarration());
        contractorHelper.setMobileNumber(contractor.getMobileNumber());

        if (!contractor.getContractorDetails().isEmpty()) {
            contractorHelper.setContractorCategory(StringUtils.isNotBlank(contractor.getContractorDetails().get(0).getCategory())
                    ? contractor.getContractorDetails().get(0).getCategory()
                    : StringUtils.EMPTY);
            contractorHelper.setContractorClass(
                    contractor.getContractorDetails().get(0).getGrade() != null
                            ? contractor.getContractorDetails().get(0).getGrade().getGrade() : StringUtils.EMPTY);
            contractorHelper.setStatus(contractor.getContractorDetails().get(0).getStatus().getCode());
        }
        contractorHelpers.add(contractorHelper);
    }

}
