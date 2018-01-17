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

import org.apache.struts2.ServletActionContext;
import org.egov.commons.EgwStatus;
import org.egov.commons.Installment;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.entity.Source;
import org.egov.demand.model.EgDemandDetails;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.CityService;
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
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.ptis.bean.PropertyNoticeInfo;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.PropertyStatusDAO;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.objection.RevisionPetition;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyID;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.domain.service.property.SMSEmailService;
import org.egov.ptis.report.bean.PropertyAckNoticeInfo;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.egov.infra.utils.DateUtils.toDefaultDateFormat;
import static org.egov.ptis.constants.PropertyTaxConstants.ANONYMOUS_USER;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_REVISION_PETITION;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_SECONDHALF_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.DATE_FORMAT_DDMMYYY;
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_GENERAL_REVISION_PETITION;
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_OF_WORK_RP;
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_REVISION_PETITION;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;
import static org.egov.ptis.domain.service.property.PropertyService.APPLICATION_VIEW_URL;

public class RevisionPetitionService extends PersistenceService<RevisionPetition, Long> {
    private static final String REVISION_PETITION_CREATED = "CREATED";
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
    private static final String CURRENT = "current";
    private static final String HISTORY = "history";
    
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
        WorkFlowMatrix wfmatrix = null;
        User user = null;
        if (objection.getId() == null) {
            if (objection.getObjectionNumber() == null)
                objection.setObjectionNumber(applicationNumberGenerator.generate());
            objection.getBasicProperty().setStatus(
                    propertyStatusDAO.getPropertyStatusByCode(PropertyTaxConstants.STATUS_OBJECTED_STR));
            objection.getBasicProperty().setUnderWorkflow(Boolean.TRUE);
            if (objection.getState() == null) {
                wfmatrix = revisionPetitionWorkFlowService.getWfMatrix(objection.getStateType(), null, null, null,
                        PropertyTaxConstants.REVISIONPETITION_CREATED, null);
                // Get the default revenue cleark from admin boundary.
                final Designation desig = designationService
                        .getDesignationByName(PropertyTaxConstants.REVENUE_CLERK_DESGN);
                List<Assignment> assignment = assignmentService.findByDesignationAndBoundary(desig.getId(), objection
                        .getBasicProperty().getPropertyID().getZone().getId());
                if (!assignment.isEmpty())
                    position = assignment.get(0).getPosition();
                else {
                    assignment = assignmentService
                            .findPrimaryAssignmentForDesignationName(PropertyTaxConstants.REVENUE_CLERK_DESGN);
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
        ApplicationIndex applicationIndex = applicationIndexService.findByApplicationNumber(objection
                .getObjectionNumber());
        final User user = securityUtils.getCurrentUser();
        if (null == applicationIndex) {
            applicationIndex = ApplicationIndex.builder().withModuleName(PTMODULENAME)
                    .withApplicationNumber(objection.getObjectionNumber()).withApplicationDate(
                            objection.getCreatedDate() != null ? objection.getCreatedDate() : new Date())
                    .withApplicationType(APPLICATION_TYPE_REVISION_PETITION)
                    .withApplicantName(objection.getBasicProperty().getFullOwnerName())
                    .withStatus(objection.getState().getValue())
                    .withUrl(format(APPLICATION_VIEW_URL, objection.getObjectionNumber(), ""))
                    .withApplicantAddress(objection.getBasicProperty().getAddress().toString())
                    .withOwnername(user.getUsername() + "::" + user.getName())
                    .withChannel(Source.SYSTEM.toString()).build();
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
            egwStatus = egwStatusDAO.getStatusByModuleAndCode(PropertyTaxConstants.OBJECTION_MODULE, status);

        else if (wfmatrix != null && wfmatrix.getNextStatus() != null && objection != null)
            egwStatus = egwStatusDAO.getStatusByModuleAndCode(PropertyTaxConstants.OBJECTION_MODULE,
                    wfmatrix.getNextStatus());
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
                smsMsg = "Revision petition created. Use " + objection.getObjectionNumber()
                        + " for future reference";
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
                                                   final HashMap<String, String> meesevaParams) {
        createRevisionPetition(objection);
        return objection;
    }

    public Assignment getWorkflowInitiator(final RevisionPetition objection) {
        Assignment wfInitiator;
        if (propertyService.isEmployee(objection.getCreatedBy())
                && !ANONYMOUS_USER.equalsIgnoreCase(objection.getCreatedBy().getName())
                && !propertyService.isCitizenPortalUser(objection.getCreatedBy())) {
            if (objection.getState() != null && objection.getState().getInitiatorPosition() != null)
                wfInitiator = propertyTaxCommonUtils.getUserAssignmentByPassingPositionAndUser(objection
                        .getCreatedBy(), objection.getState().getInitiatorPosition());
            else
                wfInitiator = assignmentService.getAllActiveEmployeeAssignmentsByEmpId(objection.getCreatedBy().getId()).get(0);
        } else if (!objection.getStateHistory().isEmpty()) {
            if (objection.getState().getInitiatorPosition() == null)
                wfInitiator = assignmentService.getAssignmentsForPosition(
                        objection.getStateHistory().get(0).getOwnerPosition().getId(), new Date()).get(0);
            else
                wfInitiator = assignmentService.getAssignmentsForPosition(
                        objection.getState().getInitiatorPosition().getId(), new Date()).get(0);
        } else
            wfInitiator = assignmentService.getAssignmentsForPosition(objection.getState().getOwnerPosition()
                    .getId(), new Date()).get(0);
        return wfInitiator;
    }

    public RevisionPetition getExistingObjections(final BasicProperty basicProperty) {
        return find("from RevisionPetition rp where rp.basicProperty = ?", basicProperty);
    }

    public RevisionPetition getExistingGRP(final BasicProperty basicProperty) {
        return find("from RevisionPetition rp where rp.basicProperty = ? and rp.type = ?", basicProperty,
                PropertyTaxConstants.NATURE_OF_WORK_GRP);
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
            Boolean isCorporation = isNotBlank(cityGrade) && cityGrade.equalsIgnoreCase(PropertyTaxConstants.CITY_GRADE_CORPORATION);
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
                reportParams.put("hearingNoticeDate", toDefaultDateFormat(objection.getHearings().get(objection.getHearings().size() - 1).getPlannedHearingDt()));
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
            reportRequest = new ReportRequest(PropertyTaxConstants.REPORT_TEMPLATENAME_REVISIONPETITION_HEARINGNOTICE,
                    objection, reportParams);
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
            final Map<String, BigDecimal> earlierDemand = ptDemandDAO
                    .getDemandCollMap(propertyService.getLatestHistoryProperty(objection.getBasicProperty().getUpicNo()));
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
            reportParams.put("HalfYearPropertyTaxTo",
                    currentDemand.get(CURR_SECONDHALF_DMD_STR).divide(BigDecimal.valueOf(2)).setScale(2));
            reportParams.put("HalfYearPropertyTaxFrom",
                    earlierDemand.get(CURR_SECONDHALF_DMD_STR).divide(BigDecimal.valueOf(2)).setScale(2));
            reportParams.put("AnnualPropertyTaxTo", currentDemand.get(CURR_SECONDHALF_DMD_STR).setScale(2).toString());
            reportParams.put("AnnualPropertyTaxFrom",
                    earlierDemand.get(CURR_SECONDHALF_DMD_STR).setScale(2).toString());

            reportRequest = new ReportRequest(PropertyTaxConstants.REPORT_TEMPLATENAME_REVISIONPETITION_ENDORSEMENT,
                    objection, reportParams);
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
        infoBean.setNewTotalTax(revTax);
        if (property.getSource().equals(PropertyTaxConstants.SOURCE_MEESEVA))
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
                        .equalsIgnoreCase(PropertyTaxConstants.DEMANDRSN_CODE_EDUCATIONAL_CESS))
                    propertyTax = propertyTax.add(demandDetail.getAmount());
                setLibraryCess(infoBean, propertyType, demandDetail);

                if (demandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode()
                        .equalsIgnoreCase(PropertyTaxConstants.DEMANDRSN_CODE_GENERAL_TAX)
                        || demandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode()
                                .equalsIgnoreCase(PropertyTaxConstants.DEMANDRSN_CODE_VACANT_TAX))
                    propertyTax = propertyTax.add(demandDetail.getAmount());
                setUCPenalty(infoBean, propertyType, demandDetail);
            }
        setTotalTax(infoBean, totalTax, propertyTax, propertyType);
    }

    private void setTotalTax(final PropertyAckNoticeInfo infoBean, BigDecimal totalTax, BigDecimal propertyTax,
            final String propertyType) {
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
                .equalsIgnoreCase(PropertyTaxConstants.DEMANDRSN_CODE_LIBRARY_CESS)) {
            if (propertyType.equalsIgnoreCase(CURRENT))
                infoBean.setRevLibraryCess(demandDetail.getAmount());
            if (propertyType.equalsIgnoreCase(HISTORY))
                infoBean.setExistingLibraryCess(demandDetail.getAmount());
        }
    }

