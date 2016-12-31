/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
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
 */
package org.egov.ptis.domain.service.revisionPetition;

import static java.lang.String.format;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_REVISION_PETITION;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_SECONDHALF_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_GENERAL_REVISION_PETITION;
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_OF_WORK_RP;
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_REVISION_PETITION;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;
import static org.egov.ptis.domain.service.property.PropertyService.APPLICATION_VIEW_URL;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.entity.Source;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.elasticsearch.entity.ApplicationIndex;
import org.egov.infra.elasticsearch.service.ApplicationIndexService;
import org.egov.infra.messaging.MessagingService;
import org.egov.infra.reporting.engine.ReportConstants.FileFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.ApplicationNumberGenerator;
import org.egov.infra.web.utils.WebUtils;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.PropertyStatusDAO;
import org.egov.ptis.domain.entity.objection.RevisionPetition;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.domain.service.property.SMSEmailService;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

public class RevisionPetitionService extends PersistenceService<RevisionPetition, Long> {
    @Autowired
    private ApplicationNumberGenerator applicationNumberGenerator;
    @Autowired
    private PropertyStatusDAO propertyStatusDAO;
    @Autowired
    DesignationService designationService;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    protected AssignmentService assignmentService;
    @Autowired
    private EgwStatusHibernateDAO egwStatusDAO;
    @Autowired
    @Qualifier("workflowService")
    protected SimpleWorkflowService<RevisionPetition> revisionPetitionWorkFlowService;
    @Autowired
    private EisCommonService eisCommonService;
    @Autowired
    private ApplicationIndexService applicationIndexService;
    private static final String REVISION_PETITION_CREATED = "CREATED";

    @Autowired
    private MessagingService messagingService;
    private SMSEmailService sMSEmailService;
    
    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;
    
    @Autowired
    private PropertyService propertyService;
    
    @Autowired
    private ReportService reportService;
    
    @Autowired
    private PtDemandDao ptDemandDAO;
    
    public RevisionPetitionService() {
        super(RevisionPetition.class);
    }

    public RevisionPetitionService(Class<RevisionPetition> type) {
        super(type);
    }

