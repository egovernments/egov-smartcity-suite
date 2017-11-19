
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
package org.egov.lcms.transactions.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.egov.eis.entity.Employee;
import org.egov.lcms.masters.entity.enums.JudgmentImplIsComplied;
import org.egov.lcms.transactions.entity.CounterAffidavit;
import org.egov.lcms.transactions.entity.EmployeeHearing;
import org.egov.lcms.transactions.entity.Hearings;
import org.egov.lcms.transactions.entity.Judgment;
import org.egov.lcms.transactions.entity.JudgmentImpl;
import org.egov.lcms.transactions.entity.LegalCase;
import org.egov.lcms.transactions.entity.LegalCaseAdvocate;
import org.egov.lcms.transactions.entity.LegalCaseDisposal;
import org.egov.lcms.transactions.entity.LegalCaseInterimOrder;
import org.egov.lcms.transactions.entity.Pwr;
import org.egov.lcms.utils.LegalCaseUtil;
import org.egov.lcms.utils.constants.LcmsConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LegalCaseSmsService {

    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource messageSource;

    @Autowired
    private LegalCaseUtil legalCaseUtil;

    // Send SMS Notification to OfficerIncharge For Legalcase
    public void sendSmsToOfficerInchargeForLegalCase(final LegalCase legalcase) {
        if (legalcase.getOfficerIncharge() != null && legalcase.getOfficerIncharge().getName() != null) {
            final String mobileNumber = legalCaseUtil.getOfficerInchargeMobileNumber(legalcase);
            if (LcmsConstants.LEGALCASE_STATUS_CREATED.equalsIgnoreCase(legalcase.getStatus().getCode()))
                getSmsForLegalCase(legalcase, mobileNumber);

        }
    }

    public void getSmsForLegalCase(final LegalCase legalcase, final String mobileNo) {
        final String smsMsg = smsBodyByCodeAndArgsWithTypeForLegalcase("msg.createlegalcase.sms", legalcase);

        if (StringUtils.isNotBlank(mobileNo) && StringUtils.isNotBlank(smsMsg))
            legalCaseUtil.sendSMSOnLegalCase(mobileNo, smsMsg);

    }

    public String smsBodyByCodeAndArgsWithTypeForLegalcase(final String code, final LegalCase legalcase) {
        final String smsMsg = messageSource.getMessage(code,
                new String[] { legalCaseUtil.getOfficerInchargeName(legalcase), legalcase.getCaseNumber(),
                        legalcase.getPetitionersNames(), legalcase.getRespondantNames() },
                null);
        return smsMsg;
    }

    // Send SMS Notification to the Employee Added in Hearing Screen
    public void sendSmsToHearingEmployee(final Hearings hearings) {
        if (hearings.getTempEmplyeeHearing() != null && !hearings.getTempEmplyeeHearing().isEmpty())
            for (final EmployeeHearing hearingEmp : hearings.getTempEmplyeeHearing())
                getSmsForHearingsEmployee(hearings, hearingEmp.getEmployee());

    }

    public void getSmsForHearingsEmployee(final Hearings hearings, final Employee employee) {
        final String mobileNo = employee.getMobileNumber();
        if (LcmsConstants.LEGALCASE_HEARING_STATUS.equalsIgnoreCase(hearings.getLegalCase().getStatus().getCode())) {
            final String smsMsg = smsBodyByCodeAndArgsWithTypeForHearingsEmployee("msg.hearingemployee.sms", hearings,
                    employee);

            if (StringUtils.isNotBlank(mobileNo) && StringUtils.isNotBlank(smsMsg))
                legalCaseUtil.sendSMSOnLegalCase(mobileNo, smsMsg);
        }

    }

    public String smsBodyByCodeAndArgsWithTypeForHearingsEmployee(final String code, final Hearings hearings,
            final Employee employee) {
        final String smsMsg = messageSource.getMessage(code,
                new String[] { employee.getName(), hearings.getLegalCase().getCaseNumber(),
                        LcmsConstants.DATEFORMATTER_DD_MM_YYYY.format(hearings.getHearingDate()).toString(),
                        hearings.getPurposeofHearings() },
                null);
        return smsMsg;
    }

    // Send SMS Notification to OfficerIncharge For InterimOrder
    public void sendSmsToOfficerInchargeInterimOrder(final LegalCaseInterimOrder legalCaseInterimOrder) {
        if (legalCaseInterimOrder.getLegalCase().getOfficerIncharge() != null
                && legalCaseInterimOrder.getLegalCase().getOfficerIncharge().getName() != null) {
            final String mobileNumber = legalCaseUtil
                    .getOfficerInchargeMobileNumber(legalCaseInterimOrder.getLegalCase());

            if (LcmsConstants.LEGALCASE_INTERIMSTAY_STATUS
                    .equalsIgnoreCase(legalCaseInterimOrder.getLegalCase().getStatus().getCode()))
                getSmsForInterimOrder(legalCaseInterimOrder, mobileNumber);
        }

    }

    public void getSmsForInterimOrder(final LegalCaseInterimOrder legalCaseInterimOrder, final String mobileNo) {
        final String smsMsg = smsBodyByCodeAndArgsWithTypeForInterimOrder("msg.interimorder.sms",
                legalCaseInterimOrder);

        if (StringUtils.isNotBlank(mobileNo) && StringUtils.isNotBlank(smsMsg))
            legalCaseUtil.sendSMSOnLegalCase(mobileNo, smsMsg);

    }

    public String smsBodyByCodeAndArgsWithTypeForInterimOrder(final String code,
            final LegalCaseInterimOrder legalCaseInterimOrder) {
        final String smsMsg = messageSource.getMessage(code,
                new String[] { legalCaseUtil.getOfficerInchargeName(legalCaseInterimOrder.getLegalCase()),
                        legalCaseInterimOrder.getLegalCase().getCaseNumber(),
                        legalCaseInterimOrder.getInterimOrder().getInterimOrderType(),
                        LcmsConstants.DATEFORMATTER_DD_MM_YYYY.format(legalCaseInterimOrder.getIoDate()).toString() },
                null);
        return smsMsg;
    }

    // Send SMS Notification to OfficerIncharge For Hearings
    public void sendSmsToOfficerInchargeForHearings(final Hearings hearings) {
        if (hearings.getLegalCase().getOfficerIncharge() != null
                && hearings.getLegalCase().getOfficerIncharge().getName() != null) {
            final String mobileNumber = legalCaseUtil.getOfficerInchargeMobileNumber(hearings.getLegalCase());

            if (LcmsConstants.LEGALCASE_HEARING_STATUS.equalsIgnoreCase(hearings.getLegalCase().getStatus().getCode()))
                getSmsForHearings(hearings, mobileNumber);
        }

    }

    public void getSmsForHearings(final Hearings hearings, final String mobileNo) {
        final String smsMsg = smsBodyByCodeAndArgsWithTypeForHearings("msg.hearing.sms", hearings);

        if (StringUtils.isNotBlank(mobileNo) && StringUtils.isNotBlank(smsMsg))
            legalCaseUtil.sendSMSOnLegalCase(mobileNo, smsMsg);

    }

    public String smsBodyByCodeAndArgsWithTypeForHearings(final String code, final Hearings hearings) {
        final String smsMsg = messageSource.getMessage(code,
                new String[] { legalCaseUtil.getOfficerInchargeName(hearings.getLegalCase()),
                        hearings.getLegalCase().getCaseNumber(),
                        LcmsConstants.DATEFORMATTER_DD_MM_YYYY.format(hearings.getHearingDate()).toString(),
                        hearings.getPurposeofHearings() },
                null);
        return smsMsg;
    }

    // Send SMS Notification to OfficerIncharge For Judgment
    public void sendSmsToOfficerInchargeForJudgment(final Judgment judgment) {
        if (judgment.getLegalCase().getOfficerIncharge() != null
                && judgment.getLegalCase().getOfficerIncharge().getName() != null) {
            final String mobileNumber = legalCaseUtil.getOfficerInchargeMobileNumber(judgment.getLegalCase());

            if (LcmsConstants.LEGALCASE_STATUS_JUDGMENT.equalsIgnoreCase(judgment.getLegalCase().getStatus().getCode()))
                getSmsForJudgment(judgment, mobileNumber);
        }

    }

    public void getSmsForJudgment(final Judgment judgment, final String mobileNo) {
        String smsMsg = "";
        if (judgment.getImplementByDate() != null)
            smsMsg = smsBodyByCodeAndArgsWithTypeForJudgmentWithImplementDate("msg.judgmentwithimplementdate.sms",
                    judgment);
        else
            smsMsg = smsBodyByCodeAndArgsWithTypeForJudgmentWithoutImplementDate("msg.judgmentwithoutimplementDate.sms",
                    judgment);

        if (StringUtils.isNotBlank(mobileNo) && StringUtils.isNotBlank(smsMsg))
            legalCaseUtil.sendSMSOnLegalCase(mobileNo, smsMsg);

    }

    public String smsBodyByCodeAndArgsWithTypeForJudgmentWithImplementDate(final String code, final Judgment judgment) {
        final String smsMsg = messageSource.getMessage(code,
                new String[] { legalCaseUtil.getOfficerInchargeName(judgment.getLegalCase()),
                        judgment.getLegalCase().getCaseNumber(), judgment.getJudgmentType().getName(),
                        LcmsConstants.DATEFORMATTER_DD_MM_YYYY.format(judgment.getImplementByDate()).toString() },
                null);
        return smsMsg;
    }

    public String smsBodyByCodeAndArgsWithTypeForJudgmentWithoutImplementDate(final String code,
            final Judgment judgment) {
        final String smsMsg = messageSource.getMessage(code,
                new String[] { legalCaseUtil.getOfficerInchargeName(judgment.getLegalCase()),
                        judgment.getLegalCase().getCaseNumber(), judgment.getJudgmentType().getName() },
                null);
        return smsMsg;
    }

    // Send SMS Notification to OfficerIncharge For JudgmentImpl
    public void sendSmsToOfficerInchargeForJudgmentImpl(final JudgmentImpl judgmentImpl) {
        if (judgmentImpl.getJudgment().getLegalCase().getOfficerIncharge() != null
                && judgmentImpl.getJudgment().getLegalCase().getOfficerIncharge().getName() != null) {
            final String mobileNumber = legalCaseUtil
                    .getOfficerInchargeMobileNumber(judgmentImpl.getJudgment().getLegalCase());

            if (LcmsConstants.LEGALCASE_STATUS_JUDGMENT_IMPLIMENTED
                    .equalsIgnoreCase(judgmentImpl.getJudgment().getLegalCase().getStatus().getCode()))
                getSmsForJudgmentImpl(judgmentImpl, mobileNumber);
        }

    }

    public void getSmsForJudgmentImpl(final JudgmentImpl judgmentImpl, final String mobileNo) {
        String smsMsg = "";
        if (judgmentImpl.getJudgmentImplIsComplied().toString().equals(JudgmentImplIsComplied.YES.toString()))
            smsMsg = smsBodyByCodeAndArgsWithTypeForJudgmentImplIsCompliedYes("msg.judgmentimpliscompliedyes.sms",
                    judgmentImpl);
        else if (judgmentImpl.getJudgmentImplIsComplied().toString().equals(JudgmentImplIsComplied.NO.toString()))
            smsMsg = smsBodyByCodeAndArgsWithTypeForJudgmentImplIsCompliedNo("msg.judgmentimpliscompliedno.sms",
                    judgmentImpl);

        if (StringUtils.isNotBlank(mobileNo) && StringUtils.isNotBlank(smsMsg))
            legalCaseUtil.sendSMSOnLegalCase(mobileNo, smsMsg);

    }

    public String smsBodyByCodeAndArgsWithTypeForJudgmentImplIsCompliedYes(final String code,
            final JudgmentImpl judgmentImpl) {
        final String smsMsg = messageSource.getMessage(code,
                new String[] { legalCaseUtil.getOfficerInchargeName(judgmentImpl.getJudgment().getLegalCase()),
                        judgmentImpl.getJudgment().getLegalCase().getCaseNumber(),
                        judgmentImpl.getJudgmentImplIsComplied().toString(),
                        LcmsConstants.DATEFORMATTER_DD_MM_YYYY.format(judgmentImpl.getDateOfCompliance()).toString() },
                null);
        return smsMsg;
    }

    public String smsBodyByCodeAndArgsWithTypeForJudgmentImplIsCompliedNo(final String code,
            final JudgmentImpl judgmentImpl) {
        final String smsMsg = messageSource.getMessage(code,
                new String[] { legalCaseUtil.getOfficerInchargeName(judgmentImpl.getJudgment().getLegalCase()),
                        judgmentImpl.getJudgment().getLegalCase().getCaseNumber(),
                        judgmentImpl.getJudgmentImplIsComplied().toString(),
                        judgmentImpl.getImplementationFailure().toString() },
                null);
        return smsMsg;
    }

    // Send SMS Notification to OfficerIncharge For CloseCase
    public void sendSmsToOfficerInchargeForCloseCase(final LegalCaseDisposal legalCaseDisposal) {
        if (legalCaseDisposal.getLegalCase().getOfficerIncharge() != null
                && legalCaseDisposal.getLegalCase().getOfficerIncharge().getName() != null) {
            final String mobileNumber = legalCaseUtil.getOfficerInchargeMobileNumber(legalCaseDisposal.getLegalCase());

            if (LcmsConstants.LEGALCASE_STATUS_CLOSED
                    .equalsIgnoreCase(legalCaseDisposal.getLegalCase().getStatus().getCode()))
                getSmsForCloseCase(legalCaseDisposal, mobileNumber);
        }

    }

    public void getSmsForCloseCase(final LegalCaseDisposal legalCaseDisposal, final String mobileNo) {
        final String smsMsg = smsBodyByCodeAndArgsWithTypeForCloseCase("msg.closecase.sms", legalCaseDisposal);

        if (StringUtils.isNotBlank(mobileNo) && StringUtils.isNotBlank(smsMsg))
            legalCaseUtil.sendSMSOnLegalCase(mobileNo, smsMsg);

    }

    public String smsBodyByCodeAndArgsWithTypeForCloseCase(final String code,
            final LegalCaseDisposal legalCaseDisposal) {
        final String smsMsg = messageSource.getMessage(code,
                new String[] { legalCaseUtil.getOfficerInchargeName(legalCaseDisposal.getLegalCase()),
                        legalCaseDisposal.getLegalCase().getCaseNumber(), LcmsConstants.DATEFORMATTER_DD_MM_YYYY
                                .format(legalCaseDisposal.getDisposalDate()).toString() },
                null);
        return smsMsg;
    }

    // Send SMS Notification to OfficerIncharge For CA
    public void sendSmsToOfficerInchargeForCounterAffidavit(final List<CounterAffidavit> counterAffidavit) {
        if (counterAffidavit.get(0).getLegalCase().getOfficerIncharge() != null
                && counterAffidavit.get(0).getLegalCase().getOfficerIncharge().getName() != null) {
            final String mobileNumber = legalCaseUtil
                    .getOfficerInchargeMobileNumber(counterAffidavit.get(0).getLegalCase());
            getSmsForCounterAffidavit(counterAffidavit, mobileNumber);
        }

    }

    public void getSmsForCounterAffidavit(final List<CounterAffidavit> counterAffidavit, final String mobileNo) {
        String smsMsg = "";
        if (counterAffidavit.get(0).getCounterAffidavitDueDate() != null)
            smsMsg = smsBodyByCodeAndArgsWithTypeForCounterAffidavitwithSubmissionDate(
                    "msg.counteraffidavitsubmissiondate.sms", counterAffidavit);

        if (counterAffidavit.get(0).getCounterAffidavitApprovalDate() != null)
            smsMsg = smsBodyByCodeAndArgsWithTypeForCounterAffidavitwithApprovalDate(
                    "msg.counteraffidavitapprovaldate.sms", counterAffidavit);

        if (StringUtils.isNotBlank(mobileNo) && StringUtils.isNotBlank(smsMsg))
            legalCaseUtil.sendSMSOnLegalCase(mobileNo, smsMsg);

    }

    public String smsBodyByCodeAndArgsWithTypeForCounterAffidavitwithSubmissionDate(final String code,
            final List<CounterAffidavit> counterAffidavit) {
        final String smsMsg = messageSource
                .getMessage(code,
                        new String[] { legalCaseUtil.getOfficerInchargeName(counterAffidavit.get(0).getLegalCase()),
                                counterAffidavit.get(0).getLegalCase().getCaseNumber(),
                                LcmsConstants.DATEFORMATTER_DD_MM_YYYY
                                        .format(counterAffidavit.get(0).getCounterAffidavitDueDate()).toString() },
                        null);
        return smsMsg;
    }

    public String smsBodyByCodeAndArgsWithTypeForCounterAffidavitwithApprovalDate(final String code,
            final List<CounterAffidavit> counterAffidavit) {
        final String smsMsg = messageSource
                .getMessage(code,
                        new String[] { legalCaseUtil.getOfficerInchargeName(counterAffidavit.get(0).getLegalCase()),
                                counterAffidavit.get(0).getLegalCase().getCaseNumber(),
                                LcmsConstants.DATEFORMATTER_DD_MM_YYYY
                                        .format(counterAffidavit.get(0).getCounterAffidavitApprovalDate()).toString() },
                        null);
        return smsMsg;
    }

    // Send SMS Notification to OfficerIncharge For Pwr
    public void sendSmsToOfficerInchargeForPWR(final List<Pwr> pwr) {
        if (pwr.get(0).getLegalCase().getOfficerIncharge() != null
                && pwr.get(0).getLegalCase().getOfficerIncharge().getName() != null) {
            final String mobileNumber = legalCaseUtil.getOfficerInchargeMobileNumber(pwr.get(0).getLegalCase());
            getSmsForPWR(pwr, mobileNumber);
        }

    }

    public void getSmsForPWR(final List<Pwr> pwr, final String mobileNo) {
        String smsMsg = "";
        if (pwr.get(0).getPwrDueDate() != null)
            smsMsg = smsBodyByCodeAndArgsWithTypeForPwrWithSubmissionDate("msg.pwrwithsubmissiondate.sms", pwr);
        if (pwr.get(0).getPwrApprovalDate() != null)
            smsMsg = smsBodyByCodeAndArgsWithTypeForPwrwithApprovalDate("msg.pwrwithapprovaldate.sms", pwr);

        if (StringUtils.isNotBlank(mobileNo) && StringUtils.isNotBlank(smsMsg))
            legalCaseUtil.sendSMSOnLegalCase(mobileNo, smsMsg);

    }

    public String smsBodyByCodeAndArgsWithTypeForPwrWithSubmissionDate(final String code, final List<Pwr> pwr) {
        final String smsMsg = messageSource.getMessage(code,
                new String[] { legalCaseUtil.getOfficerInchargeName(pwr.get(0).getLegalCase()),
                        pwr.get(0).getLegalCase().getCaseNumber(),
                        LcmsConstants.DATEFORMATTER_DD_MM_YYYY.format(pwr.get(0).getPwrDueDate()).toString() },
                null);
        return smsMsg;
    }

    public String smsBodyByCodeAndArgsWithTypeForPwrwithApprovalDate(final String code, final List<Pwr> pwr) {
        final String smsMsg = messageSource.getMessage(code,
                new String[] { legalCaseUtil.getOfficerInchargeName(pwr.get(0).getLegalCase()),
                        pwr.get(0).getLegalCase().getCaseNumber(), LcmsConstants.DATEFORMATTER_DD_MM_YYYY
                                .format(pwr.get(0).getPwrApprovalDate()).toString() },
                null);
        return smsMsg;
    }

    // Send SMS TO Add Standing Counsel
    public void sendSmsToStandingCounsel(final LegalCaseAdvocate legalCaseAdvocate) {
        if (legalCaseAdvocate.getAdvocateMaster() != null && legalCaseAdvocate.getAdvocateMaster().getName() != null) {
            final String mobileNumber = legalCaseAdvocate.getAdvocateMaster().getMobileNumber();
            getSmsForStandingCounsel(legalCaseAdvocate, mobileNumber);
        }

    }

    public void getSmsForStandingCounsel(final LegalCaseAdvocate legalCaseAdvocate, final String mobileNo) {
        final String smsMsg = smsBodyByCodeAndArgsWithTypeForStandingCounsel("msg.standingcounsel.sms",
                legalCaseAdvocate);

        if (StringUtils.isNotBlank(mobileNo) && StringUtils.isNotBlank(smsMsg))
            legalCaseUtil.sendSMSOnLegalCase(mobileNo, smsMsg);

    }

    public String smsBodyByCodeAndArgsWithTypeForStandingCounsel(final String code,
            final LegalCaseAdvocate legalCaseAdvocate) {
        final String smsMsg = messageSource.getMessage(code,
                new String[] { legalCaseAdvocate.getAdvocateMaster().getName(),
                        legalCaseAdvocate.getLegalCase().getCaseNumber(),
                        legalCaseAdvocate.getLegalCase().getPetitionersNames(),
                        legalCaseAdvocate.getLegalCase().getRespondantNames() },
                null);
        return smsMsg;
    }

    // Send SMS Notification to StandingCounsel For Hearings
    public void sendSmsToStandingCounselForHearings(final Hearings hearings) {
        if (hearings.getLegalCase().getLegalCaseAdvocates() != null
                && !hearings.getLegalCase().getLegalCaseAdvocates().isEmpty()) {
            final String mobileNumber = hearings.getLegalCase().getLegalCaseAdvocates().get(0).getAdvocateMaster()
                    .getMobileNumber();
            getSmsForStandingCounselForHearings(hearings, mobileNumber);

        }

    }

    public void getSmsForStandingCounselForHearings(final Hearings hearings, final String mobileNo) {
        final String smsMsg = smsBodyByCodeAndArgsWithTypeForStandingCounselForHearings(
                "msg.standingcounselforhearings.sms", hearings);

        if (StringUtils.isNotBlank(mobileNo) && StringUtils.isNotBlank(smsMsg))
            legalCaseUtil.sendSMSOnLegalCase(mobileNo, smsMsg);

    }

    public String smsBodyByCodeAndArgsWithTypeForStandingCounselForHearings(final String code,
            final Hearings hearings) {
        final String smsMsg = messageSource.getMessage(code,
                new String[] { hearings.getLegalCase().getLegalCaseAdvocates().get(0).getAdvocateMaster().getName(),
                        hearings.getLegalCase().getCaseNumber(),
                        LcmsConstants.DATEFORMATTER_DD_MM_YYYY.format(hearings.getHearingDate()).toString(),
                        hearings.getPurposeofHearings() },
                null);
        return smsMsg;
    }

    // Send SMS Notification to StandingCounsel For InterimOrder
    public void sendSmsToStandingCounselForInterimOrder(final LegalCaseInterimOrder legalCaseInterimOrder) {
        if (legalCaseInterimOrder.getLegalCase().getLegalCaseAdvocates() != null
                && !legalCaseInterimOrder.getLegalCase().getLegalCaseAdvocates().isEmpty()) {
            final String mobileNumber = legalCaseInterimOrder.getLegalCase().getLegalCaseAdvocates().get(0)
                    .getAdvocateMaster().getMobileNumber();
            getSmsToStandingCounselForInterimOrder(legalCaseInterimOrder, mobileNumber);

        }

    }

    public void getSmsToStandingCounselForInterimOrder(final LegalCaseInterimOrder legalCaseInterimOrder,
            final String mobileNo) {
        final String smsMsg = smsBodyByCodeAndArgsWithTypeToStandingCounselForInterimOrder(
                "msg.standingcounselforinterimorder.sms", legalCaseInterimOrder);

        if (StringUtils.isNotBlank(mobileNo) && StringUtils.isNotBlank(smsMsg))
            legalCaseUtil.sendSMSOnLegalCase(mobileNo, smsMsg);

    }

    public String smsBodyByCodeAndArgsWithTypeToStandingCounselForInterimOrder(final String code,
            final LegalCaseInterimOrder legalCaseInterimOrder) {
        final String smsMsg = messageSource.getMessage(code,
                new String[] {
                        legalCaseInterimOrder.getLegalCase().getLegalCaseAdvocates().get(0).getAdvocateMaster()
                                .getName(),
                        legalCaseInterimOrder.getLegalCase().getCaseNumber(),
                        legalCaseInterimOrder.getInterimOrder().getInterimOrderType(),
                        LcmsConstants.DATEFORMATTER_DD_MM_YYYY.format(legalCaseInterimOrder.getIoDate()).toString() },
                null);
        return smsMsg;
    }

    // Send SMS Notification to StandingCounsel For Judgment
    public void sendSmsToStandingCounselForJudgment(final Judgment judgment) {
        if (judgment.getLegalCase().getLegalCaseAdvocates() != null
                && !judgment.getLegalCase().getLegalCaseAdvocates().isEmpty()) {
            final String mobileNumber = judgment.getLegalCase().getLegalCaseAdvocates().get(0).getAdvocateMaster()
                    .getMobileNumber();
            getSmsToStandingCounselForJudgment(judgment, mobileNumber);
        }

    }

    public void getSmsToStandingCounselForJudgment(final Judgment judgment, final String mobileNo) {
        String smsMsg = "";
        if (judgment.getImplementByDate() != null)
            smsMsg = smsBodyByCodeAndArgsWithTypeToStandingCounselForJudgmentWithImplementDate(
                    "msg.standingcounselforjudgmentwithimplementdate.sms", judgment);
        else
            smsMsg = smsBodyByCodeAndArgsWithTypeToStandingCounselForJudgmentWithoutImplementDate(
                    "msg.standingcounselforjudgmentwithoutimplementdate.sms", judgment);

        if (StringUtils.isNotBlank(mobileNo) && StringUtils.isNotBlank(smsMsg))
            legalCaseUtil.sendSMSOnLegalCase(mobileNo, smsMsg);

    }

    public String smsBodyByCodeAndArgsWithTypeToStandingCounselForJudgmentWithImplementDate(final String code,
            final Judgment judgment) {
        final String smsMsg = messageSource
                .getMessage(code,
                        new String[] {
                                judgment.getLegalCase().getLegalCaseAdvocates().get(0).getAdvocateMaster().getName(),
                                judgment.getLegalCase().getCaseNumber(), judgment.getJudgmentType().getName(),

                                LcmsConstants.DATEFORMATTER_DD_MM_YYYY.format(judgment.getImplementByDate())
                                        .toString() },
                        null);
        return smsMsg;
    }

    public String smsBodyByCodeAndArgsWithTypeToStandingCounselForJudgmentWithoutImplementDate(final String code,
            final Judgment judgment) {
        final String smsMsg = messageSource
                .getMessage(code,
                        new String[] {
                                judgment.getLegalCase().getLegalCaseAdvocates().get(0).getAdvocateMaster().getName(),
                                judgment.getLegalCase().getCaseNumber(), judgment.getJudgmentType().getName() },
                        null);
        return smsMsg;
    }

    // Send SMS Notification to StandingCounsel For JudgmentImpl
    public void sendSmsToStandingCounselForJudgmentImpl(final JudgmentImpl judgmentImpl) {
        if (judgmentImpl.getJudgment().getLegalCase().getLegalCaseAdvocates() != null
                && !judgmentImpl.getJudgment().getLegalCase().getLegalCaseAdvocates().isEmpty()) {
            final String mobileNumber = judgmentImpl.getJudgment().getLegalCase().getLegalCaseAdvocates().get(0)
                    .getAdvocateMaster().getMobileNumber();
            getSmsToStandingCounselForJudgmentImpl(judgmentImpl, mobileNumber);
        }
    }

    public void getSmsToStandingCounselForJudgmentImpl(final JudgmentImpl judgmentImpl, final String mobileNo) {
        String smsMsg = "";
        if (judgmentImpl.getJudgmentImplIsComplied().toString().equals(JudgmentImplIsComplied.YES.toString()))
            smsMsg = smsBodyByCodeAndArgsWithTypeToStandingCounselForJudgmentImplIsCompliedYes(
                    "msg.standingcounselforjudgmentimpliscompliedyes.sms", judgmentImpl);
        else if (judgmentImpl.getJudgmentImplIsComplied().toString().equals(JudgmentImplIsComplied.NO.toString()))
            smsMsg = smsBodyByCodeAndArgsWithTypeToStandingCounselForJudgmentImplIsCompliedNo(
                    "msg.standingcounseljudgmentimpliscompliedno.sms", judgmentImpl);

        if (StringUtils.isNotBlank(mobileNo) && StringUtils.isNotBlank(smsMsg))
            legalCaseUtil.sendSMSOnLegalCase(mobileNo, smsMsg);

    }

    public String smsBodyByCodeAndArgsWithTypeToStandingCounselForJudgmentImplIsCompliedYes(final String code,
            final JudgmentImpl judgmentImpl) {
        final String smsMsg = messageSource.getMessage(code,
                new String[] {
                        judgmentImpl.getJudgment().getLegalCase().getLegalCaseAdvocates().get(0).getAdvocateMaster()
                                .getName(),
                        judgmentImpl.getJudgment().getLegalCase().getCaseNumber(),
                        judgmentImpl.getJudgmentImplIsComplied().toString(),
                        LcmsConstants.DATEFORMATTER_DD_MM_YYYY.format(judgmentImpl.getDateOfCompliance()).toString() },
                null);
        return smsMsg;
    }

    public String smsBodyByCodeAndArgsWithTypeToStandingCounselForJudgmentImplIsCompliedNo(final String code,
            final JudgmentImpl judgmentImpl) {
        final String smsMsg = messageSource.getMessage(code,
                new String[] {
                        judgmentImpl.getJudgment().getLegalCase().getLegalCaseAdvocates().get(0).getAdvocateMaster()
                                .getName(),
                        judgmentImpl.getJudgment().getLegalCase().getCaseNumber(),
                        judgmentImpl.getJudgmentImplIsComplied().toString(),
                        judgmentImpl.getImplementationFailure().toString() },
                null);
        return smsMsg;
    }

    // Send SMS Notification to StandingCounsel For CloseCase
    public void sendSmsToStandingCounselForCloseCase(final LegalCaseDisposal legalCaseDisposal) {
        if (legalCaseDisposal.getLegalCase().getLegalCaseAdvocates() != null
                && !legalCaseDisposal.getLegalCase().getLegalCaseAdvocates().isEmpty()) {
            final String mobileNumber = legalCaseDisposal.getLegalCase().getLegalCaseAdvocates().get(0)
                    .getAdvocateMaster().getMobileNumber();
            getSmsToStandingCounselForCloseCase(legalCaseDisposal, mobileNumber);
        }

    }

    public void getSmsToStandingCounselForCloseCase(final LegalCaseDisposal legalCaseDisposal, final String mobileNo) {
        final String smsMsg = smsBodyByCodeAndArgsWithTypeToStandingCounselForCloseCase(
                "msg.standingcounselforclosecase.sms", legalCaseDisposal);

        if (StringUtils.isNotBlank(mobileNo) && StringUtils.isNotBlank(smsMsg))
            legalCaseUtil.sendSMSOnLegalCase(mobileNo, smsMsg);

    }

    public String smsBodyByCodeAndArgsWithTypeToStandingCounselForCloseCase(final String code,
            final LegalCaseDisposal legalCaseDisposal) {
        final String smsMsg = messageSource.getMessage(code,
                new String[] {
                        legalCaseDisposal.getLegalCase().getLegalCaseAdvocates().get(0).getAdvocateMaster().getName(),
                        legalCaseDisposal.getLegalCase().getCaseNumber(),
                        LcmsConstants.DATEFORMATTER_DD_MM_YYYY.format(legalCaseDisposal.getDisposalDate()).toString() },
                null);
        return smsMsg;
    }

    // Send SMS Notification to StandingCounsel For CA
    public void sendSmsToStandingCounselForCounterAffidavit(final List<CounterAffidavit> counterAffidavit) {
        if (counterAffidavit.get(0).getLegalCase().getLegalCaseAdvocates() != null
                && !counterAffidavit.get(0).getLegalCase().getLegalCaseAdvocates().isEmpty()) {
            final String mobileNumber = counterAffidavit.get(0).getLegalCase().getLegalCaseAdvocates().get(0)
                    .getAdvocateMaster().getMobileNumber();
            getSmsToStandingCounselForCA(counterAffidavit, mobileNumber);
        }

    }

    public void getSmsToStandingCounselForCA(final List<CounterAffidavit> counterAffidavit, final String mobileNo) {

        final String smsMsg = smsBodyByCodeAndArgsWithTypeToStandingCounselForCA("msg.standingcounseltoca.sms",
                counterAffidavit);

        if (StringUtils.isNotBlank(mobileNo) && StringUtils.isNotBlank(smsMsg))
            legalCaseUtil.sendSMSOnLegalCase(mobileNo, smsMsg);

    }

    public String smsBodyByCodeAndArgsWithTypeToStandingCounselForCA(final String code,
            final List<CounterAffidavit> counterAffidavit) {
        final String smsMsg = messageSource
                .getMessage(code,
                        new String[] {
                                counterAffidavit.get(0).getLegalCase().getLegalCaseAdvocates().get(0)
                                        .getAdvocateMaster().getName(),
                                counterAffidavit.get(0).getLegalCase().getCaseNumber() },
                        null);
        return smsMsg;
    }

}