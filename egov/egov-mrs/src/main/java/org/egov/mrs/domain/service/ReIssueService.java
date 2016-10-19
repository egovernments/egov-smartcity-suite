/* eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.mrs.domain.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.egov.eis.service.EisCommonService;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.messaging.MessagingService;
import org.egov.infra.search.elastic.entity.ApplicationIndexBuilder;
import org.egov.infra.search.elastic.service.ApplicationIndexService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.mrs.application.MarriageConstants;
import org.egov.mrs.application.MarriageUtils;
import org.egov.mrs.application.service.MarriageCertificateService;
import org.egov.mrs.application.service.ReIssueDemandService;
import org.egov.mrs.application.service.workflow.RegistrationWorkflowService;
import org.egov.mrs.autonumber.MarriageRegistrationApplicationNumberGenerator;
import org.egov.mrs.domain.entity.MarriageCertificate;
import org.egov.mrs.domain.entity.MarriageDocument;
import org.egov.mrs.domain.entity.MarriageRegistration;
import org.egov.mrs.domain.entity.MrApplicant;
import org.egov.mrs.domain.entity.ReIssue;
import org.egov.mrs.domain.enums.MarriageCertificateType;
import org.egov.mrs.domain.enums.MarriageFeeType;
import org.egov.mrs.domain.repository.ReIssueRepository;
import org.egov.mrs.masters.service.MarriageFeeService;
import org.egov.pims.commons.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ReIssueService {

    private final String MSG_KEY_SMS_REISSUE_NEW = "msg.newregistration.sms";
    private final String MSG_KEY_SMS_REISSUE_REJECTION = "msg.rejectregistration.sms";
    private final String MSG_KEY_EMAIL_REISSUE_NEW_EMAIL = "msg.newregistration.mail";
    private final String MSG_KEY_EMAIL_REISSUE_NEW_SUBJECT = "msg.newregistration.mail.subject";
    private final String MSG_KEY_EMAIL_REISSUE_REJECTION_EMAIL = "msg.rejectionregistration.mail";
    private final String MSG_KEY_EMAIL_REISSUE_REJECTION_SUBJECT = "msg.rejectionregistration.mail.subject";

    private final ReIssueRepository reIssueRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private MessagingService messagingService;

    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource mrsMessageSource;

    @Autowired
    private RegistrationWorkflowService workflowService;

    @Autowired
    private ReIssueDemandService reIssueDemandService;

    @Autowired
    private ApplicationIndexService applicationIndexService;

    @Autowired
    private MarriageRegistrationApplicationNumberGenerator marriageRegistrationApplicationNumberGenerator;
 
    @Autowired
    private MarriageDocumentService marriageDocumentService;

    @Autowired
    private MarriageApplicantService marriageApplicantService;
    
    @Autowired
    private MarriageUtils marriageUtils;
    
    @Autowired
    private EisCommonService eisCommonService;
    
    @Autowired
    private MarriageFeeService marriageFeeService;
    
    @Autowired
    private MarriageCertificateService marriageCertificateService;

    @Autowired
    public ReIssueService(final ReIssueRepository reIssueRepository) {
        this.reIssueRepository = reIssueRepository;
    }

    @Transactional
    public void create(final ReIssue reIssue) {
        reIssueRepository.save(reIssue);
    }

    @Transactional
    public ReIssue update(final ReIssue reIssue) {
        return reIssueRepository.saveAndFlush(reIssue);
    }

    public ReIssue get(final Long id) {
        return reIssueRepository.findById(id);
    }

    @Transactional
    public String createReIssueApplication(final ReIssue reIssue, final WorkflowContainer workflowContainer) {

        if (StringUtils.isBlank(reIssue.getApplicationNo())) {
            reIssue.setApplicationNo(marriageRegistrationApplicationNumberGenerator.getNextReIssueApplicationNumber(reIssue));
            reIssue.setApplicationDate(new Date());
        }
        if(reIssue.getFeeCriteria()!=null)
            reIssue.setFeeCriteria(marriageFeeService.getFee(reIssue.getFeeCriteria().getId()));
        reIssue.setStatus(marriageUtils.getStatusByCodeAndModuleType(ReIssue.ReIssueStatus.CREATED.toString(), MarriageConstants.MODULE_NAME));
        if (reIssue.getFeePaid() != null && reIssue.getDemand() == null){
        	reIssue.setDemand(reIssueDemandService.createDemand(new BigDecimal(reIssue.getFeePaid())));
        }

        final Map<Long, MarriageDocument> applicantDocumentAndId = new HashMap<Long, MarriageDocument>();
        marriageDocumentService.getIndividualDocuments().forEach(document -> applicantDocumentAndId.put(document.getId(), document));

        marriageApplicantService.addDocumentsToFileStore(null, reIssue.getApplicant(), applicantDocumentAndId);

        workflowService.transition(reIssue, workflowContainer, reIssue.getApprovalComent());

        create(reIssue);

        return reIssue.getApplicationNo();
    }

    @Transactional
    public ReIssue forwardReIssue(final Long id, final ReIssue reissueModel,
            final WorkflowContainer workflowContainer) {

        final ReIssue reissue = get(id);

        updateReIssueData(reissueModel, reissue);
        
        reissue.setStatus(
                marriageUtils.getStatusByCodeAndModuleType(ReIssue.ReIssueStatus.CREATED.toString(), MarriageConstants.MODULE_NAME));
        
        update(reissue);
        workflowService.transition(reissue, workflowContainer, reissue.getApprovalComent());
        return reissue;
    }

    @Transactional
    public ReIssue approveReIssue(ReIssue reissue, final WorkflowContainer workflowContainer) {
        reissue.setStatus(
                marriageUtils.getStatusByCodeAndModuleType(ReIssue.ReIssueStatus.APPROVED.toString(), MarriageConstants.MODULE_NAME));
        reissue = update(reissue);
        workflowService.transition(reissue, workflowContainer, workflowContainer.getApproverComments());
        sendSMS(reissue);
        sendEmail(reissue);
        createReIssueAppIndex(reissue); 
        return reissue;
    }

    @Transactional
    public ReIssue rejectReIssue(ReIssue reIssue, final WorkflowContainer workflowContainer, final HttpServletRequest request) throws IOException {
        reIssue.setStatus(workflowContainer.getWorkFlowAction().equalsIgnoreCase(MarriageConstants.WFLOW_ACTION_STEP_REJECT)?
                marriageUtils.getStatusByCodeAndModuleType(ReIssue.ReIssueStatus.REJECTED.toString(), MarriageConstants.MODULE_NAME)
                :marriageUtils.getStatusByCodeAndModuleType(ReIssue.ReIssueStatus.CANCELLED.toString(), MarriageConstants.MODULE_NAME));
        if(workflowContainer.getWorkFlowAction().equalsIgnoreCase(MarriageConstants.WFLOW_ACTION_STEP_CANCEL_REISSUE)){
            MarriageCertificate marriageCertificate = marriageCertificateService.reIssueCertificate(reIssue,request,MarriageCertificateType.REJECTION.toString());
            reIssue.addCertificate(marriageCertificate);
        }
        reIssue.setRejectionReason(workflowContainer.getApproverComments());
        workflowService.transition(reIssue, workflowContainer, workflowContainer.getApproverComments());
        sendSMS(reIssue);
        sendEmail(reIssue);
        return reIssue;
    }

    private void createReIssueAppIndex(final ReIssue reIssue) {
        final User user = securityUtils.getCurrentUser();
        final ApplicationIndexBuilder applicationIndexBuilder = new ApplicationIndexBuilder(MarriageConstants.MODULE_NAME,
                reIssue.getApplicationNo(),
                reIssue.getApplicationDate(), MarriageFeeType.CERTIFICATEISSUE.name(),
                reIssue.getApplicant().getFullName(),
                reIssue.getStatus().getDescription(),
                "/mrs/reissue/" + reIssue.getId(),
                reIssue.getApplicant().getContactInfo().getResidenceAddress(), user.getUsername() + "::" + user.getName(),"")
                        .mobileNumber(reIssue.getApplicant().getContactInfo().getMobileNo());

        applicationIndexService.createApplicationIndex(applicationIndexBuilder.build());
    }

    private void updateReIssueData(final ReIssue reissueModel, final ReIssue reissue) {
        if(reissueModel.getFeeCriteria()!=null)
            reissue.setFeeCriteria(marriageFeeService.getFee(reissueModel.getFeeCriteria().getId()));
        reissue.setFeePaid(reissueModel.getFeePaid());
        if (reissueModel.getFeePaid() != null){
        	if(reissueModel.getDemand() == null){
        		reissue.setDemand(reIssueDemandService.createDemand(new BigDecimal(reissue.getFeePaid())));
        	}
        	else{
        		reIssueDemandService.updateDemand(reissue.getDemand(),new BigDecimal(reissue.getFeePaid()));
        	}
        }
        reissue.setStatus(marriageUtils.getStatusByCodeAndModuleType(ReIssue.ReIssueStatus.CREATED.toString(), MarriageConstants.MODULE_NAME));

        updateApplicantInfo(reissueModel.getApplicant(), reissue.getApplicant());
        updateDocuments(reissueModel, reissue);
    }

    private void updateDocuments(final ReIssue reissueModel, final ReIssue reissue) {

    	marriageApplicantService.deleteDocuments(reissueModel.getApplicant(), reissue.getApplicant());

        final Map<Long, MarriageDocument> individualDocumentAndId = new HashMap<Long, MarriageDocument>();
        marriageDocumentService.getIndividualDocuments().forEach(document -> individualDocumentAndId.put(document.getId(), document));

        marriageApplicantService.addDocumentsToFileStore(reissueModel.getApplicant(), reissue.getApplicant(), individualDocumentAndId);
    }

    private void updateApplicantInfo(final MrApplicant modelApplicant, final MrApplicant dbApplicant) {
        dbApplicant.getName().setFirstName(modelApplicant.getName().getFirstName());
        dbApplicant.getName().setMiddleName(modelApplicant.getName().getMiddleName());
        dbApplicant.getName().setLastName(modelApplicant.getName().getLastName());
        dbApplicant.setOtherName(modelApplicant.getOtherName());
        dbApplicant.getContactInfo().setResidenceAddress(modelApplicant.getContactInfo().getResidenceAddress());
        dbApplicant.getContactInfo().setOfficeAddress(modelApplicant.getContactInfo().getOfficeAddress());
        dbApplicant.getContactInfo().setMobileNo(modelApplicant.getContactInfo().getMobileNo());
        dbApplicant.getContactInfo().setEmail(modelApplicant.getContactInfo().getEmail());
    }

    private void sendSMS(final ReIssue reIssue) {
        String msgKey = MSG_KEY_SMS_REISSUE_NEW;

        if (reIssue.getStatus().getCode().equalsIgnoreCase(ReIssue.ReIssueStatus.REJECTED.toString()))
            msgKey = MSG_KEY_SMS_REISSUE_REJECTION;

        final String message = mrsMessageSource.getMessage(msgKey,
                new String[] { reIssue.getRegistration().getHusband().getFullName(),
                        reIssue.getRegistration().getWife().getFullName(),
                        reIssue.getRegistration().getRegistrationNo() },
                null);
        messagingService.sendSMS(reIssue.getRegistration().getHusband().getContactInfo().getMobileNo(), message);
        messagingService.sendSMS(reIssue.getRegistration().getWife().getContactInfo().getMobileNo(), message);
    }

    private void sendEmail(final ReIssue reIssue) {
        String msgKeyMail = MSG_KEY_EMAIL_REISSUE_NEW_EMAIL;
        String msgKeyMailSubject = MSG_KEY_EMAIL_REISSUE_NEW_SUBJECT;

        if (reIssue.getStatus().getCode().equalsIgnoreCase(ReIssue.ReIssueStatus.REJECTED.toString())) {
            msgKeyMail = MSG_KEY_EMAIL_REISSUE_REJECTION_EMAIL;
            msgKeyMailSubject = MSG_KEY_EMAIL_REISSUE_REJECTION_SUBJECT;
        }

        final String message = mrsMessageSource.getMessage(msgKeyMail,
                new String[] { reIssue.getRegistration().getHusband().getFullName(),
                        reIssue.getRegistration().getWife().getFullName(),
                        reIssue.getRegistration().getRegistrationNo() },
                null);

        final String subject = mrsMessageSource.getMessage(msgKeyMailSubject, null, null);
        messagingService.sendEmail(reIssue.getRegistration().getHusband().getContactInfo().getEmail(), subject, message);
        messagingService.sendEmail(reIssue.getRegistration().getWife().getContactInfo().getEmail(), subject, message);
    }
    
    @Transactional
    public ReIssue printCertificate(ReIssue reIssue, final WorkflowContainer workflowContainer,final HttpServletRequest request) throws IOException {
        MarriageCertificate marriageCertificate = marriageCertificateService.reIssueCertificate(reIssue,request,MarriageCertificateType.REJECTION.REISSUE.toString());
        reIssue.setStatus(
                marriageUtils.getStatusByCodeAndModuleType(ReIssue.ReIssueStatus.CERTIFICATEREISSUED.toString(), MarriageConstants.MODULE_NAME));
        reIssue.addCertificate(marriageCertificate);
        reIssue.setActive(true);
        workflowService.transition(reIssue, workflowContainer, workflowContainer.getApproverComments()); 
        sendSMS(reIssue);
        sendEmail(reIssue);
        return reIssue;
    }
    
    /**
     * @param registration
     * @return
     */
    public List<Hashtable<String, Object>> getHistory(final ReIssue reIssue) {
        User user = null;
        final List<Hashtable<String, Object>> historyTable = new ArrayList<Hashtable<String, Object>>();
        final State state = reIssue.getState();
        final Hashtable<String, Object> map = new Hashtable<String, Object>(0);
        if (null != state) {
            if (!reIssue.getStateHistory().isEmpty()
                    && reIssue.getStateHistory() != null)
                Collections.reverse(reIssue.getStateHistory());
            for (final StateHistory stateHistory : reIssue.getStateHistory()) {
                final Hashtable<String, Object> HistoryMap = new Hashtable<String, Object>(0);
                HistoryMap.put("date", stateHistory.getDateInfo());
                HistoryMap.put("comments", stateHistory.getComments()!=null?stateHistory.getComments():"");
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

	public boolean checkAnyWorkFlowInProgressForRegistration(MarriageRegistration registration) {
		if(reIssueRepository.findReIssueInProgressForRegistration(registration.getRegistrationNo())==null) return false; 
	return true;
	}

}
