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
package org.egov.stms.web.controller.utils;

import org.apache.commons.lang3.StringUtils;
import org.egov.commons.Installment;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.stms.masters.entity.enums.PropertyType;
import org.egov.stms.masters.service.DonationMasterService;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.entity.SewerageConnectionEstimationDetails;
import org.egov.stms.transactions.entity.SewerageDemandDetail;
import org.egov.stms.transactions.entity.SewerageFieldInspectionDetails;
import org.egov.stms.transactions.service.SewerageApplicationDetailsService;
import org.egov.stms.transactions.service.SewerageDemandService;
import org.egov.stms.transactions.service.SewerageThirdPartyServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_CREATED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_ESTIMATENOTICEGEN;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_FEEPAID;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_INITIALAPPROVED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_INSPECTIONFEEPAID;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_REJECTED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.MIXED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.NONRESIDENTIAL;
import static org.egov.stms.utils.constants.SewerageTaxConstants.RESIDENTIAL;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WFLOW_ACTION_STEP_CANCEL;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WFLOW_ACTION_STEP_FORWARD;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WFLOW_ACTION_STEP_REJECT;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WF_STATE_CONNECTION_EXECUTION_BUTTON;

@Component
public class SewerageApplicationValidator extends SewerageApplicationCommonValidator {

    private static final String NOTEMPTY_SEWERAGE_PROPERTYTYPE = "err.propertytype.required.validate";
    private static final String NOTEMPTY_SEWERAGE_PROPERTYID = "notempty.sewerage.propertyidentifier";
    private static final String RESIDENTIALCLOSETSREQUIRED = "err.noofclosetsresidential.required.validate";
    private static final String NONRESIDENTIALCLOSETSREQUIRED = "err.noofclosetsnonresidential.required.validate";
    private static final String CONNECTIONDTL_NOOFCLOSETS_RESIDENTIAL = "connectionDetail.noOfClosetsResidential";
    private static final String CONNECTIONDTL_NOOFCLOSETS_NONRESIDENTIAL = "connectionDetail.noOfClosetsNonResidential";
    private static final String SEWERAGE_NOOFCLOSETS_NONZERO = "err.numberofclosets.reject.0";
    private static final String NOTEMPTY_SEWERAGE_SHSC_NUMBER = "notempty.sewerage.shscnumber";
    private static final String NOTEMPTY_SEWERAGE_EXECUTION_DATE = "notempty.sewerage.executiondate";
    private static final String NOTEMPTY_DEMAND_AMOUNT_CURR_INSTALLMENT = "err.demandamount.emptynotallowed";
    private static final String NOTEMPTY_COLLECTION_AMOUNT_CURR_INSTALLMENT = "err.collectionamount.emptynotallowed";
    private static final String PROPERTY_IDENTIFIER_NOTEXISTS = "err.propertyidentifier.notexists";
    private static final String CONNECTIONDTL_PROPERTYTYPE = "connectionDetail.propertyType";
    private static final String CONNECTIONDTL_PROPERTYID_ISVALID = "err.connectionDetail.propertyIdentifier.validate";
    private static final String REJECTION_COMMENTS_REQUIRED = "err.application.reject.comments.required";
    private static final String NUMBEROFCLOSETS_INVALID = "err.numberofclosets.invalid";

    @Autowired
    private SewerageApplicationDetailsService sewerageApplicationDetailsService;

    @Autowired
    private SewerageThirdPartyServices sewerageThirdPartyServices;

    @Autowired
    private SewerageDemandService sewerageDemandService;

    @Autowired
    private DonationMasterService donationMasterService;

