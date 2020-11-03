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
package org.egov.ptis.domain.service.transfer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.entity.Source;
import org.egov.demand.utils.DemandConstants;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.integration.event.model.enums.ApplicationStatus;
import org.egov.infra.integration.event.model.enums.TransactionStatus;
import org.egov.infra.integration.service.ThirdPartyService;
import org.egov.infra.persistence.entity.enums.Gender;
import org.egov.infra.persistence.entity.enums.UserType;
import org.egov.infra.reporting.engine.ReportFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.rest.client.SimpleRestClient;
import org.egov.infra.script.service.ScriptService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.ApplicationNumberGenerator;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.web.utils.WebUtils;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.portal.entity.Citizen;
import org.egov.ptis.client.bill.PTBillServiceImpl;
import org.egov.ptis.client.util.PropertyTaxNumberGenerator;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.bill.PropertyTaxBillable;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.dao.property.PropertyMutationMasterDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;
import org.egov.ptis.domain.entity.property.Document;
import org.egov.ptis.domain.entity.property.DocumentType;
import org.egov.ptis.domain.entity.property.MutationRegistrationDetails;
import org.egov.ptis.domain.entity.property.PropertyAddress;
import org.egov.ptis.domain.entity.property.PropertyID;
import org.egov.ptis.domain.entity.property.PropertyMutation;
import org.egov.ptis.domain.entity.property.PropertyMutationMaster;
import org.egov.ptis.domain.entity.property.PropertyMutationTransferee;
import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;
import org.egov.ptis.domain.entity.property.PropertySource;
import org.egov.ptis.domain.entity.property.PtApplicationType;
import org.egov.ptis.domain.model.ErrorDetails;
import org.egov.ptis.domain.model.MutationFeeDetails;
import org.egov.ptis.domain.model.NewPropertyDetails;
import org.egov.ptis.domain.model.OwnerDetails;
import org.egov.ptis.domain.repository.master.mutationfee.MutationFeeRepository;
import org.egov.ptis.domain.service.notice.NoticeService;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.event.EventPublisher;
import org.egov.ptis.notice.PtNotice;
import org.egov.ptis.report.bean.PropertyAckNoticeInfo;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.egov.ptis.wtms.WaterChargesIntegrationService;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import static java.lang.String.format;
import static org.egov.ptis.constants.PropertyTaxConstants.*;

public class PropertyTransferService {
    private static final Logger LOGGER = Logger.getLogger(PropertyTransferService.class);

    @Autowired
    @Qualifier("propertyMutationService")
    private PersistenceService<PropertyMutation, Long> propertyMutationService;

    @Autowired
    @Qualifier("basicPropertyService")
    private PersistenceService<BasicProperty, Long> basicPropertyService;

    @Autowired
    private BasicPropertyDAO basicPropertyDAO;

    @Autowired
    private PropertyMutationMasterDAO propertyMutationMasterDAO;

    @Autowired
    @Qualifier("fileStoreService")
    private FileStoreService fileStoreService;

    @Autowired
    @Qualifier("propertyTaxNumberGenerator")
    private PropertyTaxNumberGenerator propertyTaxNumberGenerator;

    @Autowired
    @Qualifier("ptaxApplicationTypeService")
    private PersistenceService<PtApplicationType, Long> ptaxApplicationTypeService;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private SimpleRestClient simpleRestClient;

    @Autowired
    private ApplicationNumberGenerator applicationNumberGenerator;

    @Autowired
    private ReportService reportService;

    @Autowired
    private ScriptService scriptService;

    @Autowired
    private PTBillServiceImpl ptBillServiceImpl;

    @Autowired
    private PropertyTaxUtil propertyTaxUtil;

    @Autowired
    private CityService cityService;

    @Autowired
    private PropertyTaxBillable propertyTaxBillable;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    @Qualifier("waterChargesIntegrationServiceImpl")
    private WaterChargesIntegrationService waterChargesIntegrationService;

    @Autowired
    @Qualifier("mutationRegistrationService")
    private PersistenceService<MutationRegistrationDetails, Long> mutationRegistrationService;

    @Autowired
    private EisCommonService eisCommonService;

    @Autowired
    private DesignationService designationService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;

    @Autowired
    private MutationFeeRepository mutationFeeRepository;

    @PersistenceContext
    private EntityManager entityManager;
  
    @Autowired
    private EventPublisher eventPublisher;
    
    @Autowired
    private ThirdPartyService thirdPartyService;

    @Transactional
    public void initiatePropertyTransfer(final BasicProperty basicProperty, final PropertyMutation propertyMutation,final HttpServletRequest request,final String transactionId) {
        Boolean wsPortalRequest = Boolean.FALSE;
        if (request.getParameter(WARDSECRETARY_WSPORTAL_REQUEST) != null)
            wsPortalRequest = Boolean.valueOf(request.getParameter(WARDSECRETARY_WSPORTAL_REQUEST));
        propertyMutation.setBasicProperty(basicProperty);
        defineDocumentValue(propertyMutation);
        for (final PropertyOwnerInfo ownerInfo : basicProperty.getPropertyOwnerInfo())
            propertyMutation.getTransferorInfos().add(ownerInfo.getOwner());
        propertyMutation.setMutationDate(new Date());
        if (propertyMutation.getApplicationNo() == null)
            propertyMutation.setApplicationNo(applicationNumberGenerator.generate());
        createUserIfNotExist(propertyMutation, propertyMutation.getTransfereeInfosProxy());
        basicProperty.getPropertyMutations().add(propertyMutation);
        basicProperty.setUnderWorkflow(true);
        processAndStoreDocument(propertyMutation, null);
        if (thirdPartyService.isWardSecretaryRequest(wsPortalRequest)) {
            saveMutationAndPublishEvent(propertyMutation,request, transactionId);
        } else
        mutationRegistrationService.persist(propertyMutation.getMutationRegistrationDetails());
        if (propertyService.isCitizenPortalUser(getLoggedInUser()))
            propertyService.pushPropertyMutationPortalMessage(propertyMutation, propertyMutation.getType()
                    .equalsIgnoreCase(ADDTIONAL_RULE_REGISTERED_TRANSFER)
                    ? NATURE_REGISTERED_TRANSFER : NATURE_FULL_TRANSFER);
        basicPropertyService.persist(basicProperty);
        propertyService.updateIndexes(propertyMutation,
                propertyMutation.getType().equalsIgnoreCase(ADDTIONAL_RULE_REGISTERED_TRANSFER)
                        ? NATURE_REGISTERED_TRANSFER : NATURE_FULL_TRANSFER);
    }

