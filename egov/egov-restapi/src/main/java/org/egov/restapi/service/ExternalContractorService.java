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
package org.egov.restapi.service;

import org.apache.commons.lang.StringUtils;
import org.egov.commons.dao.BankHibernateDAO;
import org.egov.restapi.constants.RestApiConstants;
import org.egov.restapi.model.ContractorHelper;
import org.egov.restapi.model.RestErrors;
import org.egov.works.master.service.ContractorService;
import org.egov.works.models.masters.Contractor;
import org.egov.works.models.masters.ExemptionForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
        return createContractorData(contractor);
    }

    public List<RestErrors> validateContactorToCreate(final ContractorHelper contractorHelper) {
        List<RestErrors> errors = new ArrayList<>();
        RestErrors restErrors = null;
        errors = validateMandatoryFields(contractorHelper, errors);
        if (contractorHelper.getCode() != null) {
            final Contractor existingContractor = contractorService.getContractorByCode(contractorHelper.getCode());
            if (existingContractor != null) {
                restErrors = new RestErrors();
                restErrors.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_EXIST_CONTRACTOR);
                restErrors.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_EXIST_CONTRACTOR);
                errors.add(restErrors);
            }
        }

        return errors;
    }

    private List<RestErrors> validateMandatoryFields(final ContractorHelper contractorHelper, List<RestErrors> errors) {
        RestErrors restErrors = null;
        if (StringUtils.isBlank(contractorHelper.getCode())) {
            restErrors = new RestErrors();
            restErrors.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CONTRACTOR_NO_CODE);
            restErrors.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_NO_CONTRACTOR_CODE);
            errors.add(restErrors);
        }
        if (StringUtils.isBlank(contractorHelper.getName())) {
            restErrors = new RestErrors();
            restErrors.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CONTRACTOR_NO_NAME);
            restErrors.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_NO_CONTRACTOR_NAME);
            errors.add(restErrors);
        }
        if (StringUtils.isNotBlank(contractorHelper.getCode())
                && !contractorHelper.getCode().matches(ALPHANUMERICWITHSPECIALCHAR)) {
            restErrors = new RestErrors();
            restErrors.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CONTRACTOR_CODE_SPECIAL);
            restErrors.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_CONTRACTOR_CODE_SPECIAL);
            errors.add(restErrors);
        }
        if (StringUtils.isNotBlank(contractorHelper.getName())
                && !contractorHelper.getName().matches(ALPHANUMERICWITHSPECIALCHAR)) {
            restErrors = new RestErrors();
            restErrors.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CONTRACTOR_NAME_SPECIAL);
            restErrors.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_CONTRACTOR_NAME_SPECIAL);
            errors.add(restErrors);
        }
        if (StringUtils.isNotBlank(contractorHelper.getCode()) && contractorHelper.getCode().length() > 50) {
            restErrors = new RestErrors();
            restErrors.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CONTRACTOR_CODE_MAXLENGTH);
            restErrors.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_CONTRACTOR_CODE_MAXLENGTH);
            errors.add(restErrors);
        }
        if (StringUtils.isNotBlank(contractorHelper.getName()) && contractorHelper.getName().length() > 100) {
            restErrors = new RestErrors();
            restErrors.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CONTRACTOR_NAME_MAXLENGTH);
            restErrors.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_CONTRACTOR_NAME_MAXLENGTH);
            errors.add(restErrors);
        }
        errors = validateNonMandatorFields(contractorHelper, errors, restErrors);

        return errors;
    }

    private List<RestErrors> validateNonMandatorFields(final ContractorHelper contractorHelper, final List<RestErrors> errors,
            RestErrors restErrors) {
        if (StringUtils.isNotBlank(contractorHelper.getCorrespondenceAddress())
                && !contractorHelper.getCorrespondenceAddress().matches(ALPHANUMERICWITHALLSPECIALCHAR)) {
            restErrors = new RestErrors();
            restErrors.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CONTRACTOR_CORRESPONDENCEADDRESS_INVALID);
            restErrors.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_CONTRACTOR_CORRESPONDENCEADDRESS_INVALID);
            errors.add(restErrors);
        }
        if (StringUtils.isNotBlank(contractorHelper.getPaymentAddress())
                && !contractorHelper.getPaymentAddress().matches(ALPHANUMERICWITHALLSPECIALCHAR)) {
            restErrors = new RestErrors();
            restErrors.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CONTRACTOR_PAYMENTADDRESS_INVALID);
            restErrors.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_CONTRACTOR_PAYMENTADDRESS_INVALID);
            errors.add(restErrors);
        }
        if (StringUtils.isNotBlank(contractorHelper.getNarration())
                && !contractorHelper.getNarration().matches(ALPHANUMERICWITHALLSPECIALCHAR)) {
            restErrors = new RestErrors();
            restErrors.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CONTRACTOR_NARRATION_INVALID);
            restErrors.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_CONTRACTOR_NARRATION_INVALID);
            errors.add(restErrors);
        }
        if (StringUtils.isNotBlank(contractorHelper.getContactPerson())
                && !contractorHelper.getContactPerson().matches(ALPHANUMERIC_WITHSPACE)) {
            restErrors = new RestErrors();
            restErrors.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CONTRACTOR_CONTACTPERSON_INVALID);
            restErrors.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_CONTRACTOR_CONTACTPERSON_INVALID);
            errors.add(restErrors);
        }
        if (StringUtils.isNotBlank(contractorHelper.getEmail()) && !contractorHelper.getEmail().matches(EMAIL)) {
            restErrors = new RestErrors();
            restErrors.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CONTRACTOR_EMAIL_INVALID);
            restErrors.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_CONTRACTOR_EMAIL_INVALID);
            errors.add(restErrors);
        }
        if (StringUtils.isNotBlank(contractorHelper.getPanNumber())
                && !contractorHelper.getPanNumber().matches(PANNUMBER)) {
            restErrors = new RestErrors();
            restErrors.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CONTRACTOR_PANNUMBER_INVALID);
            restErrors.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_CONTRACTOR_PANNUMBER_INVALID);
            errors.add(restErrors);
        }
        if (StringUtils.isNotBlank(contractorHelper.getTinNumber())
                && !contractorHelper.getTinNumber().matches(ALPHANUMERIC)) {
            restErrors = new RestErrors();
            restErrors.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CONTRACTOR_TINNUMBER_INVALID);
            restErrors.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_CONTRACTOR_TINNUMBER_INVALID);
            errors.add(restErrors);
        }
        if (StringUtils.isNotBlank(contractorHelper.getIfscCode())
                && !contractorHelper.getIfscCode().matches(ALPHANUMERIC)) {
            restErrors = new RestErrors();
            restErrors.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CONTRACTOR_IFSCCODE_INVALID);
            restErrors.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_CONTRACTOR_IFSCCODE_INVALID);
            errors.add(restErrors);
        }
        if (StringUtils.isNotBlank(contractorHelper.getBankAccount())
                && !contractorHelper.getBankAccount().matches(ALPHANUMERIC)) {
            restErrors = new RestErrors();
            restErrors.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CONTRACTOR_BANKACCOUNT_INVALID);
            restErrors.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_CONTRACTOR_BANKACCOUNT_INVALID);
            errors.add(restErrors);
        }
        if (StringUtils.isNotBlank(contractorHelper.getPwdApprovalCode())
                && !contractorHelper.getPwdApprovalCode().matches(ALPHANUMERIC)) {
            restErrors = new RestErrors();
            restErrors.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CONTRACTOR_PWDAPPROVALCODE_INVALID);
            restErrors.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_CONTRACTOR_PWDAPPROVALCODE_INVALID);
            errors.add(restErrors);
        }
        if (StringUtils.isNotBlank(contractorHelper.getMobileNumber())
                && !contractorHelper.getMobileNumber().matches(MOBILE_NUM)) {
            restErrors = new RestErrors();
            restErrors.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CONTRACTOR_MOBILENUMBER_INVALID);
            restErrors.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_CONTRACTOR_MOBILENUMBER_INVALID);
            errors.add(restErrors);
        }
        return errors;
    }

    public List<RestErrors> validateContactorToUpdate(final ContractorHelper contractorHelper) {
        List<RestErrors> errors = new ArrayList<>();
        RestErrors restErrors = null;
        if (StringUtils.isNotBlank(contractorHelper.getCode())) {
            final Contractor existingContractor = contractorService.getContractorByCode(contractorHelper.getCode());
            if (existingContractor == null) {
                restErrors = new RestErrors();
                restErrors.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_NOT_EXIST_CONTRACTOR);
                restErrors.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_NOT_EXIST_CONTRACTOR);
                errors.add(restErrors);
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
        final ContractorHelper contractorHelper = createContractorData(contractor);
        contractorHelpers.add(contractorHelper);
    }

    private ContractorHelper createContractorData(final Contractor contractor) {
        final ContractorHelper contractorHelper = new ContractorHelper();
        contractorHelper.setCode(contractor.getCode());
        contractorHelper.setName(contractor.getName());
        if (contractor.getBank() != null)
            contractorHelper.setBankName(contractor.getBank().getName());
        else
            contractorHelper.setBankName(StringUtils.EMPTY);
        if (StringUtils.isNotBlank(contractor.getBankAccount()))
            contractorHelper.setBankAccount(contractor.getBankAccount());
        else
            contractorHelper.setBankAccount(StringUtils.EMPTY);
        if (StringUtils.isNotBlank(contractor.getContactPerson()))
            contractorHelper.setContactPerson(contractor.getContactPerson());
        else
            contractorHelper.setContactPerson(StringUtils.EMPTY);
        createContractorHeaderData(contractor, contractorHelper);

        populateContractorDetailsData(contractor, contractorHelper);
        return contractorHelper;
    }

    private void populateContractorDetailsData(final Contractor contractor, final ContractorHelper contractorHelper) {
        if (!contractor.getContractorDetails().isEmpty()
                && StringUtils.isNotBlank(contractor.getContractorDetails().get(0).getCategory()))
            contractorHelper.setContractorCategory(contractor.getContractorDetails().get(0).getCategory());
        else
            contractorHelper.setContractorCategory(StringUtils.EMPTY);
        if (!contractor.getContractorDetails().isEmpty() && contractor.getContractorDetails().get(0).getGrade() != null)
            contractorHelper.setContractorClass(contractor.getContractorDetails().get(0).getGrade().getGrade());
        else
            contractorHelper.setContractorClass(StringUtils.EMPTY);
        if (!contractor.getContractorDetails().isEmpty())
            contractorHelper.setStatus(contractor.getContractorDetails().get(0).getStatus().getCode());
        else
            contractorHelper.setStatus(StringUtils.EMPTY);
    }

    private void createContractorHeaderData(final Contractor contractor, final ContractorHelper contractorHelper) {
        if (StringUtils.isNotBlank(contractor.getCorrespondenceAddress()))
            contractorHelper.setCorrespondenceAddress(contractor.getCorrespondenceAddress());
        else
            contractorHelper.setCorrespondenceAddress(StringUtils.EMPTY);
        if (StringUtils.isNotBlank(contractor.getPaymentAddress()))
            contractorHelper.setPaymentAddress(contractor.getPaymentAddress());
        else
            contractorHelper.setPaymentAddress(StringUtils.EMPTY);
        if (StringUtils.isNotBlank(contractor.getEmail()))
            contractorHelper.setEmail(contractor.getEmail());
        else
            contractorHelper.setEmail(StringUtils.EMPTY);
        if (contractor.getExemptionForm() != null)
            contractorHelper.setExemptionName(contractor.getExemptionForm().toString());
        else
            contractorHelper.setExemptionName(StringUtils.EMPTY);
        if (StringUtils.isNotBlank(contractor.getIfscCode()))
            contractorHelper.setIfscCode(contractor.getIfscCode());
        else
            contractorHelper.setIfscCode(StringUtils.EMPTY);

        populateContractorDataToView(contractor, contractorHelper);
    }

    private void populateContractorDataToView(final Contractor contractor, final ContractorHelper contractorHelper) {
        if (StringUtils.isNotBlank(contractor.getPanNumber()))
            contractorHelper.setPanNumber(contractor.getPanNumber());
        else
            contractorHelper.setPanNumber(StringUtils.EMPTY);
        if (StringUtils.isNotBlank(contractor.getTinNumber()))
            contractorHelper.setTinNumber(contractor.getTinNumber());
        else
            contractorHelper.setTinNumber(StringUtils.EMPTY);
        if (StringUtils.isNotBlank(contractor.getPwdApprovalCode()))
            contractorHelper.setPwdApprovalCode(contractor.getPwdApprovalCode());
        else
            contractorHelper.setPwdApprovalCode(StringUtils.EMPTY);
        if (StringUtils.isNotBlank(contractor.getNarration()))
            contractorHelper.setNarration(contractor.getNarration());
        else
            contractorHelper.setNarration(StringUtils.EMPTY);
        if (StringUtils.isNotBlank(contractor.getMobileNumber()))
            contractorHelper.setMobileNumber(contractor.getMobileNumber());
        else
            contractorHelper.setMobileNumber(StringUtils.EMPTY);
    }

}