    public void validateSewerageNewApplication(final SewerageApplicationDetails sewerageApplicationDetails, final Errors errors,
            final HttpServletRequest request) {

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "connectionDetail.propertyIdentifier", NOTEMPTY_SEWERAGE_PROPERTYID);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, CONNECTIONDTL_PROPERTYTYPE, NOTEMPTY_SEWERAGE_PROPERTYTYPE);
        validatePropertyID(sewerageApplicationDetails, errors, request);
        if (sewerageApplicationDetails.getConnectionDetail().getPropertyType() != null)
            validateNumberOfClosets(errors, sewerageApplicationDetails);

    }

    private void validateNumberOfClosets(final Errors errors, final SewerageApplicationDetails sewerageApplicationDetails) {
        if (sewerageApplicationDetails.getConnectionDetail().getPropertyType() != null)
            isNumberOfClosetsEntered(errors, sewerageApplicationDetails);

        if (sewerageApplicationDetails.getConnectionDetail().getNoOfClosetsResidential() != null
                && sewerageApplicationDetails.getConnectionDetail().getNoOfClosetsResidential() == 0)
            errors.rejectValue(CONNECTIONDTL_NOOFCLOSETS_RESIDENTIAL, SEWERAGE_NOOFCLOSETS_NONZERO);

        if (sewerageApplicationDetails.getConnectionDetail().getNoOfClosetsNonResidential() != null
                && sewerageApplicationDetails.getConnectionDetail().getNoOfClosetsNonResidential() == 0)
            errors.rejectValue(CONNECTIONDTL_NOOFCLOSETS_NONRESIDENTIAL, SEWERAGE_NOOFCLOSETS_NONZERO);
        checkNumberOfClosetsValid(errors, sewerageApplicationDetails);

    }

    private void isNumberOfClosetsEntered(final Errors errors, final SewerageApplicationDetails sewerageApplicationDetails) {
        if (RESIDENTIAL.equalsIgnoreCase(sewerageApplicationDetails.getConnectionDetail().getPropertyType().toString())
                && sewerageApplicationDetails.getConnectionDetail().getNoOfClosetsResidential() == null)
            errors.rejectValue(CONNECTIONDTL_NOOFCLOSETS_RESIDENTIAL, RESIDENTIALCLOSETSREQUIRED);
        else if (NONRESIDENTIAL.equalsIgnoreCase(sewerageApplicationDetails.getConnectionDetail().getPropertyType().toString())
                && sewerageApplicationDetails.getConnectionDetail().getNoOfClosetsNonResidential() == null)
            errors.rejectValue(CONNECTIONDTL_NOOFCLOSETS_NONRESIDENTIAL, NONRESIDENTIALCLOSETSREQUIRED);
        else if (MIXED.equalsIgnoreCase(sewerageApplicationDetails.getConnectionDetail().getPropertyType().toString()))
            if (sewerageApplicationDetails.getConnectionDetail().getNoOfClosetsResidential() == null
                    && sewerageApplicationDetails.getConnectionDetail().getNoOfClosetsNonResidential() == null) {
                errors.rejectValue(CONNECTIONDTL_NOOFCLOSETS_RESIDENTIAL, RESIDENTIALCLOSETSREQUIRED);
                errors.rejectValue(CONNECTIONDTL_NOOFCLOSETS_NONRESIDENTIAL, NONRESIDENTIALCLOSETSREQUIRED);
            } else if (sewerageApplicationDetails.getConnectionDetail().getNoOfClosetsResidential() == null)
                errors.rejectValue(CONNECTIONDTL_NOOFCLOSETS_RESIDENTIAL, RESIDENTIALCLOSETSREQUIRED);
            else if (sewerageApplicationDetails.getConnectionDetail().getNoOfClosetsNonResidential() == null)
                errors.rejectValue(CONNECTIONDTL_NOOFCLOSETS_NONRESIDENTIAL, NONRESIDENTIALCLOSETSREQUIRED);
    }

    private void checkNumberOfClosetsValid(final Errors errors, final SewerageApplicationDetails sewerageApplicationDetails) {
        String validationMessage;
        if (sewerageApplicationDetails.getConnectionDetail().getNoOfClosetsNonResidential() != null) {
            validationMessage = donationMasterService.checkClosetsPresentForGivenCombination(
                    PropertyType.NON_RESIDENTIAL,
                    sewerageApplicationDetails.getConnectionDetail().getNoOfClosetsNonResidential());
            if (!validationMessage.isEmpty())
                errors.rejectValue(CONNECTIONDTL_NOOFCLOSETS_NONRESIDENTIAL, NUMBEROFCLOSETS_INVALID,
                        new String[] {
                                sewerageApplicationDetails.getConnectionDetail().getNoOfClosetsNonResidential().toString() },
                        NUMBEROFCLOSETS_INVALID);
        }
        if (sewerageApplicationDetails.getConnectionDetail().getNoOfClosetsResidential() != null) {
            validationMessage = donationMasterService.checkClosetsPresentForGivenCombination(
                    PropertyType.RESIDENTIAL,
                    sewerageApplicationDetails.getConnectionDetail().getNoOfClosetsResidential());
            if (!validationMessage.isEmpty())
                errors.rejectValue(CONNECTIONDTL_NOOFCLOSETS_RESIDENTIAL, NUMBEROFCLOSETS_INVALID,
                        new String[] { sewerageApplicationDetails.getConnectionDetail().getNoOfClosetsResidential().toString() },
                        NUMBEROFCLOSETS_INVALID);
        }

    }

    public void validatePropertyID(final SewerageApplicationDetails sewerageApplicationDetails,
            final Errors errors, final HttpServletRequest request) {
        if (sewerageApplicationDetails.getConnectionDetail() != null
                && sewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier() != null
                && !"".equals(sewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier())) {
            String errorMessage = sewerageApplicationDetailsService
                    .checkValidPropertyAssessmentNumber(sewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier());
            if (errorMessage != null && !errorMessage.equals(""))
                errors.reject(CONNECTIONDTL_PROPERTYID_ISVALID,
                        new String[] { errorMessage }, null);
            else {
                errorMessage = sewerageApplicationDetailsService
                        .checkConnectionPresentForProperty(
                                sewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier());
                if (errorMessage != null && !"".equals(errorMessage))
                    errors.reject(CONNECTIONDTL_PROPERTYID_ISVALID,
                            new String[] { errorMessage }, null);
                else
                    checkWaterTaxDue(sewerageApplicationDetails, errors, request);
            }
        }
    }

    public void validateNewApplicationUpdate(final SewerageApplicationDetails sewerageApplicationDetails,
            final BindingResult errors, final String workFlowAction) {
        if ((sewerageApplicationDetails.getStatus().getCode().equalsIgnoreCase(APPLICATION_STATUS_INITIALAPPROVED) ||
                sewerageApplicationDetails.getStatus().getCode().equalsIgnoreCase(APPLICATION_STATUS_INSPECTIONFEEPAID)) &&
                "Reject".equalsIgnoreCase(workFlowAction) && StringUtils.isBlank(sewerageApplicationDetails.getApprovalComent()))
            errors.reject(REJECTION_COMMENTS_REQUIRED);

        if ((APPLICATION_STATUS_REJECTED.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode()) ||
                APPLICATION_STATUS_FEEPAID.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode()) ||
                APPLICATION_STATUS_ESTIMATENOTICEGEN.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode())) &&
                workFlowAction.equalsIgnoreCase("Cancel") && StringUtils.isBlank(sewerageApplicationDetails.getApprovalComent()))
            errors.reject("err.application.cancel.comments.required");

        if (sewerageApplicationDetails.getConnectionDetail().getPropertyType() == null)
            errors.reject(NOTEMPTY_SEWERAGE_PROPERTYTYPE);
        else
            validateNumberOfClosets(errors, sewerageApplicationDetails);

        if (sewerageApplicationDetails.getConnectionDetail() != null
                && sewerageApplicationDetails.getConnectionDetail().getPropertyType() != null)
            validateDonationAmount(sewerageApplicationDetails, errors);

        validateFieldInspectionDetails(sewerageApplicationDetails, errors);
        validateEstimationDetails(sewerageApplicationDetails, errors);

    }

    private void validateDonationAmount(final SewerageApplicationDetails sewerageApplicationDetails, final Errors errors) {
        if (RESIDENTIAL.equalsIgnoreCase(sewerageApplicationDetails.getConnectionDetail().getPropertyType().toString()) &&
                sewerageApplicationDetails.getConnectionDetail().getNoOfClosetsResidential() != null)
            getDonationAmount(sewerageApplicationDetails, errors);
        if (NONRESIDENTIAL.equalsIgnoreCase(sewerageApplicationDetails.getConnectionDetail().getPropertyType().toString())
                && sewerageApplicationDetails.getConnectionDetail().getNoOfClosetsNonResidential() != null)
            getDonationAmount(sewerageApplicationDetails, errors);
        if (MIXED.equalsIgnoreCase(sewerageApplicationDetails.getConnectionDetail().getPropertyType().toString()) &&
                sewerageApplicationDetails.getConnectionDetail().getNoOfClosetsNonResidential() != null &&
                sewerageApplicationDetails.getConnectionDetail().getNoOfClosetsResidential() != null)
            getDonationAmount(sewerageApplicationDetails, errors);
    }

    private void validateEstimationDetails(final SewerageApplicationDetails sewerageApplicationDetails,
            final Errors errors) {
        if (!sewerageApplicationDetails.getEstimationDetailsForUpdate().isEmpty()) {
            boolean mandatoryValueEntered = false;
            boolean mandatoryValueNotEntered = false;

            final List<SewerageConnectionEstimationDetails> estimationDetailList = sewerageApplicationDetails
                    .getEstimationDetailsForUpdate();

            for (final SewerageConnectionEstimationDetails estimationDetail : estimationDetailList) {

                if (estimationDetail.getUnitOfMeasurement() != null
                        || estimationDetail.getItemDescription() != null && estimationDetail.getQuantity() != 0
                        || estimationDetail.getQuantity() != null && estimationDetail.getQuantity() != 0
                        || estimationDetail.getUnitRate() != null && estimationDetail.getUnitRate() != 0
                        || estimationDetail.getAmount() != null && estimationDetail.getAmount().intValue() != 0)
                    mandatoryValueEntered = true;

                if (estimationDetail.getUnitOfMeasurement() == null ||
                        estimationDetail.getItemDescription() == null ||
                        estimationDetail.getQuantity() == null || estimationDetail.getQuantity() == 0 ||
                        estimationDetail.getUnitRate() == null || estimationDetail.getUnitRate() == 0 ||
                        estimationDetail.getAmount() == null || estimationDetail.getAmount().intValue() == 0)
                    mandatoryValueNotEntered = true;

                if (mandatoryValueEntered && mandatoryValueNotEntered)
                    errors.reject("err.estimationdetails.mandatory.validate");
            }
        }
    }

    private void validateFieldInspectionDetails(final SewerageApplicationDetails sewerageApplicationDetails,
            final Errors errors) {
        if (!sewerageApplicationDetails.getFieldInspections().isEmpty()
                && !sewerageApplicationDetails.getFieldInspections().get(0).getFieldInspectionDetailsForUpdate().isEmpty()) {
            boolean mandatoryValueEntered = false;
            boolean mandatoryValueNotEntered = false;
            final List<SewerageFieldInspectionDetails> inspectionDetailList = sewerageApplicationDetails.getFieldInspections()
                    .get(0).getFieldInspectionDetailsForUpdate();
            for (final SewerageFieldInspectionDetails inspectionDetail : inspectionDetailList) {

                if (inspectionDetail.getScrewSize() != null || inspectionDetail.getPipeSize() != null
                        || inspectionDetail.getPipeLength() != null && inspectionDetail.getPipeLength() != 0
                        || inspectionDetail.getNoOfPipes() != null && inspectionDetail.getNoOfPipes() != 0
                        || inspectionDetail.getNoOfScrews() != null && inspectionDetail.getNoOfScrews() != 0
                        || inspectionDetail.getDistance() != null && inspectionDetail.getDistance() != 0)
                    mandatoryValueEntered = true;

                if (inspectionDetail.getScrewSize() == null ||
                        inspectionDetail.getPipeSize() == null ||
                        inspectionDetail.getPipeLength() == null || inspectionDetail.getPipeLength() == 0 ||
                        inspectionDetail.getNoOfPipes() == null || inspectionDetail.getNoOfPipes() == 0 ||
                        inspectionDetail.getNoOfScrews() == null || inspectionDetail.getNoOfScrews() == 0 ||
                        inspectionDetail.getDistance() == null || inspectionDetail.getDistance() == 0)
                    mandatoryValueNotEntered = true;

                if (mandatoryValueEntered && mandatoryValueNotEntered)
                    errors.reject("err.pipedetails.mandatory.validate");
            }
        }
    }

    // validate donation amount present or not
    public void getDonationAmount(final SewerageApplicationDetails sewerageApplicationDetails, final Errors errors) {
        final String legacyDonationAmount = getDonationAmount(
                sewerageApplicationDetails.getConnectionDetail().getPropertyType(),
                sewerageApplicationDetails.getConnectionDetail().getNoOfClosetsResidential(),
                sewerageApplicationDetails.getConnectionDetail().getNoOfClosetsNonResidential());
        if (legacyDonationAmount == null)
            errors.reject("err.donationamount.notexists");
    }

    public void validateChangeInClosetsApplication(final SewerageApplicationDetails sewerageApplicationDetails,
            final Errors errors, final HttpServletRequest request) {
        if (sewerageApplicationDetails.getConnectionDetail().getPropertyType() == null)
            errors.rejectValue(CONNECTIONDTL_PROPERTYTYPE, NOTEMPTY_SEWERAGE_PROPERTYTYPE);
        else
            validateNumberOfClosets(errors, sewerageApplicationDetails);

        if (sewerageApplicationDetails.getConnectionDetail() != null
                && sewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier() != null
                && !sewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier().equals("")) {
            final String errorMessage = sewerageApplicationDetailsService
                    .checkValidPropertyAssessmentNumber(sewerageApplicationDetails.getConnectionDetail()
                            .getPropertyIdentifier());
            if (errorMessage != null && !errorMessage.equals(""))
                errors.reject(CONNECTIONDTL_PROPERTYID_ISVALID,
                        new String[] { errorMessage }, null);
        }
        checkWaterTaxDue(sewerageApplicationDetails, errors, request);
    }

    // validate change in closet application update
    public void validateChangeInClosetsUpdateApplication(final SewerageApplicationDetails sewerageApplicationDetails,
            final Errors errors, final String workFlowAction) {

        if ((APPLICATION_STATUS_FEEPAID.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode()) ||
                APPLICATION_STATUS_ESTIMATENOTICEGEN.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode())) &&
                WFLOW_ACTION_STEP_CANCEL.equalsIgnoreCase(workFlowAction) &&
                org.apache.commons.lang.StringUtils.isBlank(sewerageApplicationDetails.getApprovalComent()))
            errors.reject("err.application.cancel.comments.required");

        if ((APPLICATION_STATUS_INSPECTIONFEEPAID.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode()) ||
                APPLICATION_STATUS_INITIALAPPROVED.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode())) &&
                WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(workFlowAction) &&
                org.apache.commons.lang.StringUtils.isBlank(sewerageApplicationDetails.getApprovalComent()))
            errors.reject(REJECTION_COMMENTS_REQUIRED);

        if (workFlowAction != null && WF_STATE_CONNECTION_EXECUTION_BUTTON.equalsIgnoreCase(workFlowAction)) {
            final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            if (sewerageApplicationDetails.getConnection().getExecutionDate() == null)
                errors.rejectValue("connection.executionDate", "err.executiondate.required.validate");
            else if (StringUtils.isNotEmpty(sewerageApplicationDetails.getConnection().getExecutionDate().toString())) {
                formatter.format(sewerageApplicationDetails.getConnection().getExecutionDate());
                if (sewerageApplicationDetails.getConnection().getExecutionDate()
                        .compareTo(sewerageApplicationDetails.getApplicationDate()) < 0)
                    errors.reject("err.connectionexecution.date.validate",
                            new String[] { formatter.format(sewerageApplicationDetails.getApplicationDate()) },
                            "err.connectionexecution.date.validate");
            }
        }

        if (sewerageApplicationDetails.getConnectionDetail().getPropertyType() == null)
            errors.rejectValue(CONNECTIONDTL_PROPERTYTYPE, NOTEMPTY_SEWERAGE_PROPERTYTYPE);
        else
            validateNumberOfClosets(errors, sewerageApplicationDetails);

        if (sewerageApplicationDetails.getConnectionDetail() != null
                && sewerageApplicationDetails.getConnectionDetail().getPropertyType() != null)
            validateDonationAmount(sewerageApplicationDetails, errors);
        validateFieldInspectionDetails(sewerageApplicationDetails, errors);
        validateEstimationDetails(sewerageApplicationDetails, errors);

    }

    // validate sewerage legacy data entry screen inputs
    public void validateLegacyData(final SewerageApplicationDetails sewerageApplicationDetails, final Errors errors,
            final HttpServletRequest request) {

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "connection.shscNumber", NOTEMPTY_SEWERAGE_SHSC_NUMBER);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "connection.executionDate", NOTEMPTY_SEWERAGE_EXECUTION_DATE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, CONNECTIONDTL_PROPERTYTYPE, NOTEMPTY_SEWERAGE_PROPERTYTYPE);

        if (sewerageApplicationDetails.getConnection().getShscNumber() != null
                && sewerageApplicationDetails.getConnection().getShscNumber().length() != 10)
            errors.rejectValue("connection.shscNumber", "err.shscnumber.length.validate", null);

        if (sewerageApplicationDetails.getConnectionDetail().getNoOfClosetsResidential() != null
                && sewerageApplicationDetails.getConnectionDetail().getNoOfClosetsResidential() == 0
                || sewerageApplicationDetails.getConnectionDetail().getNoOfClosetsNonResidential() != null
                        && sewerageApplicationDetails.getConnectionDetail().getNoOfClosetsNonResidential() == 0)
            errors.rejectValue(CONNECTIONDTL_NOOFCLOSETS_RESIDENTIAL, SEWERAGE_NOOFCLOSETS_NONZERO, null);

        if (sewerageApplicationDetails.getConnectionDetail().getPropertyType() != null)
            isNumberOfClosetsPresent(sewerageApplicationDetails, errors);

        validatePropertyAndConnection(errors, request, sewerageApplicationDetails);
        validateDemandBeanList(sewerageApplicationDetails, errors);
    }

    // validate number of closets entered or not
    public void isNumberOfClosetsPresent(final SewerageApplicationDetails sewerageApplicationDetails,
            final Errors errors) {
        if (RESIDENTIAL.equalsIgnoreCase(sewerageApplicationDetails.getConnectionDetail().getPropertyType().toString())
                && sewerageApplicationDetails.getConnectionDetail().getNoOfClosetsResidential() == null)
            errors.rejectValue(CONNECTIONDTL_NOOFCLOSETS_RESIDENTIAL, RESIDENTIALCLOSETSREQUIRED);
        else if (NONRESIDENTIAL.equalsIgnoreCase(sewerageApplicationDetails.getConnectionDetail().getPropertyType().toString())
                && sewerageApplicationDetails.getConnectionDetail().getNoOfClosetsNonResidential() == null)
            errors.rejectValue(CONNECTIONDTL_NOOFCLOSETS_NONRESIDENTIAL, NONRESIDENTIALCLOSETSREQUIRED);
        else if (MIXED.equalsIgnoreCase(sewerageApplicationDetails.getConnectionDetail().getPropertyType().toString()))
            if (sewerageApplicationDetails.getConnectionDetail().getNoOfClosetsResidential() == null
                    && sewerageApplicationDetails.getConnectionDetail().getNoOfClosetsNonResidential() == null) {
                errors.rejectValue(CONNECTIONDTL_NOOFCLOSETS_RESIDENTIAL, RESIDENTIALCLOSETSREQUIRED);
                errors.rejectValue(CONNECTIONDTL_NOOFCLOSETS_NONRESIDENTIAL, NONRESIDENTIALCLOSETSREQUIRED);
            } else if (sewerageApplicationDetails.getConnectionDetail().getNoOfClosetsResidential() == null)
                errors.rejectValue(CONNECTIONDTL_NOOFCLOSETS_RESIDENTIAL, RESIDENTIALCLOSETSREQUIRED);
            else if (sewerageApplicationDetails.getConnectionDetail().getNoOfClosetsNonResidential() == null)
                errors.rejectValue(CONNECTIONDTL_NOOFCLOSETS_NONRESIDENTIAL, NONRESIDENTIALCLOSETSREQUIRED);
    }

    // validate property identifier and sewerage connection
    private void validatePropertyAndConnection(final Errors errors, final HttpServletRequest request,
            final SewerageApplicationDetails sewerageApplicationDetails) {
        final AssessmentDetails details = sewerageThirdPartyServices
                .getPropertyDetails(sewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier(), request);
        if (details != null && details.getErrorDetails().getErrorMessage() != null)
            errors.rejectValue("connectionDetail.propertyIdentifier", PROPERTY_IDENTIFIER_NOTEXISTS, null);

        final String result = sewerageApplicationDetailsService.isConnectionExistsForProperty(
                sewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier());
        if (result != null && result != "")
            errors.reject("err.connection.alreadyexists");
    }

    // validate demand bean list for legacy record
    public void validateDemandBeanList(final SewerageApplicationDetails sewerageApplicationDetails, final Errors errors) {
        boolean isEnterDemandValue = false;
        SewerageDemandDetail sewerageDD;
        final Installment installment = sewerageDemandService.getCurrentInstallment();
        for (int i = 0; i < sewerageApplicationDetails.getDemandDetailBeanList().size(); i++) {
            sewerageDD = sewerageApplicationDetails.getDemandDetailBeanList().get(i);

            if (sewerageDD.getActualCollection() != null &&
                    BigDecimal.ZERO != sewerageDD.getActualCollection()
                    && sewerageDD.getActualAmount() == null)
                errors.rejectValue("demandDetailBeanList[" + i + "].actualAmount",
                        "err.demandamountforenteredcollectionamount.validate",
                        new String[] { sewerageDD.getInstallment() }, null);

            if (sewerageDD.getActualAmount() != null)
                isEnterDemandValue = true;
            if (sewerageDD.getActualCollection() != null &&
                    sewerageDD.getActualAmount() != null &&
                    sewerageDD.getActualCollection()
                            .compareTo(sewerageDD.getActualAmount()) > 0)
                errors.rejectValue("demandDetailBeanList[" + i + "].actualCollection",
                        "err.demandamount.collectedamount.validate", new String[] { sewerageDD.getInstallment() }, null);
            if (isEnterDemandValue && sewerageDD.getActualAmount() == null)
                errors.rejectValue("demandDetailBeanList[" + i + "].actualAmount",
                        "err.demandamountforintermediateinstallments.required",
                        new String[] { sewerageDD.getInstallment() }, null);

            if (sewerageDD.getInstallment().equals(installment.toString())) {
                if (sewerageDD.getActualAmount() == null ||
                        sewerageDD.getActualAmount() == BigDecimal.ZERO)
                    errors.rejectValue("demandDetailBeanList[" + i + "].actualAmount", NOTEMPTY_DEMAND_AMOUNT_CURR_INSTALLMENT,
                            new String[] { sewerageDD.getInstallment() }, null);
                if (sewerageDD.getActualCollection() == null ||
                        sewerageDD.getActualCollection() == BigDecimal.ZERO)
                    errors.rejectValue("demandDetailBeanList[" + i + "].actualCollection",
                            NOTEMPTY_COLLECTION_AMOUNT_CURR_INSTALLMENT,
                            new String[] { sewerageDD.getInstallment() }, null);
            }

        }
    }

    // validate donation amount
    private void validateDonationAmount(final Errors errors, final SewerageApplicationDetails sewerageApplicationDetails) {
        if (sewerageApplicationDetails.getConnectionDetail().getPropertyType() != null)
            if (RESIDENTIAL.equalsIgnoreCase(sewerageApplicationDetails.getConnectionDetail().getPropertyType().toString()) &&
                    sewerageApplicationDetails.getConnectionDetail().getNoOfClosetsResidential() != null)
                getDonationAmount(sewerageApplicationDetails, errors);
            else if (NONRESIDENTIAL
                    .equalsIgnoreCase(sewerageApplicationDetails.getConnectionDetail().getPropertyType().toString())
                    && sewerageApplicationDetails.getConnectionDetail().getNoOfClosetsNonResidential() != null)
                getDonationAmount(sewerageApplicationDetails, errors);
            else if (MIXED.equalsIgnoreCase(sewerageApplicationDetails.getConnectionDetail().getPropertyType().toString()) &&
                    sewerageApplicationDetails.getConnectionDetail().getNoOfClosetsNonResidential() != null &&
                    sewerageApplicationDetails.getConnectionDetail().getNoOfClosetsResidential() != null)
                getDonationAmount(sewerageApplicationDetails, errors);
    }

    // validate closure of application remarks
    public void validateClosureApplication(final SewerageApplicationDetails sewerageApplicationDetails, final Errors errors,
            final HttpServletRequest request) {
        if (StringUtils.isBlank(sewerageApplicationDetails.getCloseConnectionReason()))
            errors.rejectValue("closeConnectionReason", "err.closeconnection.remarks");
        validatePropertyIdAndWaterTaxDue(sewerageApplicationDetails, errors, request);
    }

    // validate property tax and water tax due
    public void validatePropertyIdAndWaterTaxDue(final SewerageApplicationDetails sewerageApplicationDetails,
            final Errors errors, final HttpServletRequest request) {
        if (sewerageApplicationDetails.getConnectionDetail() != null
                && sewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier() != null
                && !sewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier().equals("")) {
            final String errorMessage = sewerageApplicationDetailsService
                    .checkValidPropertyAssessmentNumber(sewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier());
            if (errorMessage != null && !errorMessage.equals(""))
                errors.reject(CONNECTIONDTL_PROPERTYID_ISVALID,
                        new String[] { errorMessage }, null);
            else
                checkWaterTaxDue(sewerageApplicationDetails, errors, request);
        }
    }

    // validate water tax due
    private void checkWaterTaxDue(final SewerageApplicationDetails sewerageApplicationDetails, final Errors errors,
            final HttpServletRequest request) {
        String errorMessage;
        final HashMap<String, Object> result = sewerageThirdPartyServices.getWaterTaxDueAndCurrentTax(
                sewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier(), request);
        final BigDecimal waterTaxDue = (BigDecimal) result.get("WATERTAXDUE");
        String consumerCode = result.get("CONSUMERCODE").toString();
        // Taking substring since value in consumercode is in this form [12345]
        if (consumerCode != "" && consumerCode != null)
            consumerCode = consumerCode.substring(1, consumerCode.length() - 1);
        if (waterTaxDue.compareTo(BigDecimal.ZERO) > 0) {
            errorMessage = "Property tax Assessment number "
                    + sewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier() +
                    " linked water tap connection demand with Consumer code:" + consumerCode + " is due Rs." + waterTaxDue
                    + "/- . Please clear demand and proceed";
            errors.reject(CONNECTIONDTL_PROPERTYID_ISVALID,
                    new String[] { errorMessage }, null);
        }
    }

    // validate closure application update
    public void validateUpdateClosureApplication(final SewerageApplicationDetails sewerageApplicationDetails, final Errors errors,
            final String workFlowAction) {
        if ((APPLICATION_STATUS_INITIALAPPROVED.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode()) ||
                APPLICATION_STATUS_CREATED.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode()))
                && WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(workFlowAction)
                && org.apache.commons.lang.StringUtils.isBlank(sewerageApplicationDetails.getApprovalComent()))
            errors.reject(REJECTION_COMMENTS_REQUIRED);

        if (org.apache.commons.lang.StringUtils.isBlank(sewerageApplicationDetails.getCloseConnectionReason())
                && WFLOW_ACTION_STEP_FORWARD.equalsIgnoreCase(workFlowAction))
            errors.reject("err.closeconnection.remarks");
    }

}