    @Transactional
    public void approvePropertyTransfer(final BasicProperty basicProperty, final PropertyMutation propertyMutation) {
        final PropertySource propertySource = basicProperty.getPropertyOwnerInfo().get(0).getSource();
        basicProperty.getPropertyOwnerInfo().clear();
        createUserIfNotExist(propertyMutation, propertyMutation.getTransfereeInfosProxy());
        int order = 1;
        for (final PropertyMutationTransferee propertyOwner : propertyMutation.getTransfereeInfosProxy()) {
            final PropertyOwnerInfo propertyOwnerInfo = new PropertyOwnerInfo(basicProperty, propertySource,
                    propertyOwner.getTransferee(), order++);
            basicProperty.getPropertyOwnerInfo().add(propertyOwnerInfo);
        }
        propertyMutation.setMutationDate(new Date());
        propertyService.updateIndexes(propertyMutation, propertyMutation.getType()
                .equalsIgnoreCase(ADDTIONAL_RULE_REGISTERED_TRANSFER)
                ? NATURE_REGISTERED_TRANSFER : NATURE_FULL_TRANSFER);
        waterChargesIntegrationService.updateConsumerIndex(propertyService.loadAssessmentDetails(basicProperty));
        if (Source.CITIZENPORTAL.toString().equalsIgnoreCase(propertyMutation.getSource()))
            propertyService.updatePortal(propertyMutation, propertyMutation.getType()
                    .equalsIgnoreCase(ADDTIONAL_RULE_REGISTERED_TRANSFER)
                    ? NATURE_REGISTERED_TRANSFER : NATURE_FULL_TRANSFER);
        basicPropertyService.persist(basicProperty);
        propertyService.updateIndexes(propertyMutation, propertyMutation.getType()
                .equalsIgnoreCase(ADDTIONAL_RULE_REGISTERED_TRANSFER)
                        ? NATURE_REGISTERED_TRANSFER : NATURE_FULL_TRANSFER);
    }

    @Transactional
    public void updatePropertyTransfer(final BasicProperty basicProperty, final PropertyMutation propertyMutation,
                                       final String oldTransferReason) {
        processAndStoreDocument(propertyMutation, oldTransferReason);
        updateMutationFee(propertyMutation);
        defineDocumentValue(propertyMutation);
        createUserIfNotExist(propertyMutation, propertyMutation.getTransfereeInfosProxy());
        basicProperty.setUnderWorkflow(true);
        updateMutationReason(propertyMutation);
        mutationRegistrationService.persist(propertyMutation.getMutationRegistrationDetails());
        basicPropertyService.persist(basicProperty);
        propertyService.updateIndexes(propertyMutation, propertyMutation.getType()
                .equalsIgnoreCase(PropertyTaxConstants.ADDTIONAL_RULE_REGISTERED_TRANSFER)
                ? NATURE_REGISTERED_TRANSFER : NATURE_FULL_TRANSFER);
        mutationRegistrationService.persist(propertyMutation.getMutationRegistrationDetails());
        basicPropertyService.persist(basicProperty);
        if (Source.CITIZENPORTAL.toString().equalsIgnoreCase(propertyMutation.getSource()))
            propertyService.updatePortal(propertyMutation, propertyMutation.getType()
                    .equalsIgnoreCase(ADDTIONAL_RULE_REGISTERED_TRANSFER)
                    ? NATURE_REGISTERED_TRANSFER : NATURE_FULL_TRANSFER);
    }

    @Transactional
    public void viewPropertyTransfer(final BasicProperty basicProperty, final PropertyMutation propertyMutation) {
        updateMutationFee(propertyMutation);
        propertyService.updateIndexes(propertyMutation, propertyMutation.getType()
                .equalsIgnoreCase(PropertyTaxConstants.ADDTIONAL_RULE_REGISTERED_TRANSFER)
                ? NATURE_REGISTERED_TRANSFER : NATURE_FULL_TRANSFER);
        if (Source.CITIZENPORTAL.toString().equalsIgnoreCase(propertyMutation.getSource()))
            propertyService.updatePortal(propertyMutation, propertyMutation.getType()
                    .equalsIgnoreCase(ADDTIONAL_RULE_REGISTERED_TRANSFER)
                    ? NATURE_REGISTERED_TRANSFER : NATURE_FULL_TRANSFER);
        basicPropertyService.persist(basicProperty);
        propertyService.updateIndexes(propertyMutation, propertyMutation.getType()
                .equalsIgnoreCase(PropertyTaxConstants.ADDTIONAL_RULE_REGISTERED_TRANSFER)
                        ? NATURE_REGISTERED_TRANSFER : NATURE_FULL_TRANSFER);
    }

