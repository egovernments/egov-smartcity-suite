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
package org.egov.ptis.domain.service.revisionPetition;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.egov.infra.utils.DateUtils.toDefaultDateFormat;
import static org.egov.ptis.constants.PropertyTaxConstants.ADDITIONAL_COMMISSIONER_DESIGN;
import static org.egov.ptis.constants.PropertyTaxConstants.ANONYMOUS_USER;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_GRP;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_REVISION_PETITION;
import static org.egov.ptis.constants.PropertyTaxConstants.ASSISTANT_COMMISSIONER_DESIGN;
import static org.egov.ptis.constants.PropertyTaxConstants.CITY_GRADE_CORPORATION;
import static org.egov.ptis.constants.PropertyTaxConstants.COMMISSIONER_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_SECONDHALF_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.DATE_FORMAT_DDMMYYY;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_EDUCATIONAL_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_LIBRARY_CESS;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_UNAUTHORIZED_PENALTY;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_VACANT_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMAND_RSNS_LIST;
import static org.egov.ptis.constants.PropertyTaxConstants.DEPUTY_COMMISSIONER_DESIGN;
import static org.egov.ptis.constants.PropertyTaxConstants.GENERAL_REVISION_PETITION;
import static org.egov.ptis.constants.PropertyTaxConstants.GRP_APP_STATUS_REJECTED;
import static org.egov.ptis.constants.PropertyTaxConstants.GRP_CREATED;
import static org.egov.ptis.constants.PropertyTaxConstants.GRP_HEARINGCOMPLETED;
import static org.egov.ptis.constants.PropertyTaxConstants.GRP_INSPECTIONVERIFIED;
import static org.egov.ptis.constants.PropertyTaxConstants.GRP_INSPECTION_COMPLETE;
import static org.egov.ptis.constants.PropertyTaxConstants.GRP_WF_REGISTERED;
import static org.egov.ptis.constants.PropertyTaxConstants.JUNIOR_ASSISTANT;
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_GENERAL_REVISION_PETITION;
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_OF_WORK_GRP;
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_OF_WORK_RP;
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_REVISION_PETITION;
import static org.egov.ptis.constants.PropertyTaxConstants.NON_VACANT_TAX_DEMAND_CODES;
import static org.egov.ptis.constants.PropertyTaxConstants.OBJECTION_CREATED;
import static org.egov.ptis.constants.PropertyTaxConstants.OBJECTION_HEARING_COMPLETED;
import static org.egov.ptis.constants.PropertyTaxConstants.OBJECTION_HEARING_FIXED;
import static org.egov.ptis.constants.PropertyTaxConstants.OBJECTION_MODULE;
import static org.egov.ptis.constants.PropertyTaxConstants.OBJECTION_PRINT_ENDORSEMENT;
import static org.egov.ptis.constants.PropertyTaxConstants.OBJECTION_RECORD_INSPECTIONDETAILS;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MODIFY_REASON_GENERAL_REVISION_PETITION;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MODIFY_REASON_REVISION_PETITION;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;
import static org.egov.ptis.constants.PropertyTaxConstants.REPORT_TEMPLATENAME_REVISIONPETITION_ENDORSEMENT;
import static org.egov.ptis.constants.PropertyTaxConstants.REPORT_TEMPLATENAME_REVISIONPETITION_HEARINGNOTICE;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_CLERK_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_OFFICER_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.REVISIONPETITION_CREATED;
import static org.egov.ptis.constants.PropertyTaxConstants.REVISION_PETITION;
import static org.egov.ptis.constants.PropertyTaxConstants.RP_APP_STATUS_REJECTED;
import static org.egov.ptis.constants.PropertyTaxConstants.RP_CREATED;
import static org.egov.ptis.constants.PropertyTaxConstants.RP_HEARINGCOMPLETED;
import static org.egov.ptis.constants.PropertyTaxConstants.RP_INSPECTIONVERIFIED;
import static org.egov.ptis.constants.PropertyTaxConstants.RP_INSPECTION_COMPLETE;
import static org.egov.ptis.constants.PropertyTaxConstants.RP_WF_REGISTERED;
import static org.egov.ptis.constants.PropertyTaxConstants.SENIOR_ASSISTANT;
import static org.egov.ptis.constants.PropertyTaxConstants.SOURCE_MEESEVA;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_CANCELLED;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_OBJECTED_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_APPROVE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_FORWARD;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_REJECT;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_SAVE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_SIGN;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_COMMISSIONER_APPROVAL_PENDING;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_DIGITAL_SIGNATURE_PENDING;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_REJECTED_TO_CANCEL;
import static org.egov.ptis.constants.PropertyTaxConstants.WS_VIEW_PROPERT_BY_APP_NO_URL;
import static org.egov.ptis.constants.PropertyTaxConstants.ZONAL_COMMISSIONER_DESIGN;
import static org.egov.ptis.domain.service.property.PropertyService.APPLICATION_VIEW_URL;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_REJECT_TO_CANCEL;
import static org.egov.ptis.constants.PropertyTaxConstants.APPEAL_INSPECTION_COMPLETE;
import static org.egov.ptis.constants.PropertyTaxConstants.APPEALPETITION_CODE;
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_APPEALPETITION;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_APPEAL_PETITION;
import static org.egov.ptis.constants.PropertyTaxConstants.APPEAL_INSPECTIONVERIFIED;
import static org.egov.ptis.constants.PropertyTaxConstants.APPEAL_HEARINGCOMPLETED;
import static org.egov.ptis.constants.PropertyTaxConstants.APPEAL_CREATED;
import static org.egov.ptis.constants.PropertyTaxConstants.APPEAL_WF_REGISTERED;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_APPEALPETITION;
import static org.egov.ptis.constants.PropertyTaxConstants.APPEAL_APP_STATUS_REJECTED;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_RPPROCEEDINGS;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_GRPPROCEEDINGS;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_APPEALPROCEEDINGS;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.egov.commons.EgwStatus;
import org.egov.commons.Installment;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.dao.InstallmentDao;
import org.egov.commons.entity.Source;
import org.egov.demand.model.EgDemandDetails;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.eis.service.EisCommonService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.elasticsearch.entity.ApplicationIndex;
import org.egov.infra.elasticsearch.service.ApplicationIndexService;
import org.egov.infra.integration.service.ThirdPartyService;
import org.egov.infra.notification.service.NotificationService;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.reporting.engine.ReportFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.ApplicationNumberGenerator;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.utils.StringUtils;
import org.egov.infra.web.utils.WebUtils;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.portal.entity.PortalInbox;
import org.egov.ptis.bean.PropertyNoticeInfo;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.PropertyStatusDAO;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.objection.Petition;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyID;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.domain.service.property.SMSEmailService;
import org.egov.ptis.exceptions.TaxCalculatorExeption;
import org.egov.ptis.report.bean.PropertyAckNoticeInfo;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

public class RevisionPetitionService extends PersistenceService<Petition, Long> {

    private static final String REVISION_PETITION_CREATED = "CREATED";
    private static final Logger LOGGER = Logger.getLogger(RevisionPetitionService.class);
    private static final String CURRENT = "current";
    private static final String HISTORY = "history";
    private static final String REJECT_INSPECTION_STR = "Reject Inspection";
    private static final String FORWARD_TO_APPROVER = "forward to approver";
    private static final String CANCEL_UNCONSIDERED = "cancel unconsidered";
    private static final String PRINT_ENDORESEMENT = "Print Endoresement";
    private static final String REJECT = "reject";
    private static final String CHOOSE = "----Choose----";
    public static final String OBJECTION_FORWARD = "objection.forward";
    public static final String REJECT_INSPECTION = "objection.inspection.rejection";
    private static final String APPROVE = "Approve";
    private static final String REJECTED = "Rejected";
    private static final String NON_HISTORY = "N";

