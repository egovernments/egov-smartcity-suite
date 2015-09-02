/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *     accountability and the service delivery of the government  organizations.
 *
 *      Copyright (C) <2015>  eGovernments Foundation
 *
 *      The updated version of eGov suite of products as by eGovernments Foundation
 *      is available at http://www.egovernments.org
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program. If not, see http://www.gnu.org/licenses/ or
 *      http://www.gnu.org/licenses/gpl.html .
 *
 *      In addition to the terms of the GPL license to be adhered to in using this
 *      program, the following additional terms are to be complied with:
 *
 *  	1) All versions of this program, verbatim or modified must carry this
 *  	   Legal Notice.
 *
 *  	2) Any misrepresentation of the origin of the material is prohibited. It
 *  	   is required that all modified versions of this material be marked in
 *  	   reasonable ways as different from the original version.
 *
 *  	3) This license does not grant any rights to any user of the program
 *  	   with regards to rights under trademark law for use of the trade names
 *  	   or trademarks of eGovernments Foundation.
 *
 *    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.tradelicense.domain.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentHibDao;
import org.egov.demand.model.EgDemandReasonMaster;
import org.egov.demand.model.EgReasonCategory;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.infstr.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infstr.utils.Sequence;
import org.egov.infstr.utils.SequenceGenerator;
import org.egov.tradelicense.domain.entity.FeeMatrix;
import org.egov.tradelicense.domain.entity.License;
import org.egov.tradelicense.domain.entity.LicenseAppType;
import org.egov.tradelicense.domain.entity.LicenseDemand;
import org.egov.tradelicense.domain.entity.LicenseStatus;
import org.egov.tradelicense.domain.entity.NatureOfBusiness;
import org.egov.tradelicense.domain.entity.WorkflowBean;
import org.egov.tradelicense.utils.Constants;
import org.egov.tradelicense.utils.LicenseChecklistHelper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author mani
 */
public abstract class BaseLicenseService {
    protected FeeService feeService;
    @Autowired
    protected PersistenceService persistenceService;
    protected SequenceGenerator sequenceGenerator;
    @Autowired
    protected InstallmentHibDao installmentDao;

    protected abstract WorkflowService workflowService();

    protected abstract LicenseAppType getLicenseApplicationTypeForRenew();

    protected abstract License additionalOperations(License license, Set<EgDemandReasonMaster> egDemandReasonMasters,
            Installment installment);

    protected abstract LicenseAppType getLicenseApplicationType();

    protected abstract Module getModuleName();

    protected abstract NatureOfBusiness getNatureOfBusiness();

    public PersistenceService getPersistenceService() {
        return persistenceService;
    }