    @Transactional
    public void deleteTransferee(final PropertyMutation propertyMutation, final Long transfereeId) {
        PropertyMutationTransferee userToRemove = null;
        for (final PropertyMutationTransferee user : propertyMutation.getTransfereeInfos())
            if (user.getTransferee().getId().equals(transfereeId))
                userToRemove = user;
        propertyMutation.getTransfereeInfos().remove(userToRemove);
        propertyMutationService.persist(propertyMutation);
    }

    public double calculateMutationFee(final double marketValue, final String transferReason,
                                       final PropertyMutation propertyMutation) {
        final int transferedInMonths = Months.monthsBetween(
                new LocalDate(propertyMutation.getMutationDate()).withDayOfMonth(1),
                new LocalDate(propertyMutation.getDeedDate()).withDayOfMonth(1)).getMonths();
        return (Double) scriptService
                .executeScript("PTIS-MUTATION-FEE-CALCULATOR", ScriptService.createContext("marketValue", marketValue,
                        "transferedInMonths", transferedInMonths, "transferReason", transferReason));
    }

    public BigDecimal getWaterTaxDues(final String wtmsTaxDueChecking_REST_url) {
        final Map<String, Object> waterTaxInfo = simpleRestClient.getRESTResponseAsMap(wtmsTaxDueChecking_REST_url);
        return waterTaxInfo.get("totalTaxDue") == null ? BigDecimal.ZERO : new BigDecimal(
                Double.valueOf((Double) waterTaxInfo.get("totalTaxDue")));
    }

    public BasicPropertyImpl getBasicPropertyByUpicNo(final String upicNo) {
        return (BasicPropertyImpl) basicPropertyDAO.getBasicPropertyByPropertyID(upicNo);
    }

    public List<PropertyMutationMaster> getPropertyTransferReasons() {
        return propertyMutationMasterDAO.getAllActiveReasonsByType(PROP_MUTATION_RSN);
    }

    public PropertyMutationMaster getPropertyTransferReasonsByCode(final String mutationCode) {
        return propertyMutationMasterDAO.getPropertyMutationMasterByCodeAndType(mutationCode, PROP_MUTATION_RSN);
    }

    public PropertyMutation getPropertyMutationByApplicationNo(final String applicationNo) {
        Query qry = entityManager.createNamedQuery("BY_APPLICATION_NO");
        qry.setParameter("applicationNo", applicationNo);
        return (PropertyMutation) qry.getSingleResult();
    }

    public PropertyMutation getCurrentPropertyMutationByAssessmentNo(final String assessmentNo) {
        PropertyMutation currentPropertyMutation = null;
        final BasicProperty basicProperty = getBasicPropertyByUpicNo(assessmentNo);
        if (null != basicProperty)
            for (final PropertyMutation propertyMutation : basicProperty.getPropertyMutations())
                if (!propertyMutation.getState().getValue().equals(WF_STATE_CLOSED)) {
                    currentPropertyMutation = propertyMutation;
                    break;
                }
        return currentPropertyMutation;
    }

    public ReportOutput generateAcknowledgement(final BasicProperty basicProperty,
                                                final PropertyMutation propertyMutation) {
        final Map<String, Object> reportParams = new HashMap<>();
        final PropertyAckNoticeInfo ackBean = new PropertyAckNoticeInfo();
        ackBean.setUlbLogo(cityService.getCityLogoURL());
        ackBean.setMunicipalityName(cityService.getMunicipalityName());
        ackBean.setReceivedDate(DateUtils.getDefaultFormattedDate(propertyMutation.getMutationDate()));
        if (propertyMutation.getType().equalsIgnoreCase(PropertyTaxConstants.ADDTIONAL_RULE_REGISTERED_TRANSFER)) {
            ackBean.setApplicationType(PropertyTaxConstants.ALL_READY_REGISTER);
            ackBean.setTransferpropertyText("");
            ackBean.setTransferpropertyTextEnd("");
            ackBean.setNoOfDays(((PtApplicationType) entityManager.createNamedQuery(PtApplicationType.BY_CODE)
                    .setParameter("code", "REGISTERED TRANSFER").getSingleResult()).getResolutionTime().toString());
        } else if (propertyMutation.getType().equalsIgnoreCase(PropertyTaxConstants.ADDTIONAL_RULE_PARTIAL_TRANSFER)) {
            ackBean.setApplicationType(PropertyTaxConstants.PARTT);
            ackBean.setTransferpropertyText(PropertyTaxConstants.TTTEXT);
            ackBean.setTransferpropertyTextEnd(PropertyTaxConstants.TTTEXTEND);
            ackBean.setNoOfDays(((PtApplicationType) entityManager.createNamedQuery(PtApplicationType.BY_CODE)
                    .setParameter("code", "PARTIAL TRANSFER").getSingleResult()).getResolutionTime().toString());
        } else if (propertyMutation.getType().equalsIgnoreCase(PropertyTaxConstants.ADDITIONAL_RULE_FULL_TRANSFER)) {
            ackBean.setApplicationType(PropertyTaxConstants.FULLTT);
            ackBean.setTransferpropertyText(PropertyTaxConstants.TTTEXT);
            ackBean.setTransferpropertyTextEnd(PropertyTaxConstants.TTTEXTEND);
            ackBean.setNoOfDays(((PtApplicationType) entityManager.createNamedQuery(PtApplicationType.BY_CODE)
                    .setParameter("code", "FULL TRANSFER").getSingleResult()).getResolutionTime().toString());
        }
        ackBean.setApplicationNo(propertyMutation.getApplicationNo());
        ackBean.setApplicationDate(DateUtils.getDefaultFormattedDate(propertyMutation.getMutationDate()));
        ackBean.setApplicationName(propertyMutation.getFullTranfereeName());
        if (propertyMutation.getTransfereeInfos() != null && !propertyMutation.getTransfereeInfos().isEmpty()) {
            String newOwnerName = "";
            for (final PropertyMutationTransferee usr : propertyMutation.getTransfereeInfos())
                newOwnerName = newOwnerName + usr.getTransferee().getName() + ",";
            ackBean.setOwnerName(newOwnerName.substring(0, newOwnerName.length() - 1));
        }
        ackBean.setOwnerAddress(basicProperty.getAddress().toString());
        final ReportRequest reportInput = new ReportRequest("mainTransferPropertyAck", ackBean, reportParams);
        reportInput.setReportFormat(ReportFormat.PDF);
        return reportService.createReport(reportInput);
    }

