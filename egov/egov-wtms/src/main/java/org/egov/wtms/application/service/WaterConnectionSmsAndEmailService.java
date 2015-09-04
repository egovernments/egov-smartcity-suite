/**
 * eGov suite of products aim to improve the internal efficiency,transparency, accountability and the service delivery of the
 * government organizations.
 *
 * Copyright (C) <2015> eGovernments Foundation
 *
 * The updated version of eGov suite of products as by eGovernments Foundation is available at http://www.egovernments.org
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * http://www.gnu.org/licenses/ or http://www.gnu.org/licenses/gpl.html .
 *
 * In addition to the terms of the GPL license to be adhered to in using this program, the following additional terms are to be
 * complied with:
 *
 * 1) All versions of this program, verbatim or modified must carry this Legal Notice.
 *
 * 2) Any misrepresentation of the origin of the material is prohibited. It is required that all modified versions of this
 * material be marked in reasonable ways as different from the original version.
 *
 * 3) This license does not grant any rights to any user of the program with regards to rights under trademark law for use of the
 * trade names or trademarks of eGovernments Foundation.
 *
 * In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.wtms.application.service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.OwnerName;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.utils.PropertyExtnUtils;
import org.egov.wtms.utils.WaterTaxUtils;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

@Service
public class WaterConnectionSmsAndEmailService {

    @Autowired
    private PropertyExtnUtils propertyExtnUtils;

    @Autowired
    private WaterTaxUtils waterTaxUtils;

    private String applicantName;

    @Autowired
    private ResourceBundleMessageSource messageSource;

    public void setApplicantName(final String applicantName) {
        this.applicantName = applicantName;
    }

    public String getApplicantNameBYAssessmentDetail(final WaterConnectionDetails waterConnectionDetails)
    {
        final AssessmentDetails assessmentDetailsfullFlag = propertyExtnUtils.getAssessmentDetailsForFlag(
                waterConnectionDetails.getConnection().getPropertyIdentifier(),
                PropertyExternalService.FLAG_FULL_DETAILS);

        final Iterator<OwnerName> ownerNameItr = assessmentDetailsfullFlag.getOwnerNames().iterator();
        final StringBuilder consumerName = new StringBuilder();
        if (ownerNameItr.hasNext()) {
            consumerName.append(ownerNameItr.next().getOwnerName());
            while (ownerNameItr.hasNext())
                consumerName.append(", ".concat(ownerNameItr.next().getOwnerName()));
        }
        applicantName=consumerName.toString();
        return applicantName;
    }
    public void sendSmsAndEmail(final WaterConnectionDetails waterConnectionDetails, final String workFlowAction) {
        final AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                waterConnectionDetails.getConnection().getPropertyIdentifier(),
                PropertyExternalService.FLAG_MOBILE_EMAIL);
        final String email = assessmentDetails.getPrimaryEmail();
        final String mobileNumber = assessmentDetails.getPrimaryMobileNo();
        if (waterConnectionDetails != null && waterConnectionDetails.getApplicationType() != null
                && waterConnectionDetails.getApplicationType().getCode() != null
                && waterConnectionDetails.getStatus() != null && waterConnectionDetails.getStatus().getCode() != null) {
            getApplicantNameBYAssessmentDetail(waterConnectionDetails);
            // SMS and Email for new connection
            if (WaterTaxConstants.NEWCONNECTION.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode())) {
                if (waterConnectionDetails.getState().getHistory().isEmpty()
                        && WaterTaxConstants.APPLICATION_STATUS_CREATED.equalsIgnoreCase(waterConnectionDetails
                                .getStatus().getCode())) {
                    buildSMS(waterConnectionDetails, WaterTaxConstants.SMSEMAILTYPENEWCONNCREATE, mobileNumber);
                    buildEmail(waterConnectionDetails, WaterTaxConstants.SMSEMAILTYPENEWCONNCREATE, email);
                } else if (WaterTaxConstants.APPLICATION_STATUS_APPROVED.equalsIgnoreCase(waterConnectionDetails
                        .getStatus().getCode())) {
                    buildSMS(waterConnectionDetails, WaterTaxConstants.SMSEMAILTYPENEWCONNAPPROVE, mobileNumber);
                    buildEmail(waterConnectionDetails, WaterTaxConstants.SMSEMAILTYPENEWCONNAPPROVE, email);
                } else if (WaterTaxConstants.APPLICATION_STATUS_ESTIMATENOTICEGEN
                        .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
                    buildSMS(waterConnectionDetails, WaterTaxConstants.SMSEMAILTYPENEWCONNESTNOTICE, mobileNumber);
                    buildEmail(waterConnectionDetails, WaterTaxConstants.SMSEMAILTYPENEWCONNESTNOTICE, email);
                } else if (WaterTaxConstants.APPLICATION_STATUS_FEEPAID.equalsIgnoreCase(waterConnectionDetails
                        .getStatus().getCode())) {
                    buildSMS(waterConnectionDetails, WaterTaxConstants.SMSEMAILTYPENEWCONNFEEPAID, mobileNumber);
                    buildEmail(waterConnectionDetails, WaterTaxConstants.SMSEMAILTYPENEWCONNFEEPAID, email);
                } else if (WaterTaxConstants.APPLICATION_STATUS_SANCTIONED.equalsIgnoreCase(waterConnectionDetails
                        .getStatus().getCode())) {
                    buildSMS(waterConnectionDetails, WaterTaxConstants.SMSEMAILTYPENEWCONNEXECUTION, mobileNumber);
                    buildEmail(waterConnectionDetails, WaterTaxConstants.SMSEMAILTYPENEWCONNEXECUTION, email);
                }
            }
            // SMS and Email for additional connection
            else if (WaterTaxConstants.ADDNLCONNECTION.equalsIgnoreCase(waterConnectionDetails.getApplicationType()
                    .getCode())) {
                if (waterConnectionDetails.getState().getHistory().isEmpty()
                        && WaterTaxConstants.APPLICATION_STATUS_CREATED.equalsIgnoreCase(waterConnectionDetails
                                .getStatus().getCode())) {
                    buildSMS(waterConnectionDetails, WaterTaxConstants.SMSEMAILTYPEADDITONALCONNCREATE, mobileNumber);
                    buildEmail(waterConnectionDetails, WaterTaxConstants.SMSEMAILTYPEADDITONALCONNCREATE, email);
                } else if (WaterTaxConstants.APPLICATION_STATUS_APPROVED.equalsIgnoreCase(waterConnectionDetails
                        .getStatus().getCode())) {
                    buildSMS(waterConnectionDetails, WaterTaxConstants.SMSEMAILTYPEADDITONALCONNAPPROVE, mobileNumber);
                    buildEmail(waterConnectionDetails, WaterTaxConstants.SMSEMAILTYPEADDITONALCONNAPPROVE, email);
                } else if (WaterTaxConstants.APPLICATION_STATUS_ESTIMATENOTICEGEN
                        .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
                    buildSMS(waterConnectionDetails, WaterTaxConstants.SMSEMAILTYPEADDCONNESTNOTICE, mobileNumber);
                    buildEmail(waterConnectionDetails, WaterTaxConstants.SMSEMAILTYPEADDCONNESTNOTICE, email);
                } else if (WaterTaxConstants.APPLICATION_STATUS_FEEPAID.equalsIgnoreCase(waterConnectionDetails
                        .getStatus().getCode())) {
                    buildSMS(waterConnectionDetails, WaterTaxConstants.SMSEMAILTYPENEWCONNFEEPAID, mobileNumber);
                    buildEmail(waterConnectionDetails, WaterTaxConstants.SMSEMAILTYPENEWCONNFEEPAID, email);
                } else if (WaterTaxConstants.APPLICATION_STATUS_SANCTIONED.equalsIgnoreCase(waterConnectionDetails
                        .getStatus().getCode())) {
                    buildSMS(waterConnectionDetails, WaterTaxConstants.SMSEMAILTYPENEWCONNEXECUTION, mobileNumber);
                    buildEmail(waterConnectionDetails, WaterTaxConstants.SMSEMAILTYPENEWCONNEXECUTION, email);
                }
            }
            // SMS and email for change of use
            else if (WaterTaxConstants.CHANGEOFUSE.equalsIgnoreCase(waterConnectionDetails.getApplicationType()
                    .getCode()))
                if (waterConnectionDetails.getState().getHistory().isEmpty()
                        && WaterTaxConstants.APPLICATION_STATUS_CREATED.equalsIgnoreCase(waterConnectionDetails
                                .getStatus().getCode())) {
                    buildSMS(waterConnectionDetails, WaterTaxConstants.SMSEMAILTYPECHANGEOFUSECREATE, mobileNumber);
                    buildEmail(waterConnectionDetails, WaterTaxConstants.SMSEMAILTYPECHANGEOFUSECREATE, email);
                } else if (WaterTaxConstants.APPLICATION_STATUS_APPROVED.equalsIgnoreCase(waterConnectionDetails
                        .getStatus().getCode())) {
                    buildSMS(waterConnectionDetails, WaterTaxConstants.SMSEMAILTYPECHANGEOFUSEAPPROVE, mobileNumber);
                    buildEmail(waterConnectionDetails, WaterTaxConstants.SMSEMAILTYPECHANGEOFUSEAPPROVE, email);
                } else if (WaterTaxConstants.APPLICATION_STATUS_ESTIMATENOTICEGEN
                        .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
                    buildSMS(waterConnectionDetails, WaterTaxConstants.SMSEMAILTYPECHANGEOFUSENOTICE, mobileNumber);
                    buildEmail(waterConnectionDetails, WaterTaxConstants.SMSEMAILTYPECHANGEOFUSENOTICE, email);
                } else if (WaterTaxConstants.APPLICATION_STATUS_FEEPAID.equalsIgnoreCase(waterConnectionDetails
                        .getStatus().getCode())) {
                    buildSMS(waterConnectionDetails, WaterTaxConstants.SMSEMAILTYPECHANGEOFUSEFEEPAID, mobileNumber);
                    buildEmail(waterConnectionDetails, WaterTaxConstants.SMSEMAILTYPECHANGEOFUSEFEEPAID, email);
                } else if (WaterTaxConstants.APPLICATION_STATUS_SANCTIONED.equalsIgnoreCase(waterConnectionDetails
                        .getStatus().getCode())) {
                    buildSMS(waterConnectionDetails, WaterTaxConstants.SMSEMAILTYPECHANGEOFUSEEXECUTION, mobileNumber);
                    buildEmail(waterConnectionDetails, WaterTaxConstants.SMSEMAILTYPECHANGEOFUSEEXECUTION, email);
                }
        }
    }

    public void buildSMS(final WaterConnectionDetails waterConnectionDetails, final String type,
            final String mobileNumber) {
        if (mobileNumber != null) {
            String smsMsg = null;
            Boolean flag = Boolean.FALSE;
            if (waterTaxUtils.isSmsEnabled())
                if (type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPENEWCONNCREATE)) {
                    flag = Boolean.TRUE;
                    smsMsg = SmsBodyByCodeAndArgsWithType("msg.newconncetioncreate.sms", waterConnectionDetails,
                            applicantName, type);
                } else if (type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPEADDITONALCONNCREATE)) {
                    flag = Boolean.TRUE;
                    smsMsg = SmsBodyByCodeAndArgsWithType("msg.additionalconncetioncreate.sms", waterConnectionDetails,
                            applicantName, type);

                } else if (WaterTaxConstants.SMSEMAILTYPECHANGEOFUSECREATE.equalsIgnoreCase(type)) {
                    flag = Boolean.TRUE;
                    smsMsg = SmsBodyByCodeAndArgsWithType("msg.changeofuseconncetioncreate.sms",
                            waterConnectionDetails, applicantName, type);
                } else if (type.equals(WaterTaxConstants.SMSEMAILTYPENEWCONNAPPROVE)) {
                    flag = Boolean.TRUE;
                    smsMsg = SmsBodyByCodeAndArgsWithType("msg.newconncetionapproval.sms", waterConnectionDetails,
                            applicantName, type);
                } else if (type.equals(WaterTaxConstants.SMSEMAILTYPEADDITONALCONNAPPROVE)) {
                    flag = Boolean.TRUE;
                    smsMsg = SmsBodyByCodeAndArgsWithType("msg.additionalconncetionapproval.sms",
                            waterConnectionDetails, applicantName, type);
                } else if (type.equals(WaterTaxConstants.SMSEMAILTYPECHANGEOFUSEAPPROVE)) {
                    flag = Boolean.TRUE;
                    smsMsg = SmsBodyByCodeAndArgsWithType("msg.changeofuseconnection.approval.sms",
                            waterConnectionDetails, applicantName, type);
                } else if (WaterTaxConstants.SMSEMAILTYPENEWCONNEXECUTION.equalsIgnoreCase(type)
                        || WaterTaxConstants.SMSEMAILTYPECHANGEOFUSEEXECUTION.equalsIgnoreCase(type)) {
                    flag = Boolean.TRUE;
                    if (!WaterTaxConstants.METERED.toUpperCase().equalsIgnoreCase(
                            waterConnectionDetails.getConnectionType().toString()))
                        smsMsg = SmsBodyByCodeAndArgsWithType("msg.newconncetionOnExecutionDate.sms",
                                waterConnectionDetails, applicantName, type);
                    else
                        smsMsg = SmsBodyByCodeAndArgsWithType("msg.conncetionexeuction.metered.sms",
                                waterConnectionDetails, applicantName, type);
                } else if (WaterTaxConstants.SMSEMAILTYPENEWCONNESTNOTICE.equalsIgnoreCase(type)) {
                    flag = Boolean.TRUE;
                    if (!WaterTaxConstants.BPL_CATEGORY
                            .equalsIgnoreCase(waterConnectionDetails.getCategory().getName()))
                        smsMsg = SmsBodyByCodeAndArgsWithType("msg.newconncetionOnGenerateNotice.sms",
                                waterConnectionDetails, applicantName, type);
                    else
                        smsMsg = SmsBodyByCodeAndArgsWithType("msg.noticegen.for.bpl.sms", waterConnectionDetails,
                                applicantName, type);
                } else if (WaterTaxConstants.SMSEMAILTYPECHANGEOFUSENOTICE.equalsIgnoreCase(type)) {
                    flag = Boolean.TRUE;
                    if (!WaterTaxConstants.BPL_CATEGORY
                            .equalsIgnoreCase(waterConnectionDetails.getCategory().getName()))
                        smsMsg = SmsBodyByCodeAndArgsWithType("msg.changeofuseconnection.notice.gen",
                                waterConnectionDetails, applicantName, type);
                    else
                        smsMsg = SmsBodyByCodeAndArgsWithType("msg.noticegen.for.bpl.sms", waterConnectionDetails,
                                applicantName, type);
                } else if (WaterTaxConstants.SMSEMAILTYPENEWCONNFEEPAID.equalsIgnoreCase(type)
                        || WaterTaxConstants.SMSEMAILTYPEADDCONNFEEPAID.equalsIgnoreCase(type)
                        || WaterTaxConstants.SMSEMAILTYPECHANGEOFUSEFEEPAID.equals(type)) {
                    // TODO: as off now same message format is using for all 3
                    // connection for fees Paid SMS and Mail
                    flag = Boolean.TRUE;
                    smsMsg = SmsBodyByCodeAndArgsWithType("msg.newconncetionOnFeesPaid.sms", waterConnectionDetails,
                            applicantName, type);
                } else if (WaterTaxConstants.SMSEMAILTYPEADDCONNESTNOTICE.equalsIgnoreCase(type)) {
                    flag = Boolean.TRUE;
                    if (!WaterTaxConstants.BPL_CATEGORY
                            .equalsIgnoreCase(waterConnectionDetails.getCategory().getName()))
                        smsMsg = SmsBodyByCodeAndArgsWithType("msg.addconncetionOnGenerateNotice.sms",
                                waterConnectionDetails, applicantName, type);
                    else
                        smsMsg = SmsBodyByCodeAndArgsWithType("msg.noticegen.for.bpl.sms", waterConnectionDetails,
                                applicantName, type);
                }
            if (flag)
                waterTaxUtils.sendSMSOnWaterConnection(mobileNumber, smsMsg);
        }
    }

    public void buildEmail(final WaterConnectionDetails waterConnectionDetails, final String type, final String email) {
        if (email != null)
            if (waterTaxUtils.isEmailEnabled())
                try {
                    String body = "";
                    String subject = "";
                    Boolean flag = Boolean.FALSE;
                    if (type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPENEWCONNCREATE)) {
                        flag = Boolean.TRUE;
                        body = EmailBodyByCodeAndArgsWithType("msg.newconncetioncreate.email.body",
                                waterConnectionDetails, applicantName, type);
                        subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs(
                                "msg.newconncetioncreate.email.subject", waterConnectionDetails.getApplicationNumber());
                    } else if (type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPEADDITONALCONNCREATE)) {
                        flag = Boolean.TRUE;
                        body = EmailBodyByCodeAndArgsWithType("msg.additionalconnectioncreate.email.body",
                                waterConnectionDetails, applicantName, type);
                        subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs(
                                "msg.additionalconnectioncreate.email.subject",
                                waterConnectionDetails.getApplicationNumber());
                    } else if (WaterTaxConstants.SMSEMAILTYPECHANGEOFUSECREATE.equalsIgnoreCase(type)) {
                        flag = Boolean.TRUE;
                        body = EmailBodyByCodeAndArgsWithType("msg.changeofuseconncetioncreate.email.body",
                                waterConnectionDetails, applicantName, type);
                        subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs(
                                "msg.changeofuseconncetioncreate.email.subject",
                                waterConnectionDetails.getApplicationNumber());
                    } else if (type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPENEWCONNAPPROVE)) {
                        flag = Boolean.TRUE;
                        body = EmailBodyByCodeAndArgsWithType("msg.newconncetionapproval.email.body",
                                waterConnectionDetails, applicantName, type);
                        subject = waterTaxUtils
                                .emailSubjectforEmailByCodeAndArgs("msg.newconncetionapprove.email.subject",
                                        waterConnectionDetails.getApplicationNumber());
                    } else if (type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPEADDITONALCONNAPPROVE)) {
                        flag = Boolean.TRUE;
                        body = EmailBodyByCodeAndArgsWithType("msg.additionalconncetionapproval.email.body",
                                waterConnectionDetails, applicantName, type);
                        subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs(
                                "msg.additionalconncetionapproval.email.subject",
                                waterConnectionDetails.getApplicationNumber());
                    } else if (type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPECHANGEOFUSEAPPROVE)) {
                        flag = Boolean.TRUE;
                        body = EmailBodyByCodeAndArgsWithType("msg.changeofuseconnection.approval.email.body",
                                waterConnectionDetails, applicantName, type);
                        subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs(
                                "msg.changeofuseconnection.approval.subject",
                                waterConnectionDetails.getApplicationNumber());
                    } else if (WaterTaxConstants.SMSEMAILTYPENEWCONNEXECUTION.equalsIgnoreCase(type)
                            || WaterTaxConstants.SMSEMAILTYPECHANGEOFUSEEXECUTION.equalsIgnoreCase(type)) {
                        flag = Boolean.TRUE;
                        if (!WaterTaxConstants.METERED.toUpperCase().equalsIgnoreCase(
                                waterConnectionDetails.getConnectionType().toString()))
                            body = EmailBodyByCodeAndArgsWithType("msg.newconncetionOnExecutionDate.email.body",
                                    waterConnectionDetails, applicantName, type);
                        else
                            body = EmailBodyByCodeAndArgsWithType("msg.conncetionexeuction.metered.email.body",
                                    waterConnectionDetails, applicantName, type);
                        subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs(
                                "msg.newconncetionOnExecutionDate.email.subject", waterConnectionDetails
                                .getConnection().getConsumerCode());
                    } else if (WaterTaxConstants.SMSEMAILTYPENEWCONNESTNOTICE.equalsIgnoreCase(type)) {
                        if (!WaterTaxConstants.BPL_CATEGORY.equalsIgnoreCase(waterConnectionDetails.getCategory()
                                .getName())) {
                            flag = Boolean.TRUE;
                            body = EmailBodyByCodeAndArgsWithType("msg.newconncetionOnGenerateNotice.email.body",
                                    waterConnectionDetails, applicantName, type);
                            subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs(
                                    "msg.conncetionOnGenerateNotice.email.subject",
                                    waterConnectionDetails.getApplicationNumber());
                        } else {
                            flag = Boolean.TRUE;
                            body = EmailBodyByCodeAndArgsWithType("msg.noticegen.for.bpl.email.body",
                                    waterConnectionDetails, applicantName, type);
                            subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs(
                                    "msg.noticegen.for.bpl.email.subject",
                                    waterConnectionDetails.getApplicationNumber());
                        }
                    } else if (WaterTaxConstants.SMSEMAILTYPEADDCONNESTNOTICE.equalsIgnoreCase(type))
                        if (!WaterTaxConstants.BPL_CATEGORY.equalsIgnoreCase(waterConnectionDetails.getCategory()
                                .getName())) {
                            flag = Boolean.TRUE;
                            body = EmailBodyByCodeAndArgsWithType("msg.addconncetionOnGenerateNotice.email.body",
                                    waterConnectionDetails, applicantName, type);
                            subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs(
                                    "msg.conncetionOnGenerateNotice.email.subject",
                                    waterConnectionDetails.getApplicationNumber());
                        } else {
                            flag = Boolean.TRUE;
                            body = EmailBodyByCodeAndArgsWithType("msg.noticegen.for.bpl.email.body",
                                    waterConnectionDetails, applicantName, type);
                            subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs(
                                    "msg.noticegen.for.bpl.email.subject",
                                    waterConnectionDetails.getApplicationNumber());
                        }
                    else if (WaterTaxConstants.SMSEMAILTYPECHANGEOFUSENOTICE.equalsIgnoreCase(type))
                        if (!WaterTaxConstants.BPL_CATEGORY.equalsIgnoreCase(waterConnectionDetails.getCategory()
                                .getName())) {
                            flag = Boolean.TRUE;
                            body = EmailBodyByCodeAndArgsWithType("msg.changeofuseconnection.notice.email.body",
                                    waterConnectionDetails, applicantName, type);
                            subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs(
                                    "msg.changeofuseconnection.notice.email.subject",
                                    waterConnectionDetails.getApplicationNumber());
                        } else {
                            flag = Boolean.TRUE;
                            body = EmailBodyByCodeAndArgsWithType("msg.noticegen.for.bpl.email.body",
                                    waterConnectionDetails, applicantName, type);
                            subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs(
                                    "msg.noticegen.for.bpl.email.subject",
                                    waterConnectionDetails.getApplicationNumber());
                        }
                    else if (WaterTaxConstants.SMSEMAILTYPENEWCONNFEEPAID.equalsIgnoreCase(type)
                            || WaterTaxConstants.SMSEMAILTYPEADDCONNFEEPAID.equalsIgnoreCase(type)
                            || WaterTaxConstants.SMSEMAILTYPECHANGEOFUSEFEEPAID.equalsIgnoreCase(type)) {
                        flag = Boolean.TRUE;
                        body = EmailBodyByCodeAndArgsWithType("msg.addconncetionOnfeespaid.email.body",
                                waterConnectionDetails, applicantName, type);
                        final StringBuilder emailSubject = new StringBuilder(
                                " Demand and donation amount received for water tax application ");
                        emailSubject.append(waterConnectionDetails.getApplicationNumber());
                        subject = emailSubject.toString();
                    }
                    if (flag)
                        waterTaxUtils.sendEmailOnWaterConnection(email, body, subject);

                } catch (final EGOVRuntimeException egovExp) {
                    // emailMsg =messageSource.getMessage("email.failure.msg1");
                }
            else {
                // emailMsg= messageSource.getMessage("email.failure.msg1");
            }

    }

    public void sendSmsAndEmailOnRejection(final WaterConnectionDetails waterConnectionDetails,
            final String approvalComent) {
        if ((waterConnectionDetails.getApplicationType().getCode().equals(WaterTaxConstants.NEWCONNECTION)
                || waterConnectionDetails.getApplicationType().getCode()
                        .equalsIgnoreCase(WaterTaxConstants.ADDNLCONNECTION) || waterConnectionDetails
                .getApplicationType().getCode().equalsIgnoreCase(WaterTaxConstants.CHANGEOFUSE))
                && waterConnectionDetails.getStatus().getCode().equals(WaterTaxConstants.APPLICATION_STATUS_CANCELLED)) {
            final AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                    waterConnectionDetails.getConnection().getPropertyIdentifier(),
                    PropertyExternalService.FLAG_MOBILE_EMAIL);
            final String email = assessmentDetails.getPrimaryEmail();
            final String mobileNumber = assessmentDetails.getPrimaryMobileNo();
            getApplicantNameBYAssessmentDetail(waterConnectionDetails);
            if (mobileNumber != null)
                if (waterTaxUtils.isSmsEnabled()
                        && waterConnectionDetails.getApplicationType().getCode()
                                .equals(WaterTaxConstants.NEWCONNECTION)) {
                    final String smsMsg = waterTaxUtils.smsAndEmailBodyByCodeAndArgsForRejection(
                            "msg.newconncetionRejection.sms", approvalComent, applicantName);
                    waterTaxUtils.sendSMSOnWaterConnection(mobileNumber, smsMsg);
                } else if (waterTaxUtils.isSmsEnabled()
                        && waterConnectionDetails.getApplicationType().getCode()
                                .equals(WaterTaxConstants.ADDNLCONNECTION)) {
                    final String smsMsg = waterTaxUtils.smsAndEmailBodyByCodeAndArgsForRejection(
                            "msg.addconncetionRejection.sms", approvalComent, applicantName);
                    waterTaxUtils.sendSMSOnWaterConnection(mobileNumber, smsMsg);

                } else if (waterTaxUtils.isSmsEnabled()
                        && waterConnectionDetails.getApplicationType().getCode().equals(WaterTaxConstants.CHANGEOFUSE)) {
                    final String smsMsg = waterTaxUtils.smsAndEmailBodyByCodeAndArgsForRejection(
                            "msg.changeofuserejection.sms", approvalComent, applicantName);
                    waterTaxUtils.sendSMSOnWaterConnection(mobileNumber, smsMsg);
                }
            if (email != null)
                if (waterTaxUtils.isSmsEnabled()
                        && waterConnectionDetails.getApplicationType().getCode()
                                .equals(WaterTaxConstants.NEWCONNECTION)) {
                    final String body = waterTaxUtils.smsAndEmailBodyByCodeAndArgsForRejection(
                            "msg.newconncetionrejection.email.body", approvalComent, applicantName);
                    final String subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs(
                            "msg.newconncetionrejection.email.subject", waterConnectionDetails.getApplicationNumber());
                    waterTaxUtils.sendEmailOnWaterConnection(email, body, subject);
                } else if (waterTaxUtils.isSmsEnabled()
                        && waterConnectionDetails.getApplicationType().getCode()
                                .equals(WaterTaxConstants.ADDNLCONNECTION)) {
                    final String body = waterTaxUtils.smsAndEmailBodyByCodeAndArgsForRejection(
                            "msg.addconncetionrejection.email.body", approvalComent, applicantName);
                    final String subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs(
                            "msg.addconncetionrejection.email.subject", waterConnectionDetails.getApplicationNumber());
                    waterTaxUtils.sendEmailOnWaterConnection(email, body, subject);
                } else if (waterTaxUtils.isSmsEnabled()
                        && waterConnectionDetails.getApplicationType().getCode().equals(WaterTaxConstants.CHANGEOFUSE)) {
                    final String body = waterTaxUtils.smsAndEmailBodyByCodeAndArgsForRejection(
                            "msg.changeofuserejection.email.body", approvalComent, applicantName);
                    final String subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs(
                            "msg.changeofuserejection.email.subject", waterConnectionDetails.getApplicationNumber());
                    waterTaxUtils.sendEmailOnWaterConnection(email, body, subject);
                }
        }
    }

    public String smsAndEmailBodyByCodeAndArgs(final String code, final WaterConnectionDetails waterConnectionDetails,
            final String applicantName) {
        final String smsMsg = messageSource.getMessage(
                code,
                new String[] { applicantName, waterConnectionDetails.getApplicationNumber(),
                        waterTaxUtils.getCityName() }, null);
        return smsMsg;
    }

    public String EmailBodyByCodeAndArgsWithType(final String code,
            final WaterConnectionDetails waterConnectionDetails, final String applicantName, final String type) {
        String emailBody = "";
        final DecimalFormat amountFormat = new DecimalFormat("#.00");
        if (type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPENEWCONNCREATE)
                || type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPEADDITONALCONNCREATE))
            emailBody = messageSource.getMessage(
                    code,
                    new String[] { applicantName, waterConnectionDetails.getApplicationNumber(),
                            waterTaxUtils.getCityName() }, null);
        // Dear {0},\n\nYour New tap connection application is accepted and the
        // acknowledgement number is {1}.\n \nPlease use this number
        // as reference in all your future transactions.\n\nThis is computer
        // generated Email and does not need any signature and also
        // please do not reply to this e-mail.\n\nThanks ,\n{2}
        else if (type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPENEWCONNAPPROVE)
                || type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPEADDITONALCONNAPPROVE))
            emailBody = messageSource.getMessage(code,
                    new String[] { applicantName, waterConnectionDetails.getApplicationNumber(),
                    waterConnectionDetails.getConnection().getConsumerCode(), waterTaxUtils.getCityName() },
                    null);
        // Dear {0},\n\nThe water tap connection application with
        // Acknowledgement No. {1} has been approved with Consumer No. {2}
        // Monthly water tax will be generated after the tap execution.\n
        // \nPlease keep this Consumer Number for future transactions
        // on your water tap..\n\nThis is computer generated email and does not
        // need any signature and also please do not reply
        // to this e-mail.\n\nRegards,\n{3}
        else if (type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPENEWCONNESTNOTICE)
                || type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPEADDCONNESTNOTICE)) {

            if (!WaterTaxConstants.BPL_CATEGORY.equalsIgnoreCase(waterConnectionDetails.getCategory().getName()))
                emailBody = messageSource.getMessage(
                        code,
                        new String[] {
                                applicantName,
                                waterConnectionDetails.getApplicationNumber(),
                                String.valueOf(amountFormat.format(waterConnectionDetails.getDonationCharges())),
                                String.valueOf(amountFormat.format(waterConnectionDetails.getFieldInspectionDetails()
                                        .getEstimationCharges())),
                                        String.valueOf(amountFormat.format(waterConnectionDetails.getDonationCharges()
                                                + waterConnectionDetails.getFieldInspectionDetails().getEstimationCharges())),
                                                waterTaxUtils.getCityName() }, null);
            // Dear {0},\n\nWe have processed your application for new tap
            // connection with acknowledgement number {1}.and generated an
            // estimation
            // notice.\n\n Donation amount and Estimation amount for your
            // application will be Rs.{2}.00/-and Rs.{3}.00/- respectively .
            // We request you to pay the amount Rs.{4}.00/- ({2}+{3})at the ULB
            // counter. so that we can process your request for work
            // order.\n\nThis is computer generated Email and does not need any
            // signature and also please do not reply to this e-mail.\n\nThanks
            // ,\n{5}
            else
                emailBody = messageSource.getMessage(
                        code,
                        new String[] {
                                applicantName,
                                WaterTaxConstants.NEWCONNECTION.equalsIgnoreCase(waterConnectionDetails
                                        .getApplicationType().getCode()) ? "new water" : "additioanl water",
                                                waterConnectionDetails.getApplicationNumber(),
                                                String.valueOf(amountFormat.format(waterConnectionDetails.getFieldInspectionDetails()
                                                        .getEstimationCharges())), waterTaxUtils.getCityName() }, null);
            // Dear {0},\n\nWe have processed your application for {1} tap
            // connection with acknowledgement number {2} and generated an
            // estimation notice.\n Estimation amount for your application will
            // be Rs.{3}/-.
            // We request you to pay the same at the ULB counter,so that we can
            // process
            // your request for work order.\n\nThis is computer generated email
            // and does
            // not need any signature and also please do not reply to this
            // email.\n\nThanks ,\n{4}
        } else if (type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPENEWCONNEXECUTION)
                || type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPECHANGEOFUSEEXECUTION)) {
            final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            if (!WaterTaxConstants.METERED.toUpperCase().equalsIgnoreCase(
                    waterConnectionDetails.getConnectionType().toString()))
                emailBody = messageSource.getMessage(code,
                        new String[] { applicantName, waterConnectionDetails.getConnection().getConsumerCode(),
                        formatter.format(waterConnectionDetails.getExecutionDate()).toString(),
                        amountFormat.format(waterConnectionDetails.getDemand().getBaseDemand()).toString(),
                        waterTaxUtils.getCityName() }, null);
            else
                emailBody = messageSource.getMessage(
                        code,
                        new String[] { applicantName, waterConnectionDetails.getConnection().getConsumerCode(),
                        formatter.format(waterConnectionDetails.getExecutionDate()).toString(),
                        waterTaxUtils.getCityName() }, null);
            // Dear {0},\n\nWater tap connection with H.S.C number {1} is
            // installed at your site on {2} by our Asst engineer.
            // Please pay the tax before the due date to avail uninterrupted
            // service.\n\nThis is computer generated email and
            // does not need any signature and also please do not reply to this
            // email.\n\nThanks ,\n{3}
        }
        // Dear {0},\n\nWater tap connection with H.S.C number {1} is installed
        // at your site on {2} by our Asst engineer and your monthly
        // water tax demand will be Rs.{3}.00/-.Please pay the tax before the
        // due date to avail uninterrupted service.\n\nThis is computer
        // generated Email and does not need any signature and also please do
        // not reply to this e-mail.\n\nThanks ,\n{4}
        // TODO: while collectinge fees sending message not working with
        // MessageSourse Cos Its not able to find Message.properties
        // so hardcoding sms and Email body
        else if (type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPENEWCONNFEEPAID)
                || type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPEADDCONNFEEPAID)
                || type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPECHANGEOFUSEFEEPAID)) {
            final String amountToDisplay = String.valueOf(amountFormat.format(waterConnectionDetails
                    .getDonationCharges() + waterConnectionDetails.getFieldInspectionDetails().getEstimationCharges()));
            final StringBuffer emailBodyBuilder = new StringBuffer()
                    .append("Dear ")
                    .append(applicantName)
                    .append(",")
                    .append("\n\nWe have received Estimation and donation amount of Rs.")
                    .append(amountToDisplay)
                    .append("/- against your water connection application number ")
                    .append(waterConnectionDetails.getApplicationNumber())
                    .append(".We will be now processing your application to issue an work order.\n\nThis is computer generated email and does not need any signature and also please do not reply to this email.\n\nRegards,")
                    .append("\n").append(waterTaxUtils.getCityName());
            emailBody = emailBodyBuilder.toString();
        } else if (WaterTaxConstants.SMSEMAILTYPECHANGEOFUSECREATE.equalsIgnoreCase(type))
            emailBody = messageSource.getMessage(code,
                    new String[] { applicantName, waterConnectionDetails.getConnection().getConsumerCode(),
                    waterConnectionDetails.getApplicationNumber(), waterTaxUtils.getCityName() }, null);
        // Dear {0},\nYour application for change of use for the H.S.C number
        // {1} is accepted and the acknowledgement number
        // is {2}.\n
        // Please use this number as reference in all your future
        // transactions.\n\nThis is computer generated email and does
        // not need any
        // signature and also please do not reply to this email.\n\nThanks
        // ,\n{3}
        else if (WaterTaxConstants.SMSEMAILTYPECHANGEOFUSEAPPROVE.equalsIgnoreCase(type))
            emailBody = messageSource.getMessage(
                    code,
                    new String[] { applicantName, waterConnectionDetails.getApplicationNumber(),
                            waterTaxUtils.getCityName() }, null);
        // Dear {0},\n\nYour application for change of use is accepted with
        // acknowledgement No. {1} has been approved.
        // Your water tax will be generated after the tap execution.\n \nPlease
        // keep this Application Number for future
        // transactions on your water tap..\n\nThis is computer generated email
        // and does not need any signature and
        // also please do not reply to this email.\n\nRegards,\n{3}
        else if (WaterTaxConstants.SMSEMAILTYPECHANGEOFUSENOTICE.equalsIgnoreCase(type))
            if (!WaterTaxConstants.BPL_CATEGORY.equalsIgnoreCase(waterConnectionDetails.getCategory().getName()))
                emailBody = messageSource.getMessage(
                        code,
                        new String[] {
                                applicantName,
                                waterConnectionDetails.getApplicationNumber(),
                                String.valueOf(amountFormat.format(waterConnectionDetails.getDonationCharges())),
                                String.valueOf(amountFormat.format(waterConnectionDetails.getFieldInspectionDetails()
                                        .getEstimationCharges())),
                                        String.valueOf(amountFormat.format(waterConnectionDetails.getDonationCharges()
                                                + waterConnectionDetails.getFieldInspectionDetails().getEstimationCharges())),
                                                waterTaxUtils.getCityName() }, null);
        // Dear {0},\nWe have processed your application for change of use
            // request with acknowledgement number {1}.and
        // generated an
        // estimation notice.\n\n Donation amount and Estimation amount for
            // your
        // application will be Rs.{2}.00/-and Rs.{3}.00/- respectively .
        // We request you to pay the amount Rs.{4}.00/- ({2}+{3})at the ULB
        // counter. so that we can process your request for work
        // order.\n\nThis is computer generated Email and does not need any
        // signature and also please do not reply to this e-mail.\n\nThanks
        // ,\n{5}
            else
                emailBody = messageSource.getMessage(
                        code,
                        new String[] {
                                applicantName,
                                "change of",
                                waterConnectionDetails.getApplicationNumber(),
                                String.valueOf(amountFormat.format(waterConnectionDetails.getFieldInspectionDetails()
                                        .getEstimationCharges())), waterTaxUtils.getCityName() }, null);
        // Dear {0},\n\nWe have processed your application for {1} tap
        // connection with acknowledgement number {2} and generated an
        // estimation notice.\n Estimation amount for your application will be
        // Rs.{3}/-.
        // We request you to pay the same at the ULB counter,so that we can
        // process
        // your request for work order.\n\nThis is computer generated email and
        // does
        // not need any signature and also please do not reply to this
        // email.\n\nThanks ,\n{4}

        return emailBody;
    }

    public String SmsBodyByCodeAndArgsWithType(final String code, final WaterConnectionDetails waterConnectionDetails,
            final String applicantName, final String type) {
        String smsMsg = "";
        final DecimalFormat amountFormat = new DecimalFormat("#.00");
        if (type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPENEWCONNCREATE)
                || type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPEADDITONALCONNCREATE))
            smsMsg = messageSource.getMessage(
                    code,
                    new String[] { applicantName, waterConnectionDetails.getApplicationNumber(),
                            waterTaxUtils.getCityName() }, null);
        // Dear {0}, Your New tap connection application is accepted and the
        // acknowledgement number is {1}. Please use this number as
        // reference in all your future transactions.\nThanks, {2}
        else if (type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPENEWCONNAPPROVE)
                || type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPEADDITONALCONNAPPROVE))
            smsMsg = messageSource.getMessage(code, new String[] { applicantName,
                    waterConnectionDetails.getConnection().getConsumerCode(), waterTaxUtils.getCityName() }, null);
        // Dear {0}, Your new water tap connection application processed with
        // Consumer No.{1}. Monthly water tax will be generated after
        // the tap execution..\nThanks, {2}
        else if (type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPENEWCONNESTNOTICE)
                || type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPEADDCONNESTNOTICE)) {
            if (!WaterTaxConstants.BPL_CATEGORY.equalsIgnoreCase(waterConnectionDetails.getCategory().getName()))
                smsMsg = messageSource.getMessage(
                        code,
                        new String[] {
                                applicantName,
                                waterConnectionDetails.getApplicationNumber(),
                                String.valueOf(amountFormat.format(waterConnectionDetails.getDonationCharges())),
                                String.valueOf(amountFormat.format(waterConnectionDetails.getFieldInspectionDetails()
                                        .getEstimationCharges())),
                                        String.valueOf(amountFormat.format(waterConnectionDetails.getDonationCharges()
                                                + waterConnectionDetails.getFieldInspectionDetails().getEstimationCharges())),
                                                waterTaxUtils.getCityName() }, null);
            // --- for NON BPL --
            // Dear {0}, We have processed your application for new tap
            // connection with acknowledgement number {1} and generated an
            // estimation
            // notice.\n Donation amount and Estimation amount for your
            // application will
            // be Rs.{2}.00/-and Rs.{3}.00/- respectively .We request you to pay
            // the amount Rs.{4}.00/- ({2}+{3})at the ULB counter. so that we
            // can
            // process your request for work order.\nThanks, {5}
            else
                smsMsg = messageSource.getMessage(
                        code,
                        new String[] {
                                applicantName,
                                WaterTaxConstants.NEWCONNECTION.equalsIgnoreCase(waterConnectionDetails
                                        .getApplicationType().getCode()) ? "new water" : "additional water",
                                                waterConnectionDetails.getApplicationNumber(),
                                                String.valueOf(amountFormat.format(waterConnectionDetails.getFieldInspectionDetails()
                                                        .getEstimationCharges())), waterTaxUtils.getCityName() }, null);
            // -- for BPL --
            // Dear {0}, We have processed your application for {1} tap
            // connection with acknowledgement number {2} and generated an
            // estimation notice.\n\n Estimation amount for your application
            // will be Rs.{3}/-. We request you to pay the same at the
            // ULB counter,so that we can process your request for work
            // order.\nThanks, {4}
        } else if (type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPENEWCONNEXECUTION)
                || type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPECHANGEOFUSEEXECUTION)) {
            final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            if (!WaterTaxConstants.METERED.toUpperCase().equalsIgnoreCase(
                    waterConnectionDetails.getConnectionType().toString()))
                smsMsg = messageSource.getMessage(code,
                        new String[] { applicantName, waterConnectionDetails.getConnection().getConsumerCode(),
                        formatter.format(waterConnectionDetails.getExecutionDate()).toString(),
                        amountFormat.format(waterConnectionDetails.getDemand().getBaseDemand()).toString(),
                        waterTaxUtils.getCityName() }, null);
            else
                smsMsg = messageSource.getMessage(
                        code,
                        new String[] { applicantName, waterConnectionDetails.getConnection().getConsumerCode(),
                        formatter.format(waterConnectionDetails.getExecutionDate()).toString(),
                                waterTaxUtils.getCityName() }, null);
            // Dear {0}, Water tap connection with H.S.C number {1} is installed
            // at your site on {2} by our Asst engineer.
            // Please pay the tax before the due date to avail uninterrupted
            // service.\nThanks, {3}
        }
        // Dear {0}, Water tap connection with H.S.C number {1} is installed at
        // your site on {2} by our Asst engineer and your
        // demand will be Rs.{3}.00/-.Please pay the tax before the due date
        // to avail uninterrupted service.\nThanks, {4}
        else if (type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPENEWCONNFEEPAID)
                || type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPEADDCONNFEEPAID)
                || type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPECHANGEOFUSEFEEPAID)) {
            final String amountToDisplay = String.valueOf(amountFormat.format(waterConnectionDetails
                    .getDonationCharges() + waterConnectionDetails.getFieldInspectionDetails().getEstimationCharges()));
            final StringBuffer smsBody = new StringBuffer().append("Dear ").append(applicantName)
                    .append(",We have received Estimation and donation amount of Rs.").append(amountToDisplay)
                    .append("/- against your water connection application number ")
                    .append(waterConnectionDetails.getApplicationNumber())
                    .append(".We will be now processing your application to issue an work order.\nThanks,\n")
                    .append(waterTaxUtils.getCityName());
            smsMsg = smsBody.toString();
        } else if (WaterTaxConstants.SMSEMAILTYPECHANGEOFUSECREATE.equalsIgnoreCase(type))
            smsMsg = messageSource.getMessage(code,
                    new String[] { applicantName, waterConnectionDetails.getConnection().getConsumerCode(),
                    waterConnectionDetails.getApplicationNumber(), waterTaxUtils.getCityName() }, null);
        else if (WaterTaxConstants.SMSEMAILTYPECHANGEOFUSEAPPROVE.equalsIgnoreCase(type))
            smsMsg = messageSource.getMessage(
                    code,
                    new String[] { applicantName, waterConnectionDetails.getApplicationNumber(),
                            waterTaxUtils.getCityName() }, null);
        // Dear {0}, Your application request for change of use is accepted with
        // acknowledgement No.{1}.
        // Please use this number in all future communication..\nThanks, {2}
        else if (WaterTaxConstants.SMSEMAILTYPECHANGEOFUSENOTICE.equalsIgnoreCase(type))
            if (!WaterTaxConstants.BPL_CATEGORY.equalsIgnoreCase(waterConnectionDetails.getCategory().getName()))
                smsMsg = messageSource.getMessage(
                        code,
                        new String[] {
                                applicantName,
                                waterConnectionDetails.getApplicationNumber(),
                                String.valueOf(amountFormat.format(waterConnectionDetails.getDonationCharges())),
                                String.valueOf(amountFormat.format(waterConnectionDetails.getFieldInspectionDetails()
                                        .getEstimationCharges())),
                                        String.valueOf(amountFormat.format(waterConnectionDetails.getDonationCharges()
                                                + waterConnectionDetails.getFieldInspectionDetails().getEstimationCharges())),
                                                waterTaxUtils.getCityName() }, null);
        // Dear {0}, We have processed your application for change of use
            // request with acknowledgement number {1}
        // and generated an estimation notice.\n\n Donation amount and
            // Estimation amount for your application will
        // be Rs.{2}/- and Rs.{3}/- respectively. We request you to pay the
            // amount Rs.{4}/- ({2}+{3}) at the ULB
        // counter, so that we can process your request for work
            // order.\nThanks, {5}
            else
                smsMsg = messageSource.getMessage(
                        code,
                        new String[] {
                                applicantName,
                                "change of",
                                waterConnectionDetails.getApplicationNumber(),
                                String.valueOf(amountFormat.format(waterConnectionDetails.getFieldInspectionDetails()
                                        .getEstimationCharges())), waterTaxUtils.getCityName() }, null);
        // -- for BPL --
        // Dear {0}, We have processed your application for {1} tap
        // connection with acknowledgement number {2} and generated an
        // estimation notice.\n\n Estimation amount for your application
        // will be Rs.{3}/-. We request you to pay the same at the
        // ULB counter,so that we can process your request for work
        // order.\nThanks, {4}
        return smsMsg;
    }

}