    /**
     * Create revision petition
     *
     * @param objection
     * @return
     */
    @Transactional
    public RevisionPetition createRevisionPetition(RevisionPetition objection) {
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
                if (assignment.size() > 0)
                    position = assignment.get(0).getPosition();
                else {
                    assignment = assignmentService
                            .findPrimaryAssignmentForDesignationName(PropertyTaxConstants.REVENUE_CLERK_DESGN);
                    if (assignment.size() > 0)
                        position = assignment.get(0).getPosition();
                }

                updateRevisionPetitionStatus(wfmatrix, objection, null);

                if (position != null)
                    user = eisCommonService.getUserForPosition(position.getId(), new Date());

                objection.start().withNextAction(wfmatrix.getPendingActions())
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
                    .withApplicationType(APPLICATION_TYPE_REVISION_PETITION).withApplicantName(objection.getBasicProperty().getFullOwnerName())
                    .withStatus(objection.getState().getValue()).withUrl(format(APPLICATION_VIEW_URL, objection.getObjectionNumber(), ""))
                    .withApplicantAddress(objection.getBasicProperty().getAddress().toString()).withOwnername(user.getUsername() + "::" + user.getName())
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
        if (status != null && !"".equals(status))
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
    public RevisionPetition updateRevisionPetition(RevisionPetition objection) {
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
        if (objection != null) {
            for (PropertyOwnerInfo ownerInfo : objection.getBasicProperty().getPropertyOwnerInfo()) {
                sendEmailAndSms(objection, ownerInfo.getOwner(), applicationType);
            }
        }
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
        if (StringUtils.isNotBlank(mobileNumber) && StringUtils.isNotBlank(smsMsg))
            messagingService.sendSMS(mobileNumber, smsMsg);
        if (StringUtils.isNotBlank(emailid) && StringUtils.isNotBlank(emailBody))
            messagingService.sendEmail(emailid, emailSubject, emailBody);
    }

    public SMSEmailService getsMSEmailService() {
        return sMSEmailService;
    }

    public void setsMSEmailService(final SMSEmailService sMSEmailService) {
        this.sMSEmailService = sMSEmailService;
    }
    
    public RevisionPetition createRevisionPetition(RevisionPetition objection, HashMap<String, String> meesevaParams){
        createRevisionPetition(objection);
        return objection;
    }
    
    public Assignment getWorkflowInitiator(RevisionPetition objection) {
        Assignment wfInitiator;
        if (propertyService.isEmployee(objection.getCreatedBy())){
                if(objection.getState() != null  && objection.getState().getInitiatorPosition() != null)
                    wfInitiator = propertyTaxCommonUtils.getUserAssignmentByPassingPositionAndUser(objection
                    .getCreatedBy(),objection.getState().getInitiatorPosition());
                else 
                    wfInitiator = assignmentService.getPrimaryAssignmentForUser(objection.getCreatedBy().getId());
        }
        else if (!objection.getStateHistory().isEmpty())
            wfInitiator = assignmentService.getPrimaryAssignmentForPositon(objection.getStateHistory().get(0)
                    .getOwnerPosition().getId());
        else{
            wfInitiator = assignmentService.getPrimaryAssignmentForPositon(objection.getState().getOwnerPosition()
                    .getId());
        }
        return wfInitiator;
    }
    
    public RevisionPetition getExistingObjections(BasicProperty basicProperty){
            return find("from RevisionPetition rp where rp.basicProperty = ?" ,basicProperty);
    }
    

    /**
     * @param reportOutput
     * @param objection
     * @return ReportOutput
     */
    public ReportOutput createHearingNoticeReport(ReportOutput reportOutput, final RevisionPetition objection,
            final String noticeNo) {
        reportOutput.setReportFormat(FileFormat.PDF);
        final HashMap<String, Object> reportParams = new HashMap<>();
        final SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
        String natureOfWork;
        ReportRequest reportRequest;
        if (objection != null) {
            final HttpServletRequest request = ServletActionContext.getRequest();
            final String cityName = request.getSession().getAttribute("citymunicipalityname").toString();
            final String cityGrade = request.getSession().getAttribute("cityGrade") != null
                    ? request.getSession().getAttribute("cityGrade").toString() : null;
            Boolean isCorporation;
            if (cityGrade != null && cityGrade != ""
                    && cityGrade.equalsIgnoreCase(PropertyTaxConstants.CITY_GRADE_CORPORATION)) {
                isCorporation = true;
            } else
                isCorporation = false;
            if (NATURE_OF_WORK_RP.equalsIgnoreCase(objection.getType())) {
                natureOfWork = NATURE_REVISION_PETITION;

            } else
                natureOfWork = NATURE_GENERAL_REVISION_PETITION;
            reportParams.put("isCorporation", isCorporation);
            reportParams.put("cityName", cityName);
            reportParams.put("recievedBy", objection.getBasicProperty().getFullOwnerName());
            reportParams.put("natureOfWork", natureOfWork);

            if (objection.getHearings() != null && !objection.getHearings().isEmpty()
                    && objection.getHearings().get(objection.getHearings().size() - 1).getPlannedHearingDt() != null)
                reportParams.put("hearingNoticeDate", dateformat
                        .format(objection.getHearings().get(objection.getHearings().size() - 1).getPlannedHearingDt()));
            else
                reportParams.put("hearingNoticeDate", "");
            reportParams.put("currentDate", dateformat.format(new Date()));
            reportParams.put("recievedOn", dateformat.format(objection.getRecievedOn()));
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

        reportOutput.setReportFormat(FileFormat.PDF);
        final HashMap<String, Object> reportParams = new HashMap<>();
        final SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
        String natureOfWork;
        ReportRequest reportRequest;
        if (objection != null) {
            final Map<String, BigDecimal> currentDemand = ptDemandDAO.getDemandCollMap(objection.getProperty());
            final Map<String, BigDecimal> earlierDemand = ptDemandDAO
                    .getDemandCollMap(objection.getBasicProperty().getProperty());
            final HttpServletRequest request = ServletActionContext.getRequest();
            final String url = WebUtils.extractRequestDomainURL(request, false);
            final String cityLogo = url.concat(PropertyTaxConstants.IMAGE_CONTEXT_PATH)
                    .concat((String) request.getSession().getAttribute("citylogo"));
            final String cityName = request.getSession().getAttribute("citymunicipalityname").toString();
            if (NATURE_OF_WORK_RP.equalsIgnoreCase(objection.getType())) {
                natureOfWork = NATURE_REVISION_PETITION;

            } else
                natureOfWork = NATURE_GENERAL_REVISION_PETITION;
            reportParams.put("logoPath", cityLogo);
            reportParams.put("cityName", cityName);
            reportParams.put("natureOfWork", natureOfWork);
            reportParams.put("recievedBy", objection.getBasicProperty().getFullOwnerName());
            reportParams.put("docNumberObjection", objection.getObjectionNumber());
            reportParams.put("currentDate", dateformat.format(new Date()));
            reportParams.put("receivedOn", dateformat.format(objection.getRecievedOn()));
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
}