    @Transactional
    public ReportOutput generateTransferNotice(final BasicProperty basicProperty,
                                               final PropertyMutation propertyMutation, final String actionType,
                                               final boolean isCorporation) {
        final PtNotice notice = noticeService.getNoticeByNoticeTypeAndApplicationNumber(NOTICE_TYPE_MUTATION_CERTIFICATE,
                propertyMutation.getApplicationNo());
        ReportOutput reportOutput = new ReportOutput();
        List<Assignment> loggedInUserAssignment;
        String loggedInUserDesignation;
        String noticeNo = null;
        if (WFLOW_ACTION_STEP_GENERATE_TRANSFER_NOTICE.equalsIgnoreCase(actionType)) {
            final FileStoreMapper fsm = notice.getFileStore();
            final File file = fileStoreService.fetch(fsm, FILESTORE_MODULE_NAME);
            byte[] bFile;
            try {
                bFile = FileUtils.readFileToByteArray(file);
            } catch (final IOException e) {
                throw new ApplicationRuntimeException("Exception while generating Mutation Certificate : " + e);
            }
            reportOutput.setReportOutputData(bFile);
            reportOutput.setReportFormat(ReportFormat.PDF);
            propertyMutation.transition().end().withNextAction(null)
                    .withOwner(propertyMutation.getCurrentState().getOwnerPosition());
            basicProperty.setUnderWorkflow(false);
            propertyService.updateIndexes(propertyMutation, propertyMutation.getType()
                    .equalsIgnoreCase(PropertyTaxConstants.ADDTIONAL_RULE_REGISTERED_TRANSFER)
                    ? NATURE_REGISTERED_TRANSFER : NATURE_FULL_TRANSFER);
        } else {
            final PropertyAckNoticeInfo noticeBean = new PropertyAckNoticeInfo();
            noticeBean.setMunicipalityName(cityService.getMunicipalityName());
            final BasicProperty basicProp = propertyMutation.getBasicProperty();
            final Map<String, Object> reportParams = new HashMap<>();
            final List<User> users = eisCommonService.getAllActiveUsersByGivenDesig(designationService
                    .getDesignationByName(COMMISSIONER_DESGN).getId());
            noticeBean.setApproverName(users.get(0).getName());
            reportParams.put("userSignature", securityUtils.getCurrentUser().getSignature() != null
                    ? new ByteArrayInputStream(securityUtils.getCurrentUser().getSignature()) : "");
            reportParams.put("isCorporation", isCorporation);
            loggedInUserAssignment = assignmentService.getAssignmentByPositionAndUserAsOnDate(
                    propertyMutation.getCurrentState().getOwnerPosition().getId(), ApplicationThreadLocals.getUserId(),
                    new Date());
            loggedInUserDesignation = !loggedInUserAssignment.isEmpty() ? loggedInUserAssignment.get(0).getDesignation().getName()
                    : "";
            if (COMMISSIONER_DESGN.equalsIgnoreCase(loggedInUserDesignation))
                reportParams.put("isCommissioner", true);
            else
                reportParams.put("isCommissioner", false);

            noticeBean.setNoticeNumber(notice != null ? notice.getNoticeNo() : "N/A");
            noticeBean.setOldOwnerName(propertyMutation.getFullTranferorName());
            noticeBean.setOldOwnerGuardianRelation(propertyMutation.getTransferorGuardianRelation());
            noticeBean.setNewOwnerName(propertyMutation.getFullTranfereeName());
            noticeBean.setNewOwnerGuardianRelation(propertyMutation.getTransfereeGuardianRelation());
            if (!MUTATIONRS_DECREE_BY_CIVIL_COURT.equalsIgnoreCase(propertyMutation.getMutationReason().getMutationName())) {
                if (propertyMutation.getDeedDate() != null)
                    noticeBean.setRegDocDate(DateUtils.getDefaultFormattedDate(propertyMutation.getDeedDate()));
                noticeBean.setRegDocNo(propertyMutation.getDeedNo());
            } else {
                if (propertyMutation.getDecreeDate() != null)
                    noticeBean.setRegDocDate(DateUtils.getDefaultFormattedDate(propertyMutation.getDecreeDate()));
                noticeBean.setRegDocNo(propertyMutation.getDecreeNumber());
            }
            noticeBean.setAssessmentNo(basicProp.getUpicNo());
            noticeBean.setApprovedDate(DateUtils.getDefaultFormattedDate(propertyMutation.getMutationDate()));
            if (basicProp.getAddress() != null) {
                final PropertyAddress address = basicProp.getAddress();
                noticeBean.setOwnerAddress(address.toString());
                if (StringUtils.isNotBlank(address.getHouseNoBldgApt()))
                    noticeBean.setDoorNo(address.getHouseNoBldgApt());
                else
                    noticeBean.setDoorNo("N/A");
            }
            final PropertyID propertyId = basicProp.getPropertyID();
            noticeBean.setLocalityName(propertyId.getLocality().getName());
            if (WFLOW_ACTION_STEP_SIGN.equals(actionType))
                noticeNo = propertyTaxNumberGenerator.generateNoticeNumber(NOTICE_TYPE_MUTATION_CERTIFICATE);
            noticeBean.setNoticeNumber(noticeNo != null ? noticeNo : " ");

            final ReportRequest reportInput = new ReportRequest(
                    PropertyTaxConstants.REPORT_TEMPLATENAME_TRANSFER_CERTIFICATE, noticeBean, reportParams);
            reportInput.setReportFormat(ReportFormat.PDF);
            reportOutput = reportService.createReport(reportInput);
            if (WFLOW_ACTION_STEP_SIGN.equals(actionType)) {
                if (notice == null)
                    noticeService.saveNotice(propertyMutation.getApplicationNo(), noticeNo,
                            NOTICE_TYPE_MUTATION_CERTIFICATE, basicProperty,
                            new ByteArrayInputStream(reportOutput.getReportOutputData()));
                else
                    noticeService.updateNotice(notice, new ByteArrayInputStream(reportOutput.getReportOutputData()));
                noticeService.getSession().flush();
            }
        }
        propertyService.updateIndexes(propertyMutation, propertyMutation.getType()
                .equalsIgnoreCase(PropertyTaxConstants.ADDTIONAL_RULE_REGISTERED_TRANSFER)
                        ? NATURE_REGISTERED_TRANSFER : NATURE_FULL_TRANSFER);
        if (Source.CITIZENPORTAL.toString().equalsIgnoreCase(propertyMutation.getSource()))
            propertyService.updatePortal(propertyMutation, propertyMutation.getType()
                    .equalsIgnoreCase(ADDTIONAL_RULE_REGISTERED_TRANSFER)
                    ? NATURE_REGISTERED_TRANSFER : NATURE_FULL_TRANSFER);
        return reportOutput;
    }