    @Autowired
    protected AssignmentService assignmentService;
    @Autowired
    @Qualifier("workflowService")
    protected SimpleWorkflowService<Petition> revisionPetitionWorkFlowService;
    @Autowired
    DesignationService designationService;
    @Autowired
    private ApplicationNumberGenerator applicationNumberGenerator;
    @Autowired
    private PropertyStatusDAO propertyStatusDAO;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private EgwStatusHibernateDAO egwStatusDAO;
    @Autowired
    private EisCommonService eisCommonService;
    @Autowired
    private ApplicationIndexService applicationIndexService;
    @Autowired
    private NotificationService notificationService;
    private SMSEmailService sMSEmailService;

    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private PtDemandDao ptDemandDAO;

    @Autowired
    private UserService userService;

    @Autowired
    private CityService cityService;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    private PropertyTaxUtil propertyTaxUtil;

    @Autowired
    private InstallmentDao installmentDao;

    @Autowired
    private ModuleService moduleDao;
    
    @Autowired
    private ThirdPartyService thirdPartyService;
    
    public RevisionPetitionService() {
        super(Petition.class);
    }

    public RevisionPetitionService(final Class<Petition> type) {
        super(type);
    }

    /**
     * Create revision petition
     *
     * @param petition
     * @return
     */
    @Transactional
    public Petition createRevisionPetition(final Petition petition) {
        Petition petitions;
        propertyService.processAndStoreDocument(petition.getDocuments());
        if (petition.getId() == null)
            petitions = persist(petition);
        else
            petitions = merge(petition);

        return petitions;

    }

    /**
     * Api to save revision petition using rest api's.
     *
     * @param petition
     * @return
     */
    @Transactional
    public Petition createRevisionPetitionForRest(Petition petition) {
        Position position = null;
        WorkFlowMatrix wfmatrix;
        User user = null;
        if (petition.getId() == null) {
            if (petition.getObjectionNumber() == null)
                petition.setObjectionNumber(applicationNumberGenerator.generate());
            petition.getBasicProperty().setStatus(propertyStatusDAO.getPropertyStatusByCode(STATUS_OBJECTED_STR));
            petition.getBasicProperty().setUnderWorkflow(Boolean.TRUE);
            if (petition.getState() == null) {
                wfmatrix = revisionPetitionWorkFlowService.getWfMatrix(petition.getStateType(), null, null, null,
                        REVISIONPETITION_CREATED, null);
                // Get the default revenue cleark from admin boundary.
                final Designation desig = designationService.getDesignationByName(REVENUE_CLERK_DESGN);
                List<Assignment> assignment = assignmentService.findByDesignationAndBoundary(desig.getId(),
                        petition.getBasicProperty().getPropertyID().getZone().getId());
                if (!assignment.isEmpty())
                    position = assignment.get(0).getPosition();
                else {
                    assignment = assignmentService.findPrimaryAssignmentForDesignationName(REVENUE_CLERK_DESGN);
                    if (!assignment.isEmpty())
                        position = assignment.get(0).getPosition();
                }

                updateRevisionPetitionStatus(wfmatrix, petition, null);

                if (position != null)
                    user = eisCommonService.getUserForPosition(position.getId(), new Date());

                petition.transition().start().withNextAction(wfmatrix.getPendingActions())
                        .withStateValue(wfmatrix.getCurrentState()).withOwner(position)
                        .withSenderName(user != null && user.getName() != null ? user.getName() : "").withOwner(user)
                        .withComments("");
            }

            applyAuditing(petition.getState());
            petition = persist(petition);
            updateIndex(petition);

            sendEmailandSms(petition, REVISION_PETITION_CREATED);
        } else
            petition = merge(petition);

        return petition;

    }

    /**
     * Update elastic search index
     *
     * @param petition
     */
    private void updateIndex(final Petition petition) {
        ApplicationIndex applicationIndex = applicationIndexService
                .findByApplicationNumber(petition.getObjectionNumber());
        final User user = securityUtils.getCurrentUser();
        if (null == applicationIndex) {
            applicationIndex = ApplicationIndex.builder().withModuleName(PTMODULENAME)
                    .withApplicationNumber(petition.getObjectionNumber())
                    .withApplicationDate(petition.getCreatedDate() != null ? petition.getCreatedDate() : new Date())
                    .withApplicationType(APPLICATION_TYPE_REVISION_PETITION)
                    .withApplicantName(petition.getBasicProperty().getFullOwnerName())
                    .withStatus(petition.getState().getValue())
                    .withUrl(format(APPLICATION_VIEW_URL, petition.getObjectionNumber(), ""))
                    .withApplicantAddress(petition.getBasicProperty().getAddress().toString())
                    .withOwnername(user.getUsername() + "::" + user.getName()).withChannel(Source.SYSTEM.toString())
                    .build();
            applicationIndexService.createApplicationIndex(applicationIndex);
        } else {
            applicationIndex.setStatus(petition.getState().getValue());
            applicationIndexService.updateApplicationIndex(applicationIndex);
        }
    }

    /**
     * @param wfmatrix
     * @param petition
     * @param status
     */
    private void updateRevisionPetitionStatus(final WorkFlowMatrix wfmatrix, final Petition petition,
            final String status) {

        EgwStatus egwStatus = null;
        if (isNotBlank(status))
            egwStatus = egwStatusDAO.getStatusByModuleAndCode(OBJECTION_MODULE, status);

        else if (wfmatrix != null && wfmatrix.getNextStatus() != null && petition != null)
            egwStatus = egwStatusDAO.getStatusByModuleAndCode(OBJECTION_MODULE, wfmatrix.getNextStatus());
        if (egwStatus != null)
            petition.setEgwStatus(egwStatus);

    }

    /**
     * Api to update revision petition.
     *
     * @param petition
     * @return
     */

    @Transactional
    public Petition updateRevisionPetition(final Petition petition) {
        Petition petitions;
        if (petition.getId() == null)
            petitions = persist(petition);
        else
            petitions = update(petition);

        return petitions;

    }

    /**
     * Get revision petition by application number
     *
     * @param applicationNumber
     * @return
     */
    public Petition getRevisionPetitionByApplicationNumber(final String applicationNumber) {
        Petition revPetitionObject;
        final Criteria appCriteria = getSession().createCriteria(Petition.class, "revPetiton");
        appCriteria.add(Restrictions.eq("revPetiton.objectionNumber", applicationNumber));
        revPetitionObject = (Petition) appCriteria.uniqueResult();

        return revPetitionObject;
    }

    /**
     * Api to send EMAIL and SMS.
     *
     * @param petition
     * @param applicationType
     */
    public void sendEmailandSms(final Petition petition, final String applicationType) {
        if (petition != null)
            for (final PropertyOwnerInfo ownerInfo : petition.getBasicProperty().getPropertyOwnerInfo())
                sendEmailAndSms(petition, ownerInfo.getOwner(), applicationType);
    }

    private void sendEmailAndSms(final Petition petition, final User user, final String applicationType) {
        final String mobileNumber = user.getMobileNumber();
        final String emailid = user.getEmailId();
        final String applicantName = user.getName();
        final List<String> args = new ArrayList<>();
        args.add(applicantName);
        String smsMsg = "";
        String emailSubject = "";
        String emailBody = "";

        if (applicationType != null && applicationType.equalsIgnoreCase(REVISION_PETITION_CREATED)) {
            args.add(petition.getObjectionNumber());
            if (mobileNumber != null)
                smsMsg = new StringBuilder(petition.getType()).append(" created. Use ")
                        .append(petition.getObjectionNumber()).append(" for future reference").toString();
            if (emailid != null) {
                emailSubject = new StringBuilder(petition.getType()).append(" created.").toString();
                emailBody =new StringBuilder(petition.getType()).append(" created. Use ").append(petition.getObjectionNumber())
                        .append(" for future reference").toString();
            }
        }
        if (isNotBlank(mobileNumber) && isNotBlank(smsMsg))
            notificationService.sendSMS(mobileNumber, smsMsg);
        if (isNotBlank(emailid) && isNotBlank(emailBody))
            notificationService.sendEmail(emailid, emailSubject, emailBody);
    }

    public SMSEmailService getsMSEmailService() {
        return sMSEmailService;
    }

    public void setsMSEmailService(final SMSEmailService sMSEmailService) {
        this.sMSEmailService = sMSEmailService;
    }

