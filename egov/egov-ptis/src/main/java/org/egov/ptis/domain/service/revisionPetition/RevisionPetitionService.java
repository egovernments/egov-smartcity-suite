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
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_OBJECTED_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_APPROVE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_FORWARD;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_REJECT;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_SAVE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_SIGN;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_COMMISSIONER_APPROVAL_PENDING;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_DIGITAL_SIGNATURE_PENDING;
import static org.egov.ptis.constants.PropertyTaxConstants.ZONAL_COMMISSIONER_DESIGN;
import static org.egov.ptis.domain.service.property.PropertyService.APPLICATION_VIEW_URL;

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
import org.egov.ptis.domain.entity.objection.RevisionPetition;
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

public class RevisionPetitionService extends PersistenceService<RevisionPetition, Long> {

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
    protected SimpleWorkflowService<RevisionPetition> revisionPetitionWorkFlowService;
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

    public RevisionPetitionService() {
        super(RevisionPetition.class);
    }

    public RevisionPetitionService(final Class<RevisionPetition> type) {
        super(type);
    }

    /**
     * Create revision petition
     *
     * @param objection
     * @return
     */
    @Transactional
    public RevisionPetition createRevisionPetition(final RevisionPetition objection) {
        RevisionPetition revisionPetition;
        propertyService.processAndStoreDocument(objection.getDocuments());
        if (objection.getId() == null)
            revisionPetition = persist(objection);
        else
            revisionPetition = merge(objection);

        return revisionPetition;

    }

    /**
     * Api to save revision petition using rest api's.
     *
     * @param objection
     * @return
     */
    @Transactional
    public RevisionPetition createRevisionPetitionForRest(RevisionPetition objection) {
        Position position = null;
        WorkFlowMatrix wfmatrix;
        User user = null;
        if (objection.getId() == null) {
            if (objection.getObjectionNumber() == null)
                objection.setObjectionNumber(applicationNumberGenerator.generate());
            objection.getBasicProperty().setStatus(propertyStatusDAO.getPropertyStatusByCode(STATUS_OBJECTED_STR));
            objection.getBasicProperty().setUnderWorkflow(Boolean.TRUE);
            if (objection.getState() == null) {
                wfmatrix = revisionPetitionWorkFlowService.getWfMatrix(objection.getStateType(), null, null, null,
                        REVISIONPETITION_CREATED, null);
                // Get the default revenue cleark from admin boundary.
                final Designation desig = designationService.getDesignationByName(REVENUE_CLERK_DESGN);
                List<Assignment> assignment = assignmentService.findByDesignationAndBoundary(desig.getId(),
                        objection.getBasicProperty().getPropertyID().getZone().getId());
                if (!assignment.isEmpty())
                    position = assignment.get(0).getPosition();
                else {
                    assignment = assignmentService.findPrimaryAssignmentForDesignationName(REVENUE_CLERK_DESGN);
                    if (!assignment.isEmpty())
                        position = assignment.get(0).getPosition();
                }

                updateRevisionPetitionStatus(wfmatrix, objection, null);

                if (position != null)
                    user = eisCommonService.getUserForPosition(position.getId(), new Date());

                objection.transition().start().withNextAction(wfmatrix.getPendingActions())
                        .withStateValue(wfmatrix.getCurrentState()).withOwner(position)
                        .withSenderName(user != null && user.getName() != null ? user.getName() : "").withOwner(user)
                        .withComments("");
            }

            applyAuditing(objection.getState());
            objection = persist(objection);
            updateIndex(objection);

            sendEmailandSms(objection, REVISION_PETITION_CREATED);
        } else
            objection = merge(objection);

        return objection;

    }

    /**
     * Update elastic search index
     *
     * @param objection
     */
    private void updateIndex(final RevisionPetition objection) {
        ApplicationIndex applicationIndex = applicationIndexService
                .findByApplicationNumber(objection.getObjectionNumber());
        final User user = securityUtils.getCurrentUser();
        if (null == applicationIndex) {
            applicationIndex = ApplicationIndex.builder().withModuleName(PTMODULENAME)
                    .withApplicationNumber(objection.getObjectionNumber())
                    .withApplicationDate(objection.getCreatedDate() != null ? objection.getCreatedDate() : new Date())
                    .withApplicationType(APPLICATION_TYPE_REVISION_PETITION)
                    .withApplicantName(objection.getBasicProperty().getFullOwnerName())
                    .withStatus(objection.getState().getValue())
                    .withUrl(format(APPLICATION_VIEW_URL, objection.getObjectionNumber(), ""))
                    .withApplicantAddress(objection.getBasicProperty().getAddress().toString())
                    .withOwnername(user.getUsername() + "::" + user.getName()).withChannel(Source.SYSTEM.toString())
                    .build();
            applicationIndexService.createApplicationIndex(applicationIndex);
        } else {
            applicationIndex.setStatus(objection.getState().getValue());
            applicationIndexService.updateApplicationIndex(applicationIndex);
        }
    }