    private void createUserIfNotExist(final PropertyMutation propertyMutation,
                                      final List<PropertyMutationTransferee> transferees) {
        propertyMutation.getTransfereeInfos().clear();
        for (final PropertyMutationTransferee transferee : transferees) {
            if (transferee != null) {
                User user = null;
                /*
                 * if (null != transferee.getTransferee().getAadhaarNumber() &&
                 * !transferee.getTransferee().getAadhaarNumber().isEmpty()) { List<User> userList =
                 * userService.getUserByAadhaarNumberAndType(transferee.getTransferee().getAadhaarNumber(),
                 * transferee.getTransferee().getType()); if (userList != null && !userList.isEmpty()) for (int i = 0; i <
                 * userList.size(); i++) if
                 * (userList.get(i).getAadhaarNumber().equalsIgnoreCase(transferee.getTransferee().getAadhaarNumber()) &&
                 * userList.get(i).getMobileNumber() .equalsIgnoreCase(transferee.getTransferee().getMobileNumber()) &&
                 * userList.get(i).getName().equalsIgnoreCase(transferee.getTransferee().getName())) user = userList.get(i); }
                 * else
                 */
                if (StringUtils.isNotBlank(transferee.getTransferee().getMobileNumber())) {
                    Query qry = entityManager.createNamedQuery("USER_BY_NAMEANDMOBILENO");
                    qry.setParameter("name", transferee.getTransferee().getName());
                    qry.setParameter("mobileNumber", transferee.getTransferee().getMobileNumber());
                    qry.setParameter("gender", transferee.getTransferee().getGender());
                    if (!qry.getResultList().isEmpty())
                        user = (User) qry.getResultList().get(0);
                }
                if (user == null) {
                    final Citizen newOwner = new Citizen();
                    newOwner.setAadhaarNumber(transferee.getTransferee().getAadhaarNumber());
                    newOwner.setEmailId(transferee.getTransferee().getEmailId());
                    newOwner.setMobileNumber(transferee.getTransferee().getMobileNumber());
                    newOwner.setGender(transferee.getTransferee().getGender());
                    newOwner.setGuardian(transferee.getTransferee().getGuardian());
                    newOwner.setGuardianRelation(transferee.getTransferee().getGuardianRelation());
                    newOwner.setSalutation(transferee.getTransferee().getSalutation());
                    newOwner.setName(transferee.getTransferee().getName());
                    newOwner.setPassword("NOTSET");
                    newOwner.setUsername(propertyTaxUtil.generateUserName(transferee.getTransferee().getName()));
                    userService.createUser(newOwner);
                    transferee.setTransferee(newOwner);
                    transferee.setPropertyMutation(propertyMutation);
                    transferee.setOwnerType(UserType.CITIZEN);
                } else {
                    user.setEmailId(transferee.getTransferee().getEmailId());
                    user.setGuardian(transferee.getTransferee().getGuardian());
                    user.setGuardianRelation(transferee.getTransferee().getGuardianRelation());
                    transferee.setTransferee(user);
                    transferee.setPropertyMutation(propertyMutation);

                }
            }
            propertyMutation.addTransfereeInfos(transferee);
        }
    }

