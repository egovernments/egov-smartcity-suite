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
package org.egov.stms.transactions.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.ValidationException;

import org.egov.commons.entity.Source;
import org.egov.demand.model.EgDemand;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.search.elastic.entity.ApplicationIndex;
import org.egov.infra.search.elastic.entity.ApplicationIndexBuilder;
import org.egov.infra.search.elastic.entity.enums.ApprovalStatus;
import org.egov.infra.search.elastic.entity.enums.ClosureStatus;
import org.egov.infra.search.elastic.service.ApplicationIndexService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.pims.commons.Position;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.OwnerName;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.stms.autonumber.SHSCNumberGenerator;
import org.egov.stms.autonumber.SewerageApplicationNumberGenerator;
import org.egov.stms.autonumber.SewerageEstimationNumberGenerator;
import org.egov.stms.autonumber.SewerageWorkOrderNumberGenerator;
import org.egov.stms.elasticSearch.service.SewerageIndexService;
import org.egov.stms.masters.entity.enums.SewerageConnectionStatus;
import org.egov.stms.masters.repository.SewerageApplicationTypeRepository;
import org.egov.stms.notice.entity.SewerageNotice;
import org.egov.stms.notice.service.SewerageNoticeService;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.entity.SewerageDemandConnection;
import org.egov.stms.transactions.repository.SewerageApplicationDetailsRepository;
import org.egov.stms.transactions.workflow.ApplicationWorkflowCustomDefaultImpl;
import org.egov.stms.utils.SewerageTaxUtils;
import org.egov.stms.utils.constants.SewerageTaxConstants;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SewerageApplicationDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(SewerageApplicationDetailsService.class);

    @Autowired
    private SewerageTaxUtils sewerageTaxUtils;

    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource stmsMessageSource;

    protected SewerageApplicationDetailsRepository sewerageApplicationDetailsRepository;

    @Autowired
    private SewerageApplicationTypeRepository sewerageApplicationTypeRepository;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private ApplicationIndexService applicationIndexService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private AutonumberServiceBeanResolver beanResolver;

    @Autowired
    private SewerageDemandService sewerageDemandService;

    @Autowired
    @Qualifier("seweargeApplicationWorkflowCustomDefaultImpl")
    private ApplicationWorkflowCustomDefaultImpl applicationWorkflowCustomDefaultImpl;

    @Autowired
    private EisCommonService eisCommonService;

    @Autowired
    private SewerageIndexService sewerageIndexService;

    @Autowired
    private SewerageConnectionSmsAndEmailService sewerageConnectionSmsAndEmailService;
    
    @Autowired
    private SewerageNoticeService sewerageNoticeService;

    @Autowired
    public SewerageApplicationDetailsService(
            final SewerageApplicationDetailsRepository sewerageApplicationDetailsRepository) {
        this.sewerageApplicationDetailsRepository = sewerageApplicationDetailsRepository;
    }

    public SewerageApplicationDetails findBy(final Long id) {
        return sewerageApplicationDetailsRepository.findOne(id);
    }

    public SewerageApplicationDetails findByApplicationNumber(final String applicationNumber) {
        return sewerageApplicationDetailsRepository.findByApplicationNumber(applicationNumber);
    }

    public SewerageApplicationDetails findByApplicationNumberAndConnectionStatus(final String applicationNumber,
            final SewerageConnectionStatus status) {
        return sewerageApplicationDetailsRepository.findByApplicationNumberAndConnection_Status(applicationNumber,
                status);
    }

    public SewerageApplicationDetails findByConnectionShscNumberAndConnectionStatus(final String shscNumber,final SewerageConnectionStatus status) {
        return sewerageApplicationDetailsRepository.findByConnection_ShscNumberAndConnection_Status(shscNumber,
                status);
    }
    
    public SewerageApplicationDetails findByConnectionShscNumber(final String shscNumber){
        return sewerageApplicationDetailsRepository.findByConnection_ShscNumber(shscNumber);
    }
    
    @Transactional
    public SewerageApplicationDetails createLegacySewerageConnection(
            final SewerageApplicationDetails sewerageApplicationDetails, final HttpServletRequest request) {

        if (sewerageApplicationDetails.getApplicationNumber() == null) {
            SewerageApplicationNumberGenerator sewerageApplnNumberGenerator = beanResolver
                    .getAutoNumberServiceFor(SewerageApplicationNumberGenerator.class);
            if (sewerageApplnNumberGenerator != null)
                sewerageApplicationDetails.setApplicationNumber(sewerageApplnNumberGenerator
                        .generateNextApplicationNumber(sewerageApplicationDetails));
        }
        sewerageApplicationDetails.getConnection().setLegacy(true);
        sewerageApplicationDetails.getConnection().setStatus(SewerageConnectionStatus.ACTIVE);
        sewerageApplicationDetails.setActive(true);
        sewerageApplicationDetails.setApplicationDate(new Date());
        final Date disposalDate = getDisposalDate(sewerageApplicationDetails, sewerageApplicationDetails.getApplicationType().getProcessingTime());
        sewerageApplicationDetails.setDisposalDate(disposalDate);
        
        if (sewerageApplicationDetails != null && sewerageApplicationDetails.getCurrentDemand() == null) {
            final EgDemand demand = sewerageDemandService.createDemandOnLegacyConnection(
                    sewerageApplicationDetails.getDemandDetailBeanList(), sewerageApplicationDetails);
            if (demand != null){
                SewerageDemandConnection sdc = new SewerageDemandConnection();
                sdc.setDemand(demand);
                sdc.setApplicationDetails(sewerageApplicationDetails);
                sewerageApplicationDetails.addDemandConnections(sdc);
            } 
        }
        sewerageApplicationDetailsRepository.save(sewerageApplicationDetails);
        updateIndexes(sewerageApplicationDetails);
        return sewerageApplicationDetails;
    }

    @Transactional
    public SewerageApplicationDetails createNewSewerageConnection(
            final SewerageApplicationDetails sewerageApplicationDetails, final Long approvalPosition,
            final String approvalComent, final String additionalRule, final String workFlowAction,
            final HttpServletRequest request) {

        if (sewerageApplicationDetails.getApplicationNumber() == null) {
            SewerageApplicationNumberGenerator sewerageApplnNumberGenerator = beanResolver
                    .getAutoNumberServiceFor(SewerageApplicationNumberGenerator.class);
            if (sewerageApplnNumberGenerator != null)
                sewerageApplicationDetails.setApplicationNumber(sewerageApplnNumberGenerator
                        .generateNextApplicationNumber(sewerageApplicationDetails));
        }

        sewerageApplicationDetails.setApplicationDate(new Date());
        final Date disposalDate = getDisposalDate(sewerageApplicationDetails, sewerageApplicationDetails.getApplicationType().getProcessingTime());
        sewerageApplicationDetails.setDisposalDate(disposalDate);
        
        if (LOG.isDebugEnabled())
            LOG.debug("applicationWorkflowCustomDefaultImpl initialization is done");
        if(!sewerageApplicationDetails.getApplicationType().getCode().equalsIgnoreCase(SewerageTaxConstants.CLOSESEWERAGECONNECTION)){
            if (sewerageApplicationDetails != null && sewerageApplicationDetails.getCurrentDemand() == null) {
                final EgDemand demand = sewerageDemandService.createDemandOnNewConnection(
                        sewerageApplicationDetails.getConnectionFees(), sewerageApplicationDetails);
                if (demand != null){
                    SewerageDemandConnection sdc = new SewerageDemandConnection();
                    sdc.setDemand(demand);
                    sdc.setApplicationDetails(sewerageApplicationDetails);
                    sewerageApplicationDetails.addDemandConnections(sdc);
                }
            }
        }
        sewerageApplicationDetailsRepository.save(sewerageApplicationDetails);

        
        if(sewerageApplicationDetails.getApplicationType().getCode().equalsIgnoreCase(SewerageTaxConstants.CLOSESEWERAGECONNECTION)){
            applicationWorkflowCustomDefaultImpl.createCloseConnectionWorkflowTransition(sewerageApplicationDetails,
                    approvalPosition, approvalComent, additionalRule, workFlowAction);
        }else{
            applicationWorkflowCustomDefaultImpl.createCommonWorkflowTransition(sewerageApplicationDetails,
                approvalPosition, approvalComent, additionalRule, workFlowAction);
        }
       
        updateIndexes(sewerageApplicationDetails);
       /* if (SewerageTaxConstants.APPLICATION_STATUS_COLLECTINSPECTIONFEE.equalsIgnoreCase(sewerageApplicationDetails
                .getStatus().getCode()) || SewerageTaxConstants.APPLICATION_STATUS_CREATED.equalsIgnoreCase(sewerageApplicationDetails
                                .getStatus().getCode()))
        sewerageConnectionSmsAndEmailService.sendSmsAndEmail(sewerageApplicationDetails, request);*/
        return sewerageApplicationDetails;
    }

    @Transactional
    public void save(final SewerageApplicationDetails detail) {
        sewerageApplicationDetailsRepository.save(detail);
    }

    public Date getDisposalDate(final SewerageApplicationDetails sewerageApplicationDetails,
            final Integer appProcessTime) {
        final Calendar c = Calendar.getInstance();
        c.setTime(sewerageApplicationDetails.getApplicationDate());
        c.add(Calendar.DATE, appProcessTime);
        return c.getTime();
    }

    public  List<SewerageApplicationDetails> getSewerageConnectionDetailsByPropertyIDentifier(final String propertyIdentifier) {
        return sewerageApplicationDetailsRepository.getSewerageConnectionDetailsByPropertyID(propertyIdentifier);
    }
    
    public SewerageApplicationDetails findByConnection_ShscNumberAndIsActive(final String shscNumber) {
        return sewerageApplicationDetailsRepository.getActiveSewerageApplicationByShscNumber(shscNumber);
    }
    

    public String checkValidPropertyAssessmentNumber(final String asessmentNumber) {
        String errorMessage = "";
        final AssessmentDetails assessmentDetails = sewerageTaxUtils.getAssessmentDetailsForFlag(asessmentNumber,
                PropertyExternalService.FLAG_FULL_DETAILS);
        errorMessage = validateProperty(assessmentDetails);
        if (errorMessage.isEmpty())
            errorMessage = validatePTDue(asessmentNumber, assessmentDetails);
        return errorMessage;
    }

    /**
     * @param assessmentDetails
     * @return ErrorMessage If PropertyId is Not Valid
     */
    private String validateProperty(final AssessmentDetails assessmentDetails) {
        String errorMessage = "";
        if (assessmentDetails.getErrorDetails() != null && assessmentDetails.getErrorDetails().getErrorCode() != null)
            errorMessage = assessmentDetails.getErrorDetails().getErrorMessage();
        return errorMessage;
    }

    private String validatePTDue(final String asessmentNumber, final AssessmentDetails assessmentDetails) {
        String errorMessage = "";
        if (assessmentDetails.getPropertyDetails() != null
                && assessmentDetails.getPropertyDetails().getTaxDue() != null
                && assessmentDetails.getPropertyDetails().getTaxDue().doubleValue() > 0)

            /**
             * If property tax due present and configuration value is 'NO' then
             * restrict not to allow new water tap connection application. If
             * configuration value is 'YES' then new water tap connection can be
             * created even though there is Property Tax Due present.
             **/
            if (!sewerageTaxUtils.isNewConnectionAllowedIfPTDuePresent())
                errorMessage = stmsMessageSource.getMessage("err.validate.property.taxdue", new String[] {
                        assessmentDetails.getPropertyDetails().getTaxDue().toString(), asessmentNumber, "new" }, null);
        return errorMessage;
    }

    public String checkConnectionPresentForProperty(final String propertyID) {
        String validationMessage = "";
        final  List<SewerageApplicationDetails> sewerageApplicationDetails = getSewerageConnectionDetailsByPropertyIDentifier(propertyID);
        if (sewerageApplicationDetails != null && !sewerageApplicationDetails.isEmpty())
            if (sewerageApplicationDetails.get(0).getConnection().getStatus().toString()
                    .equalsIgnoreCase(SewerageConnectionStatus.ACTIVE.toString()))
                validationMessage = stmsMessageSource.getMessage("err.validate.newconnection.active", new String[] {
                        sewerageApplicationDetails.get(0).getConnection().getShscNumber(), propertyID }, null);
            else if (sewerageApplicationDetails.get(0).getConnection().getStatus().toString()
                    .equalsIgnoreCase(SewerageConnectionStatus.INPROGRESS.toString()))
                validationMessage = stmsMessageSource.getMessage("err.validate.newconnection.application.inprocess",
                        new String[] { propertyID, sewerageApplicationDetails.get(0).getApplicationNumber() }, null);
            else if (sewerageApplicationDetails.get(0).getConnection().getStatus().toString()
                    .equalsIgnoreCase(SewerageConnectionStatus.CLOSED.toString()))
                validationMessage = stmsMessageSource.getMessage("err.validate.newconnection.closed", new String[] {
                        sewerageApplicationDetails.get(0).getConnection().getShscNumber(), propertyID }, null);
            else if (sewerageApplicationDetails.get(0).getConnection().getStatus().toString()
                    .equalsIgnoreCase(SewerageConnectionStatus.INACTIVE.toString()))
                validationMessage = stmsMessageSource.getMessage("err.validate.newconnection.inactive", new String[] {
                        sewerageApplicationDetails.get(0).getConnection().getShscNumber(), propertyID }, null);
        return validationMessage; 
    }

    public void updateIndexes(final SewerageApplicationDetails sewerageApplicationDetails) {
        // TODO : Need to make Rest API call to get assessmentdetails
        final AssessmentDetails assessmentDetails = sewerageTaxUtils.getAssessmentDetailsForFlag(
                sewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier(),
                PropertyExternalService.FLAG_FULL_DETAILS);
        if (LOG.isDebugEnabled())
            LOG.debug(" updating Indexes Started... ");
        Iterator<OwnerName> ownerNameItr = null;
        if (null != assessmentDetails.getOwnerNames())
            ownerNameItr = assessmentDetails.getOwnerNames().iterator();
        final StringBuilder consumerName = new StringBuilder();
        final StringBuilder mobileNumber = new StringBuilder();
        Assignment assignment = null;
        User user = null;
        Integer elapsedDays = 0;
        final StringBuilder aadharNumber = new StringBuilder();
        if (null != ownerNameItr && ownerNameItr.hasNext()) {
            final OwnerName primaryOwner = ownerNameItr.next();
            consumerName.append(primaryOwner.getOwnerName() != null ? primaryOwner.getOwnerName() : "");
            mobileNumber.append(primaryOwner.getMobileNumber() != null ? primaryOwner.getMobileNumber() : "");
            aadharNumber.append(primaryOwner.getAadhaarNumber() != null ? primaryOwner.getAadhaarNumber() : "");
            while (ownerNameItr.hasNext()) {
                final OwnerName secondaryOwner = ownerNameItr.next();
                consumerName.append(",").append(
                        secondaryOwner.getOwnerName() != null ? secondaryOwner.getOwnerName() : "");
                mobileNumber.append(",").append(
                        secondaryOwner.getMobileNumber() != null ? secondaryOwner.getMobileNumber() : "");
                aadharNumber.append(",").append(
                        secondaryOwner.getAadhaarNumber() != null ? secondaryOwner.getAadhaarNumber() : "");
            }

        }
        List<Assignment> asignList = null;
        if (sewerageApplicationDetails.getState() != null
                && sewerageApplicationDetails.getState().getOwnerPosition() != null) {
            assignment = assignmentService.getPrimaryAssignmentForPositionAndDate(sewerageApplicationDetails.getState()
                    .getOwnerPosition().getId(), new Date());
            if (assignment != null) {
                asignList = new ArrayList<Assignment>();
                asignList.add(assignment);
            } else if (assignment == null)
                asignList = assignmentService.getAssignmentsForPosition(sewerageApplicationDetails.getState()
                        .getOwnerPosition().getId(), new Date());
            if (!asignList.isEmpty())
                user = userService.getUserById(asignList.get(0).getEmployee().getId());
        } else
            user = securityUtils.getCurrentUser();

        // For legacy application - create only SewarageIndex
        if (sewerageApplicationDetails.getConnection().getLegacy()
                && (null == sewerageApplicationDetails.getId() || (null != sewerageApplicationDetails.getId() && sewerageApplicationDetails
                        .getStatus().getCode().equalsIgnoreCase(SewerageTaxConstants.APPLICATION_STATUS_SANCTIONED)))) {
            sewerageIndexService.createSewarageIndex(sewerageApplicationDetails, assessmentDetails);
            return;
        }

        ApplicationIndex applicationIndex = applicationIndexService.findByApplicationNumber(sewerageApplicationDetails
                .getApplicationNumber());
        // update existing application index
        if (applicationIndex != null && null != sewerageApplicationDetails.getId()) {
            applicationIndex.setStatus(sewerageApplicationDetails.getStatus().getDescription());
            applicationIndex.setOwnername(user != null ? user.getUsername() + "::" + user.getName() : "");
            // applicationIndex.setApplicantAddress(assessmentDetails.getPropertyAddress());

            /*
             * Commented out as its required if we allow editing PT assessment
             * no on reject //After reject, from creators inbox if
             * (sewerageApplicationDetails
             * .getStatus().getCode().equals(SewerageTaxConstants
             * .APPLICATION_STATUS_CREATED) ||
             * sewerageApplicationDetails.getStatus
             * ().getCode().equals(SewerageTaxConstants
             * .APPLICATION_STATUS_COLLECTINSPECTIONFEE) ||
             * sewerageApplicationDetails
             * .getStatus().getCode().equals(SewerageTaxConstants
             * .APPLICATION_STATUS_INSPECTIONFEEPAID)){
             * applicationIndex.setApplicantName(consumerName.toString());
             * applicationIndex
             * .setApplicantAddress(assessmentDetails.getPropertyAddress()); }
             */

            // mark application index as closed on Connection Sanction
            if (sewerageApplicationDetails.getStatus().getCode()
                    .equals(SewerageTaxConstants.APPLICATION_STATUS_SANCTIONED)) {
                elapsedDays = (int) TimeUnit.DAYS.convert(new Date().getTime()
                        - sewerageApplicationDetails.getApplicationDate().getTime(), TimeUnit.MILLISECONDS);
                applicationIndex.setElapsedDays(elapsedDays);
                applicationIndex.setApproved(ApprovalStatus.APPROVED);
                applicationIndex.setClosed(ClosureStatus.YES);
            }
            // mark application index as rejected and closed on Connection
            // cancellation
            else if (sewerageApplicationDetails.getStatus().getCode()
                    .equals(SewerageTaxConstants.APPLICATION_STATUS_CANCELLED)) {
                elapsedDays = (int) TimeUnit.DAYS.convert(new Date().getTime()
                        - sewerageApplicationDetails.getApplicationDate().getTime(), TimeUnit.MILLISECONDS);
                applicationIndex.setElapsedDays(elapsedDays);
                applicationIndex.setApproved(ApprovalStatus.REJECTED);
                applicationIndex.setClosed(ClosureStatus.YES);
            }

            if (sewerageApplicationDetails.getConnection().getShscNumber() != null)
                applicationIndex.setConsumerCode(sewerageApplicationDetails.getConnection().getShscNumber());
            applicationIndexService.updateApplicationIndex(applicationIndex);

            sewerageIndexService.createSewarageIndex(sewerageApplicationDetails, assessmentDetails);
        } else {
            // Create New ApplicationIndex on create sewerage connection
            if (sewerageApplicationDetails.getApplicationDate() == null)
                sewerageApplicationDetails.setApplicationDate(new Date());
            final String url = "/stms/application/view/" + sewerageApplicationDetails.getApplicationNumber();
            if (LOG.isDebugEnabled())
                LOG.debug("Application Index creation Started... ");
            final ApplicationIndexBuilder applicationIndexBuilder = new ApplicationIndexBuilder(
                    SewerageTaxConstants.APPL_INDEX_MODULE_NAME, sewerageApplicationDetails.getApplicationNumber(),
                    sewerageApplicationDetails.getApplicationDate(), sewerageApplicationDetails.getApplicationType()
                            .getName(), consumerName.toString(), sewerageApplicationDetails.getStatus()
                            .getDescription().toString(), url, assessmentDetails.getPropertyAddress(),
                    user.getUsername() + "::" + user.getName(), Source.SYSTEM.toString());
            if (sewerageApplicationDetails.getDisposalDate() != null)
                applicationIndexBuilder.disposalDate(sewerageApplicationDetails.getDisposalDate());
            applicationIndexBuilder.mobileNumber(mobileNumber.toString());
            applicationIndexBuilder.aadharNumber(aadharNumber.toString());
            applicationIndexBuilder.approved(ApprovalStatus.INPROGRESS);
            applicationIndexBuilder.closed(ClosureStatus.NO);
            applicationIndex = applicationIndexBuilder.build();
            applicationIndexService.createApplicationIndex(applicationIndex);
            if (LOG.isDebugEnabled())
                LOG.debug("Application Index creation completed...");
            sewerageIndexService.createSewarageIndex(sewerageApplicationDetails, assessmentDetails);
        }
    }

    public BigDecimal getTotalAmount(final SewerageApplicationDetails sewerageApplicationDetails) {
        final BigDecimal balance = BigDecimal.ZERO;
        if(sewerageApplicationDetails!=null){
            final EgDemand currentDemand = sewerageApplicationDetails.getCurrentDemand();
            if (currentDemand != null) {
                /*
                 * final List<Object> instVsAmt =
                 * connectionDemandService.getDmdCollAmtInstallmentWise
                 * (currentDemand); for (final Object object : instVsAmt) { final
                 * Object[] ddObject = (Object[]) object; final BigDecimal dmdAmt =
                 * (BigDecimal) ddObject[2]; BigDecimal collAmt = BigDecimal.ZERO;
                 * if (ddObject[2] != null) collAmt = new BigDecimal((Double)
                 * ddObject[3]); balance = balance.add(dmdAmt.subtract(collAmt)); }
                 */
            }
        }
        return balance;
    }

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public Map showApprovalDetailsByApplcationCurState(final SewerageApplicationDetails sewerageApplicationDetails) {
        final Map<String, String> modelParams = new HashMap<String, String>();
        if (sewerageApplicationDetails.getState() != null) {
            String currentState = sewerageApplicationDetails.getState().getValue();
            if (currentState.equalsIgnoreCase(SewerageTaxConstants.WF_STATE_INSPECTIONFEE_PENDING)
                    || currentState.equalsIgnoreCase(SewerageTaxConstants.WF_STATE_ASSISTANT_APPROVED)
                    || currentState.equalsIgnoreCase(SewerageTaxConstants.WF_STATE_DEPUTY_EXE_APPROVED)
                    || currentState.equalsIgnoreCase(SewerageTaxConstants.WF_STATE_PAYMENTDONE)) {
                modelParams.put("showApprovalDtls", "no");
            } else {
                modelParams.put("showApprovalDtls", "yes");
            }
            if (currentState.equalsIgnoreCase(SewerageTaxConstants.WF_STATE_INSPECTIONFEE_COLLECTED)
                    || currentState.equalsIgnoreCase(SewerageTaxConstants.WF_STATE_CLERK_APPROVED)) {
                modelParams.put("mode", "edit");
            } else if (currentState.equalsIgnoreCase(SewerageTaxConstants.WF_STATE_REJECTED)) {
                modelParams.put("mode", "editOnReject");
            } else
                modelParams.put("mode", "view");
        }
        return modelParams;
    }

    @Transactional
    public SewerageApplicationDetails updateSewerageApplicationDetails(
            final SewerageApplicationDetails sewerageApplicationDetails, final Long approvalPosition,
            final String approvalComent, final String additionalRule, final String workFlowAction, final String mode,
            final ReportOutput reportOutput, final HttpServletRequest request,final HttpSession session) throws ValidationException {

        //In change in closet if demand reduced, sewerage tax collection shld not be done. Hence directly fwd application from DEE to EE and 
        //also generate workorder notice no for dee approved applications 
        if ((sewerageApplicationDetails.getStatus().getCode()
                .equalsIgnoreCase(SewerageTaxConstants.APPLICATION_STATUS_FEEPAID)
                && sewerageApplicationDetails.getState().getValue()
                        .equalsIgnoreCase(SewerageTaxConstants.WF_STATE_PAYMENTDONE)) 
              || (sewerageApplicationDetails.getStatus().getCode()
                                .equalsIgnoreCase(SewerageTaxConstants.APPLICATION_STATUS_DEEAPPROVED)
                                && additionalRule!=null && additionalRule.equalsIgnoreCase(SewerageTaxConstants.CHANGEINCLOSETS_NOCOLLECTION))         
                ) {
            if (sewerageApplicationDetails.getWorkOrderNumber() == null) {
                SewerageWorkOrderNumberGenerator workOrderNumberGenerator = beanResolver
                        .getAutoNumberServiceFor(SewerageWorkOrderNumberGenerator.class);
                if (workOrderNumberGenerator != null) {
                    sewerageApplicationDetails.setWorkOrderNumber(workOrderNumberGenerator.generateSewerageWorkOrderNumber());
                    sewerageApplicationDetails.setWorkOrderDate(new Date()); 
                }
            }
            
            if (sewerageApplicationDetails.getConnection().getShscNumber() == null) {  
                SHSCNumberGenerator shscNumberGenerator = beanResolver
                        .getAutoNumberServiceFor(SHSCNumberGenerator.class);
                if (shscNumberGenerator != null) {
                    sewerageApplicationDetails.getConnection().setShscNumber(
                            shscNumberGenerator.generateNextSHSCNumber(sewerageApplicationDetails));
                }
            }
        }
        
        if (sewerageApplicationDetails.getStatus().getCode() != null
                && (sewerageApplicationDetails.getStatus().getCode()
                        .equalsIgnoreCase(SewerageTaxConstants.APPLICATION_STATUS_INITIALAPPROVED))
                || sewerageApplicationDetails.getStatus().getCode()
                        .equalsIgnoreCase(SewerageTaxConstants.APPLICATION_STATUS_INSPECTIONFEEPAID)
                || sewerageApplicationDetails.getStatus().getCode()
                        .equalsIgnoreCase(SewerageTaxConstants.APPLICATION_STATUS_CREATED)) {
            if (sewerageApplicationDetails != null && sewerageApplicationDetails.getCurrentDemand() == null) {
                final EgDemand demand = sewerageDemandService.createDemandOnNewConnection(
                        sewerageApplicationDetails.getConnectionFees(), sewerageApplicationDetails);
                if (demand != null){
                    SewerageDemandConnection sdc = new SewerageDemandConnection();
                    sdc.setDemand(demand);
                    sdc.setApplicationDetails(sewerageApplicationDetails);
                    sewerageApplicationDetails.addDemandConnections(sdc);
                }
            } else{
                if(sewerageApplicationDetails.getApplicationType().getCode().equalsIgnoreCase(SewerageTaxConstants.CHANGEINCLOSETS)){
                    SewerageApplicationDetails oldSewerageAppDtls = findByConnection_ShscNumberAndIsActive(sewerageApplicationDetails.getConnection().getShscNumber());
                    sewerageDemandService.updateDemandOnChangeInClosets(oldSewerageAppDtls,sewerageApplicationDetails.getConnectionFees(),
                            sewerageApplicationDetails.getCurrentDemand());
                } else{
                    sewerageDemandService.updateDemand(sewerageApplicationDetails.getConnectionFees(),
                        sewerageApplicationDetails.getCurrentDemand());
                } 
            }
        }

        if(sewerageApplicationDetails.getStatus().getCode().equalsIgnoreCase(SewerageTaxConstants.APPLICATION_STATUS_DEEAPPROVED) && 
                sewerageApplicationDetails.getState().getValue().equalsIgnoreCase(SewerageTaxConstants.WF_STATE_DEPUTY_EXE_APPROVED) && 
                   additionalRule!=null && !additionalRule.equalsIgnoreCase(SewerageTaxConstants.CHANGEINCLOSETS_NOCOLLECTION)){
            if(sewerageApplicationDetails.getEstimationNumber() == null){
                SewerageEstimationNumberGenerator estimationNumberGenerator = beanResolver.getAutoNumberServiceFor(SewerageEstimationNumberGenerator.class);
                if(estimationNumberGenerator != null){
                    sewerageApplicationDetails.setEstimationNumber(estimationNumberGenerator.generateEstimationNumber());
                    sewerageApplicationDetails.setEstimationDate(new Date());
                }
            }
        }
        if(additionalRule!=null && additionalRule.equalsIgnoreCase(SewerageTaxConstants.CHANGEINCLOSETS_NOCOLLECTION))
            applicationStatusChange(sewerageApplicationDetails, workFlowAction, additionalRule);
        else
            applicationStatusChange(sewerageApplicationDetails, workFlowAction, mode);  
        
        // Generate the sewerage notices based on type of notice and save into DB.
           if (sewerageApplicationDetails.getStatus().getCode()
                   .equalsIgnoreCase(SewerageTaxConstants.APPLICATION_STATUS_ESTIMATENOTICEGEN)) {
               SewerageNotice sewerageNotice = sewerageNoticeService.generateReportForEstimation(
                       sewerageApplicationDetails, session);
               if (sewerageNotice != null)
                   sewerageApplicationDetails.addNotice(sewerageNotice);
           } else if (sewerageApplicationDetails.getStatus().getCode()
                   .equalsIgnoreCase(SewerageTaxConstants.APPLICATION_STATUS_WOGENERATED)) {//TODO: CHECK THIS STATUS IS CORRECT
               SewerageNotice sewerageNotice = sewerageNoticeService.generateReportForWorkOrder(
                       sewerageApplicationDetails, session);
               if (sewerageNotice != null)
                   sewerageApplicationDetails.addNotice(sewerageNotice);
           }
           
        final SewerageApplicationDetails updatedSewerageApplicationDetails = sewerageApplicationDetailsRepository
                .save(sewerageApplicationDetails);

        if (LOG.isDebugEnabled())
            LOG.debug("applicationWorkflowCustomDefaultImpl initialization is done");

        applicationWorkflowCustomDefaultImpl.createCommonWorkflowTransition(updatedSewerageApplicationDetails,
                approvalPosition, approvalComent, additionalRule, workFlowAction);
        
        if(sewerageApplicationDetails.getApplicationType().getCode().equalsIgnoreCase(SewerageTaxConstants.CHANGEINCLOSETS)
                && sewerageApplicationDetails.getParent() != null){
            updateIndexes(sewerageApplicationDetails.getParent());
        }    
       updateIndexes(sewerageApplicationDetails);
      
       if (SewerageTaxConstants.APPLICATION_STATUS_COLLECTINSPECTIONFEE.equalsIgnoreCase(sewerageApplicationDetails
               .getStatus().getCode()) || SewerageTaxConstants.APPLICATION_STATUS_DEEAPPROVED.equalsIgnoreCase(sewerageApplicationDetails.getStatus()
                       .getCode()) || SewerageTaxConstants.APPLICATION_STATUS_CREATED.equalsIgnoreCase(sewerageApplicationDetails
                               .getStatus().getCode()) || SewerageTaxConstants.APPLICATION_STATUS_FINALAPPROVED.equalsIgnoreCase(sewerageApplicationDetails
                                               .getStatus().getCode()) || SewerageTaxConstants.APPLICATION_STATUS_REJECTED.equalsIgnoreCase(sewerageApplicationDetails
                                                       .getStatus().getCode())){
           sewerageApplicationDetails.setApprovalComent(approvalComent);
           sewerageConnectionSmsAndEmailService.sendSmsAndEmail(sewerageApplicationDetails, request);
       }
        
        return updatedSewerageApplicationDetails;
    }

    // TODO : commented out code as statuses are changed. Need to correct

    public void applicationStatusChange(final SewerageApplicationDetails sewerageApplicationDetails,
            final String workFlowAction, final String mode) {

        if (null != sewerageApplicationDetails && null != sewerageApplicationDetails.getStatus()
                && null != sewerageApplicationDetails.getStatus().getCode())
            if (SewerageTaxConstants.WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(workFlowAction)) {
                sewerageApplicationDetails.setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(
                        SewerageTaxConstants.APPLICATION_STATUS_REJECTED, SewerageTaxConstants.MODULETYPE));
            } else if (SewerageTaxConstants.WFLOW_ACTION_STEP_CANCEL.equalsIgnoreCase(workFlowAction)) {
                // In case of change in closet, mark only the application as cancelled on cancel application from creator's inbox
                if(sewerageApplicationDetails.getApplicationType().getCode().equalsIgnoreCase(SewerageTaxConstants.CHANGEINCLOSETS)){
                    sewerageApplicationDetails.setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(
                            SewerageTaxConstants.APPLICATION_STATUS_CANCELLED, SewerageTaxConstants.MODULETYPE));
                    sewerageApplicationDetails.getConnection().setStatus(SewerageConnectionStatus.ACTIVE);
                }else{   // In case of new connection, connection is made inactive on cancel application from creator's inbox
                    sewerageApplicationDetails.getConnection().setStatus(SewerageConnectionStatus.INACTIVE);
                    sewerageApplicationDetails.setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(
                        SewerageTaxConstants.APPLICATION_STATUS_CANCELLED, SewerageTaxConstants.MODULETYPE));
                }
                
            } else if (sewerageApplicationDetails.getStatus().getCode()
                    .equals(SewerageTaxConstants.APPLICATION_STATUS_CREATED)
                    || sewerageApplicationDetails.getStatus().getCode()
                            .equals(SewerageTaxConstants.APPLICATION_STATUS_INSPECTIONFEEPAID)) {
                sewerageApplicationDetails.setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(
                        SewerageTaxConstants.APPLICATION_STATUS_INITIALAPPROVED, SewerageTaxConstants.MODULETYPE));
            } else if (sewerageApplicationDetails.getStatus().getCode()
                    .equals(SewerageTaxConstants.APPLICATION_STATUS_COLLECTINSPECTIONFEE))
                sewerageApplicationDetails.setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(
                        SewerageTaxConstants.APPLICATION_STATUS_INSPECTIONFEEPAID, SewerageTaxConstants.MODULETYPE));
            else if (sewerageApplicationDetails.getStatus().getCode()
                    .equals(SewerageTaxConstants.APPLICATION_STATUS_INITIALAPPROVED))
                sewerageApplicationDetails.setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(
                        SewerageTaxConstants.APPLICATION_STATUS_DEEAPPROVED, SewerageTaxConstants.MODULETYPE));
            else if (sewerageApplicationDetails.getStatus().getCode()
                    .equals(SewerageTaxConstants.APPLICATION_STATUS_DEEAPPROVED)){
                if(mode!=null && mode.equalsIgnoreCase(SewerageTaxConstants.CHANGEINCLOSETS_NOCOLLECTION))
                    sewerageApplicationDetails.setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(
                            SewerageTaxConstants.APPLICATION_STATUS_FINALAPPROVED, SewerageTaxConstants.MODULETYPE));
                else    
                    sewerageApplicationDetails.setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(
                        SewerageTaxConstants.APPLICATION_STATUS_ESTIMATENOTICEGEN, SewerageTaxConstants.MODULETYPE));
            }
            else if (sewerageApplicationDetails.getStatus().getCode()
                    .equals(SewerageTaxConstants.APPLICATION_STATUS_ESTIMATENOTICEGEN))
                sewerageApplicationDetails.setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(
                        SewerageTaxConstants.APPLICATION_STATUS_FEEPAID, SewerageTaxConstants.MODULETYPE));
            else if (sewerageApplicationDetails.getStatus().getCode()
                    .equals(SewerageTaxConstants.APPLICATION_STATUS_FEEPAID)
                    && sewerageApplicationDetails.getState().getValue()
                            .equalsIgnoreCase(SewerageTaxConstants.WF_STATE_PAYMENTDONE))
                sewerageApplicationDetails.setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(
                        SewerageTaxConstants.APPLICATION_STATUS_FINALAPPROVED, SewerageTaxConstants.MODULETYPE));
            else if (sewerageApplicationDetails.getStatus().getCode()
                    .equals(SewerageTaxConstants.APPLICATION_STATUS_FINALAPPROVED))
                sewerageApplicationDetails.setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(
                        SewerageTaxConstants.APPLICATION_STATUS_WOGENERATED, SewerageTaxConstants.MODULETYPE));
            else if (sewerageApplicationDetails.getStatus().getCode()
                    .equals(SewerageTaxConstants.APPLICATION_STATUS_WOGENERATED)) {
                sewerageApplicationDetails.setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(
                        SewerageTaxConstants.APPLICATION_STATUS_SANCTIONED, SewerageTaxConstants.MODULETYPE));
                // Make Connection status active on connection execution
                if (sewerageApplicationDetails.getConnection().getStatus().equals(SewerageConnectionStatus.INPROGRESS))
                    sewerageApplicationDetails.getConnection().setStatus(SewerageConnectionStatus.ACTIVE);
                sewerageApplicationDetails.setActive(true); 
            } else if (sewerageApplicationDetails.getStatus().getCode()
                    .equals(SewerageTaxConstants.APPLICATION_STATUS_REJECTED)) {
                if (sewerageTaxUtils.isInspectionFeeCollectionRequired()) {
                    if (sewerageApplicationDetails.getCurrentDemand().getAmtCollected()
                            .compareTo(BigDecimal.ZERO) == 0)
                        sewerageApplicationDetails.setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(
                                SewerageTaxConstants.APPLICATION_STATUS_COLLECTINSPECTIONFEE,
                                SewerageTaxConstants.MODULETYPE));
                    else
                        sewerageApplicationDetails.setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(
                                SewerageTaxConstants.APPLICATION_STATUS_INSPECTIONFEEPAID,
                                SewerageTaxConstants.MODULETYPE));
                } else {
                    sewerageApplicationDetails.setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(
                            SewerageTaxConstants.APPLICATION_STATUS_CREATED, SewerageTaxConstants.MODULETYPE));
                }
            }
    }
    
    
    @Transactional
    public SewerageApplicationDetails updateCloseSewerageApplicationDetails(
            final SewerageApplicationDetails sewerageApplicationDetails, final Long approvalPosition,
            final String approvalComent, final String additionalRule, final String workFlowAction, 
            final ReportOutput reportOutput, final HttpServletRequest request,final HttpSession session) throws ValidationException {

        //Application status change
        if (null != sewerageApplicationDetails && null != sewerageApplicationDetails.getStatus()
                && null != sewerageApplicationDetails.getStatus().getCode())
            if (SewerageTaxConstants.WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(workFlowAction)) {
                sewerageApplicationDetails.setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(
                        SewerageTaxConstants.APPLICATION_STATUS_REJECTED, SewerageTaxConstants.MODULETYPE));
            } else if (SewerageTaxConstants.WFLOW_ACTION_STEP_CANCEL.equalsIgnoreCase(workFlowAction)) {
                sewerageApplicationDetails.getConnection().setStatus(SewerageConnectionStatus.ACTIVE);
                sewerageApplicationDetails.setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(
                        SewerageTaxConstants.APPLICATION_STATUS_CANCELLED, SewerageTaxConstants.MODULETYPE));
            } else if (sewerageApplicationDetails.getStatus().getCode()
                    .equals(SewerageTaxConstants.APPLICATION_STATUS_CREATED)) {
                sewerageApplicationDetails.setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(
                        SewerageTaxConstants.APPLICATION_STATUS_INITIALAPPROVED, SewerageTaxConstants.MODULETYPE));
            } else if (sewerageApplicationDetails.getStatus().getCode()
                    .equals(SewerageTaxConstants.APPLICATION_STATUS_INITIALAPPROVED))
                sewerageApplicationDetails.setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(
                        SewerageTaxConstants.APPLICATION_STATUS_DEEAPPROVED, SewerageTaxConstants.MODULETYPE));
            else if (sewerageApplicationDetails.getStatus().getCode()
                    .equals(SewerageTaxConstants.APPLICATION_STATUS_DEEAPPROVED)){
                      // Make Connection status closed on EE approval
                    sewerageApplicationDetails.getConnection().setStatus(SewerageConnectionStatus.CLOSED);
                    sewerageApplicationDetails.setActive(true); 
                    sewerageApplicationDetails.setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(
                            SewerageTaxConstants.APPLICATION_STATUS_CLOSERSANCTIONED, SewerageTaxConstants.MODULETYPE));
            } else if (sewerageApplicationDetails.getStatus().getCode()
                    .equals(SewerageTaxConstants.APPLICATION_STATUS_REJECTED)) {
                    sewerageApplicationDetails.setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(
                            SewerageTaxConstants.APPLICATION_STATUS_CREATED, SewerageTaxConstants.MODULETYPE));
            }
        
        // Generate the sewerage notices based on type of notice and save into DB.
           if (sewerageApplicationDetails.getStatus().getCode()
                   .equalsIgnoreCase(SewerageTaxConstants.APPLICATION_STATUS_CLOSERSANCTIONED)) {
               SewerageNotice sewerageNotice = sewerageNoticeService.generateReportForCloseConnection(
                       sewerageApplicationDetails, session);
               if (sewerageNotice != null)
                   sewerageApplicationDetails.addNotice(sewerageNotice);
           } 
           
        final SewerageApplicationDetails updatedSewerageApplicationDetails = sewerageApplicationDetailsRepository
                .save(sewerageApplicationDetails);

        if (LOG.isDebugEnabled())
            LOG.debug("applicationWorkflowCustomDefaultImpl initialization is done");

        applicationWorkflowCustomDefaultImpl.createCloseConnectionWorkflowTransition(updatedSewerageApplicationDetails,
                approvalPosition, approvalComent, additionalRule, workFlowAction);
        
        if(sewerageApplicationDetails.getParent() != null){
            updateIndexes(sewerageApplicationDetails.getParent());
        }    
       updateIndexes(sewerageApplicationDetails);
      
       //TODO support sms and email for close connection 
       /*if (SewerageTaxConstants.APPLICATION_STATUS_DEEAPPROVED.equalsIgnoreCase(sewerageApplicationDetails.getStatus()
                       .getCode()) || SewerageTaxConstants.APPLICATION_STATUS_CREATED.equalsIgnoreCase(sewerageApplicationDetails
                               .getStatus().getCode()) || SewerageTaxConstants.APPLICATION_STATUS_FINALAPPROVED.equalsIgnoreCase(sewerageApplicationDetails
                                               .getStatus().getCode()) || SewerageTaxConstants.APPLICATION_STATUS_REJECTED.equalsIgnoreCase(sewerageApplicationDetails
                                                       .getStatus().getCode())){
           sewerageApplicationDetails.setApprovalComent(approvalComent);
           sewerageConnectionSmsAndEmailService.sendSmsAndEmail(sewerageApplicationDetails, request);
       }*/
        
        return updatedSewerageApplicationDetails;
    }

    public List<Hashtable<String, Object>> getHistory(final SewerageApplicationDetails sewerageApplicationDetails) {
        User user = null;
        final List<Hashtable<String, Object>> historyTable = new ArrayList<Hashtable<String, Object>>();
        final State state = sewerageApplicationDetails.getState();
        final Hashtable<String, Object> map = new Hashtable<String, Object>(0);
        if (null != state) {
            if (!sewerageApplicationDetails.getStateHistory().isEmpty()
                    && sewerageApplicationDetails.getStateHistory() != null)
                Collections.reverse(sewerageApplicationDetails.getStateHistory());
            for (final StateHistory stateHistory : sewerageApplicationDetails.getStateHistory()) {
                final Hashtable<String, Object> HistoryMap = new Hashtable<String, Object>(0);
                HistoryMap.put("date", stateHistory.getDateInfo());
                HistoryMap.put("comments", stateHistory.getComments());
                HistoryMap.put("updatedBy", stateHistory.getLastModifiedBy().getUsername() + "::"
                        + stateHistory.getLastModifiedBy().getName());
                HistoryMap.put("status", stateHistory.getValue());
                final Position owner = stateHistory.getOwnerPosition();
                user = stateHistory.getOwnerUser();
                if (null != user) {
                    HistoryMap.put("user", user.getUsername() + "::" + user.getName());
                    HistoryMap.put("department",
                            null != eisCommonService.getDepartmentForUser(user.getId()) ? eisCommonService
                                    .getDepartmentForUser(user.getId()).getName() : "");
                } else if (null != owner && null != owner.getDeptDesig()) {
                    user = eisCommonService.getUserForPosition(owner.getId(), new Date());
                    HistoryMap
                            .put("user", null != user.getUsername() ? user.getUsername() + "::" + user.getName() : "");
                    HistoryMap.put("department", null != owner.getDeptDesig().getDepartment() ? owner.getDeptDesig()
                            .getDepartment().getName() : "");
                }
                historyTable.add(HistoryMap);
            }

            map.put("date", state.getDateInfo());
            map.put("comments", state.getComments() != null ? state.getComments() : "");
            map.put("updatedBy", state.getLastModifiedBy().getUsername() + "::" + state.getLastModifiedBy().getName());
            map.put("status", state.getValue());
            final Position ownerPosition = state.getOwnerPosition();
            user = state.getOwnerUser();
            if (null != user) {
                map.put("user", user.getUsername() + "::" + user.getName());
                map.put("department", null != eisCommonService.getDepartmentForUser(user.getId()) ? eisCommonService
                        .getDepartmentForUser(user.getId()).getName() : "");
            } else if (null != ownerPosition && null != ownerPosition.getDeptDesig()) {
                user = eisCommonService.getUserForPosition(ownerPosition.getId(), new Date());
                map.put("user", null != user.getUsername() ? user.getUsername() + "::" + user.getName() : "");
                map.put("department", null != ownerPosition.getDeptDesig().getDepartment() ? ownerPosition
                        .getDeptDesig().getDepartment().getName() : "");
            }
            historyTable.add(map);
        }
        return historyTable;
    }

    public void updateStateTransition(final SewerageApplicationDetails sewerageApplicationDetails,
            final Long approvalPosition, final String approvalComent, final String additionalRule,
            final String workFlowAction) {
        if (approvalPosition != null && additionalRule != null
                && org.apache.commons.lang.StringUtils.isNotEmpty(workFlowAction))
            applicationWorkflowCustomDefaultImpl.createCommonWorkflowTransition(sewerageApplicationDetails,
                    approvalPosition, approvalComent, additionalRule, workFlowAction);
        // TODO : update index on collection
    }
    
    public SewerageApplicationDetails checkModifyClosetInProgress(final String shscNumber){
      return sewerageApplicationDetailsRepository.getSewerageApplicationInWorkFlow(shscNumber);
    }
    
    public SewerageApplicationDetails checkSHSCNumberExists(final String shscNumber) {
        return sewerageApplicationDetailsRepository.findByConnection_ShscNumber(shscNumber);
    }

}