    public void setPersistenceService(final PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public void setFeeService(final FeeService feeService) {
        this.feeService = feeService;
    }

    public void setSequenceGenerator(final SequenceGenerator sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

    @SuppressWarnings("unchecked")
    public void create(License license) {
        final LicenseAppType appType = getLicenseApplicationType();
        final NatureOfBusiness nature = getNatureOfBusiness();
        final List<FeeMatrix> feeList = feeService.getFeeList(license.getTradeName(), appType, nature);
        final BigDecimal totalAmount = BigDecimal.ZERO;
        // calculateFee code sets the fee type like cnc or pfa etc etc only required on create
        // this.feeService.calculateFee(license, license.getTradeName(), this.getLicenseApplicationType(),
        // this.getNatureOfBusiness(), license.getOtherCharges(), license.getDeduction());
        // Setting Fee Type String
        feeService.setFeeType(feeList, license);
        final Installment installment = installmentDao
                .getInsatllmentByModuleForGivenDate(getModuleName(), license.getApplicationDate());
        final EgReasonCategory reasonCategory = (EgReasonCategory) persistenceService
                .find("from org.egov.demand.model.EgReasonCategory where name='Fee'");
        final Set<EgDemandReasonMaster> egDemandReasonMasters = reasonCategory.getEgDemandReasonMasters();
        String feeType = "";
        if (getModuleName().getName().equals(Constants.ELECTRICALLICENSE_MODULENAME))
            feeType = getFeeTypeForElectricalLicense(license);
        else
            feeType = license.getClass().getSimpleName().toUpperCase();
        final String runningApplicationNumber = getNextRunningNumber(feeType + "_APPLICATION_NUMBER");
        license = license.create(feeList, appType, nature, installment, egDemandReasonMasters, totalAmount,
                runningApplicationNumber, license.getFeeTypeStr(), getModuleName());
        license.getLicensee().setLicense(license);
        final LicenseStatus status = (LicenseStatus) persistenceService.find(
                "from org.egov.license.domain.entity.LicenseStatus where name=? ", Constants.LICENSE_STATUS_ACKNOWLEDGED);
        license.updateStatus(status);
        license = additionalOperations(license, egDemandReasonMasters, installment);
        persistenceService.create(license);
    }

    public String getFeeTypeForElectricalLicense(final License license) {
        String feeType = null;
        if (license != null && license.getLicenseSubType() != null)
            if (license.getLicenseSubType().getCode().equals(Constants.MAINTENANCE_CONTRACTORS))
                feeType = license.getLicenseSubType().getCode();
            else if (license.getLicenseSubType().getCode().equals(Constants.LIFT_CONTRACTORS))
                feeType = license.getLicenseSubType().getCode();
            else if (license.getLicenseSubType().getCode().equals(Constants.BRAND_OWNER))
                feeType = license.getLicenseSubType().getCode();
            else if (license.getLicenseSubType().getCode().equals(Constants.CONSULTANTS))
                feeType = license.getLicenseSubType().getCode();
            else if (license.getLicenseSubType().getCode().equals(Constants.CONTRACTORS))
                feeType = license.getLicenseSubType().getCode();
            else if (license.getLicenseSubType().getCode().equals(Constants.DISTRIBUTOR))
                feeType = license.getLicenseSubType().getCode();
            else if (license.getLicenseSubType().getCode().equals(Constants.POLE_SUPPLIER_CONTRACTORS))
                feeType = license.getLicenseSubType().getCode();
            else if (license.getLicenseSubType().getCode().equals(Constants.PUMP_MAINTENANCE))
                feeType = license.getLicenseSubType().getCode();
            else if (license.getLicenseSubType().getCode().equals(Constants.BOT_CONTRACTORS))
                feeType = license.getLicenseSubType().getCode();
            else if (license.getLicenseSubType().getCode().equals(Constants.FIRE_CONTRACTORS))
                feeType = license.getLicenseSubType().getCode();
        return feeType;
    }

    public void enterExistingLicense(License license) {

        final LicenseAppType appType = getLicenseApplicationType();
        final NatureOfBusiness nature = getNatureOfBusiness();
        final List<FeeMatrix> feeList = feeService.getFeeList(license.getTradeName(), appType, nature);
        boolean isPFA = false;
        for (final FeeMatrix fee : feeList)
            if (fee.getFeeType().getName().equalsIgnoreCase(FeeService.PFA)) {
                isPFA = true;
                break;
            }
        final Calendar issueDate = Calendar.getInstance();
        issueDate.setTime(license.getDateOfCreation());
        final Calendar maxAllowdedDate = Calendar.getInstance();
        final Calendar minAllowdedDate = Calendar.getInstance();
        minAllowdedDate.setTime(license.getDateOfCreation());
        final Calendar instance = Calendar.getInstance();
        int year = instance.get(Calendar.YEAR);
        if (issueDate.after(maxAllowdedDate))
            throw new ValidationException("dateOfCreation", "license.issuedate.maxout");
        final Module module = getModuleName();
        if (isPFA && module.getName().contains("Hospital")) {
            year = year - 3;
            instance.set(year, 2, 31);
            minAllowdedDate.setTime(instance.getTime());
        } else if (isPFA && module.getName().contains("Hawker")) {
            year = year - 1;
            instance.set(year, 2, 31);
            minAllowdedDate.setTime(instance.getTime());
        } else if (isPFA) {
            year = year - 5;
            instance.set(year, 2, 31);
            minAllowdedDate.setTime(instance.getTime());
        } else if (module.getName().contains("Hospital")) {
            year = year - 3;
            instance.set(year, 2, 31);
            minAllowdedDate.setTime(instance.getTime());
        } else if (module.getName().contains("PwdContractor") || module.getName().contains("ElectricalContractor")) {
            final int month = instance.get(Calendar.MONTH);
            int day = instance.get(Calendar.DAY_OF_MONTH);
            day = day - 1;
            year = year - 3;
            instance.set(year, month, day);
            minAllowdedDate.setTime(instance.getTime());
        } else {
            year = year - 1;
            instance.set(year, 2, 31);
            minAllowdedDate.setTime(instance.getTime());
        }
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        if (issueDate.before(minAllowdedDate))
            throw new ValidationException("dateOfCreation", "license.issuedate.minout", sdf.format(minAllowdedDate.getTime()));
        final BigDecimal totalAmount = BigDecimal.ZERO;
        // final BigDecimal totalAmount = this.feeService.calculateFeeForExisting(license, license.getTradeName(),
        // this.getLicenseApplicationTypeForRenew(), this.getNatureOfBusiness(), license.getOtherCharges(),
        // license.getDeduction());
        // Setting Fee Type String
        feeService.setFeeType(feeList, license);
        final Installment installment = installmentDao.getInsatllmentByModuleForGivenDate(module, license.getDateOfCreation());
        if (installment == null)
            throw new ValidationException("dateOfCreation", "license.installment.notavail");
        final EgReasonCategory reasonCategory = (EgReasonCategory) persistenceService
                .find("from org.egov.demand.model.EgReasonCategory where name='Fee'");
        final Set<EgDemandReasonMaster> egDemandReasonMasters = reasonCategory.getEgDemandReasonMasters();
        String feeType = "";
        if (getModuleName().getName().equals(Constants.ELECTRICALLICENSE_MODULENAME))
            feeType = getFeeTypeForElectricalLicense(license);
        else
            feeType = license.getClass().getSimpleName().toUpperCase();
        final String runningApplicationNumber = getNextRunningNumber(feeType + "_APPLICATION_NUMBER");
        license = license.create(feeList, appType, nature, installment, egDemandReasonMasters, totalAmount,
                runningApplicationNumber, license.getFeeTypeStr(), module);

        license.getLicensee().setLicense(license);
        final LicenseStatus status = (LicenseStatus) persistenceService.find(
                "from org.egov.license.domain.entity.LicenseStatus where name=? ", Constants.LICENSE_STATUS_ACKNOWLEDGED);
        license.updateStatus(status);
        license = additionalOperations(license, egDemandReasonMasters, installment);
        license = license.updateCollectedForExisting(license);
        persistenceService.create(license);
    }

    public LicenseDemand getCurrentYearDemand(final License license) {
        final Date currDate = new Date();
        final LicenseDemand currLicenseDemand = (LicenseDemand) persistenceService
                .find("from LicenseDemand ld where ld.license.id=? and (ld.egInstallmentMaster.fromDate <= ? and ld.egInstallmentMaster.toDate >=?)",
                        license.getId(), currDate, currDate);
        return currLicenseDemand;
    }

    @SuppressWarnings("unchecked")
    public List<Installment> getCurrAndPreviousInstallment() {
        final Installment installment = installmentDao.getInsatllmentByModuleForGivenDate(getModuleName(), new Date());
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(installment.getInstallmentYear());
        calendar.add(Calendar.YEAR, -1);
        final Date previousInstallmentDate = calendar.getTime();
        final Installment previousInstallment = installmentDao.getInsatllmentByModuleForGivenDate(getModuleName(),
                previousInstallmentDate);
        final List<Installment> installmentList = new ArrayList<Installment>();
        installmentList.add(installment);
        installmentList.add(previousInstallment);
        return installmentList;
    }

    /**
     * @param feeType like PFA or CNC etc
     * @param applDate
     * @return
     */
    public String getNextRunningNumber(final String feeType) {
        final Sequence seq = sequenceGenerator.getNextNumberWithFormat(feeType, Constants.APPLICATIONNO_LENGTH,
                new Character('0'));
        return seq.getFormattedNumber();
    }

    /**
     * TODO -- Fix me 
     */
    public License processWorkflowForLicense(final License license, final WorkflowBean workflowBean) {
        Integer userID = null;
        /*for (final State state : license.getState().getHistory())
            if (state != null && state.getCreatedBy() != null)
                if (state.getValue().equals(Constants.WORKFLOW_STATE_NEW)) {
                    userID = state.getCreatedBy().getId();
                    break;
                }
        if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONAPPROVE)) {
            if (!license.isPaid())
                throw new ValidationException("applicationNumber", "license.fee.notcollected", license.getApplicationNumber());
            Position position = eisCommonsManager.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
            license.setCreationAndExpiryDate();
            license.changeState(Constants.WORKFLOW_STATE_TYPE_CREATENEWLICENSE + Constants.WORKFLOW_STATE_APPROVED, position,
                    workflowBean.getComments());
            if (license.getIsUpgrade() == null && license.getTempLicenseNumber() == null) {
                String feeType = "";
                if (getName().getName().equals(Constants.ELECTRICALLICENSE_MODULENAME))
                    feeType = getFeeTypeForElectricalLicense(license);
                else
                    feeType = license.getClass().getSimpleName().toUpperCase();
                final String nextRunningLicenseNumber = getNextRunningLicenseNumber(feeType + "_" + license.getFeeTypeStr()
                        + "_LICENSE_NUMBER");
                license.generateLicenseNumber(nextRunningLicenseNumber);
            }
            final Module module = license.getTradeName().getLicenseType().getModule();
            if (module.getName().equals(Constants.PWDLICENSE_MODULENAME))
                if (org.apache.commons.lang.StringUtils.isBlank(license.getContractorCode())
                        && org.apache.commons.lang.StringUtils.isEmpty(license.getContractorCode()))
                    license.setContractorCode(license.getLicenseNumber());
            position = eisCommonsManager.getPositionByUserId(userID);
            if (license.getTradeName().isNocApplicable() != null && license.getTradeName().isNocApplicable())
                license.changeState(Constants.WORKFLOW_STATE_TYPE_CREATENEWLICENSE + Constants.WORKFLOW_STATE_GENERATENOC,
                        position, workflowBean.getComments());
            else
                license.changeState(
                        Constants.WORKFLOW_STATE_TYPE_CREATENEWLICENSE + Constants.WORKFLOW_STATE_GENERATECERTIFICATE, position,
                        workflowBean.getComments());
        } else if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONFORWARD)) {
            final Position nextPosition = eisCommonsManager.getPositionByUserId(workflowBean.getApproverUserId());
            license.changeState(Constants.WORKFLOW_STATE_TYPE_CREATENEWLICENSE + Constants.WORKFLOW_STATE_FORWARDED,
                    nextPosition, workflowBean.getComments());
        } else if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONREJECT)) {
            if (license.getState().getValue().contains(Constants.WORKFLOW_STATE_REJECTED)) {
                final Position position = eisCommonsManager.getPositionByUserId(userID);
                license.changeState(Constants.WORKFLOW_STATE_TYPE_CREATENEWLICENSE
                        + Constants.WORKFLOW_STATE_GENERATEREJCERTIFICATE, position, workflowBean.getComments());

            } else {
                final Position position = eisCommonsManager.getPositionByUserId(userID);
                license.changeState(Constants.WORKFLOW_STATE_TYPE_CREATENEWLICENSE + Constants.WORKFLOW_STATE_REJECTED, position,
                        workflowBean.getComments());
            }
        } else if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONPRINTCOMPLETED)) {

            if (license.getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATEREJCERTIFICATE)) {
                final Position position = eisCommonsManager.getPositionByUserId(userID);
                workflowService().end(license, position);
                final LicenseStatus activeStatus = (LicenseStatus) persistenceService
                        .find("from org.egov.license.domain.entity.LicenseStatus where code='REJ'");
                license.setStatus(activeStatus);
            }

        } else if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONGENERATEDCERTIFICATE)) {
            final Position position = eisCommonsManager.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
            workflowService().end(license, position);
            final LicenseStatus activeStatus = (LicenseStatus) persistenceService
                    .find("from org.egov.license.domain.entity.LicenseStatus where code='ACT'");
            license.setStatus(activeStatus);
        }*/
        return license;
    }

    /**
     * TODO -- Fix me 
     */
    public License processWorkflowForRenewLicense(final License license, final WorkflowBean workflowBean) {
        Integer userID = null;
        /*for (final State state : license.getState().getHistory())
            if (state != null && state.getCreatedBy() != null)
                if (state.getValue().equals(Constants.WORKFLOW_STATE_NEW)) {
                    userID = new UserManagerBean().getUserByID(state.getCreatedBy().getId()).getId();
                    break;
                }
        if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONAPPROVE)) {
            if (!license.isPaid())
                throw new ValidationException("licenseNumber", "renew.fee.notcollected", license.getLicenseNumber());
            Position position = eisCommonsManager.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
            license.updateExpiryDate(new Date());
            license.changeState(Constants.WORKFLOW_STATE_TYPE_RENEWLICENSE + Constants.WORKFLOW_STATE_APPROVED, position,
                    workflowBean.getComments());
            position = eisCommonsManager.getPositionByUserId(userID);
            license.changeState(Constants.WORKFLOW_STATE_TYPE_RENEWLICENSE + Constants.WORKFLOW_STATE_GENERATECERTIFICATE,
                    position, workflowBean.getComments());
        } else if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONFORWARD)) {
            final Position nextPosition = eisCommonsManager.getPositionByUserId(workflowBean.getApproverUserId());
            license.changeState(Constants.WORKFLOW_STATE_TYPE_RENEWLICENSE + Constants.WORKFLOW_STATE_FORWARDED, nextPosition,
                    workflowBean.getComments());
        } else if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONREJECT)) {
            if (license.getState().getValue().contains(Constants.WORKFLOW_STATE_REJECTED)) {
                final Position position = eisCommonsManager.getPositionByUserId(userID);
                license.changeState(Constants.WORKFLOW_STATE_TYPE_RENEWLICENSE + Constants.WORKFLOW_STATE_GENERATEREJCERTIFICATE,
                        position, workflowBean.getComments());

            } else {
                final Position position = eisCommonsManager.getPositionByUserId(userID);
                license.changeState(Constants.WORKFLOW_STATE_TYPE_RENEWLICENSE + Constants.WORKFLOW_STATE_REJECTED, position,
                        workflowBean.getComments());
            }
        } else if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONPRINTCOMPLETED)) {

            if (license.getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATEREJCERTIFICATE)) {
                final Position position = eisCommonsManager.getPositionByUserId(userID);
                workflowService().end(license, position);
                final LicenseStatus activeStatus = (LicenseStatus) persistenceService
                        .find("from org.egov.license.domain.entity.LicenseStatus where code='REJ'");
                license.setStatus(activeStatus);
            }

        } else if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONGENERATEDCERTIFICATE)) {
            final Position position = eisCommonsManager.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
            workflowService().end(license, position);
            final LicenseStatus activeStatus = (LicenseStatus) persistenceService
                    .find("from org.egov.license.domain.entity.LicenseStatus where code='ACT'");
            license.setStatus(activeStatus);
        }*/
        return license;
    }

    /**
     * TODO -- Fix me 
     */
    /*public void endWorkFlowForLicense(final License license) {
        final Position position = eisCommonsManager.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
        workflowService().end(license, position);
    }

    public void initiateWorkFlowForLicense(final License license, final WorkflowBean workflowBean) {
        final Position position = eisCommonsManager.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
        if (position != null)
        {
            workflowService().start(license, position, workflowBean.getComments());
            license.changeState(Constants.WORKFLOW_STATE_TYPE_CREATENEWLICENSE + "NEW", position, workflowBean.getComments());
            final LicenseStatus underWorkflowStatus = (LicenseStatus) persistenceService
                    .find("from org.egov.license.domain.entity.LicenseStatus where code='UWF'");
            license.setStatus(underWorkflowStatus);
            final Module module = license.getTradeName().getLicenseType().getModule();
            if ((module.getName().equals(Constants.ELECTRICALLICENSE_MODULENAME) || module.getName().equals(
                    Constants.PWDLICENSE_MODULENAME))
                    && license.getFeeExemption() != null && license.getFeeExemption().equals("YES"))
                license.changeState(Constants.WORKFLOW_STATE_TYPE_CREATENEWLICENSE + Constants.WORKFLOW_REG_FEE_STATE_COLLECTED,
                        position, workflowBean.getComments());
        }
    }

    public void initiateWorkFlowForLicenseDraft(final License license, final WorkflowBean workflowBean) {
        final Position position = eisCommonsManager.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
        workflowService().start(license, position, workflowBean.getComments());
        final LicenseStatus underWorkflowStatus = (LicenseStatus) persistenceService
                .find("from org.egov.license.domain.entity.LicenseStatus where code='UWF'");
        license.setStatus(underWorkflowStatus);
    }

    public void initiateWorkFlowForRenewLicense(final License license, final WorkflowBean workflowBean) {
        final Position position = eisCommonsManager.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
        try {
            workflowService().start(license, position, workflowBean.getComments());
        } catch (final EGOVRuntimeException e) {
            if (license.getState().getValue().equalsIgnoreCase("END")) {
                license.setState(null);
                persistenceService.persist(license);
                workflowService().start(license, position, workflowBean.getComments());
            } else
                throw e;
        }
        license.changeState(Constants.WORKFLOW_STATE_TYPE_RENEWLICENSE + "NEW", position, workflowBean.getComments());
        final LicenseStatus underWorkflowStatus = (LicenseStatus) persistenceService
                .find("from org.egov.license.domain.entity.LicenseStatus where code='UWF'");
        license.setStatus(underWorkflowStatus);
    }*/

    public String getNextRunningLicenseNumber(final String feeType) {
        final Sequence seq = sequenceGenerator.getNextNumberWithFormat(feeType, Constants.LICENSENO_LENGTH, new Character('0'));
        return seq.getFormattedNumber();
    }

    /**
     * method to get checklist details
     * 
     * @param license
     * @return checkList
     */
    public List<LicenseChecklistHelper> getLicenseChecklist(final License license) {
        final List<LicenseChecklistHelper> checkList = new ArrayList<LicenseChecklistHelper>();
        if (license.getLicenseCheckList() != null) {
            final String[] str = license.getLicenseCheckList().split("\\^");
            for (final Object obj : str)
                checkList.add(new LicenseChecklistHelper(obj.toString(), obj.toString(), "checked"));
        }
        return checkList;
    }

    public void renew(License license) {
        final LicenseAppType appType = getLicenseApplicationTypeForRenew();
        final NatureOfBusiness nature = getNatureOfBusiness();
        final List<FeeMatrix> feeList = feeService.getFeeList(license.getTradeName(), appType, nature);
        final BigDecimal totalAmount = BigDecimal.ZERO;
        // feeService.calculateFee(license, license.getTradeName(), getLicenseApplicationTypeForRenew(),
        // getNatureOfBusiness(), BigDecimal.ZERO, BigDecimal.ZERO);
        Installment installment = installmentDao.getInsatllmentByModuleForGivenDate(getModuleName(), new Date());
        final Date renewalDate = new Date();
        final String dateDiffToExpiryDate = license.getDateDiffToExpiryDate(renewalDate);
        if (dateDiffToExpiryDate != null) {
            boolean isExpired;
            final String[] split = dateDiffToExpiryDate.split("/");
            isExpired = split[0].equalsIgnoreCase("false") ? false : true;
            final int noOfMonths = Integer.parseInt(split[1]);

            if (isExpired == false && noOfMonths <= 1) {
                final Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                cal.add(Calendar.MONTH, 1);
                installment = installmentDao.getInsatllmentByModuleForGivenDate(getModuleName(), cal.getTime());
            } else if (isExpired == true && noOfMonths <= 6)
                installmentDao.getInsatllmentByModuleForGivenDate(getModuleName(), renewalDate);
            else
                throw new EGOVRuntimeException("License already Expired Cant renew");

        }

        final EgReasonCategory reasonCategory = (EgReasonCategory) persistenceService
                .find("from org.egov.demand.model.EgReasonCategory where name='Fee'");
        final Set<EgDemandReasonMaster> egDemandReasonMasters = reasonCategory.getEgDemandReasonMasters();
        String feeType = "";
        if (getModuleName().getName().equals(Constants.ELECTRICALLICENSE_MODULENAME))
            feeType = getFeeTypeForElectricalLicense(license);
        else
            feeType = license.getClass().getSimpleName().toUpperCase();
        final String runningApplicationNumber = getNextRunningNumber(feeType + "_APPLICATION_NUMBER");
        license = license.renew(feeList, appType, nature, installment, egDemandReasonMasters, totalAmount,
                runningApplicationNumber, license.getFeeTypeStr(), getModuleName(), renewalDate);
        HibernateUtil.getCurrentSession().flush();
        HibernateUtil.getCurrentSession().refresh(license);
        license = additionalOperations(license, egDemandReasonMasters, installment);
        persistenceService.update(license);

        final LicenseStatus status = (LicenseStatus) persistenceService.find(
                "from org.egov.license.domain.entity.LicenseStatus where name=? ", Constants.LICENSE_STATUS_ACKNOWLEDGED);
        license.updateStatus(status);
    }

    public License updateLicenseForFinalApproval(final License license) {
        final LicenseStatus status = (LicenseStatus) persistenceService
                .find("from org.egov.license.domain.entity.LicenseStatus where code='ACT'");
        license.setStatus(status);
        license.setCreationAndExpiryDateForEnterLicense();
        String feeType = "";
        if (getModuleName().getName().equals(Constants.ELECTRICALLICENSE_MODULENAME))
            feeType = getFeeTypeForElectricalLicense(license);
        else
            feeType = license.getClass().getSimpleName().toUpperCase();
        final String nextRunningLicenseNumber = getNextRunningLicenseNumber(feeType + "_" + license.getFeeTypeStr()
                + "_LICENSE_NUMBER");
        license.generateLicenseNumber(nextRunningLicenseNumber);
        return license;
    }

    @SuppressWarnings("unchecked")
    public License createDemandForViolationFee(License license) {
        final Installment installment = installmentDao.getInsatllmentByModuleForGivenDate(getModuleName(), new Date());
        license = license.raiseDemandForViolationFee(installment, license);
        persistenceService.update(license);
        return license;
    }

}