    private void processAndStoreDocument(final PropertyMutation propertyMutation, final String oldTransferReason) {
        if (StringUtils.isNotBlank(oldTransferReason)
                && Arrays
                .asList(propertyMutation.getMutationReason().getCode(), oldTransferReason)
                .contains("SUCCESSION")
                && !oldTransferReason.equals(propertyMutation.getMutationReason().getCode()))
            propertyMutation.getDocuments().clear();
        if (propertyMutation.getDocuments().isEmpty() && !propertyMutation.getDocumentsProxy().isEmpty())
            propertyMutation.setDocuments(propertyMutation.getDocumentsProxy());
        else
            updateDocuments(propertyMutation);
        saveDocuments(propertyMutation.getDocuments());
    }

    private void updateDocuments(final PropertyMutation propertyMutation) {
        for (Document document : propertyMutation.getDocuments())
            for (Document applicationDocument : propertyMutation.getDocumentsProxy())
                if (applicationDocument.getType().getId() == document.getType().getId()
                        && !applicationDocument.getUploadsFileName().isEmpty()) {
                    if (!document.getFiles().isEmpty())
                        document.getFiles().clear();
                    final FileStoreMapper fileStore = fileStoreService.store(applicationDocument.getUploads().get(0),
                            applicationDocument.getUploadsFileName().get(0),
                            applicationDocument.getUploadsContentType().get(1), FILESTORE_MODULE_NAME);
                    document.getFiles().add(fileStore);
                }
    }

    private void saveDocuments(final List<Document> documents) {
        documents.forEach(document -> {
            if (!document.getUploads().isEmpty()) {
                int fileCount = 0;
                for (final File file : document.getUploads()) {
                    if (!document.getFiles().isEmpty())
                        document.getFiles().clear();
                    final FileStoreMapper fileStore = fileStoreService.store(file,
                            document.getUploadsFileName().get(fileCount),
                            document.getUploadsContentType().get(fileCount++), FILESTORE_MODULE_NAME);
                    document.getFiles().add(fileStore);
                }
            }
            if (document.getType() != null)
                document.setType(entityManager.find(DocumentType.class, document.getType().getId()));
        });
    }

    public String generateReceipt(final PropertyMutation propertyMutation) {
        propertyTaxBillable.setBasicProperty(propertyMutation.getBasicProperty());
        propertyTaxBillable.setMutationFeePayment(Boolean.TRUE);
        propertyTaxBillable.setMutationFee(propertyMutation.getMutationFee());
        if (SecurityUtils.userAnonymouslyAuthenticated())
            propertyTaxBillable.setCollectionType(DemandConstants.COLLECTIONTYPE_ONLINE);
        else
            propertyTaxBillable.setCollectionType(DemandConstants.COLLECTIONTYPE_COUNTER);
        propertyTaxBillable.setBillType(propertyTaxUtil.getBillTypeByCode(BILLTYPE_AUTO));
        propertyTaxBillable.setCallbackForApportion(Boolean.FALSE);
        propertyTaxBillable.setMutationApplicationNo(propertyMutation.getApplicationNo());
        propertyTaxBillable.setReferenceNumber(propertyTaxNumberGenerator.generateManualBillNumber(propertyMutation
                .getBasicProperty().getPropertyID()));
        return ptBillServiceImpl.getBillXML(propertyTaxBillable);
    }

    public String getLoggedInUserDesignation() {
        final Designation designation = propertyTaxUtil.getDesignationForUser(ApplicationThreadLocals.getUserId());
        return designation.getName();
    }

    public User getLoggedInUser() {
        return securityUtils.getCurrentUser();
    }

    @Transactional
    public void updateMutationCollection(final PropertyMutation propertyMutation) {
        propertyMutationService.persist(propertyMutation);
    }

    public String getCityName() {
        return cityService.getCityByURL(ApplicationThreadLocals.getDomainName()).getName();
    }

    public Designation getUserDesigantion() {
        return propertyTaxUtil.getDesignationForUser(ApplicationThreadLocals.getUserId());
    }

    public PropertyMutation initiatePropertyTransfer(final BasicProperty basicproperty, final PropertyMutation propertyMutation,
            final HashMap<String, String> meesevaParams) {
        // passing transaction id as empty , because request id from meeseva
        initiatePropertyTransfer(basicproperty, propertyMutation,null, "");
        return propertyMutation;
    }

    /**
     * Used in REST API for initiating property transfer
     *
     * @param propertyMutation
     * @return
     */
    public PropertyMutation transitionWorkFlow(final PropertyMutation propertyMutation) {
        final DateTime currentDate = new DateTime();
        final String approverComments = "Property has been successfully forwarded.";
        final Assignment assignment = propertyService.getAssignmentsForDesignation(PropertyTaxConstants.COMMISSIONER_DESGN)
                .get(0);
        final Position pos = assignment.getPosition();
        propertyMutation.transition().start().withSenderName("anonymous user")
                .withComments(approverComments).withStateValue(PropertyTaxConstants.WF_STATE_REVENUE_OFFICER_APPROVED)
                .withDateInfo(currentDate.toDate()).withOwner(pos)
                .withNextAction(PropertyTaxConstants.WF_STATE_COMMISSIONER_APPROVAL_PENDING)
                .withNatureOfTask(NATURE_TITLE_TRANSFER);

        return propertyMutation;
    }