    private void setUCPenalty(final PropertyAckNoticeInfo infoBean, final String propertyType,
            final EgDemandDetails demandDetail) {
        if (demandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode()
                .equalsIgnoreCase(PropertyTaxConstants.DEMANDRSN_CODE_UNAUTHORIZED_PENALTY)) {
            if (propertyType.equalsIgnoreCase(CURRENT))
                infoBean.setRevUCPenalty(demandDetail.getAmount());
            if (propertyType.equalsIgnoreCase(HISTORY))
                infoBean.setExistingUCPenalty(demandDetail.getAmount());
        }
    }

    public Boolean validateDemand(final RevisionPetition objection) {
        Boolean demandIncerased = false;
        Set<Ptdemand> newDemandSet = objection.getProperty().getPtDemandSet();
        List<Ptdemand> ptDemandList = new ArrayList<>();
        ptDemandList.addAll(newDemandSet);

        Installment currentInstall = propertyTaxCommonUtils.getCurrentInstallment();

        BigDecimal oldDemand = getDemandforCurrenttInst(propertyService.getInstallmentWiseDemand(
                ptDemandDAO.getNonHistoryCurrDmdForProperty(objection.getBasicProperty().getProperty())), currentInstall);
        BigDecimal newDemand = getDemandforCurrenttInst(propertyService.getInstallmentWiseDemand(ptDemandList.get(0)),
                currentInstall);
        if (newDemand.compareTo(oldDemand) > 0) 
            demandIncerased= true;
        return demandIncerased;
    }

    private BigDecimal getDemandforCurrenttInst(Map<Installment, BigDecimal> instWiseDemand, Installment currentInstall) {
        BigDecimal demand = BigDecimal.ZERO;
        for (Map.Entry<Installment, BigDecimal> entry : instWiseDemand.entrySet()){
            if(entry.getKey().equals(currentInstall))
                demand= entry.getValue();
        }
        return demand;
    }


}
