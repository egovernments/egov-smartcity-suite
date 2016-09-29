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

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.messaging.MessagingService;
import org.egov.infra.search.elastic.entity.ApplicationIndexBuilder;
import org.egov.infra.search.elastic.service.ApplicationIndexService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.ApplicationNumberGenerator;
import org.egov.mrs.application.Constants;
import org.egov.mrs.application.service.ReIssueDemandService;
import org.egov.mrs.application.service.workflow.RegistrationWorkflowService;
import org.egov.mrs.domain.entity.Applicant;
import org.egov.mrs.domain.entity.Document;
import org.egov.mrs.domain.entity.ReIssue;
import org.egov.mrs.domain.enums.ApplicationStatus;
import org.egov.mrs.domain.enums.FeeType;
import org.egov.mrs.domain.repository.ReIssueRepository;
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
    private ApplicationNumberGenerator applicationNumberGenerator;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private ApplicantService applicantService;

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
            reIssue.setApplicationNo(applicationNumberGenerator.generate());
            reIssue.setApplicationDate(new Date());
        }

        reIssue.setStatus(ApplicationStatus.Created);
        reIssue.setDemand(reIssueDemandService.createDemand(new BigDecimal(reIssue.getFeePaid())));

        final Map<Long, Document> applicantDocumentAndId = new HashMap<Long, Document>();
        documentService.getReIssueApplicantDocs().forEach(document -> applicantDocumentAndId.put(document.getId(), document));

        applicantService.addDocumentsToFileStore(null, reIssue.getApplicant(), applicantDocumentAndId);

        workflowService.transition(reIssue, workflowContainer, reIssue.getApprovalComent());

        create(reIssue);

        return reIssue.getApplicationNo();
    }

    @Transactional
    public ReIssue forwardReIssue(final Long id, final ReIssue reissueModel,
            final WorkflowContainer workflowContainer) {

        final ReIssue reissue = get(id);

        updateReIssueData(reissueModel, reissue);

        workflowService.transition(reissue, workflowContainer, reissue.getApprovalComent());
        return update(reissue);
    }

    @Transactional
    public ReIssue approveReIssue(final Long id, final WorkflowContainer workflowContainer) {
        final ReIssue reissue = get(id);
        reissue.setStatus(ApplicationStatus.Approved);
        workflowService.transition(reissue, workflowContainer, reissue.getApprovalComent());
        sendSMS(reissue);
        sendEmail(reissue);
        createReIssueAppIndex(reissue);
        return update(reissue);
    }

    @Transactional
    public ReIssue rejectReIssue(final Long id, final WorkflowContainer workflowContainer) {
        final ReIssue reIssue = get(id);
        reIssue.setStatus(ApplicationStatus.Rejected);
        reIssue.setRejectionReason(workflowContainer.getApproverComments());
        sendSMS(reIssue);
        sendEmail(reIssue);
        workflowService.transition(reIssue, workflowContainer, reIssue.getApprovalComent());

        return update(reIssue);
    }

    private void createReIssueAppIndex(final ReIssue reIssue) {
        final User user = securityUtils.getCurrentUser();
        final ApplicationIndexBuilder applicationIndexBuilder = new ApplicationIndexBuilder(Constants.MODULE_NAME,
                reIssue.getApplicationNo(),
                reIssue.getApplicationDate(), FeeType.REISSUE.name(),
                reIssue.getApplicant().getFullName(),
                reIssue.getStatus().name(),
                "/mrs/reissue/" + reIssue.getId(),
                reIssue.getApplicant().getContactInfo().getResidenceAddress(), user.getUsername() + "::" + user.getName(),"")
                        .mobileNumber(reIssue.getApplicant().getContactInfo().getMobileNo());

        applicationIndexService.createApplicationIndex(applicationIndexBuilder.build());
    }

    private void updateReIssueData(final ReIssue reissueModel, final ReIssue reissue) {
        reissue.setFeeCriteria(reissueModel.getFeeCriteria());
        reissue.setFeePaid(reissueModel.getFeePaid());
        reissue.setDemand(reIssueDemandService.createDemand(new BigDecimal(reissue.getFeePaid())));
        reissue.setStatus(ApplicationStatus.Created);

        updateApplicantInfo(reissueModel.getApplicant(), reissue.getApplicant());
        updateDocuments(reissueModel, reissue);
    }

    private void updateDocuments(final ReIssue reissueModel, final ReIssue reissue) {

        applicantService.deleteDocuments(reissueModel.getApplicant(), reissue.getApplicant());

        final Map<Long, Document> individualDocumentAndId = new HashMap<Long, Document>();
        documentService.getReIssueApplicantDocs().forEach(document -> individualDocumentAndId.put(document.getId(), document));

        applicantService.addDocumentsToFileStore(reissueModel.getApplicant(), reissue.getApplicant(), individualDocumentAndId);
    }

    private void updateApplicantInfo(final Applicant modelApplicant, final Applicant dbApplicant) {
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

        if (reIssue.getStatus() == ApplicationStatus.Rejected)
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

        if (reIssue.getStatus() == ApplicationStatus.Rejected) {
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

}
