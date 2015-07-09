/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
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
package org.egov.wtms.application.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.EgModules;
import org.egov.eis.service.EisCommonService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.CityWebsiteService;
import org.egov.infra.messaging.email.EmailService;
import org.egov.infra.messaging.sms.SMSService;
import org.egov.infra.search.elastic.entity.ApplicationIndex;
import org.egov.infra.search.elastic.entity.ApplicationIndexBuilder;
import org.egov.infra.search.elastic.service.ApplicationIndexService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.ApplicationNumberGenerator;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.pims.commons.Position;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.OwnerName;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.wtms.application.entity.WaterConnection;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.repository.WaterConnectionDetailsRepository;
import org.egov.wtms.elasticSearch.service.ConsumerIndexService;
import org.egov.wtms.masters.entity.ApplicationType;
import org.egov.wtms.masters.entity.DocumentNames;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.masters.entity.enums.ConnectionType;
import org.egov.wtms.masters.service.ApplicationProcessTimeService;
import org.egov.wtms.masters.service.DocumentNamesService;
import org.egov.wtms.utils.ConsumerNumberGenerator;
import org.egov.wtms.utils.WaterTaxUtils;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class WaterConnectionDetailsService {

    protected WaterConnectionDetailsRepository waterConnectionDetailsRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ApplicationNumberGenerator applicationNumberGenerator;

    @Autowired
    private ApplicationProcessTimeService applicationProcessTimeService;

    @Autowired
    private ApplicationIndexService applicationIndexService;

    @Autowired
    private DocumentNamesService documentNamesService;

    @Autowired
    private ConnectionDemandService connectionDemandService;

    @Autowired
    private PropertyExternalService propertyExternalService;

    @Autowired
    private CityWebsiteService cityWebsiteService;

    @Autowired
    private EisCommonService eisCommonService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    private ConsumerNumberGenerator consumerNumberGenerator;

    @Autowired
    private ConsumerIndexService consumerIndexService;

    @Autowired
    private SMSService smsService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private WaterTaxUtils waterTaxUtils;

    private String applicantName;

    @Autowired
    public WaterConnectionDetailsService(final WaterConnectionDetailsRepository waterConnectionDetailsRepository) {
        this.waterConnectionDetailsRepository = waterConnectionDetailsRepository;
    }

    public WaterConnectionDetails findBy(final Long waterConnectionId) {
        return waterConnectionDetailsRepository.findOne(waterConnectionId);
    }

    public List<WaterConnectionDetails> findAll() {
        return waterConnectionDetailsRepository
                .findAll(new Sort(Sort.Direction.ASC, WaterTaxConstants.APPLICATION_NUMBER));
    }

    public WaterConnectionDetails findByApplicationNumber(final String applicationNumber) {
        return waterConnectionDetailsRepository.findByApplicationNumber(applicationNumber);
    }

    public WaterConnectionDetails load(final Long id) {
        return waterConnectionDetailsRepository.getOne(id);
    }

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public Page<WaterConnectionDetails> getListWaterConnectionDetails(final Integer pageNumber,
            final Integer pageSize) {
        final Pageable pageable = new PageRequest(pageNumber - 1, pageSize, Sort.Direction.ASC,
                WaterTaxConstants.APPLICATION_NUMBER);
        return waterConnectionDetailsRepository.findAll(pageable);
    }

    @Transactional
    public WaterConnectionDetails createNewWaterConnection(final WaterConnectionDetails waterConnectionDetails,
            final Long approvalPosition, final String approvalComent) {
        if (waterConnectionDetails.getApplicationNumber() == null)
            waterConnectionDetails.setApplicationNumber(applicationNumberGenerator.generate());

        final Integer appProcessTime = applicationProcessTimeService.getApplicationProcessTime(
                waterConnectionDetails.getApplicationType(), waterConnectionDetails.getCategory());
        waterConnectionDetails.setDemand(connectionDemandService.createDemand(waterConnectionDetails));
        if (appProcessTime != null) {
            final Calendar c = Calendar.getInstance();
            c.setTime(waterConnectionDetails.getApplicationDate());
            c.add(Calendar.DATE, appProcessTime);
            waterConnectionDetails.setDisposalDate(c.getTime());
        }
        final WaterConnectionDetails savedWaterConnectionDetails = waterConnectionDetailsRepository
                .save(waterConnectionDetails);
        createWorkflowTransition(savedWaterConnectionDetails, approvalPosition, approvalComent);
        updateIndexes(savedWaterConnectionDetails);
        sendSmsOnCreateNewConnection(waterConnectionDetails);
        sendEmailOnCreateNewConnection(waterConnectionDetails);
        return savedWaterConnectionDetails;
    }

    public List<ConnectionType> getAllConnectionTypes() {
        return Arrays.asList(ConnectionType.values());
    }

    public Map<String, String> getConnectionTypesMap() {
        final Map<String, String> connectionTypeMap = new LinkedHashMap<String, String>();
        connectionTypeMap.put(ConnectionType.METERED.toString(), WaterTaxConstants.METERED);
        connectionTypeMap.put(ConnectionType.NON_METERED.toString(), WaterTaxConstants.NON_METERED);
        return connectionTypeMap;
    }

    public List<DocumentNames> getAllActiveDocumentNames(final ApplicationType applicationType) {
        return documentNamesService.getAllActiveDocumentNamesByApplicationType(applicationType);
    }

    public String checkValidPropertyAssessmentNumber(final String asessmentNumber) {
        String errorMessage = "";
        final AssessmentDetails assessmentDetails = propertyExternalService.getPropertyDetails(asessmentNumber);
        if (assessmentDetails.getErrorDetails() != null && assessmentDetails.getErrorDetails().getErrorCode() != null)
            errorMessage = assessmentDetails.getErrorDetails().getErrorMessage();
        return errorMessage;
    }

    public String getCityName() {
        return cityWebsiteService.getCityWebSiteByURL(EgovThreadLocals.getDomainName()).getCityName();
    }

    public String getCityCode() {
        return cityWebsiteService.getCityWebSiteByURL(EgovThreadLocals.getDomainName()).getCode();
    }

    public WaterConnectionDetails findByApplicationNumberOrConsumerCode(final String number) {
        return waterConnectionDetailsRepository.findByApplicationNumberOrConnection_ConsumerCode(number, number);
    }

    public WaterConnectionDetails findByConnection(final WaterConnection waterConnection) {
        return waterConnectionDetailsRepository.findByConnection(waterConnection);
    }

    public List<Hashtable<String, Object>> getHistory(final WaterConnectionDetails waterConnectionDetails) {
        User user = null;
        final List<Hashtable<String, Object>> historyTable = new ArrayList<Hashtable<String, Object>>();
        final State state = waterConnectionDetails.getState();
        final Hashtable<String, Object> map = new Hashtable<String, Object>(0);
        if (null != state) {
            map.put("date", state.getDateInfo());
            map.put("comments", state.getComments());
            map.put("updatedBy", state.getLastModifiedBy().getName());
            map.put("status", state.getValue());
            final Position ownerPosition = state.getOwnerPosition();
            user = state.getOwnerUser();
            if (null != user) {
                map.put("user", user.getUsername());
                map.put("department", null != eisCommonService.getDepartmentForUser(user.getId())
                        ? eisCommonService.getDepartmentForUser(user.getId()).getName() : "");
            } else if (null != ownerPosition && null != ownerPosition.getDeptDesig()) {
                user = eisCommonService.getUserForPosition(ownerPosition.getId(), new Date());
                map.put("user", null != user.getUsername() ? user.getUsername() : "");
                map.put("department", null != ownerPosition.getDeptDesig().getDepartment()
                        ? ownerPosition.getDeptDesig().getDepartment().getName() : "");
            }
            historyTable.add(map);
            if (!waterConnectionDetails.getStateHistory().isEmpty() && waterConnectionDetails.getStateHistory() != null)
                Collections.reverse(waterConnectionDetails.getStateHistory());
            for (final StateHistory stateHistory : waterConnectionDetails.getStateHistory()) {
                final Hashtable<String, Object> HistoryMap = new Hashtable<String, Object>(0);
                HistoryMap.put("date", stateHistory.getDateInfo());
                HistoryMap.put("comments", stateHistory.getComments());
                HistoryMap.put("updatedBy", stateHistory.getLastModifiedBy().getName());
                HistoryMap.put("status", stateHistory.getValue());
                final Position owner = stateHistory.getOwnerPosition();
                user = stateHistory.getOwnerUser();
                if (null != user) {
                    HistoryMap.put("user", user.getUsername());
                    HistoryMap.put("department", null != eisCommonService.getDepartmentForUser(user.getId())
                            ? eisCommonService.getDepartmentForUser(user.getId()).getName() : "");
                } else if (null != owner && null != owner.getDeptDesig()) {
                    user = eisCommonService.getUserForPosition(owner.getId(), new Date());
                    HistoryMap.put("user", null != user.getUsername() ? user.getUsername() : "");
                    HistoryMap.put("department", null != owner.getDeptDesig().getDepartment()
                            ? owner.getDeptDesig().getDepartment().getName() : "");
                }
                historyTable.add(HistoryMap);
            }
        }
        return historyTable;
    }

    private void createWorkflowTransition(final WaterConnectionDetails waterConnectionDetails,
            final Long approvalPosition, final String approvalComent) {
        final User user = securityUtils.getCurrentUser();
        final Position assignee = positionMasterService.getPositionById(approvalPosition);
        waterConnectionDetails.transition().start().withSenderName(user.getName())
                .withStateValue(WaterConnectionDetails.WorkFlowState.CREATED.toString()).withOwner(assignee)
                .withComments(approvalComent).withDateInfo(new Date());
    }

    @Transactional
    public WaterConnectionDetails updateNewWaterConnection(final WaterConnectionDetails waterConnectionDetails,
            final Long approvalPosition, final String approvalComent) {
        final User user = securityUtils.getCurrentUser();

        if (null != approvalPosition && !approvalPosition.equals(Long.valueOf(0))) {
            final Position owner = eisCommonService.getPrimaryAssignmentPositionForEmp(approvalPosition);
            waterConnectionDetails.transition(true).withOwner(owner).withComments(approvalComent)
                    .withSenderName(user.getName())
                    .withStateValue(WaterConnectionDetails.WorkFlowState.CHECKED.toString()).withDateInfo(new Date());
        } else
            waterConnectionDetails.transition(true).end().withComments(approvalComent).withSenderName(user.getName())
                    .withStateValue(WaterConnectionDetails.WorkFlowState.APPROVED.toString()).withDateInfo(new Date());

        if (waterConnectionDetails.getState() != null
                && waterConnectionDetails.getState().getValue().equals(WaterTaxConstants.APPROVED)) {
            waterConnectionDetails.setConnectionStatus(ConnectionStatus.ACTIVE);
            if (waterConnectionDetails.getConnection().getConsumerCode() == null)
                waterConnectionDetails.getConnection().setConsumerCode(consumerNumberGenerator.generate());
            updateIndexes(waterConnectionDetails);

        }

        final WaterConnectionDetails updatedWaterConnectionDetails = waterConnectionDetailsRepository
                .save(waterConnectionDetails);
        sendSmsOnApprovalOfNewConnection(waterConnectionDetails);
        sendEmailOnApprovalOfNewConnection(waterConnectionDetails);
        return updatedWaterConnectionDetails;
    }

    private void updateIndexes(final WaterConnectionDetails waterConnectionDetails) {

        final AssessmentDetails assessmentDetails = propertyExternalService
                .getPropertyDetails(waterConnectionDetails.getConnection().getPropertyIdentifier());

        final Iterator<OwnerName> ownerNameItr = assessmentDetails.getOwnerNames().iterator();
        final StringBuilder consumerName = new StringBuilder();
        if (ownerNameItr.hasNext()) {
            consumerName.append(ownerNameItr.next().getOwnerName());
            while (ownerNameItr.hasNext())
                consumerName.append(", ".concat(ownerNameItr.next().getOwnerName()));
        }
        setApplicantName(consumerName.toString());

        if (waterConnectionDetails.getState().getValue().equals(WaterTaxConstants.APPROVED)) {
            final ApplicationIndex applicationIndex = applicationIndexService
                    .findByApplicationNumber(waterConnectionDetails.getApplicationNumber());
            applicationIndex.setStatus(waterConnectionDetails.getConnectionStatus().toString());
            applicationIndexService.updateApplicationIndex(applicationIndex);
            consumerIndexService.createConsumerIndex(waterConnectionDetails, assessmentDetails);

        } else {
            final String strQuery = "select md from EgModules md where md.name=:name";
            final Query hql = getCurrentSession().createQuery(strQuery);
            hql.setParameter("name", WaterTaxConstants.EGMODULES_NAME);

            final ApplicationIndexBuilder applicationIndexBuilder = new ApplicationIndexBuilder(
                    ((EgModules) hql.uniqueResult()).getName(), waterConnectionDetails.getApplicationNumber(),
                    waterConnectionDetails.getApplicationDate(), waterConnectionDetails.getApplicationType().getName(),
                    consumerName.toString(), waterConnectionDetails.getConnectionStatus().toString(),
                    "/wtms/application/view/" + waterConnectionDetails.getApplicationNumber());

            if (waterConnectionDetails.getDisposalDate() != null)
                applicationIndexBuilder.disposalDate(waterConnectionDetails.getDisposalDate());
            if (waterConnectionDetails.getConnection().getMobileNumber() != null)
                applicationIndexBuilder.mobileNumber(waterConnectionDetails.getConnection().getMobileNumber());
            final ApplicationIndex applicationIndex = applicationIndexBuilder.build();
            applicationIndexService.createApplicationIndex(applicationIndex);
        }
    }

    private void sendSmsOnCreateNewConnection(final WaterConnectionDetails waterConnectionDetails) {

        if (waterConnectionDetails.getConnection().getMobileNumber() != null)
            if (waterTaxUtils.isSmsEnabled()) {
                final StringBuffer smsBody = new StringBuffer().append("Dear ").append(applicantName)
                        .append(", Your new water tap connection application request accepted with Acknowledgement No.")
                        .append(waterConnectionDetails.getApplicationNumber())
                        .append(". Please use this number in all future communication.").append(" -- Thanks, ")
                        .append(getCityName());

                smsService.sendSMS(smsBody.toString(), "91" + waterConnectionDetails.getConnection().getMobileNumber());
            }
    }

    private void sendSmsOnApprovalOfNewConnection(final WaterConnectionDetails waterConnectionDetails) {

        if (waterConnectionDetails.getConnection().getMobileNumber() != null
                && waterConnectionDetails.getConnection().getConsumerCode() != null)
            if (waterTaxUtils.isSmsEnabled()) {
                // TODO -- This is temporary sms message. This needs to be
                // replaced
                // with proper message once the requirements are clear
                final StringBuffer smsBody = new StringBuffer().append("Dear ").append(applicantName)
                        .append(", Your new water tap connection application processed with Consumer No.")
                        .append(waterConnectionDetails.getConnection().getConsumerCode())
                        .append(". Monthly water tax will be generated after the tap execution.")
                        // .append("and the connection charges/security
                        // deposit/donation charges is fixed @ Rs.xxxxx/-. ")
                        // .append(". You may collect the demand notice from the
                        // office ")
                        .append(" -- Thanks, ").append(getCityName());

                smsService.sendSMS(smsBody.toString(), "91" + waterConnectionDetails.getConnection().getMobileNumber());
            }
    }

    private void sendEmailOnCreateNewConnection(final WaterConnectionDetails waterConnectionDetails) {

        if (waterConnectionDetails.getConnection().getEmail() != null)
            if (waterTaxUtils.isEmailEnabled()) {
                final StringBuffer emailBody = new StringBuffer().append("Dear ").append(applicantName)
                        .append(",\n\nThe acknowledgement number for your water tap connection application is ")
                        .append(waterConnectionDetails.getApplicationNumber())
                        .append(".\nPlease keep this acknowledgement number to search application, pay donation fees/connection charges/security deposit and to generate estimation/work order notice. ")
                        .append("\n \n \nThis is computer generated email and does not need any signature and also please do not reply to this e-mail.")
                        .append("\n \nRegards, ").append("\n").append(getCityName());
                final StringBuffer emailSubject = new StringBuffer()
                        .append("Water tap connection application request accepted with acknowledgment No. ")
                        .append(waterConnectionDetails.getApplicationNumber());

                emailService.sendMail(waterConnectionDetails.getConnection().getEmail(), emailBody.toString(),
                        emailSubject.toString());
            }
    }

    private void sendEmailOnApprovalOfNewConnection(final WaterConnectionDetails waterConnectionDetails) {

        if (waterConnectionDetails.getConnection().getEmail() != null
                && waterConnectionDetails.getConnection().getConsumerCode() != null)
            if (waterTaxUtils.isEmailEnabled()) {
                // TODO -- This is temporary email message. This needs to be
                // replaced
                // with proper message once the requirements are clear
                final StringBuffer emailBody = new StringBuffer().append("Dear ").append(applicantName)
                        .append(",\n \nThe water tap connection application with Acknowledgement No. ")
                        .append(waterConnectionDetails.getApplicationNumber())
                        // TODO - H.S.C. No: Consumer Number
                        .append(" has been approved with Consumer No. ")
                        .append(waterConnectionDetails.getConnection().getConsumerCode())
                        // TODO - . Sending Work order Notice as part of
                        // attachment.
                        .append(". Monthly water tax will be generated after the tap execution.")
                        // TODO - H.S.C. No: Consumer Number
                        .append("\n \nPlease keep this Consumer Number for future transactions on your water tap.")
                        .append(".\n \n \nThis is computer generated email and does not need any signature and also please do not reply to this e-mail.")
                        .append("\n \nRegards, ").append("\n").append(getCityName());
                final StringBuffer emailSubject = new StringBuffer()
                        .append("Water tap connection application with acknowledgment No. ")
                        .append(waterConnectionDetails.getApplicationNumber()).append(" is approved");

                emailService.sendMail(waterConnectionDetails.getConnection().getEmail(), emailBody.toString(),
                        emailSubject.toString());
            }
    }

    public void setApplicantName(final String applicantName) {
        this.applicantName = applicantName;
    }

}
