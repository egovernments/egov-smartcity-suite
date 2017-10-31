/*
 * eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
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
 *           Further, all user interfaces, including but not limited to citizen facing interfaces,
 *           Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *           derived works should carry eGovernments Foundation logo on the top right corner.
 *
 * 	       For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 * 	       For any further queries on attribution, including queries on brand guidelines,
 *           please contact contact@egovernments.org
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

package org.egov.tl.service;

import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.tl.entity.License;
import org.egov.tl.utils.LicenseUtils;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.entity.WorkflowBean;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.egov.tl.utils.Constants.BUTTONAPPROVE;
import static org.egov.tl.utils.Constants.BUTTONREJECT;
import static org.egov.tl.utils.Constants.LICENSE_STATUS_ACKNOWLEDGED;
import static org.egov.tl.utils.Constants.SIGNWORKFLOWACTION;
import static org.egov.tl.utils.Constants.STATUS_UNDERWORKFLOW;
import static org.egov.tl.utils.Constants.NEWLICENSE;
import static org.egov.tl.utils.Constants.RENEWLICENSE;

@Service
@Transactional(readOnly = true)
public class LicenseApplicationService extends TradeLicenseService {
    @Autowired
    private TradeLicenseSmsAndEmailService tradeLicenseSmsAndEmailService;
    @Autowired
    private LicenseProcessWorkflowService licenseProcessWorkflowService;
    @Autowired
    private LicenseUtils licenseUtils;

    @Override
    public License createWithMeseva(TradeLicense license, WorkflowBean wfBean) {
        return create(license, wfBean);
    }

    @Override
    public License renewWithMeeseva(TradeLicense license, WorkflowBean wfBean) {
        return renew(license, wfBean);
    }

    @Transactional
    @Override
    public License create(final TradeLicense license, final WorkflowBean workflowBean) {
        final Date fromRange = installmentDao.getInsatllmentByModuleForGivenDate(this.getModuleName(), new DateTime().toDate())
                .getFromDate();
        final Date toRange = installmentDao
                .getInsatllmentByModuleForGivenDate(this.getModuleName(), new DateTime().plusYears(1).toDate()).getToDate();
        if (license.getCommencementDate().before(fromRange) || license.getCommencementDate().after(toRange))
            throw new ValidationException("TL-009", "TL-009");
        license.setLicenseAppType(getLicenseApplicationType());
        raiseNewDemand(license);
        license.getLicensee().setLicense(license);
        license.setStatus(licenseStatusService.getLicenseStatusByName(LICENSE_STATUS_ACKNOWLEDGED));
        if (isBlank(license.getApplicationNumber()))
            license.setApplicationNumber(licenseNumberUtils.generateApplicationNumber());
        processAndStoreDocument(license);
        if (securityUtils.currentUserIsEmployee())
            licenseProcessWorkflowService.createNewLicenseWorkflowTransition((TradeLicense) license, workflowBean);

        else
            licenseProcessWorkflowService.getWfWithThirdPartyOp((TradeLicense) license, workflowBean);
        licenseRepository.save(license);
        licenseApplicationIndexService.createOrUpdateLicenseApplicationIndex(license);
        sendEmailAndSMS(license, workflowBean.getWorkFlowAction());
        return license;
    }

    @Transactional
    @Override
    public License renew(final TradeLicense license, final WorkflowBean workflowBean) {
        if (!currentUserIsMeeseva())
            license.setApplicationNumber(licenseNumberUtils.generateApplicationNumber());
        recalculateDemand(this.feeMatrixService.getLicenseFeeDetails(license,
                license.getLicenseDemand().getEgInstallmentMaster().getFromDate()), license);
        license.setStatus(licenseStatusService.getLicenseStatusByName(LICENSE_STATUS_ACKNOWLEDGED));
        license.setLicenseAppType(this.getLicenseApplicationTypeForRenew());
        processAndStoreDocument(license);
        if (securityUtils.currentUserIsEmployee())
            licenseProcessWorkflowService.createNewLicenseWorkflowTransition((TradeLicense) license, workflowBean);

        else
            licenseProcessWorkflowService.getWfWithThirdPartyOp((TradeLicense) license, workflowBean);
        this.licenseRepository.save(license);
        sendEmailAndSMS(license, workflowBean.getWorkFlowAction());
        licenseApplicationIndexService.createOrUpdateLicenseApplicationIndex(license);
        return license;
    }


    @Transactional
    public License save(final TradeLicense license) {
        license.setLicenseAppType(getLicenseApplicationType());
        if (securityUtils.currentUserIsEmployee())
            licenseRepository.save(license);
        return license;
    }

    @Transactional
    @Override
    public void updateTradeLicense(final TradeLicense license, final WorkflowBean workflowBean) {
        updateDemandForChangeTradeArea(license);
        if (!license.isPaid())
            license.setCollectionPending(true);
        else
            license.setCollectionPending(false);
        if (BUTTONREJECT.equalsIgnoreCase(workflowBean.getWorkFlowAction()))
            licenseProcessWorkflowService.getRejectTransition((TradeLicense) license, workflowBean);
        else
            licenseProcessWorkflowService.createNewLicenseWorkflowTransition((TradeLicense) license, workflowBean);

        if (BUTTONAPPROVE.equals(workflowBean.getWorkFlowAction()) && isEmpty(license.getLicenseNumber()) && license.isNewApplication())
            license.setLicenseNumber(licenseNumberUtils.generateLicenseNumber());

        licenseRepository.save(license);
        tradeLicenseSmsAndEmailService.sendSmsAndEmail(license, workflowBean.getWorkFlowAction());
        licenseApplicationIndexService.createOrUpdateLicenseApplicationIndex(license);
    }

    public void digitalSignature(String applicationNumber) {
        if (isNotBlank(applicationNumber)) {
            License license = licenseRepository.findByApplicationNumber(applicationNumber);
            WorkflowBean workflowBean = new WorkflowBean();
            workflowBean.setWorkFlowAction(SIGNWORKFLOWACTION);
            workflowBean.setAdditionaRule(license.isNewApplication() ? NEWLICENSE : RENEWLICENSE);
            licenseProcessWorkflowService.createNewLicenseWorkflowTransition((TradeLicense) license, workflowBean);
            licenseRepository.save(license);
            tradeLicenseSmsAndEmailService.sendSMsAndEmailOnDigitalSign(license);
        }

    }

    public void collectionTransition(TradeLicense tradeLicense) {
        licenseUtils.licenseStatusUpdate(tradeLicense, STATUS_UNDERWORKFLOW);
        licenseProcessWorkflowService.collectionWorkflowTransition(tradeLicense);
    }

}