    public Petition createRevisionPetition(final Petition petition,
            final Map<String, String> meesevaParams) {
        createRevisionPetition(petition);
        return petition;
    }

    public Assignment getWorkflowInitiator(final Petition petition) {
        Assignment wfInitiator;
        if (propertyService.isEmployee(petition.getCreatedBy())
                && !ANONYMOUS_USER.equalsIgnoreCase(petition.getCreatedBy().getName())
                && !propertyService.isCitizenPortalUser(petition.getCreatedBy())) {
            if (petition.getState() != null && petition.getState().getInitiatorPosition() != null)
                wfInitiator = propertyTaxCommonUtils.getUserAssignmentByPassingPositionAndUser(petition.getCreatedBy(),
                        petition.getState().getInitiatorPosition());
            else
                wfInitiator = assignmentService.getAllActiveEmployeeAssignmentsByEmpId(petition.getCreatedBy().getId())
                        .get(0);
        } else if (!petition.getStateHistory().isEmpty()) {
            if (petition.getState().getInitiatorPosition() == null)
                wfInitiator = assignmentService.getAssignmentsForPosition(
                        petition.getStateHistory().get(0).getOwnerPosition().getId(), new Date()).get(0);
            else
                wfInitiator = assignmentService
                        .getAssignmentsForPosition(petition.getState().getInitiatorPosition().getId(), new Date())
                        .get(0);
        } else
            wfInitiator = assignmentService
                    .getAssignmentsForPosition(petition.getState().getOwnerPosition().getId(), new Date()).get(0);
        return wfInitiator;
    }

    public Petition getExistingObjections(final BasicProperty basicProperty) {
        @SuppressWarnings("unchecked")
        List<Petition> petition = (List<Petition>) entityManager.createNamedQuery("RP_BY_BASICPROPERTY")
                .setParameter("basicProperty", basicProperty).getResultList();
        return !petition.isEmpty() ? petition.get(0) : null;
    }

    public Petition getExistingGRP(final BasicProperty basicProperty) {
        return find("from Petition rp where rp.basicProperty = ? and rp.type = ?", basicProperty,
                NATURE_OF_WORK_GRP);
    }

    /**
     * @param reportOutput
     * @param petition
     * @return ReportOutput
     */
    public ReportOutput createHearingNoticeReport(ReportOutput reportOutput, final Petition petition,
            final String noticeNo) {
        reportOutput.setReportFormat(ReportFormat.PDF);
        final HashMap<String, Object> reportParams = new HashMap<>();
        String natureOfWork;
        ReportRequest reportRequest;
        if (petition != null) {
            final HttpServletRequest request = ServletActionContext.getRequest();
            final String cityName = request.getSession().getAttribute("citymunicipalityname").toString();
            final String cityGrade = request.getSession().getAttribute("cityGrade") != null
                    ? request.getSession().getAttribute("cityGrade").toString() : null;
            final Boolean isCorporation = isNotBlank(cityGrade) && cityGrade.equalsIgnoreCase(CITY_GRADE_CORPORATION);
            if (NATURE_OF_WORK_RP.equalsIgnoreCase(petition.getType()))
                natureOfWork = NATURE_REVISION_PETITION;
            else if(WFLOW_ACTION_APPEALPETITION.equalsIgnoreCase(petition.getType()))
                natureOfWork = NATURE_APPEALPETITION;
            else
                natureOfWork = NATURE_GENERAL_REVISION_PETITION;
            reportParams.put("isCorporation", isCorporation);
            reportParams.put("cityName", cityName);
            reportParams.put("recievedBy", petition.getBasicProperty().getFullOwnerName());
            reportParams.put("natureOfWork", natureOfWork);
            

            if (petition.getHearings() != null && !petition.getHearings().isEmpty()
                    && petition.getHearings().get(petition.getHearings().size() - 1).getPlannedHearingDt() != null)
                reportParams.put("hearingNoticeDate", toDefaultDateFormat(
                        petition.getHearings().get(petition.getHearings().size() - 1).getPlannedHearingDt()));
            else
                reportParams.put("hearingNoticeDate", "");
            reportParams.put("currentDate", toDefaultDateFormat(new Date()));
            reportParams.put("recievedOn", toDefaultDateFormat(petition.getRecievedOn()));
            reportParams.put("docNumberObjection", noticeNo);
            reportParams.put("houseNo", petition.getBasicProperty().getAddress().getHouseNoBldgApt());
            reportParams.put("locality", petition.getBasicProperty().getPropertyID().getLocality().getName());
            reportParams.put("assessmentNo", petition.getBasicProperty().getUpicNo());
            reportParams.put("hearingTime",
                    petition.getHearings().get(petition.getHearings().size() - 1).getHearingTime());
            reportParams.put("hearingVenue",
                    petition.getHearings().get(petition.getHearings().size() - 1).getHearingVenue());
            reportRequest = new ReportRequest(REPORT_TEMPLATENAME_REVISIONPETITION_HEARINGNOTICE, petition,
                    reportParams);
            reportOutput = reportService.createReport(reportRequest);
        }
        return reportOutput;
    }

