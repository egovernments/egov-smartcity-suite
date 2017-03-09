/*
l * eGov suite of products aim to improve the internal efficiency,transparency,
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
package org.egov.ptis.domain.service.transfer;

import static org.egov.ptis.constants.PropertyTaxConstants.ADDTIONAL_RULE_REGISTERED_TRANSFER;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_TRANSFER_OF_OWNERSHIP;
import static org.egov.ptis.constants.PropertyTaxConstants.COMMISSIONER_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.FILESTORE_MODULE_NAME;
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_TITLE_TRANSFER;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_MUTATION_CERTIFICATE;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_ISACTIVE;
import static org.egov.ptis.constants.PropertyTaxConstants.TRANSFER;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_GENERATE_TRANSFER_NOTICE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_SIGN;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_CLOSED;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
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
import org.egov.infra.persistence.entity.enums.Gender;
import org.egov.infra.persistence.entity.enums.UserType;
import org.egov.infra.reporting.engine.ReportConstants.FileFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.rest.client.SimpleRestClient;
import org.egov.infra.script.service.ScriptService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.ApplicationNumberGenerator;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
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
import org.egov.ptis.domain.entity.enums.TransactionType;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;
import org.egov.ptis.domain.entity.property.Document;
import org.egov.ptis.domain.entity.property.DocumentType;
import org.egov.ptis.domain.entity.property.MutationRegistrationDetails;
import org.egov.ptis.domain.entity.property.PropertyAddress;
import org.egov.ptis.domain.entity.property.PropertyID;
import org.egov.ptis.domain.entity.property.PropertyImpl;
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
import org.egov.ptis.domain.service.notice.NoticeService;
import org.egov.ptis.domain.service.property.PropertyService;
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

public class PropertyTransferService {

    @Autowired
    @Qualifier("propertyMutationService")
    private PersistenceService<PropertyMutation, Long> propertyMutationService;

    @Autowired
    @Qualifier("propertyImplService")
    private PersistenceService<PropertyImpl, Long> propertyImplService;

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
    @Qualifier("documentTypePersistenceService")
    private PersistenceService<DocumentType, Long> documentTypePersistenceService;

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

    @Transactional
    public void initiatePropertyTransfer(final BasicProperty basicProperty, final PropertyMutation propertyMutation) {
        propertyMutation.setBasicProperty(basicProperty);
        propertyMutation.setProperty(basicProperty.getActiveProperty());
        // Setting Document value
        defineDocumentValue(propertyMutation);
        for (final PropertyOwnerInfo ownerInfo : basicProperty.getPropertyOwnerInfo())
            propertyMutation.getTransferorInfos().add(ownerInfo.getOwner());
        propertyMutation.setMutationDate(new Date());
        if (propertyMutation.getApplicationNo() == null)
            propertyMutation.setApplicationNo(applicationNumberGenerator.generate());
        createUserIfNotExist(propertyMutation, propertyMutation.getTransfereeInfosProxy());
        basicProperty.getPropertyMutations().add(propertyMutation);
        basicProperty.setUnderWorkflow(true);
        processAndStoreDocument(propertyMutation.getDocuments());
        propertyService.updateIndexes(propertyMutation, APPLICATION_TYPE_TRANSFER_OF_OWNERSHIP);
        mutationRegistrationService.persist(propertyMutation.getMutationRegistrationDetails());
        basicPropertyService.persist(basicProperty);
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
        propertyService.updateIndexes(propertyMutation, APPLICATION_TYPE_TRANSFER_OF_OWNERSHIP);
        waterChargesIntegrationService.updateConsumerIndex(propertyService.loadAssessmentDetails(basicProperty));
        basicPropertyService.persist(basicProperty);
    }

    @Transactional
    public void updatePropertyTransfer(final BasicProperty basicProperty, final PropertyMutation propertyMutation) {
        processAndStoreDocument(propertyMutation.getDocuments());
        checkAllMandatoryDocumentsAttached(propertyMutation);
        updateMutationFee(propertyMutation);
        defineDocumentValue(propertyMutation);
        createUserIfNotExist(propertyMutation, propertyMutation.getTransfereeInfosProxy());
        basicProperty.setUnderWorkflow(true);
        propertyService.updateIndexes(propertyMutation, APPLICATION_TYPE_TRANSFER_OF_OWNERSHIP);
        mutationRegistrationService.persist(propertyMutation.getMutationRegistrationDetails());
        basicPropertyService.persist(basicProperty);
    }

    @Transactional
    public void viewPropertyTransfer(final BasicProperty basicProperty, final PropertyMutation propertyMutation) {
        updateMutationFee(propertyMutation);
        propertyService.updateIndexes(propertyMutation, APPLICATION_TYPE_TRANSFER_OF_OWNERSHIP);
        basicPropertyService.persist(basicProperty);
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

    public BigDecimal getWaterTaxDues(final String wtmsTaxDueChecking_REST_url, final String upicNo) {
        final HashMap<String, Object> waterTaxInfo = simpleRestClient.getRESTResponseAsMap(wtmsTaxDueChecking_REST_url);
        return waterTaxInfo.get("totalTaxDue") == null ? BigDecimal.ZERO : new BigDecimal(
                Double.valueOf((Double) waterTaxInfo.get("totalTaxDue")));
    }

    public PropertyImpl getActiveProperty(final String upicNo) {
        return propertyImplService.findByNamedQuery("getPropertyByUpicNoAndStatus", upicNo, STATUS_ISACTIVE);
    }

    public BasicPropertyImpl getBasicPropertyByUpicNo(final String upicNo) {
        return (BasicPropertyImpl) basicPropertyDAO.getBasicPropertyByPropertyID(upicNo);
    }

    public List<DocumentType> getPropertyTransferDocumentTypes() {
        return documentTypePersistenceService.findAllByNamedQuery(DocumentType.DOCUMENTTYPE_BY_TRANSACTION_TYPE,
                TransactionType.TRANSFER);
    }

    public List<PropertyMutationMaster> getPropertyTransferReasons() {
        return propertyMutationMasterDAO.getAllPropertyMutationMastersByType(TRANSFER);
    }

    public PropertyMutationMaster getPropertyTransferReasonsByCode(final String mutationCode) {
        return propertyMutationMasterDAO.getPropertyMutationMasterByCodeAndType(mutationCode, TRANSFER);
    }

    public PropertyMutation getPropertyMutationByApplicationNo(final String applicationNo) {
        return propertyMutationService.findByNamedQuery("BY_APPLICATION_NO", applicationNo);
    }

    public PropertyMutation getCurrentPropertyMutationByAssessmentNo(final String assessmentNo) {
        PropertyMutation currentPropertyMutation = null;
        final BasicProperty basicProperty = getBasicPropertyByUpicNo(assessmentNo);
        if (null != basicProperty)
            for (final PropertyMutation propertyMutation : basicProperty.getPropertyMutations())
                // Checking for mutation object which is in workflow
                if (!propertyMutation.getState().getValue().equals(WF_STATE_CLOSED)) {
                    currentPropertyMutation = propertyMutation;
                    break;
                }
        return currentPropertyMutation;
    }

    public ReportOutput generateAcknowledgement(final BasicProperty basicProperty,
            final PropertyMutation propertyMutation, final String cityName, final String cityLogo) {
        final Map<String, Object> reportParams = new HashMap<String, Object>();
        final PropertyAckNoticeInfo ackBean = new PropertyAckNoticeInfo();
        ackBean.setUlbLogo(cityLogo);
        ackBean.setMunicipalityName(cityName);
        ackBean.setReceivedDate(new SimpleDateFormat("dd/MM/yyyy").format(propertyMutation.getMutationDate()));
        if (propertyMutation.getType().equalsIgnoreCase(PropertyTaxConstants.ADDTIONAL_RULE_REGISTERED_TRANSFER)) {
            ackBean.setApplicationType(PropertyTaxConstants.ALL_READY_REGISTER);
            ackBean.setTransferpropertyText("");
            ackBean.setTransferpropertyTextEnd("");
            ackBean.setNoOfDays(ptaxApplicationTypeService.findByNamedQuery(PtApplicationType.BY_CODE, "REGISTERED TRANSFER")
                    .getResolutionTime().toString());
        } else if (propertyMutation.getType().equalsIgnoreCase(PropertyTaxConstants.ADDTIONAL_RULE_PARTIAL_TRANSFER)) {
            ackBean.setApplicationType(PropertyTaxConstants.PARTT);
            ackBean.setTransferpropertyText(PropertyTaxConstants.TTTEXT);
            ackBean.setTransferpropertyTextEnd(PropertyTaxConstants.TTTEXTEND);
            ackBean.setNoOfDays(ptaxApplicationTypeService.findByNamedQuery(PtApplicationType.BY_CODE, "PARTIAL TRANSFER")
                    .getResolutionTime().toString());
        } else if (propertyMutation.getType().equalsIgnoreCase(PropertyTaxConstants.ADDTIONAL_RULE_FULL_TRANSFER)) {
            ackBean.setApplicationType(PropertyTaxConstants.FULLTT);
            ackBean.setTransferpropertyText(PropertyTaxConstants.TTTEXT);
            ackBean.setTransferpropertyTextEnd(PropertyTaxConstants.TTTEXTEND);
            ackBean.setNoOfDays(ptaxApplicationTypeService.findByNamedQuery(PtApplicationType.BY_CODE, "FULL TRANSFER")
                    .getResolutionTime().toString());
        }
        ackBean.setApplicationNo(propertyMutation.getApplicationNo());
        ackBean.setApplicationDate(new SimpleDateFormat("dd/MM/yyyy").format(propertyMutation.getMutationDate()));
        ackBean.setApplicationName(propertyMutation.getFullTranfereeName());
        if (propertyMutation.getTransfereeInfos() != null && propertyMutation.getTransfereeInfos().size() > 0) {
            String newOwnerName = "";
            for (final PropertyMutationTransferee usr : propertyMutation.getTransfereeInfos())
                newOwnerName = newOwnerName + usr.getTransferee().getName() + ",";
            ackBean.setOwnerName(newOwnerName.substring(0, newOwnerName.length() - 1));
        }
        ackBean.setOwnerAddress(basicProperty.getAddress().toString());
        // ackBean.setNoOfDays(ptaxApplicationTypeService.findByNamedQuery(PtApplicationType.BY_CODE, TRANSFER)
        // .getResolutionTime().toString());

        final ReportRequest reportInput = new ReportRequest("mainTransferPropertyAck", ackBean, reportParams);
        reportInput.setReportFormat(FileFormat.PDF);
        return reportService.createReport(reportInput);
    }

    @Transactional
    public ReportOutput generateTransferNotice(final BasicProperty basicProperty,
            final PropertyMutation propertyMutation, final String cityName, final String cityLogo, final String actionType,
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
            reportOutput.setReportFormat(FileFormat.PDF);
            propertyMutation.transition().end();
            basicProperty.setUnderWorkflow(false);
        } else {
            final PropertyAckNoticeInfo noticeBean = new PropertyAckNoticeInfo();
            noticeBean.setMunicipalityName(cityName);
            final BasicProperty basicProp = propertyMutation.getBasicProperty();
            final Map<String, Object> reportParams = new HashMap<String, Object>();
            final List<User> users = eisCommonService.getAllActiveUsersByGivenDesig(designationService
                    .getDesignationByName(COMMISSIONER_DESGN).getId());
            noticeBean.setApproverName(users.get(0).getName());
            reportParams.put("userSignature", securityUtils.getCurrentUser().getSignature() != null
                    ? new ByteArrayInputStream(securityUtils.getCurrentUser().getSignature()) : "");
            reportParams.put("isCorporation", isCorporation);

            final User user = securityUtils.getCurrentUser();
            loggedInUserAssignment = assignmentService.getAssignmentByPositionAndUserAsOnDate(
                    propertyMutation.getCurrentState().getOwnerPosition().getId(), user.getId(), new Date());
            loggedInUserDesignation = !loggedInUserAssignment.isEmpty() ? loggedInUserAssignment.get(0).getDesignation().getName()
                    : "";
            if (COMMISSIONER_DESGN.equalsIgnoreCase(loggedInUserDesignation))
                reportParams.put("isCommissioner", true);
            else
                reportParams.put("isCommissioner", false);

            noticeBean.setNoticeNumber(notice != null ? notice.getNoticeNo() : "N/A");
            noticeBean.setOldOwnerName(propertyMutation.getFullTranferorName());
            noticeBean.setOldOwnerParentName(propertyMutation.getFullTransferorGuardianName());
            noticeBean.setNewOwnerName(propertyMutation.getFullTranfereeName());
            noticeBean.setNewOwnerGuardianRelation(propertyMutation.getTransfereeGuardianRelation());
            if (propertyMutation.getDeedDate() != null)
                noticeBean.setRegDocDate(new SimpleDateFormat("dd/MM/yyyy").format(propertyMutation.getDeedDate()));
            noticeBean.setRegDocNo(propertyMutation.getDeedNo());
            noticeBean.setAssessmentNo(basicProp.getUpicNo());
            noticeBean.setApprovedDate(new SimpleDateFormat("dd/MM/yyyy").format(propertyMutation.getMutationDate()));
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
            reportInput.setReportFormat(FileFormat.PDF);
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
        return reportOutput;
    }

    private void checkAllMandatoryDocumentsAttached(final PropertyMutation propertyMutation) {
        for (final Document document : propertyMutation.getDocuments())
            if ((document.getType().isMandatory() || document.isEnclosed()) && document.getFiles().isEmpty())
                throw new ValidationException(new ValidationError("documents",
                        "Please attach mandatory/marked enclosed documents."));
    }

    private void createUserIfNotExist(final PropertyMutation propertyMutation,
            final List<PropertyMutationTransferee> transferees) {
        propertyMutation.getTransfereeInfos().clear();
        for (final PropertyMutationTransferee transferee : transferees) {
            if (transferee != null) {
                User user = null;
                if (null != transferee.getTransferee().getAadhaarNumber()
                        && !transferee.getTransferee().getAadhaarNumber().isEmpty()) {
                    List<User> userList = new ArrayList<User>();
                    userList = userService.getUserByAadhaarNumberAndType(transferee.getTransferee().getAadhaarNumber(),
                            transferee.getTransferee().getType());
                    if (userList != null && !userList.isEmpty())
                        for (int i = 0; i < userList.size(); i++)
                            if (userList.get(i).getAadhaarNumber().equalsIgnoreCase(transferee.getTransferee().getAadhaarNumber())
                                    &&
                                    userList.get(i).getMobileNumber()
                                            .equalsIgnoreCase(transferee.getTransferee().getMobileNumber())
                                    &&
                                    userList.get(i).getName().equalsIgnoreCase(transferee.getTransferee().getName()))
                                user = userList.get(i);
                } else
                    user = (User) basicPropertyService.find(
                            "From User where name = ? and mobileNumber = ? and gender = ? ", transferee.getTransferee().getName(),
                            transferee.getTransferee().getMobileNumber(), transferee.getTransferee().getGender());
                if (user == null) {
                    if (UserType.CITIZEN.equals(transferee.getTransferee().getType())) {
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

                    }
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

    private void processAndStoreDocument(final List<Document> documents) {
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
            if (document.getId() == null || document.getType() == null)
                document.setType(documentTypePersistenceService.load(document.getType().getId(), DocumentType.class));
        });
    }

    public String generateReceipt(final PropertyMutation propertyMutation) {
        propertyTaxBillable.setBasicProperty(propertyMutation.getBasicProperty());
        propertyTaxBillable.setMutationFeePayment(Boolean.TRUE);
        propertyTaxBillable.setMutationFee(propertyMutation.getMutationFee());
        propertyTaxBillable.setCollectionType(DemandConstants.COLLECTIONTYPE_COUNTER);
        propertyTaxBillable.setCallbackForApportion(Boolean.FALSE);
        propertyTaxBillable.setMutationApplicationNo(propertyMutation.getApplicationNo());
        propertyTaxBillable.setUserId(ApplicationThreadLocals.getUserId());
        propertyTaxBillable.setReferenceNumber(propertyTaxNumberGenerator.generateManualBillNumber(propertyMutation
                .getBasicProperty().getPropertyID()));
        return ptBillServiceImpl.getBillXML(propertyTaxBillable);
    }

    public String getLoggedInUserDesignation() {
        final Designation designation = propertyTaxUtil.getDesignationForUser(securityUtils.getCurrentUser().getId());
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
        final Long userId = securityUtils.getCurrentUser().getId();
        final Designation designation = propertyTaxUtil.getDesignationForUser(userId);
        return designation;
    }

    public PropertyMutation initiatePropertyTransfer(final BasicProperty basicproperty, final PropertyMutation propertyMutation,
            final HashMap<String, String> meesevaParams) {
        initiatePropertyTransfer(basicproperty, propertyMutation);
        return propertyMutation;
    }

    /**
     * Used in REST API for initiating property transfer
     * @param propertyMutation
     * @return
     */
    public PropertyMutation transitionWorkFlow(final PropertyMutation propertyMutation) {
        final DateTime currentDate = new DateTime();
        final String approverComments = "Property has been successfully forwarded.";
        final Assignment assignment = propertyService.getAssignmentsForDesignation(PropertyTaxConstants.COMMISSIONER_DESGN)
                .get(0);
        final Position pos = assignment.getPosition();

        // TODO - sender name to be edited in future
        propertyMutation.transition().start().withSenderName("anonymous user")
                .withComments(approverComments).withStateValue(PropertyTaxConstants.WF_STATE_REVENUE_OFFICER_APPROVED)
                .withDateInfo(currentDate.toDate()).withOwner(pos)
                .withNextAction(PropertyTaxConstants.WF_STATE_COMMISSIONER_APPROVAL_PENDING)
                .withNatureOfTask(NATURE_TITLE_TRANSFER);

        return propertyMutation;
    }

    /**
     * Creates PropertyMutation object for REST API
     * @param assessmentNumber
     * @param mutationReasonCode
     * @param saleDetails
     * @param deedNo
     * @param deedDate
     * @param ownerDetailsList
     * @return
     * @throws ParseException
     */
    public NewPropertyDetails createPropertyMutation(final String assessmentNumber, final String mutationReasonCode,
            final String saleDetails, final String deedNo, final String deedDate, final List<OwnerDetails> ownerDetailsList)
            throws ParseException {
        PropertyMutation propertyMutation = new PropertyMutation();
        NewPropertyDetails newPropertyDetails = null;
        BasicProperty basicProperty = getBasicPropertyByUpicNo(assessmentNumber);
        final PropertyMutationMaster mutationMaster = getPropertyTransferReasonsByCode(mutationReasonCode);
        propertyMutation.setDeedNo(deedNo);
        propertyMutation.setDeedDate(propertyService.convertStringToDate(deedDate));
        propertyMutation.setSaleDetail(saleDetails);
        propertyMutation.setMutationReason(mutationMaster);
        propertyMutation.setBasicProperty(basicProperty);
        propertyMutation.setProperty(basicProperty.getActiveProperty());
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
     * @param propertyMutation
     * @param ownerDetailsList
     * @return
     */
    private List<PropertyMutationTransferee> getTransfereesInfoList(final PropertyMutation propertyMutation,
            final List<OwnerDetails> ownerDetailsList) {
        final List<PropertyMutationTransferee> transfereeInfoList = new ArrayList<PropertyMutationTransferee>(0);
        for (final OwnerDetails ownerDetais : ownerDetailsList) {
            final PropertyMutationTransferee transfereeInfo = new PropertyMutationTransferee();
            final User owner = new User();
            owner.setAadhaarNumber(ownerDetais.getAadhaarNo());
            owner.setSalutation(ownerDetais.getSalutationCode());
            owner.setName(ownerDetais.getName());
            owner.setGender(Gender.valueOf(ownerDetais.getGender()));
            owner.setMobileNumber(ownerDetais.getMobileNumber());
            owner.setEmailId(ownerDetais.getEmailId());
            owner.setGuardianRelation(ownerDetais.getGuardianRelation());
            owner.setGuardian(ownerDetais.getGuardian());
            owner.setType(UserType.CITIZEN);
            transfereeInfo.setTransferee(owner);
            transfereeInfo.setPropertyMutation(propertyMutation);
            transfereeInfoList.add(transfereeInfo);
        }
        return transfereeInfoList;
    }

    /**
     * API to calculate mutation fee
     * @param partyValue
     * @param departmentValue
     * @return MutationFee
     */
    public BigDecimal calculateMutationFee(final BigDecimal partyValue, final BigDecimal departmentValue) {
        BigDecimal documentValue = BigDecimal.ZERO;
        BigDecimal mutationFee = BigDecimal.ZERO;
        // Maximum among partyValue and departmentValue will be considered as the documentValue
        documentValue = partyValue.compareTo(departmentValue) > 0 ? partyValue : departmentValue;

        if (documentValue.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal excessDocValue = BigDecimal.ZERO;
            BigDecimal multiplicationFactor = BigDecimal.ZERO;
            final MutationFeeDetails mutationFeeDetails = (MutationFeeDetails) basicPropertyService.find(
                    "from MutationFeeDetails where lowLimit <= ? and (highLimit is null OR highLimit >= ?)", documentValue,
                    documentValue);
            if (mutationFeeDetails != null) {
                if (mutationFeeDetails.getFlatAmount() != null
                        && mutationFeeDetails.getFlatAmount().compareTo(BigDecimal.ZERO) > 0)
                    if (mutationFeeDetails.getIsRecursive().toString().equalsIgnoreCase("N"))
                        mutationFee = mutationFeeDetails.getFlatAmount();
                    else {
                        excessDocValue = documentValue.subtract(mutationFeeDetails.getLowLimit()).add(BigDecimal.ONE);
                        multiplicationFactor = excessDocValue.divide(mutationFeeDetails.getRecursiveFactor(),
                                BigDecimal.ROUND_CEILING);
                        mutationFee = mutationFeeDetails.getFlatAmount()
                                .add(multiplicationFactor.multiply(mutationFeeDetails.getRecursiveAmount()));
                    }
                if (mutationFeeDetails.getPercentage() != null
                        && mutationFeeDetails.getPercentage().compareTo(BigDecimal.ZERO) > 0)
                    if (mutationFeeDetails.getIsRecursive().toString().equalsIgnoreCase("N"))
                        mutationFee = documentValue.multiply(mutationFeeDetails.getPercentage())
                                .divide(PropertyTaxConstants.BIGDECIMAL_100);
            }
        }
        return mutationFee.setScale(0, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * API to set Document Value (Market Value)
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

        if (propertyService.isEmployee(propertyMutation.getCreatedBy())) {
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
                    .getPrimaryAssignmentForPositon(propertyMutation.getState().getOwnerPosition().getId());

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
     * @param propertyMutation Object basicProperty Object
     * @return Assignment
     */
    public Assignment getAssignmentForThirdPartyByMutationType(PropertyMutation propertyMutation,
            BasicProperty basicproperty, User user) {
        if (propertyService.isCscOperator(user)){
            if (propertyMutation.getType().equals(PropertyTaxConstants.ADDTIONAL_RULE_FULL_TRANSFER))
                return propertyTaxCommonUtils.getCommissionerAsgnForFullTransfer();
            else
                return propertyService.getMappedAssignmentForCscOperator(basicproperty);
        }
        else{
            if (propertyMutation.getType().equals(PropertyTaxConstants.ADDTIONAL_RULE_FULL_TRANSFER))
                return propertyTaxCommonUtils.getCommissionerAsgnForFullTransfer();
            else
                return propertyService.getUserPositionByZone(basicproperty, false);
        }

    }

}