    /**
     * @param wfmatrix
     * @param objection
     * @param status
     */
    private void updateRevisionPetitionStatus(final WorkFlowMatrix wfmatrix, final RevisionPetition objection,
            final String status) {

        EgwStatus egwStatus = null;
        if (isNotBlank(status))
            egwStatus = egwStatusDAO.getStatusByModuleAndCode(OBJECTION_MODULE, status);

        else if (wfmatrix != null && wfmatrix.getNextStatus() != null && objection != null)
            egwStatus = egwStatusDAO.getStatusByModuleAndCode(OBJECTION_MODULE, wfmatrix.getNextStatus());
        if (egwStatus != null)
            objection.setEgwStatus(egwStatus);

    }

    /**
     * Api to update revision petition.
     *
     * @param objection
     * @return
     */

    @Transactional
    public RevisionPetition updateRevisionPetition(final RevisionPetition objection) {
        RevisionPetition revisionPetition;
        if (objection.getId() == null)
            revisionPetition = persist(objection);
        else
            revisionPetition = update(objection);

        return revisionPetition;

    }

    /**
     * Get revision petition by application number
     *
     * @param applicationNumber
     * @return
     */
    public RevisionPetition getRevisionPetitionByApplicationNumber(final String applicationNumber) {
        RevisionPetition revPetitionObject;
        final Criteria appCriteria = getSession().createCriteria(RevisionPetition.class, "revPetiton");
        appCriteria.add(Restrictions.eq("revPetiton.objectionNumber", applicationNumber));
        revPetitionObject = (RevisionPetition) appCriteria.uniqueResult();

        return revPetitionObject;
    }

    /**
     * Api to send EMAIL and SMS.
     *
     * @param objection
     * @param applicationType
     */
    public void sendEmailandSms(final RevisionPetition objection, final String applicationType) {
        if (objection != null)
            for (final PropertyOwnerInfo ownerInfo : objection.getBasicProperty().getPropertyOwnerInfo())
                sendEmailAndSms(objection, ownerInfo.getOwner(), applicationType);
    }