    /**
     * Creates PropertyMutation object for REST API
     *
     * @param assessmentNumber
     * @param mutationReasonCode
     * @param deedNo
     * @param deedDate
     * @param ownerDetailsList
     * @return
     * @throws ParseException
     */
    public NewPropertyDetails createPropertyMutation(final String assessmentNumber, final String mutationReasonCode,
                                                     final String deedNo, final String deedDate, final List<OwnerDetails> ownerDetailsList)
            throws ParseException {
        PropertyMutation propertyMutation = new PropertyMutation();
        NewPropertyDetails newPropertyDetails = null;
        BasicProperty basicProperty = getBasicPropertyByUpicNo(assessmentNumber);
        final PropertyMutationMaster mutationMaster = getPropertyTransferReasonsByCode(mutationReasonCode);
        propertyMutation.setDeedNo(deedNo);
        propertyMutation.setDeedDate(propertyService.convertStringToDate(deedDate));
        propertyMutation.setMutationReason(mutationMaster);
        propertyMutation.setBasicProperty(basicProperty);
        transitionWorkFlow(propertyMutation);
        basicPropertyService.applyAuditing(propertyMutation);
        basicProperty.getPropertyMutations().add(propertyMutation);
        basicPropertyService.applyAuditing(propertyMutation.getState());
        basicProperty.setUnderWorkflow(true);
        propertyMutation.setTransfereeInfosProxy(getTransfereesInfoList(propertyMutation, ownerDetailsList));
        createUserIfNotExist(propertyMutation, propertyMutation.getTransfereeInfosProxy());

        for (final PropertyOwnerInfo ownerInfo : basicProperty.getPropertyOwnerInfo())
            propertyMutation.getTransferorInfos().add(ownerInfo.getOwner());
        propertyMutation.setMutationDate(new Date());
        if (propertyMutation.getApplicationNo() == null)
            propertyMutation.setApplicationNo(applicationNumberGenerator.generate());

        propertyMutation = propertyMutationService.persist(propertyMutation);

        basicProperty = basicPropertyService.persist(basicProperty);
        if (null != propertyMutation) {
            newPropertyDetails = new NewPropertyDetails();
            newPropertyDetails.setApplicationNo(basicProperty.getUpicNo());
            final ErrorDetails errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_SUCCESS);
            errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_SUCCESS);
            newPropertyDetails.setErrorDetails(errorDetails);
        }
        return newPropertyDetails;
    }

    /**
     * Creates Transferee list, used in REST API
     *
     * @param propertyMutation
     * @param ownerDetailsList
     * @return
     */
    private List<PropertyMutationTransferee> getTransfereesInfoList(final PropertyMutation propertyMutation,
                                                                    final List<OwnerDetails> ownerDetailsList) {
        final List<PropertyMutationTransferee> transfereeInfoList = new ArrayList<>();
        for (final OwnerDetails ownerDetais : ownerDetailsList) {
            final PropertyMutationTransferee transfereeInfo = new PropertyMutationTransferee();
            final User owner = new User(UserType.CITIZEN);
            owner.setAadhaarNumber(ownerDetais.getAadhaarNo());
            owner.setSalutation(ownerDetais.getSalutationCode());
            owner.setName(ownerDetais.getName());
            owner.setGender(Gender.valueOf(ownerDetais.getGender()));
            owner.setMobileNumber(ownerDetais.getMobileNumber());
            owner.setEmailId(ownerDetais.getEmailId());
            owner.setGuardianRelation(ownerDetais.getGuardianRelation());
            owner.setGuardian(ownerDetais.getGuardian());
            transfereeInfo.setTransferee(owner);
            transfereeInfo.setPropertyMutation(propertyMutation);
            transfereeInfoList.add(transfereeInfo);
        }
        return transfereeInfoList;
    }

    /**
     * API to calculate mutation fee
     *
     * @param partyValue
     * @param departmentValue
     * @return MutationFee
     */
    public BigDecimal calculateMutationFee(final BigDecimal partyValue, final BigDecimal departmentValue) {
        BigDecimal mutationFee = BigDecimal.ZERO;
        BigDecimal documentValue = partyValue.compareTo(departmentValue) > 0 ? partyValue : departmentValue;

        if (documentValue.compareTo(BigDecimal.ZERO) > 0) {
            List<MutationFeeDetails> mutationFeeDetailsList = mutationFeeRepository.getMutationFee(documentValue);
            if (!mutationFeeDetailsList.isEmpty()) {
                MutationFeeDetails mutationFeeDetails = mutationFeeDetailsList.get(0);
                if (mutationFeeDetails != null) {
                    if (mutationFeeDetails.getFlatAmount() != null
                            && mutationFeeDetails.getFlatAmount().compareTo(BigDecimal.ZERO) > 0)
                        if ("N".equalsIgnoreCase(mutationFeeDetails.getIsRecursive().toString()))
                            mutationFee = mutationFeeDetails.getFlatAmount();
                        else {
                            BigDecimal excessDocValue = documentValue.subtract(mutationFeeDetails.getLowLimit())
                                    .add(BigDecimal.ONE);
                            BigDecimal multiplicationFactor = excessDocValue.divide(mutationFeeDetails.getRecursiveFactor(),
                                    BigDecimal.ROUND_CEILING);
                            mutationFee = mutationFeeDetails.getFlatAmount()
                                    .add(multiplicationFactor.multiply(mutationFeeDetails.getRecursiveAmount()));
                        }
                    if (mutationFeeDetails.getPercentage() != null
                            && mutationFeeDetails.getPercentage().compareTo(BigDecimal.ZERO) > 0
                            && mutationFeeDetails.getIsRecursive().toString().equalsIgnoreCase("N"))
                        mutationFee = documentValue.multiply(mutationFeeDetails.getPercentage())
                                .divide(PropertyTaxConstants.BIGDECIMAL_100);
                }
            }
        }
        return mutationFee.setScale(0, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * API to set Document Value (Market Value)
     *
     * @param propertyMutation Object
     * @return void
     */
    public void defineDocumentValue(final PropertyMutation propertyMutation) {
        propertyMutation.setMarketValue(
                propertyMutation.getPartyValue().compareTo(propertyMutation.getDepartmentValue()) > 0
                        ? propertyMutation.getPartyValue() : propertyMutation.getDepartmentValue());
    }

    public void updateMutationFee(final PropertyMutation propertyMutation) {
        if (propertyMutation.getMutationFee() == null
                && propertyMutation.getType().equalsIgnoreCase(ADDTIONAL_RULE_REGISTERED_TRANSFER))
            propertyMutation.setMutationFee(
                    calculateMutationFee(propertyMutation.getPartyValue(), propertyMutation.getDepartmentValue()));
    }

    public Assignment getWorkflowInitiator(final PropertyMutation propertyMutation) {
        Assignment wfInitiator;
        List<Assignment> assignment;
        if (propertyService.isEmployee(propertyMutation.getCreatedBy())
                && !ANONYMOUS_USER.equalsIgnoreCase(propertyMutation.getCreatedBy().getName())
                && !propertyService.isCitizenPortalUser(propertyMutation.getCreatedBy())) {
            if (isStateNotNull(propertyMutation))
                wfInitiator = wfInitiatorIfStateNotNull(propertyMutation);
            else
                wfInitiator = assignmentService.getPrimaryAssignmentForUser(propertyMutation.getCreatedBy().getId());
        } else if (propertyMutation.getState().getInitiatorPosition() != null) {
            assignment = assignmentService
                    .getAssignmentsForPosition(propertyMutation.getState().getInitiatorPosition().getId(), new Date());
            wfInitiator = getActiveInitiator(assignment);
            if (wfInitiator == null && !propertyMutation.getStateHistory().isEmpty())
                wfInitiator = assignmentService.getPrimaryAssignmentForPositon(
                        propertyMutation.getStateHistory().get(0).getOwnerPosition().getId());
        } else
            wfInitiator = assignmentService
                    .getAssignmentsForPosition(propertyMutation.getState().getOwnerPosition().getId()).get(0);

        return wfInitiator;
    }

    private Assignment wfInitiatorIfStateNotNull(final PropertyMutation propertyMutation) {
        List<Assignment> assignment;
        Assignment wfInitiator = null;
        final Assignment assigmnt = propertyTaxCommonUtils.getUserAssignmentByPassingPositionAndUser(
                propertyMutation.getCreatedBy(), propertyMutation.getState().getInitiatorPosition());
        if (assigmnt != null && assigmnt.getEmployee().isActive())
            wfInitiator = assigmnt;
        if (wfInitiator == null) {
            assignment = assignmentService.getAssignmentsForPosition(
                    propertyMutation.getState().getInitiatorPosition().getId(), new Date());
            wfInitiator = getActiveInitiator(assignment);
        }
        return wfInitiator;
    }

    private boolean isStateNotNull(final PropertyMutation propertyMutation) {
        return propertyMutation.getState() != null && propertyMutation.getState().getInitiatorPosition() != null;
    }

    private Assignment getActiveInitiator(final List<Assignment> assignment) {
        Assignment wfInitor = null;
        for (final Assignment assign : assignment)
            if (assign.getEmployee().isActive()) {
                wfInitor = assign;
                break;
            }
        return wfInitor;
    }

    /**
     * API to get Assignment for Third Party user based on mutation type
     *
     * @param propertyMutation Object basicProperty Object
     * @return Assignment
     */
    public Assignment getAssignmentForThirdPartyByMutationType(final PropertyMutation propertyMutation,
            final BasicProperty basicproperty) {

        if (propertyMutation.getType().equals(PropertyTaxConstants.ADDITIONAL_RULE_FULL_TRANSFER))
            return propertyTaxCommonUtils.getCommissionerAsgnForFullTransfer();
        else
            return propertyService.getMappedAssignmentForBusinessUser(basicproperty);
    }

    public void updateMutationReason(final PropertyMutation propertyMutation) {
        final String reasonForTransfer = propertyMutation.getMutationReason().getMutationName();
        if (MUTATIONRS_DECREE_BY_CIVIL_COURT.equalsIgnoreCase(reasonForTransfer)) {
            propertyMutation.setDeedDate(null);
            propertyMutation.setDeedNo(null);
        } else {
            propertyMutation.setDecreeDate(null);
            propertyMutation.setDecreeNumber(null);
            propertyMutation.setCourtName(null);
        }
    }
    
    @Transactional
    public void saveMutationAndPublishEvent(final PropertyMutation propertyMutation, final HttpServletRequest request,
            final String transactionId) {
        try {
            mutationRegistrationService.persist(propertyMutation.getMutationRegistrationDetails());
            String viewURL = format(WS_VIEW_PROPERT_BY_APP_NO_URL, WebUtils.extractRequestDomainURL(request, false),
                    propertyMutation.getApplicationNo(), APPLICATION_TYPE_TRANSFER_OF_OWNERSHIP);
            eventPublisher.publishWSEvent(transactionId,
                    TransactionStatus.SUCCESS, propertyMutation.getApplicationNo(), ApplicationStatus.INPROGRESS, viewURL,
                    "Title Transfer Application Created");
        } catch (Exception ex) {
            LOGGER.error("exception while initiating registered transfer", ex);
            eventPublisher.publishWSEvent(transactionId,
                    TransactionStatus.FAILED, propertyMutation.getApplicationNo(), null, null,
                    "Title Transfer Application Creation Failed");

        }
    }
}