    /**
     * @param reportOutput
     * @param petition
     * @return
     */
    public ReportOutput createEndoresement(ReportOutput reportOutput, final Petition petition) {

        reportOutput.setReportFormat(ReportFormat.PDF);
        final HashMap<String, Object> reportParams = new HashMap<>();
        String natureOfWork;
        ReportRequest reportRequest;
        if (petition != null) {
            final Map<String, BigDecimal> currentDemand = ptDemandDAO.getDemandCollMap(petition.getProperty());
            final Map<String, BigDecimal> earlierDemand = ptDemandDAO.getDemandCollMap(
                    propertyService.getLatestHistoryProperty(petition.getBasicProperty().getUpicNo()));
            if (NATURE_OF_WORK_RP.equalsIgnoreCase(petition.getType()))
                natureOfWork = NATURE_REVISION_PETITION;
            else if(WFLOW_ACTION_APPEALPETITION.equalsIgnoreCase(petition.getType()))
                natureOfWork = NATURE_APPEALPETITION;
            else
                natureOfWork = NATURE_GENERAL_REVISION_PETITION;
            reportParams.put("logoPath", cityService.getCityLogoURL());
            reportParams.put("cityName", cityService.getMunicipalityName());
            reportParams.put("natureOfWork", natureOfWork);
            reportParams.put("recievedBy", petition.getBasicProperty().getFullOwnerName());
            reportParams.put("docNumberObjection", petition.getObjectionNumber());
            reportParams.put("currentDate", toDefaultDateFormat(new Date()));
            reportParams.put("receivedOn", toDefaultDateFormat(petition.getRecievedOn()));
            reportParams.put("HouseNo", petition.getBasicProperty().getUpicNo());
            reportParams.put("wardNumber", petition.getBasicProperty().getBoundary() != null
                    ? petition.getBasicProperty().getBoundary().getName() : "");
                        reportParams.put("HalfYearPropertyTaxTo",
                                        currentDemand.get(CURR_SECONDHALF_DMD_STR).setScale(2, BigDecimal.ROUND_HALF_UP));
                        reportParams.put("HalfYearPropertyTaxFrom",
                                        earlierDemand.get(CURR_SECONDHALF_DMD_STR).setScale(2, BigDecimal.ROUND_HALF_UP));
                        reportParams.put("AnnualPropertyTaxTo", currentDemand.get(CURR_SECONDHALF_DMD_STR)
                                        .multiply(BigDecimal.valueOf(2)).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                        reportParams.put("AnnualPropertyTaxFrom", earlierDemand.get(CURR_SECONDHALF_DMD_STR)
                                        .multiply(BigDecimal.valueOf(2)).setScale(2, BigDecimal.ROUND_HALF_UP).toString());

            reportRequest = new ReportRequest(REPORT_TEMPLATENAME_REVISIONPETITION_ENDORSEMENT, petition,
                    reportParams);
            reportOutput = reportService.createReport(reportRequest);
        }
        return reportOutput;

    }

    public void setNoticeInfo(final PropertyImpl property, final PropertyNoticeInfo propertyNotice,
            final BasicPropertyImpl basicProperty, final Petition petition) {
        final PropertyAckNoticeInfo infoBean = new PropertyAckNoticeInfo();
        final Address ownerAddress = basicProperty.getAddress();
        BigDecimal totalTax = BigDecimal.ZERO;
        BigDecimal propertyTax = BigDecimal.ZERO;
        if (basicProperty.getPropertyOwnerInfo().size() > 1)
            infoBean.setOwnerName(basicProperty.getFullOwnerName().concat(" and others"));
        else
            infoBean.setOwnerName(basicProperty.getFullOwnerName());

        infoBean.setOwnerAddress(basicProperty.getAddress().toString());
        infoBean.setApplicationNo(property.getApplicationNo());
        infoBean.setDoorNo(ownerAddress.getHouseNoBldgApt());
        if (isNotBlank(ownerAddress.getLandmark()))
            infoBean.setStreetName(ownerAddress.getLandmark());
        else
            infoBean.setStreetName("N/A");
        final SimpleDateFormat formatNowYear = new SimpleDateFormat("yyyy");
        final String occupancyYear = formatNowYear.format(basicProperty.getPropOccupationDate());
        infoBean.setInstallmentYear(occupancyYear);
        infoBean.setAssessmentNo(basicProperty.getUpicNo());
        final SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
        infoBean.setAssessmentDate(dateformat.format(basicProperty.getAssessmentdate()));
        final Ptdemand currDemand = ptDemandDAO.getNonHistoryCurrDmdForProperty(property);

        // Sets data for the current property
        prepareTaxInfoForProperty(infoBean, totalTax, propertyTax, currDemand, CURRENT);
        if (currDemand.getDmdCalculations() != null && currDemand.getDmdCalculations().getAlv() != null)
            infoBean.setNew_rev_ARV(currDemand.getDmdCalculations().getAlv());

        // Sets data for the latest history property
        final PropertyImpl historyProperty = propertyService.getLatestHistoryProperty(basicProperty.getUpicNo());
        final Ptdemand historyDemand = ptDemandDAO.getNonHistoryCurrDmdForProperty(historyProperty);
        if (historyProperty != null && historyDemand != null) {
            totalTax = BigDecimal.ZERO;
            propertyTax = BigDecimal.ZERO;
            prepareTaxInfoForProperty(infoBean, totalTax, propertyTax, historyDemand, HISTORY);
            if (historyDemand.getDmdCalculations() != null && historyDemand.getDmdCalculations().getAlv() != null)
                infoBean.setExistingARV(historyDemand.getDmdCalculations().getAlv());
        }

        final PropertyID boundaryDetails = basicProperty.getPropertyID();
        infoBean.setZoneName(boundaryDetails.getZone().getName());
        infoBean.setWardName(boundaryDetails.getWard().getName());
        infoBean.setAreaName(boundaryDetails.getArea().getName());
        infoBean.setLocalityName(boundaryDetails.getLocality().getName());
        infoBean.setNoticeDate(new Date());
        infoBean.setApplicationDate(DateUtils.getFormattedDate(petition.getCreatedDate(), DATE_FORMAT_DDMMYYY));
        infoBean.setHearingDate(
                DateUtils.getFormattedDate(petition.getHearings().get(0).getPlannedHearingDt(), DATE_FORMAT_DDMMYYY));
        infoBean.setActualHearingDate(
                DateUtils.getFormattedDate(petition.getHearings().get(0).getActualHearingDt(), DATE_FORMAT_DDMMYYY));
        final User approver = userService.getUserById(ApplicationThreadLocals.getUserId());
        infoBean.setApproverName(approver.getName());
        final BigDecimal revTax = currDemand.getBaseDemand();
        infoBean.setNewTotalTax(revTax.setScale(0, BigDecimal.ROUND_HALF_UP));
        if (property.getSource().equals(SOURCE_MEESEVA))
            infoBean.setMeesevaNo(property.getApplicationNo());
        if(petition.getDisposalDate() != null){
        infoBean.setDisposalDate(formatNowYear.format(petition.getDisposalDate()));
        }
        propertyNotice.setOwnerInfo(infoBean);
    }

    /**
     * Sets data for the current property and history property based on the propertyType (either new/history)
     */
    private void prepareTaxInfoForProperty(final PropertyAckNoticeInfo infoBean, BigDecimal totalTax,
            BigDecimal propertyTax, final Ptdemand currDemand, final String propertyType) {
        for (final EgDemandDetails demandDetail : currDemand.getEgDemandDetails())
            if (demandDetail.getEgDemandReason().getEgInstallmentMaster()
                    .equals(propertyTaxCommonUtils.getCurrentPeriodInstallment())) {
                totalTax = totalTax.add(propertyTaxCommonUtils.getTotalDemandVariationAmount(demandDetail));

                if (demandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode()
                        .equalsIgnoreCase(DEMANDRSN_CODE_EDUCATIONAL_TAX))
                    propertyTax = propertyTax.add(propertyTaxCommonUtils.getTotalDemandVariationAmount(demandDetail));
                setLibraryCess(infoBean, propertyType, demandDetail);

                if (NON_VACANT_TAX_DEMAND_CODES
                        .contains(demandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode())
                        || demandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode()
                                .equalsIgnoreCase(DEMANDRSN_CODE_VACANT_TAX))
                    propertyTax = propertyTax.add(propertyTaxCommonUtils.getTotalDemandVariationAmount(demandDetail));
                setUCPenalty(infoBean, propertyType, demandDetail);
            }
        setTotalTax(infoBean, totalTax, propertyTax, propertyType);
    }

    private void setTotalTax(final PropertyAckNoticeInfo infoBean, final BigDecimal totalTax,
            final BigDecimal propertyTax, final String propertyType) {
        if (propertyType.equalsIgnoreCase(CURRENT)) {
            infoBean.setRevTotalTax(totalTax);
            infoBean.setRevPropertyTax(propertyTax);
        }
        if (propertyType.equalsIgnoreCase(HISTORY)) {
            infoBean.setExistingTotalTax(totalTax);
            infoBean.setExistingPropertyTax(propertyTax);
        }
    }

    private void setLibraryCess(final PropertyAckNoticeInfo infoBean, final String propertyType,
            final EgDemandDetails demandDetail) {
        if (demandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode()
                .equalsIgnoreCase(DEMANDRSN_CODE_LIBRARY_CESS)) {
            if (propertyType.equalsIgnoreCase(CURRENT))
                infoBean.setRevLibraryCess(propertyTaxCommonUtils.getTotalDemandVariationAmount(demandDetail));
            if (propertyType.equalsIgnoreCase(HISTORY))
                infoBean.setExistingLibraryCess(propertyTaxCommonUtils.getTotalDemandVariationAmount(demandDetail));
        }
    }

    private void setUCPenalty(final PropertyAckNoticeInfo infoBean, final String propertyType,
            final EgDemandDetails demandDetail) {
        if (demandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode()
                .equalsIgnoreCase(DEMANDRSN_CODE_UNAUTHORIZED_PENALTY)) {
            if (propertyType.equalsIgnoreCase(CURRENT))
                infoBean.setRevUCPenalty(propertyTaxCommonUtils.getTotalDemandVariationAmount(demandDetail));
            if (propertyType.equalsIgnoreCase(HISTORY))
                infoBean.setExistingUCPenalty(propertyTaxCommonUtils.getTotalDemandVariationAmount(demandDetail));
        }
    }

    public Boolean validateDemand(final Petition petition) {
        Boolean demandIncerased = false;
        final Set<Ptdemand> newDemandSet = petition.getProperty().getPtDemandSet();
        final List<Ptdemand> ptDemandList = new ArrayList<>();
        ptDemandList.addAll(newDemandSet);

        final BigDecimal oldDemand = getDemandforCurrenttInst(propertyService.getInstallmentWiseDemand(
                ptDemandDAO.getNonHistoryCurrDmdForProperty(petition.getBasicProperty().getProperty())));
        final BigDecimal newDemand = getDemandforCurrenttInst(
                propertyService.getInstallmentWiseDemand(ptDemandList.get(0)));
        if (newDemand.compareTo(oldDemand) > 0)
            demandIncerased = true;
        return demandIncerased;
    }

    private BigDecimal getDemandforCurrenttInst(final Map<Installment, BigDecimal> instWiseDemand) {
        BigDecimal demand = BigDecimal.ZERO;
        final Installment currentInstall = propertyTaxCommonUtils.getCurrentPeriodInstallment();
        for (final Map.Entry<Installment, BigDecimal> entry : instWiseDemand.entrySet())
            if (entry.getKey().equals(currentInstall))
                demand = entry.getValue();
        return demand;
    }

    public Map<String, String[]> updateStateAndStatus(final Petition petition, final Long approverPositionId,
            final String workFlowAction, final String approverComments, final String approverName,final boolean wsPortalRequest) {
        Position position = null;
        WorkFlowMatrix wfmatrix;
        Assignment wfInitiator;
        List<Assignment> loggedInUserAssign;
        String loggedInUserDesignation = "";
        String pendingAction;
        User user = securityUtils.getCurrentUser();
        final Map<String, String[]> actionMessages = new HashMap<>();
        final Boolean loggedUserIsEmployee = propertyService.isEmployee(user)
                && !ANONYMOUS_USER.equalsIgnoreCase(user.getName());
        final Boolean citizenPortalUser = propertyService.isCitizenPortalUser(user);
        if (petition.getState() != null) {
            loggedInUserAssign = assignmentService.getAssignmentByPositionAndUserAsOnDate(
                    petition.getCurrentState().getOwnerPosition().getId(), user.getId(), new Date());
            loggedInUserDesignation = !loggedInUserAssign.isEmpty()
                    ? loggedInUserAssign.get(0).getDesignation().getName() : null;
        }

        if (loggedInUserDesignation != null && (JUNIOR_ASSISTANT.equals(loggedInUserDesignation)
                || SENIOR_ASSISTANT.equals(loggedInUserDesignation)))
            loggedInUserDesignation = null;

        if (petition.getId() != null)
            wfInitiator = getWorkflowInitiator(petition);
        else
            wfInitiator = propertyTaxCommonUtils.getWorkflowInitiatorAssignment(user.getId());

        if (approverPositionId != null && approverPositionId != -1)
            position = positionMasterService.getPositionById(approverPositionId);
        if (WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(workFlowAction) && loggedInUserDesignation != null
                && loggedInUserDesignation.endsWith(COMMISSIONER_DESGN))
            pendingAction = new StringBuilder().append(loggedInUserDesignation).append(" ").append("Approval Pending")
                    .toString();
        else
            pendingAction = getPendingActions(petition);

        if (null == petition.getState()) {
            if (!citizenPortalUser && loggedUserIsEmployee && !ANONYMOUS_USER.equalsIgnoreCase(user.getName()))
                wfmatrix = revisionPetitionWorkFlowService.getWfMatrix(petition.getStateType(), null, null,
                        getAdditionalRule(petition), null, null, null);
            else
                wfmatrix = revisionPetitionWorkFlowService.getWfMatrix(petition.getStateType(), null, null,
                        getAdditionalRule(petition), "Created", null, null);
        } else if (petition.getCurrentState().getValue().equalsIgnoreCase(RP_INSPECTION_COMPLETE)
                || petition.getCurrentState().getValue().equalsIgnoreCase(GRP_INSPECTION_COMPLETE) 
                ||petition.getCurrentState().getValue().equalsIgnoreCase(APPEAL_INSPECTION_COMPLETE))
            wfmatrix = revisionPetitionWorkFlowService.getWfMatrix(petition.getStateType(), null, null,
                    getAdditionalRule(petition), petition.getCurrentState().getValue(),
                    petition.getCurrentState().getNextAction(), null, loggedInUserDesignation);
        else if (!OBJECTION_CREATED.equalsIgnoreCase(petition.getEgwStatus().getCode())
                && loggedInUserDesignation != null && loggedInUserDesignation.endsWith("Commissioner")
                && (WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(workFlowAction)
                        || WFLOW_ACTION_STEP_FORWARD.equalsIgnoreCase(workFlowAction)))
            wfmatrix = revisionPetitionWorkFlowService.getWfMatrix(petition.getStateType(), null, null,
                    getAdditionalRule(petition), petition.getCurrentState().getValue(), pendingAction, null,
                    loggedInUserDesignation);
        else
            wfmatrix = revisionPetitionWorkFlowService.getWfMatrix(petition.getStateType(), null, null,
                    getAdditionalRule(petition), petition.getCurrentState().getValue(),
                    pendingAction != null ? pendingAction : null, null, null);
        if (petition.getState() == null) {
            if (position == null && (approverPositionId == null || approverPositionId != -1)) {
                Assignment assignment;
                if (propertyService.isCscOperator(user) || thirdPartyService.isWardSecretaryRequest(wsPortalRequest))
                    assignment = propertyService.getMappedAssignmentForBusinessUser(petition.getBasicProperty());
                else
                    assignment = propertyService.getUserPositionByZone(petition.getBasicProperty(), false);
                if (assignment != null)
                    position = assignment.getPosition();
            }
            updateRevisionPetitionStatus(wfmatrix, petition, OBJECTION_CREATED);

            if (position != null)
                user = eisCommonService.getUserForPosition(position.getId(), new Date());
            String natureType = petition.getType().equalsIgnoreCase(WFLOW_ACTION_APPEALPETITION) ? NATURE_APPEALPETITION
                    : NATURE_GENERAL_REVISION_PETITION;
            petition.transition().start()
                    .withNextAction((wfmatrix.getPendingActions() != null) ? wfmatrix.getPendingActions() : "")
                    .withStateValue(wfmatrix.getNextState()).withDateInfo(new DateTime().toDate()).withOwner(position)
                    .withSenderName(user.getUsername() + "::" + user.getName()).withOwner(user)
                    .withComments(approverComments).withNextAction(wfmatrix.getNextAction())
                    .withInitiator(wfInitiator != null ? wfInitiator.getPosition() : null)
                    .withNatureOfTask(NATURE_OF_WORK_RP.equalsIgnoreCase(petition.getType()) ? NATURE_REVISION_PETITION
                            : natureType)
                    .withSLA(propertyService.getSlaValue(returnApplicationtype(petition.getType())));
            if (loggedUserIsEmployee && user != null)
                actionMessages.put(OBJECTION_FORWARD,
                        new String[] { user.getName().concat("~").concat(position.getName()) });
        } else if (workFlowAction != null && !"".equals(workFlowAction)
                && !WFLOW_ACTION_STEP_SAVE.equalsIgnoreCase(workFlowAction)) {

            if (WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(workFlowAction)
                    || workFlowAction.equalsIgnoreCase(CANCEL_UNCONSIDERED)) {

                wfmatrix = revisionPetitionWorkFlowService.getPreviousStateFromWfMatrix(petition.getStateType(), null,
                        null, getAdditionalRule(petition), petition.getCurrentState().getValue(),
                        petition.getCurrentState().getNextAction());
                if (approverPositionId == null || approverPositionId != -1)
                    position = petition.getCurrentState().getOwnerPosition();
            }
            if (WFLOW_ACTION_STEP_SIGN.equalsIgnoreCase(workFlowAction)) {
                if (propertyService.isEmployee(petition.getCreatedBy()))
                    position = assignmentService.getPrimaryAssignmentForUser(petition.getCreatedBy().getId())
                            .getPosition();
                else if (!petition.getStateHistory().isEmpty())
                    position = assignmentService.getPrimaryAssignmentForPositon(
                            petition.getStateHistory().get(0).getOwnerPosition().getId()).getPosition();
                else
                    position = petition.getState().getOwnerPosition();
            } else if (position == null)
                position = positionMasterService.getPositionByUserId(user.getId());

            if (wfmatrix != null)
                actionMessages.putAll(workFlowTransition(petition, workFlowAction, approverComments, wfmatrix,
                        position, approverPositionId, approverName));

        } else if (!StringUtils.isBlank(workFlowAction) && WFLOW_ACTION_STEP_SAVE.equalsIgnoreCase(workFlowAction))
            actionMessages.put("file.save", new String[] {});
        return actionMessages;

    }

    /**
     * @param petition
     * @param workFlowAction
     * @param comments
     * @param wfmatrix
     * @param position
     * @param user
     */
    public Map<String, String[]> workFlowTransition(final Petition petition, final String workFlowAction,
            final String approverComments, final WorkFlowMatrix wfmatrix, Position position,
            final Long approverPositionId, final String approverName) {
        boolean positionFoundInHistory = false;
        Assignment nextAssignment;
        String loggedInUserDesignation;
        String nextAction = null;
        final String nextState = null;
        User user;
        List<Assignment> loggedInUserAssign;
        user = securityUtils.getCurrentUser();
        final Map<String, String[]> actionMessages = new HashMap<>();
        loggedInUserAssign = assignmentService.getAssignmentByPositionAndUserAsOnDate(
                petition.getCurrentState().getOwnerPosition().getId(), user.getId(), new Date());
        loggedInUserDesignation = !loggedInUserAssign.isEmpty() ? loggedInUserAssign.get(0).getDesignation().getName()
                : null;
        final Assignment wfInitiator = getWorkflowInitiator(petition);
        if (WFLOW_ACTION_STEP_FORWARD.equalsIgnoreCase(workFlowAction)
                || workFlowAction.equalsIgnoreCase("approve petition")
                || workFlowAction.equalsIgnoreCase(FORWARD_TO_APPROVER)) {

            if (wfmatrix != null && (wfmatrix.getNextStatus() != null
                    && wfmatrix.getNextStatus().equalsIgnoreCase(OBJECTION_HEARING_FIXED)
                    || wfmatrix.getCurrentState().equalsIgnoreCase(RP_INSPECTIONVERIFIED)
                    || wfmatrix.getCurrentState().equalsIgnoreCase(RP_WF_REGISTERED)
                    || petition.getState().getValue().equalsIgnoreCase(GRP_WF_REGISTERED)
                    || petition.getState().getValue().equalsIgnoreCase(APPEAL_WF_REGISTERED))) {
                for (final StateHistory<Position> stateHistoryObj : petition.getState().getHistory()) {
                    if (stateHistoryObj.getValue().equalsIgnoreCase(RP_CREATED)) {
                        position = stateHistoryObj.getOwnerPosition();
                        final User sender = eisCommonService.getUserForPosition(position.getId(), new Date());
                        if (sender != null)
                            actionMessages.put(OBJECTION_FORWARD,
                                    new String[] { sender.getName().concat("~").concat(position.getName()) });
                        positionFoundInHistory = true;
                        break;
                    }
                    if (stateHistoryObj.getValue().equalsIgnoreCase(RP_WF_REGISTERED)
                            && !loggedInUserDesignation.endsWith(COMMISSIONER_DESGN)) {
                        position = wfInitiator.getPosition();
                        actionMessages.put(OBJECTION_FORWARD, new String[] { wfInitiator.getEmployee().getName()
                                .concat("~").concat(wfInitiator.getPosition().getName()) });
                        if (petition.getEgwStatus() != null
                                && petition.getEgwStatus().getCode().equalsIgnoreCase(OBJECTION_CREATED))
                            updateRevisionPetitionStatus(wfmatrix, petition, OBJECTION_HEARING_FIXED);
                        positionFoundInHistory = true;
                        break;
                    }
                }
                if (!positionFoundInHistory && petition.getState() != null
                        && Arrays.asList(RP_CREATED, RP_WF_REGISTERED, GRP_CREATED, GRP_WF_REGISTERED, APPEAL_CREATED,
                                APPEAL_WF_REGISTERED)
                                .contains(petition.getState().getValue())) {
                    positionFoundInHistory = true;
                    updateRevisionPetitionStatus(wfmatrix, petition, OBJECTION_HEARING_FIXED);
                    position = petition.getState().getInitiatorPosition() != null
                            ? petition.getState().getInitiatorPosition() : wfInitiator.getPosition();
                    actionMessages.put(OBJECTION_FORWARD, new String[] { wfInitiator.getEmployee().getName().concat("~")
                            .concat(wfInitiator.getPosition().getName()) });
                }
            }
            if (approverPositionId != null && approverPositionId != -1
                    && workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_FORWARD)
                    && Arrays
                            .asList(ASSISTANT_COMMISSIONER_DESIGN, DEPUTY_COMMISSIONER_DESIGN,
                                    ADDITIONAL_COMMISSIONER_DESIGN, ZONAL_COMMISSIONER_DESIGN, REVENUE_OFFICER_DESGN)
                            .contains(loggedInUserDesignation))
                if (petition.getState().getNextAction().equalsIgnoreCase(OBJECTION_PRINT_ENDORSEMENT)
                        || petition.getState().getNextAction().equalsIgnoreCase(WF_STATE_DIGITAL_SIGNATURE_PENDING))
                    nextAction = petition.getState().getNextAction();
                else {
                    nextAssignment = assignmentService.getAssignmentsForPosition(approverPositionId, new Date()).get(0);
                    final String nextDesignation = nextAssignment.getDesignation().getName();
                    position = positionMasterService.getPositionById(approverPositionId);
                    final String designation = nextDesignation.split(" ")[0];
                    nextAction = nextDesignation.equalsIgnoreCase(COMMISSIONER_DESGN)
                            ? WF_STATE_COMMISSIONER_APPROVAL_PENDING
                            : new StringBuilder().append(designation).append(" ")
                                    .append(WF_STATE_COMMISSIONER_APPROVAL_PENDING).toString();
                }

            petition.transition().progressWithStateCopy()
                    .withStateValue(nextState != null ? nextState : wfmatrix.getNextState()).withOwner(position)
                    .withSenderName(user.getUsername() + "::" + user.getName()).withDateInfo(new DateTime().toDate())
                    .withNextAction(nextAction != null ? nextAction : wfmatrix.getNextAction())
                    .withComments(approverComments);

            if (wfmatrix.getNextAction() != null && wfmatrix.getNextAction().equalsIgnoreCase("END"))
                petition.transition().end().withStateValue(wfmatrix.getNextState())
                        .withOwner(petition.getCurrentState().getOwnerPosition())
                        .withSenderName(user.getUsername() + "::" + user.getName())
                        .withNextAction(wfmatrix.getNextAction()).withDateInfo(new DateTime().toDate())
                        .withComments(approverComments).withNextAction(null)
                        .withOwner(petition.getCurrentState().getOwnerPosition());

            if (wfmatrix.getNextStatus() != null)
                updateRevisionPetitionStatus(wfmatrix, petition, null);
            if (approverName != null && !approverName.isEmpty() && !approverName.equalsIgnoreCase(CHOOSE))
                actionMessages.put(OBJECTION_FORWARD,
                        new String[] { approverName.concat("~").concat(position.getName()) });
            else if (user != null && !positionFoundInHistory)
                actionMessages.put(OBJECTION_FORWARD,
                        new String[] { user.getName().concat("~").concat(position.getName()) });

        } else if (workFlowAction.equalsIgnoreCase(REJECT_INSPECTION_STR)) {
            final List<StateHistory<Position>> stateHistoryList = petition.getStateHistory();
            Assignment wfInit = null;
            for (final StateHistory<Position> stateHistoryObj : stateHistoryList)
                if (stateHistoryObj.getValue().equalsIgnoreCase(RP_HEARINGCOMPLETED)
                        || stateHistoryObj.getValue().equalsIgnoreCase(GRP_HEARINGCOMPLETED)
                        || stateHistoryObj.getValue().equalsIgnoreCase(APPEAL_HEARINGCOMPLETED)) {
                    position = stateHistoryObj.getOwnerPosition();
                    wfInit = propertyService.getUserOnRejection(petition);
                    break;
                }
            if (wfInit != null) {
                petition.setEgwStatus(
                        egwStatusDAO.getStatusByModuleAndCode(OBJECTION_MODULE, OBJECTION_HEARING_COMPLETED));

                if (position != null) {
                    petition.transition().progressWithStateCopy().withNextAction(OBJECTION_RECORD_INSPECTIONDETAILS)
                            .withStateValue(
                                    PROPERTY_MODIFY_REASON_REVISION_PETITION.equalsIgnoreCase(petition.getType())
                                            ? RP_APP_STATUS_REJECTED
                                            : (WFLOW_ACTION_APPEALPETITION.equalsIgnoreCase(petition.getType())
                                                    ? APPEAL_APP_STATUS_REJECTED : GRP_APP_STATUS_REJECTED))
                            .withOwner(position).withSenderName(user.getUsername() + "::" + user.getName())
                            .withDateInfo(new DateTime().toDate()).withComments(approverComments);
                    final String actionMessage = propertyTaxUtil.getApproverUserName(position.getId());
                    if (actionMessage != null)
                        actionMessages.put(OBJECTION_FORWARD, new String[] { actionMessage });
                }
            } else
                actionMessages.put(REJECT_INSPECTION, new String[] { petition.getBasicProperty().getUpicNo() });

        } else if (workFlowAction.equalsIgnoreCase(REJECT)) {
            final List<StateHistory<Position>> stateHistoryList = petition.getStateHistory();
            for (final StateHistory<Position> stateHistoryObj : stateHistoryList)
                if (stateHistoryObj.getValue().equalsIgnoreCase(petition.getCurrentState().getValue())) {
                    position = stateHistoryObj.getOwnerPosition();
                    break;
                }
            if (petition.getCurrentState() != null
                    && (petition.getCurrentState().getValue().equalsIgnoreCase(REJECTED)
                            || petition.getCurrentState().getValue().equalsIgnoreCase(RP_CREATED))) {
                petition.transition().end().withStateValue(wfmatrix.getNextState()).withOwner(position)
                        .withSenderName(user.getUsername() + "::" + user.getName())
                        .withNextAction(wfmatrix.getNextAction()).withDateInfo(new DateTime().toDate())
                        .withComments(approverComments).withNextAction(null)
                        .withOwner(petition.getCurrentState().getOwnerPosition());

                updateRevisionPetitionStatus(wfmatrix, petition, REJECTED);

            } else {
                petition.transition().progressWithStateCopy().withStateValue(wfmatrix.getCurrentState())
                        .withOwner(position).withSenderName(user.getUsername() + "::" + user.getName())
                        .withDateInfo(new DateTime().toDate()).withNextAction(wfmatrix.getPendingActions())
                        .withComments(approverComments);

                if (workFlowAction.equalsIgnoreCase(REJECT))
                    updateRevisionPetitionStatus(wfmatrix, petition, null);
            }

            if (approverName != null && !approverName.isEmpty() && !approverName.equalsIgnoreCase(CHOOSE))
                actionMessages.put(OBJECTION_FORWARD,
                        new String[] { approverName.concat("~").concat(position.getName()) });
            else if (user != null)
                actionMessages.put(OBJECTION_FORWARD,
                        new String[] { user.getName().concat("~").concat(position.getName()) });
        } else if (workFlowAction.equalsIgnoreCase(PRINT_ENDORESEMENT)) {
            position = petition.getState().getOwnerPosition();
            petition.transition().progressWithStateCopy().withStateValue(wfmatrix.getCurrentState())
                    .withOwner(position).withSenderName(user.getUsername() + "::" + user.getName())
                    .withDateInfo(new DateTime().toDate()).withNextAction(wfmatrix.getNextAction())
                    .withComments(approverComments);
        }else if(workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_REJECT_TO_CANCEL))
        {
            wFRejectToCancel(petition, approverComments, user);
            updateRevisionPetitionStatus(wfmatrix, petition, REJECTED);
        }
        else if (WFLOW_ACTION_STEP_SIGN.equalsIgnoreCase(workFlowAction))
            petition.transition().progressWithStateCopy().withStateValue(wfmatrix.getCurrentState())
                    .withOwner(position).withSenderName(user.getUsername() + "::" + user.getName())
                    .withDateInfo(new DateTime().toDate()).withNextAction(wfmatrix.getNextAction())
                    .withComments(approverComments);
        else if (workFlowAction.equalsIgnoreCase(APPROVE)) {
            position = petition.getState().getOwnerPosition();
            petition.transition().progressWithStateCopy().withStateValue(wfmatrix.getNextState()).withOwner(position)
                    .withSenderName(user.getUsername() + "::" + user.getName()).withDateInfo(new DateTime().toDate())
                    .withNextAction(wfmatrix.getNextAction()).withComments(approverComments);
        }
        applyAuditing(petition.getState());
        return actionMessages;
    }

    public String getPendingActions(final Petition petition) {
        if (petition != null && petition.getId() != null) {
            if (RP_INSPECTIONVERIFIED.equalsIgnoreCase(petition.getCurrentState().getValue())
                    || GRP_INSPECTIONVERIFIED.equalsIgnoreCase(petition.getCurrentState().getValue())
                    || APPEAL_INSPECTIONVERIFIED.equalsIgnoreCase(petition.getCurrentState().getValue())
                    || petition.getCurrentState().getValue().endsWith("Forwarded")
                    || petition.getCurrentState().getValue().endsWith("Approved"))
                return petition.getCurrentState().getNextAction();
            else
                return null;
        } else
            return null;

    }

    public String getAdditionalRule(final Petition petition) {
        String addittionalRule;
        if (PROPERTY_MODIFY_REASON_GENERAL_REVISION_PETITION.equals(petition.getType()))
            addittionalRule = GENERAL_REVISION_PETITION;
        else if(WFLOW_ACTION_APPEALPETITION.equalsIgnoreCase(petition.getType()))
                addittionalRule = APPEALPETITION_CODE;
        else
            addittionalRule = REVISION_PETITION;
        return addittionalRule;
    }

    public Property modifyRPDemand(final PropertyImpl propertyModel, final PropertyImpl oldProperty)
            throws TaxCalculatorExeption {
        Date propCompletionDate;
        if (propertyModel.getPropertyDetail().getPropertyTypeMaster().getCode()
                .equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
            propCompletionDate = propertyModel.getPropertyDetail().getDateOfCompletion();
        else
            propCompletionDate = propertyService
                    .getLowestDtOfCompFloorWise(propertyModel.getPropertyDetail().getFloorDetails());
        final PropertyImpl newProperty = (PropertyImpl) propertyService.createDemand(propertyModel, propCompletionDate);
        Property modProperty = null;
        if (oldProperty == null)
            LOGGER.info("modifyBasicProp, Could not get the previous property. DCB for arrears will be incorrect");
        else {
            final PropertyImpl historyProperty = (PropertyImpl) propertyService
                    .getHistoryPropertyByUpinNo(oldProperty.getBasicProperty());
            modProperty = propertyService.createDemandForModify(historyProperty, newProperty, propCompletionDate);
            modProperty = createArrearsDemand(oldProperty, propCompletionDate, newProperty);
        }

        Map<Installment, Set<EgDemandDetails>> demandDetailsSetByInstallment;
        List<Installment> installments;

        final Set<EgDemandDetails> oldEgDemandDetailsSet = propertyService.getLatestDemandforHistoryProp(oldProperty)
                .getEgDemandDetails();
        demandDetailsSetByInstallment = propertyService.getEgDemandDetailsSetByInstallment(oldEgDemandDetailsSet);
        installments = new ArrayList<>(demandDetailsSetByInstallment.keySet());
        Collections.sort(installments);
        for (final Installment inst : installments) {
            final Map<String, BigDecimal> dmdRsnAmt = new LinkedHashMap<>();
            for (final String rsn : DEMAND_RSNS_LIST) {
                final EgDemandDetails newDmndDtls = propertyService
                        .getEgDemandDetailsForReason(demandDetailsSetByInstallment.get(inst), rsn);
                if (newDmndDtls != null && newDmndDtls.getAmtCollected() != null
                        && newDmndDtls.getAmtCollected().compareTo(BigDecimal.ZERO) > 0)
                    dmdRsnAmt.put(newDmndDtls.getEgDemandReason().getEgDemandReasonMaster().getCode(),
                            newDmndDtls.getAmtCollected());
            }
            propertyService.getExcessCollAmtMap().put(inst, dmdRsnAmt);
        }
        final Ptdemand currentDemand = propertyService.getCurrrentDemand(modProperty);
        demandDetailsSetByInstallment = propertyService
                .getEgDemandDetailsSetByInstallment(currentDemand.getEgDemandDetails());
        installments = new ArrayList<>(demandDetailsSetByInstallment.keySet());
        Collections.sort(installments);
        for (final Installment inst : installments) {
            final Map<String, BigDecimal> dmdRsnAmt = new LinkedHashMap<>();
            for (final String rsn : DEMAND_RSNS_LIST) {
                final EgDemandDetails newDmndDtls = propertyService
                        .getEgDemandDetailsForReason(demandDetailsSetByInstallment.get(inst), rsn);
                if (newDmndDtls != null && newDmndDtls.getAmtCollected() != null) {
                    final BigDecimal extraCollAmt = newDmndDtls.getAmtCollected().subtract(propertyTaxCommonUtils.getTotalDemandVariationAmount(newDmndDtls));
                    // If there is extraColl then add to map
                    if (extraCollAmt.compareTo(BigDecimal.ZERO) > 0) {
                        dmdRsnAmt.put(newDmndDtls.getEgDemandReason().getEgDemandReasonMaster().getCode(),
                                extraCollAmt);
                        newDmndDtls.setAmtCollected(newDmndDtls.getAmtCollected().subtract(extraCollAmt));
                        newDmndDtls.setModifiedDate(new Date());
                    }
                }
            }
            propertyService.getExcessCollAmtMap().put(inst, dmdRsnAmt);
        }

        LOGGER.info("Excess Collection - " + propertyService.getExcessCollAmtMap());

        propertyService.adjustExcessCollectionAmount(installments, demandDetailsSetByInstallment, currentDemand);
        return modProperty;
    }

    public Property createArrearsDemand(final Property property, final Date dateOfCompletion,
            final PropertyImpl modProperty) {
        final Installment currInstall = propertyTaxCommonUtils.getCurrentInstallment();
        Ptdemand currPtDmd = null;
        for (final Ptdemand demand : modProperty.getPtDemandSet())
            if (NON_HISTORY.equalsIgnoreCase(demand.getIsHistory())
                    && demand.getEgInstallmentMaster().equals(currInstall)) {
                currPtDmd = demand;
                break;
            }
        Ptdemand latestHistoryDemand = propertyService
                .getLatestDemandforHistoryProp(propertyService.getHistoryPropertyByUpinNo(property.getBasicProperty()));
        Installment effectiveInstall;
        final Module module = moduleDao.getModuleByName(PTMODULENAME);
        effectiveInstall = installmentDao.getInsatllmentByModuleForGivenDate(module, dateOfCompletion);
        propertyService.addArrDmdDetToCurrentDmd(latestHistoryDemand, currPtDmd, effectiveInstall, false);
        return modProperty;
    }
    
    public void cancelObjection(final Petition petition) {
        petition.getBasicProperty().setUnderWorkflow(Boolean.FALSE);
        petition.getProperty().setStatus(PropertyTaxConstants.STATUS_CANCELLED);
        updateRevisionPetitionStatus(null, petition, PropertyTaxConstants.CANCELLED);
        petition.transition().end().withOwner(petition.getCurrentState().getOwnerPosition()).withNextAction(null);
    }
    
    @Transactional
    public void updateIndexAndPushToPortalInbox(final Petition petition) {
        if (petition.getType().equalsIgnoreCase(NATURE_OF_WORK_RP))
            propertyService.updateIndexes(petition, APPLICATION_TYPE_REVISION_PETITION);
        else if (petition.getType().equalsIgnoreCase(WFLOW_ACTION_APPEALPETITION))
            propertyService.updateIndexes(petition, APPLICATION_TYPE_APPEAL_PETITION);
        else
            propertyService.updateIndexes(petition, APPLICATION_TYPE_GRP);
        if (Source.CITIZENPORTAL.toString().equalsIgnoreCase(petition.getSource())) {
            final PortalInbox portalInbox = propertyService.getPortalInbox(petition.getObjectionNumber());
            if (portalInbox != null)
                propertyService.updatePortal(petition,returnApplicationtype(petition.getType()));
        }
    }
    
    private void wFRejectToCancel(final Petition petition, final String approvarComments,
            final User user) {
        String nextAction;
        petition.getProperty().setStatus(STATUS_CANCELLED);
        final String stateValue = petition.getCurrentState().getValue().split(":")[0] + ":" + WF_STATE_REJECTED_TO_CANCEL;
        nextAction = WF_STATE_DIGITAL_SIGNATURE_PENDING;
        petition.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                .withComments(approvarComments).withStateValue(stateValue)
                .withDateInfo(new DateTime().toDate()).withOwner(petition.getState().getOwnerPosition())
                .withNextAction(nextAction);
    }
    
    public String returWorkflowType(String wfTypes) {
        String wftype = "";
        for (Map.Entry<String, String> type : petitionType.entrySet()) {
            if (type.getKey().equals(wfTypes))
                wftype = type.getValue();
        }
        return wftype;
    }

    public String returnNoticeType(String type) {
        String noticetype = "";
        for (Map.Entry<String, String> notice : petitionNoticeType.entrySet()) {
            if (notice.getKey().equals(type))
                noticetype = notice.getValue();
        }
        return noticetype;
    }
    
    public String returnApplicationtype(String type) {
        String applicationtype = "";
        for (Map.Entry<String, String> application : applicationTypes.entrySet()) {
            if (application.getKey().equals(type))
                applicationtype = application.getValue();
        }
        return applicationtype;
    }
    
    public final Map<String, String> petitionType = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;
        {
            put(NATURE_OF_WORK_RP, NATURE_OF_WORK_RP);
            put(WFLOW_ACTION_APPEALPETITION, WFLOW_ACTION_APPEALPETITION);
            put(NATURE_OF_WORK_GRP, NATURE_OF_WORK_GRP);
        }
    };

    public final Map<String, String> petitionNoticeType = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;
        {
            put(NATURE_OF_WORK_RP, NOTICE_TYPE_RPPROCEEDINGS);
            put(NATURE_OF_WORK_GRP, NOTICE_TYPE_GRPPROCEEDINGS);
            put(WFLOW_ACTION_APPEALPETITION, NOTICE_TYPE_APPEALPROCEEDINGS);

        }
    };
    
    public final Map<String, String> applicationTypes = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;
        {
            put(NATURE_OF_WORK_RP, APPLICATION_TYPE_REVISION_PETITION);
            put(NATURE_OF_WORK_GRP, APPLICATION_TYPE_GRP);
            put(WFLOW_ACTION_APPEALPETITION, APPLICATION_TYPE_APPEAL_PETITION);

        }
    };
    
    public Map<String, String> getViewURLAndMsgForWS(final Petition petition, final String wfType) {
        Map<String, String> details = new HashMap<>();
        String viewURL = null;
        String succeessMsg = null;
        String failureMsg = null;
        if (NATURE_OF_WORK_RP.equalsIgnoreCase(wfType)) {
            viewURL = format(WS_VIEW_PROPERT_BY_APP_NO_URL,
                    WebUtils.extractRequestDomainURL(ServletActionContext.getRequest(), false),
                    petition.getObjectionNumber(), APPLICATION_TYPE_REVISION_PETITION);
            succeessMsg = "Property Revision Petition Initiated";
            failureMsg = "Property Revision Petition Failed";
        } else if (NATURE_OF_WORK_GRP.equalsIgnoreCase(wfType)) {
            viewURL = format(WS_VIEW_PROPERT_BY_APP_NO_URL,
                    WebUtils.extractRequestDomainURL(ServletActionContext.getRequest(), false),
                    petition.getObjectionNumber(), APPLICATION_TYPE_GRP);
            succeessMsg = "Property General Revision Petition Initiated";
            failureMsg = "Property General Revision Petition Failed";
        }
        details.put("viewURL", viewURL);
        details.put("succeessMsg", succeessMsg);
        details.put("failureMsg", failureMsg);
        return details;
    }
}