    private void sendEmailAndSms(final RevisionPetition objection, final User user, final String applicationType) {
        final String mobileNumber = user.getMobileNumber();
        final String emailid = user.getEmailId();
        final String applicantName = user.getName();
        final List<String> args = new ArrayList<>();
        args.add(applicantName);
        String smsMsg = "";
        String emailSubject = "";
        String emailBody = "";

        if (applicationType != null && applicationType.equalsIgnoreCase(REVISION_PETITION_CREATED)) {
            args.add(objection.getObjectionNumber());
            if (mobileNumber != null)
                smsMsg = "Revision petition created. Use " + objection.getObjectionNumber() + " for future reference";
            if (emailid != null) {
                emailSubject = "Revision petition created.";
                emailBody = "Revision petition created. Use " + objection.getObjectionNumber()
                        + " for future reference";
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

    public RevisionPetition createRevisionPetition(final RevisionPetition objection,
            final Map<String, String> meesevaParams) {
        createRevisionPetition(objection);
        return objection;
    }

    public Assignment getWorkflowInitiator(final RevisionPetition objection) {
        Assignment wfInitiator;
        if (propertyService.isEmployee(objection.getCreatedBy())
                && !ANONYMOUS_USER.equalsIgnoreCase(objection.getCreatedBy().getName())
                && !propertyService.isCitizenPortalUser(objection.getCreatedBy())) {
            if (objection.getState() != null && objection.getState().getInitiatorPosition() != null)
                wfInitiator = propertyTaxCommonUtils.getUserAssignmentByPassingPositionAndUser(objection.getCreatedBy(),
                        objection.getState().getInitiatorPosition());
            else
                wfInitiator = assignmentService.getAllActiveEmployeeAssignmentsByEmpId(objection.getCreatedBy().getId())
                        .get(0);
        } else if (!objection.getStateHistory().isEmpty()) {
            if (objection.getState().getInitiatorPosition() == null)
                wfInitiator = assignmentService.getAssignmentsForPosition(
                        objection.getStateHistory().get(0).getOwnerPosition().getId(), new Date()).get(0);
            else
                wfInitiator = assignmentService
                        .getAssignmentsForPosition(objection.getState().getInitiatorPosition().getId(), new Date())
                        .get(0);
        } else
            wfInitiator = assignmentService
                    .getAssignmentsForPosition(objection.getState().getOwnerPosition().getId(), new Date()).get(0);
        return wfInitiator;
    }

    public RevisionPetition getExistingObjections(final BasicProperty basicProperty) {
        return find("from RevisionPetition rp where rp.basicProperty = ?", basicProperty);
    }

    public RevisionPetition getExistingGRP(final BasicProperty basicProperty) {
        return find("from RevisionPetition rp where rp.basicProperty = ? and rp.type = ?", basicProperty,
                NATURE_OF_WORK_GRP);
    }

    /**
     * @param reportOutput
     * @param objection
     * @return ReportOutput
     */
    public ReportOutput createHearingNoticeReport(ReportOutput reportOutput, final RevisionPetition objection,
            final String noticeNo) {
        reportOutput.setReportFormat(ReportFormat.PDF);
        final HashMap<String, Object> reportParams = new HashMap<>();
        String natureOfWork;
        ReportRequest reportRequest;
        if (objection != null) {
            final HttpServletRequest request = ServletActionContext.getRequest();
            final String cityName = request.getSession().getAttribute("citymunicipalityname").toString();
            final String cityGrade = request.getSession().getAttribute("cityGrade") != null
                    ? request.getSession().getAttribute("cityGrade").toString() : null;
            final Boolean isCorporation = isNotBlank(cityGrade) && cityGrade.equalsIgnoreCase(CITY_GRADE_CORPORATION);
            if (NATURE_OF_WORK_RP.equalsIgnoreCase(objection.getType()))
                natureOfWork = NATURE_REVISION_PETITION;
            else
                natureOfWork = NATURE_GENERAL_REVISION_PETITION;
            reportParams.put("isCorporation", isCorporation);
            reportParams.put("cityName", cityName);
            reportParams.put("recievedBy", objection.getBasicProperty().getFullOwnerName());
            reportParams.put("natureOfWork", natureOfWork);

            if (objection.getHearings() != null && !objection.getHearings().isEmpty()
                    && objection.getHearings().get(objection.getHearings().size() - 1).getPlannedHearingDt() != null)
                reportParams.put("hearingNoticeDate", toDefaultDateFormat(
                        objection.getHearings().get(objection.getHearings().size() - 1).getPlannedHearingDt()));
            else
                reportParams.put("hearingNoticeDate", "");
            reportParams.put("currentDate", toDefaultDateFormat(new Date()));
            reportParams.put("recievedOn", toDefaultDateFormat(objection.getRecievedOn()));
            reportParams.put("docNumberObjection", noticeNo);
            reportParams.put("houseNo", objection.getBasicProperty().getAddress().getHouseNoBldgApt());
            reportParams.put("locality", objection.getBasicProperty().getPropertyID().getLocality().getName());
            reportParams.put("assessmentNo", objection.getBasicProperty().getUpicNo());
            reportParams.put("hearingTime",
                    objection.getHearings().get(objection.getHearings().size() - 1).getHearingTime());
            reportParams.put("hearingVenue",
                    objection.getHearings().get(objection.getHearings().size() - 1).getHearingVenue());
            reportRequest = new ReportRequest(REPORT_TEMPLATENAME_REVISIONPETITION_HEARINGNOTICE, objection,
                    reportParams);
            reportOutput = reportService.createReport(reportRequest);
        }
        return reportOutput;
    }

    /**
     * @param reportOutput
     * @param objection
     * @return
     */
    public ReportOutput createEndoresement(ReportOutput reportOutput, final RevisionPetition objection) {

        reportOutput.setReportFormat(ReportFormat.PDF);
        final HashMap<String, Object> reportParams = new HashMap<>();
        String natureOfWork;
        ReportRequest reportRequest;
        if (objection != null) {
            final Map<String, BigDecimal> currentDemand = ptDemandDAO.getDemandCollMap(objection.getProperty());
            final Map<String, BigDecimal> earlierDemand = ptDemandDAO.getDemandCollMap(
                    propertyService.getLatestHistoryProperty(objection.getBasicProperty().getUpicNo()));
            if (NATURE_OF_WORK_RP.equalsIgnoreCase(objection.getType()))
                natureOfWork = NATURE_REVISION_PETITION;
            else
                natureOfWork = NATURE_GENERAL_REVISION_PETITION;
            reportParams.put("logoPath", cityService.getCityLogoURL());
            reportParams.put("cityName", cityService.getMunicipalityName());
            reportParams.put("natureOfWork", natureOfWork);
            reportParams.put("recievedBy", objection.getBasicProperty().getFullOwnerName());
            reportParams.put("docNumberObjection", objection.getObjectionNumber());
            reportParams.put("currentDate", toDefaultDateFormat(new Date()));
            reportParams.put("receivedOn", toDefaultDateFormat(objection.getRecievedOn()));
            reportParams.put("HouseNo", objection.getBasicProperty().getUpicNo());
            reportParams.put("wardNumber", objection.getBasicProperty().getBoundary() != null
                    ? objection.getBasicProperty().getBoundary().getName() : "");
            reportParams.put("HalfYearPropertyTaxTo", currentDemand.get(CURR_SECONDHALF_DMD_STR).setScale(2));
            reportParams.put("HalfYearPropertyTaxFrom", earlierDemand.get(CURR_SECONDHALF_DMD_STR).setScale(2));
            reportParams.put("AnnualPropertyTaxTo",
                    currentDemand.get(CURR_SECONDHALF_DMD_STR).multiply(BigDecimal.valueOf(2)).setScale(2).toString());
            reportParams.put("AnnualPropertyTaxFrom",
                    earlierDemand.get(CURR_SECONDHALF_DMD_STR).multiply(BigDecimal.valueOf(2)).setScale(2).toString());

            reportRequest = new ReportRequest(REPORT_TEMPLATENAME_REVISIONPETITION_ENDORSEMENT, objection,
                    reportParams);
            reportOutput = reportService.createReport(reportRequest);
        }
        return reportOutput;

    }

    public void setNoticeInfo(final PropertyImpl property, final PropertyNoticeInfo propertyNotice,
            final BasicPropertyImpl basicProperty, final RevisionPetition objection) {
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
        infoBean.setApplicationDate(DateUtils.getFormattedDate(objection.getCreatedDate(), DATE_FORMAT_DDMMYYY));
        infoBean.setHearingDate(
                DateUtils.getFormattedDate(objection.getHearings().get(0).getPlannedHearingDt(), DATE_FORMAT_DDMMYYY));
        infoBean.setActualHearingDate(
                DateUtils.getFormattedDate(objection.getHearings().get(0).getActualHearingDt(), DATE_FORMAT_DDMMYYY));
        final User approver = userService.getUserById(ApplicationThreadLocals.getUserId());
        infoBean.setApproverName(approver.getName());
        final BigDecimal revTax = currDemand.getBaseDemand();
        infoBean.setNewTotalTax(revTax.setScale(0, BigDecimal.ROUND_HALF_UP));
        if (property.getSource().equals(SOURCE_MEESEVA))
            infoBean.setMeesevaNo(property.getApplicationNo());
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
                totalTax = totalTax.add(demandDetail.getAmount());

                if (demandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode()
                        .equalsIgnoreCase(DEMANDRSN_CODE_EDUCATIONAL_TAX))
                    propertyTax = propertyTax.add(demandDetail.getAmount());
                setLibraryCess(infoBean, propertyType, demandDetail);

                if (NON_VACANT_TAX_DEMAND_CODES
                        .contains(demandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode())
                        || demandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode()
                                .equalsIgnoreCase(DEMANDRSN_CODE_VACANT_TAX))
                    propertyTax = propertyTax.add(demandDetail.getAmount());
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
                infoBean.setRevLibraryCess(demandDetail.getAmount());
            if (propertyType.equalsIgnoreCase(HISTORY))
                infoBean.setExistingLibraryCess(demandDetail.getAmount());
        }
    }

    private void setUCPenalty(final PropertyAckNoticeInfo infoBean, final String propertyType,
            final EgDemandDetails demandDetail) {
        if (demandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode()
                .equalsIgnoreCase(DEMANDRSN_CODE_UNAUTHORIZED_PENALTY)) {
            if (propertyType.equalsIgnoreCase(CURRENT))
                infoBean.setRevUCPenalty(demandDetail.getAmount());
            if (propertyType.equalsIgnoreCase(HISTORY))
                infoBean.setExistingUCPenalty(demandDetail.getAmount());
        }
    }

    public Boolean validateDemand(final RevisionPetition objection) {
        Boolean demandIncerased = false;
        final Set<Ptdemand> newDemandSet = objection.getProperty().getPtDemandSet();
        final List<Ptdemand> ptDemandList = new ArrayList<>();
        ptDemandList.addAll(newDemandSet);

        final BigDecimal oldDemand = getDemandforCurrenttInst(propertyService.getInstallmentWiseDemand(
                ptDemandDAO.getNonHistoryCurrDmdForProperty(objection.getBasicProperty().getProperty())));
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

    public Map<String, String[]> updateStateAndStatus(final RevisionPetition objection, final Long approverPositionId,
            final String workFlowAction, final String approverComments, final String approverName) {
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
        if (objection.getState() != null) {
            loggedInUserAssign = assignmentService.getAssignmentByPositionAndUserAsOnDate(
                    objection.getCurrentState().getOwnerPosition().getId(), user.getId(), new Date());
            loggedInUserDesignation = !loggedInUserAssign.isEmpty()
                    ? loggedInUserAssign.get(0).getDesignation().getName() : null;
        }

        if (loggedInUserDesignation != null && (JUNIOR_ASSISTANT.equals(loggedInUserDesignation)
                || SENIOR_ASSISTANT.equals(loggedInUserDesignation)))
            loggedInUserDesignation = null;

        if (objection.getId() != null)
            wfInitiator = getWorkflowInitiator(objection);
        else
            wfInitiator = propertyTaxCommonUtils.getWorkflowInitiatorAssignment(user.getId());

        if (approverPositionId != null && approverPositionId != -1)
            position = positionMasterService.getPositionById(approverPositionId);
        if (WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(workFlowAction) && loggedInUserDesignation != null
                && loggedInUserDesignation.endsWith(COMMISSIONER_DESGN))
            pendingAction = new StringBuilder().append(loggedInUserDesignation).append(" ").append("Approval Pending")
                    .toString();
        else
            pendingAction = getPendingActions(objection);

        if (null == objection.getState()) {
            if (!citizenPortalUser && loggedUserIsEmployee && !ANONYMOUS_USER.equalsIgnoreCase(user.getName()))
                wfmatrix = revisionPetitionWorkFlowService.getWfMatrix(objection.getStateType(), null, null,
                        getAdditionalRule(objection), null, null, null);
            else
                wfmatrix = revisionPetitionWorkFlowService.getWfMatrix(objection.getStateType(), null, null,
                        getAdditionalRule(objection), "Created", null, null);
        } else if (objection.getCurrentState().getValue().equalsIgnoreCase(RP_INSPECTION_COMPLETE)
                || objection.getCurrentState().getValue().equalsIgnoreCase(GRP_INSPECTION_COMPLETE))
            wfmatrix = revisionPetitionWorkFlowService.getWfMatrix(objection.getStateType(), null, null,
                    getAdditionalRule(objection), objection.getCurrentState().getValue(),
                    objection.getCurrentState().getNextAction(), null, loggedInUserDesignation);
        else if (!OBJECTION_CREATED.equalsIgnoreCase(objection.getEgwStatus().getCode())
                && loggedInUserDesignation != null && loggedInUserDesignation.endsWith("Commissioner")
                && (WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(workFlowAction)
                        || WFLOW_ACTION_STEP_FORWARD.equalsIgnoreCase(workFlowAction)))
            wfmatrix = revisionPetitionWorkFlowService.getWfMatrix(objection.getStateType(), null, null,
                    getAdditionalRule(objection), objection.getCurrentState().getValue(), pendingAction, null,
                    loggedInUserDesignation);
        else
            wfmatrix = revisionPetitionWorkFlowService.getWfMatrix(objection.getStateType(), null, null,
                    getAdditionalRule(objection), objection.getCurrentState().getValue(),
                    pendingAction != null ? pendingAction : null, null, null);
        if (objection.getState() == null) {
            if (position == null && (approverPositionId == null || approverPositionId != -1)) {
                Assignment assignment;
                if (propertyService.isCscOperator(user))
                    assignment = propertyService.getMappedAssignmentForCscOperator(objection.getBasicProperty());
                else
                    assignment = propertyService.getUserPositionByZone(objection.getBasicProperty(), false);
                if (assignment != null)
                    position = assignment.getPosition();
            }
            updateRevisionPetitionStatus(wfmatrix, objection, OBJECTION_CREATED);

            if (position != null)
                user = eisCommonService.getUserForPosition(position.getId(), new Date());

            objection.transition().start().withNextAction(wfmatrix.getPendingActions())
                    .withStateValue(wfmatrix.getNextState()).withDateInfo(new DateTime().toDate()).withOwner(position)
                    .withSenderName(user.getUsername() + "::" + user.getName()).withOwner(user)
                    .withComments(approverComments).withNextAction(wfmatrix.getNextAction())
                    .withInitiator(wfInitiator != null ? wfInitiator.getPosition() : null)
                    .withNatureOfTask(NATURE_OF_WORK_RP.equalsIgnoreCase(objection.getType()) ? NATURE_REVISION_PETITION
                            : NATURE_GENERAL_REVISION_PETITION)
                    .withSLA(propertyService.getSlaValue(NATURE_OF_WORK_RP.equalsIgnoreCase(objection.getType())
                            ? APPLICATION_TYPE_REVISION_PETITION : APPLICATION_TYPE_GRP));

            if (loggedUserIsEmployee && user != null)
                actionMessages.put(OBJECTION_FORWARD,
                        new String[] { user.getName().concat("~").concat(position.getName()) });
            updateIndexAndPushToPortalInbox(objection);

        } else if (workFlowAction != null && !"".equals(workFlowAction)
                && !WFLOW_ACTION_STEP_SAVE.equalsIgnoreCase(workFlowAction)) {

            if (WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(workFlowAction)
                    || workFlowAction.equalsIgnoreCase(CANCEL_UNCONSIDERED)) {

                wfmatrix = revisionPetitionWorkFlowService.getPreviousStateFromWfMatrix(objection.getStateType(), null,
                        null, getAdditionalRule(objection), objection.getCurrentState().getValue(),
                        objection.getCurrentState().getNextAction());
                if (approverPositionId == null || approverPositionId != -1)
                    position = objection.getCurrentState().getOwnerPosition();
            }
            if (WFLOW_ACTION_STEP_SIGN.equalsIgnoreCase(workFlowAction)) {
                if (propertyService.isEmployee(objection.getCreatedBy()))
                    position = assignmentService.getPrimaryAssignmentForUser(objection.getCreatedBy().getId())
                            .getPosition();
                else if (!objection.getStateHistory().isEmpty())
                    position = assignmentService.getPrimaryAssignmentForPositon(
                            objection.getStateHistory().get(0).getOwnerPosition().getId()).getPosition();
                else
                    position = objection.getState().getOwnerPosition();
            } else if (position == null)
                position = positionMasterService.getPositionByUserId(user.getId());

            if (wfmatrix != null)
                actionMessages.putAll(workFlowTransition(objection, workFlowAction, approverComments, wfmatrix,
                        position, approverPositionId, approverName));
            // Update elastic search index on each workflow.
            updateIndexAndPushToPortalInbox(objection);

        } else if (!StringUtils.isBlank(workFlowAction) && WFLOW_ACTION_STEP_SAVE.equalsIgnoreCase(workFlowAction))
            actionMessages.put("file.save", new String[] {});
        return actionMessages;

    }

    /**
     * @param objection
     * @param workFlowAction
     * @param comments
     * @param wfmatrix
     * @param position
     * @param user
     */
    public Map<String, String[]> workFlowTransition(final RevisionPetition objection, final String workFlowAction,
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
                objection.getCurrentState().getOwnerPosition().getId(), user.getId(), new Date());
        loggedInUserDesignation = !loggedInUserAssign.isEmpty() ? loggedInUserAssign.get(0).getDesignation().getName()
                : null;
        final Assignment wfInitiator = getWorkflowInitiator(objection);
        if (WFLOW_ACTION_STEP_FORWARD.equalsIgnoreCase(workFlowAction)
                || workFlowAction.equalsIgnoreCase("approve objection")
                || workFlowAction.equalsIgnoreCase(FORWARD_TO_APPROVER)) {

            if (wfmatrix != null && (wfmatrix.getNextStatus() != null
                    && wfmatrix.getNextStatus().equalsIgnoreCase(OBJECTION_HEARING_FIXED)
                    || wfmatrix.getCurrentState().equalsIgnoreCase(RP_INSPECTIONVERIFIED)
                    || wfmatrix.getCurrentState().equalsIgnoreCase(RP_WF_REGISTERED)
                    || objection.getState().getValue().equalsIgnoreCase(GRP_WF_REGISTERED))) {
                for (final StateHistory<Position> stateHistoryObj : objection.getState().getHistory()) {
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
                        if (objection.getEgwStatus() != null
                                && objection.getEgwStatus().getCode().equalsIgnoreCase(OBJECTION_CREATED))
                            updateRevisionPetitionStatus(wfmatrix, objection, OBJECTION_HEARING_FIXED);
                        positionFoundInHistory = true;
                        break;
                    }
                }
                if (!positionFoundInHistory && objection.getState() != null
                        && Arrays.asList(RP_CREATED, RP_WF_REGISTERED, GRP_CREATED, GRP_WF_REGISTERED)
                                .contains(objection.getState().getValue())) {
                    positionFoundInHistory = true;
                    updateRevisionPetitionStatus(wfmatrix, objection, OBJECTION_HEARING_FIXED);
                    position = objection.getState().getInitiatorPosition() != null
                            ? objection.getState().getInitiatorPosition() : wfInitiator.getPosition();
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
                if (objection.getState().getNextAction().equalsIgnoreCase(OBJECTION_PRINT_ENDORSEMENT)
                        || objection.getState().getNextAction().equalsIgnoreCase(WF_STATE_DIGITAL_SIGNATURE_PENDING))
                    nextAction = objection.getState().getNextAction();
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

            objection.transition().progressWithStateCopy()
                    .withStateValue(nextState != null ? nextState : wfmatrix.getNextState()).withOwner(position)
                    .withSenderName(user.getUsername() + "::" + user.getName()).withDateInfo(new DateTime().toDate())
                    .withNextAction(nextAction != null ? nextAction : wfmatrix.getNextAction())
                    .withComments(approverComments);

            if (wfmatrix.getNextAction() != null && wfmatrix.getNextAction().equalsIgnoreCase("END"))
                objection.transition().end().withStateValue(wfmatrix.getNextState())
                        .withOwner(objection.getCurrentState().getOwnerPosition())
                        .withSenderName(user.getUsername() + "::" + user.getName())
                        .withNextAction(wfmatrix.getNextAction()).withDateInfo(new DateTime().toDate())
                        .withComments(approverComments).withNextAction(null)
                        .withOwner(objection.getCurrentState().getOwnerPosition());

            if (wfmatrix.getNextStatus() != null)
                updateRevisionPetitionStatus(wfmatrix, objection, null);
            if (approverName != null && !approverName.isEmpty() && !approverName.equalsIgnoreCase(CHOOSE))
                actionMessages.put(OBJECTION_FORWARD,
                        new String[] { approverName.concat("~").concat(position.getName()) });
            else if (user != null && !positionFoundInHistory)
                actionMessages.put(OBJECTION_FORWARD,
                        new String[] { user.getName().concat("~").concat(position.getName()) });

        } else if (workFlowAction.equalsIgnoreCase(REJECT_INSPECTION_STR)) {
            final List<StateHistory<Position>> stateHistoryList = objection.getStateHistory();
            Assignment wfInit = null;
            for (final StateHistory<Position> stateHistoryObj : stateHistoryList)
                if (stateHistoryObj.getValue().equalsIgnoreCase(RP_HEARINGCOMPLETED)
                        || stateHistoryObj.getValue().equalsIgnoreCase(GRP_HEARINGCOMPLETED)) {
                    position = stateHistoryObj.getOwnerPosition();
                    wfInit = propertyService.getUserOnRejection(objection);
                    break;
                }
            if (wfInit != null) {
                objection.setEgwStatus(
                        egwStatusDAO.getStatusByModuleAndCode(OBJECTION_MODULE, OBJECTION_HEARING_COMPLETED));

                if (position != null) {
                    objection.transition().progressWithStateCopy().withNextAction(OBJECTION_RECORD_INSPECTIONDETAILS)
                            .withStateValue(
                                    PROPERTY_MODIFY_REASON_REVISION_PETITION.equalsIgnoreCase(objection.getType())
                                            ? RP_APP_STATUS_REJECTED : GRP_APP_STATUS_REJECTED)
                            .withOwner(position).withSenderName(user.getUsername() + "::" + user.getName())
                            .withDateInfo(new DateTime().toDate()).withComments(approverComments);
                    final String actionMessage = propertyTaxUtil.getApproverUserName(position.getId());
                    if (actionMessage != null)
                        actionMessages.put(OBJECTION_FORWARD, new String[] { actionMessage });
                }
            } else
                actionMessages.put(REJECT_INSPECTION, new String[] { objection.getBasicProperty().getUpicNo() });

        } else if (workFlowAction.equalsIgnoreCase(REJECT)) {
            final List<StateHistory<Position>> stateHistoryList = objection.getStateHistory();
            for (final StateHistory<Position> stateHistoryObj : stateHistoryList)
                if (stateHistoryObj.getValue().equalsIgnoreCase(objection.getCurrentState().getValue())) {
                    position = stateHistoryObj.getOwnerPosition();
                    break;
                }
            if (objection.getCurrentState() != null
                    && (objection.getCurrentState().getValue().equalsIgnoreCase(REJECTED)
                            || objection.getCurrentState().getValue().equalsIgnoreCase(RP_CREATED))) {
                objection.transition().end().withStateValue(wfmatrix.getNextState()).withOwner(position)
                        .withSenderName(user.getUsername() + "::" + user.getName())
                        .withNextAction(wfmatrix.getNextAction()).withDateInfo(new DateTime().toDate())
                        .withComments(approverComments).withNextAction(null)
                        .withOwner(objection.getCurrentState().getOwnerPosition());

                updateRevisionPetitionStatus(wfmatrix, objection, REJECTED);

            } else {
                objection.transition().progressWithStateCopy().withStateValue(wfmatrix.getCurrentState())
                        .withOwner(position).withSenderName(user.getUsername() + "::" + user.getName())
                        .withDateInfo(new DateTime().toDate()).withNextAction(wfmatrix.getPendingActions())
                        .withComments(approverComments);

                if (workFlowAction.equalsIgnoreCase(REJECT))
                    updateRevisionPetitionStatus(wfmatrix, objection, null);
            }

            if (approverName != null && !approverName.isEmpty() && !approverName.equalsIgnoreCase(CHOOSE))
                actionMessages.put(OBJECTION_FORWARD,
                        new String[] { approverName.concat("~").concat(position.getName()) });
            else if (user != null)
                actionMessages.put(OBJECTION_FORWARD,
                        new String[] { user.getName().concat("~").concat(position.getName()) });
        } else if (workFlowAction.equalsIgnoreCase(PRINT_ENDORESEMENT)) {
            position = objection.getState().getOwnerPosition();
            objection.transition().progressWithStateCopy().withStateValue(wfmatrix.getCurrentState())
                    .withOwner(position).withSenderName(user.getUsername() + "::" + user.getName())
                    .withDateInfo(new DateTime().toDate()).withNextAction(wfmatrix.getNextAction())
                    .withComments(approverComments);
        } else if (WFLOW_ACTION_STEP_SIGN.equalsIgnoreCase(workFlowAction))
            objection.transition().progressWithStateCopy().withStateValue(wfmatrix.getCurrentState())
                    .withOwner(position).withSenderName(user.getUsername() + "::" + user.getName())
                    .withDateInfo(new DateTime().toDate()).withNextAction(wfmatrix.getNextAction())
                    .withComments(approverComments);
        else if (workFlowAction.equalsIgnoreCase(APPROVE)) {
            position = objection.getState().getOwnerPosition();
            objection.transition().progressWithStateCopy().withStateValue(wfmatrix.getNextState()).withOwner(position)
                    .withSenderName(user.getUsername() + "::" + user.getName()).withDateInfo(new DateTime().toDate())
                    .withNextAction(wfmatrix.getNextAction()).withComments(approverComments);
        }
        applyAuditing(objection.getState());
        return actionMessages;
    }

    public String getPendingActions(final RevisionPetition objection) {
        if (objection != null && objection.getId() != null) {
            if (RP_INSPECTIONVERIFIED.equalsIgnoreCase(objection.getCurrentState().getValue())
                    || GRP_INSPECTIONVERIFIED.equalsIgnoreCase(objection.getCurrentState().getValue())
                    || objection.getCurrentState().getValue().endsWith("Forwarded")
                    || objection.getCurrentState().getValue().endsWith("Approved"))
                return objection.getCurrentState().getNextAction();
            else
                return null;
        } else
            return null;

    }

    public String getAdditionalRule(final RevisionPetition objection) {
        String addittionalRule;
        if (PROPERTY_MODIFY_REASON_GENERAL_REVISION_PETITION.equals(objection.getType()))
            addittionalRule = GENERAL_REVISION_PETITION;
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
                    final BigDecimal extraCollAmt = newDmndDtls.getAmtCollected().subtract(newDmndDtls.getAmount());
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
    
    public void cancelObjection(final RevisionPetition objection) {
        objection.getBasicProperty().setUnderWorkflow(Boolean.FALSE);
        objection.getProperty().setStatus(PropertyTaxConstants.STATUS_CANCELLED);
        updateRevisionPetitionStatus(null, objection, PropertyTaxConstants.CANCELLED);
        objection.transition().end().withOwner(objection.getCurrentState().getOwnerPosition()).withNextAction(null);
    }

    public void updateIndexAndPushToPortalInbox(final RevisionPetition objection) {
        if (objection.getType().equalsIgnoreCase(NATURE_OF_WORK_RP))
            propertyService.updateIndexes(objection, APPLICATION_TYPE_REVISION_PETITION);
        else
            propertyService.updateIndexes(objection, APPLICATION_TYPE_GRP);
        if (Source.CITIZENPORTAL.toString().equalsIgnoreCase(objection.getSource())) {
            final PortalInbox portalInbox = propertyService.getPortalInbox(objection.getObjectionNumber());
            if (portalInbox != null)
                propertyService.updatePortal(objection, "RP".equalsIgnoreCase(objection.getType())
                        ? APPLICATION_TYPE_REVISION_PETITION : APPLICATION_TYPE_GRP);
        }
    }

}
