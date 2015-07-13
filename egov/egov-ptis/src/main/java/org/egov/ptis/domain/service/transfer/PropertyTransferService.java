/*    eGov suite of products aim to improve the internal efficiency,transparency,
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
 * 	1) All versions of this program, verbatim or modified must carry this
 * 	   Legal Notice.
 *
 * 	2) Any misrepresentation of the origin of the material is prohibited. It
 * 	   is required that all modified versions of this material be marked in
 * 	   reasonable ways as different from the original version.
 *
 * 	3) This license does not grant any rights to any user of the program
 * 	   with regards to rights under trademark law for use of the trade names
 * 	   or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org
 ******************************************************************************/
package org.egov.ptis.domain.service.transfer;

import static org.egov.dcb.bean.Payment.AMOUNT;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_ISACTIVE;
import static org.egov.ptis.constants.PropertyTaxConstants.TRANSFER;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.dcb.bean.Payment;
import org.egov.demand.model.EgBill;
import org.egov.demand.utils.DemandConstants;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.persistence.entity.enums.UserType;
import org.egov.infra.reporting.engine.ReportConstants.FileFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.rest.client.SimpleRestClient;
import org.egov.infra.script.service.ScriptService;
import org.egov.infra.utils.ApplicationNumberGenerator;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.portal.entity.Citizen;
import org.egov.ptis.client.integration.utils.CollectionHelper;
import org.egov.ptis.client.util.PropertyTaxNumberGenerator;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.bill.PropertyTaxBillable;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.dao.property.PropertyMutationMasterDAO;
import org.egov.ptis.domain.entity.enums.TransactionType;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;
import org.egov.ptis.domain.entity.property.Document;
import org.egov.ptis.domain.entity.property.DocumentType;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyMutation;
import org.egov.ptis.domain.entity.property.PropertyMutationMaster;
import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;
import org.egov.ptis.domain.entity.property.PropertySource;
import org.egov.ptis.domain.entity.property.PtApplicationType;
import org.egov.ptis.report.bean.PropertyAckNoticeInfo;
import org.hibernate.FlushMode;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

public class PropertyTransferService extends PersistenceService<PropertyMutation, Long> {
    private static final Logger LOGGER = Logger.getLogger(PropertyTransferService.class);

    @Autowired
    @Qualifier("propertyImplService")
    private PersistenceService<PropertyImpl, Long> propertyImplService;

    @Autowired
    @Qualifier("basicPropertyService")
    private PersistenceService<BasicProperty, Long> basicPropertyService;

    @Autowired
    private BasicPropertyDAO basicPropertyDAO;

    @Autowired
    private PtDemandDao ptDemandDAO;

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
    private SimpleRestClient simpleRestClient;

    @Autowired
    private ApplicationNumberGenerator applicationNumberGenerator;

    @Autowired
    private ReportService reportService;

    @Autowired
    private ScriptService scriptService;

    @Transactional
    public void initiatePropertyTransfer(final BasicProperty basicProperty, final PropertyMutation propertyMutation) {
        propertyMutation.setBasicProperty(basicProperty);
        propertyMutation.setProperty(basicProperty.getActiveProperty());
        for (final PropertyOwnerInfo ownerInfo : basicProperty.getPropertyOwnerInfo())
            propertyMutation.getTransferorInfos().add(ownerInfo.getOwner());
        propertyMutation.setMutationDate(new Date());
        propertyMutation.setApplicationNo(applicationNumberGenerator.generate());
        createUserIfNotExist(propertyMutation.getTransfereeInfos());
        basicProperty.getPropertyMutations().add(propertyMutation);
        basicProperty.setUnderWorkflow(true);
        propertyMutation.transition().start();
        processAndStoreDocument(propertyMutation.getDocuments());
        basicPropertyService.persist(basicProperty);
    }

    @Transactional
    public void approvePropertyTransfer(final BasicProperty basicProperty, final PropertyMutation propertyMutation) {
        checkAllMandatoryDocumentsAttached(propertyMutation);
        final PropertySource propertySource = basicProperty.getPropertyOwnerInfo().get(0).getSource();
        basicProperty.getPropertyOwnerInfo().clear();
        createUserIfNotExist(propertyMutation.getTransfereeInfos());
        int order = 1;
        for (final User propertyOwner : propertyMutation.getTransfereeInfos()) {
            final PropertyOwnerInfo propertyOwnerInfo = new PropertyOwnerInfo(basicProperty, propertySource, propertyOwner,
                    order++);
            basicProperty.getPropertyOwnerInfo().add(propertyOwnerInfo);
        }
        basicProperty.setUnderWorkflow(false);
        basicPropertyService.persist(basicProperty);
    }

    @Transactional
    public void deleteTransferee(final PropertyMutation propertyMutation, final Long transfereeId) {
        User userToRemove = null;
        for (final User user : propertyMutation.getTransfereeInfos())
            if (user.getId().equals(transfereeId))
                userToRemove = user;
        propertyMutation.getTransfereeInfos().remove(userToRemove);
        persist(propertyMutation);
    }

    public double calculateMutationFee(final double marketValue, final String transferReason,
            final PropertyMutation propertyMutation) {
        final int transferedInMonths = Months.monthsBetween(new LocalDate(propertyMutation.getMutationDate()).withDayOfMonth(1),
                new LocalDate(propertyMutation.getDeedDate()).withDayOfMonth(1)).getMonths();
        return (Double) scriptService.executeScript("PTIS-MUTATION-FEE-CALCULATOR", ScriptService.createContext("marketValue",
                marketValue, "transferedInMonths", transferedInMonths, "transferReason", transferReason));
    }

    public BigDecimal getWaterTaxDues(final String wtmsTaxDueChecking_REST_url, final String upicNo) {
        final HashMap<String, Object> waterTaxInfo = simpleRestClient.getRESTResponseAsMap(wtmsTaxDueChecking_REST_url);
        return waterTaxInfo.get("totalTaxDue") == null ? BigDecimal.ZERO
                : new BigDecimal(Double.valueOf((Double) waterTaxInfo.get("totalTaxDue")));
    }

    public PropertyImpl getActiveProperty(final String upicNo) {
        return propertyImplService.findByNamedQuery("getPropertyByUpicNoAndStatus", upicNo, STATUS_ISACTIVE);
    }

    public BasicPropertyImpl getBasicPropertyByUpicNo(final String upicNo) {
        return (BasicPropertyImpl) basicPropertyDAO.getBasicPropertyByPropertyID(upicNo);
    }

    public Map<String, BigDecimal> getCurrentPropertyTaxDetails(final Property propertyImpl) {
        return ptDemandDAO.getDemandCollMap(propertyImpl);
    }

    public List<DocumentType> getPropertyTransferDocumentTypes() {
        return documentTypePersistenceService.findAllByNamedQuery(DocumentType.DOCUMENTTYPE_BY_TRANSACTION_TYPE,
                TransactionType.TRANSFER);
    }

    public List<PropertyMutationMaster> getPropertyTransferReasons() {
        return propertyMutationMasterDAO.getAllPropertyMutationMastersByType(TRANSFER);
    }

    public ReportOutput generateAcknowledgement(final BasicProperty basicProperty, final PropertyMutation propertyMutation,
            final String cityName, final String cityLogo) {
        final Map<String, Object> reportParams = new HashMap<String, Object>();
        final PropertyAckNoticeInfo ackBean = new PropertyAckNoticeInfo();
        ackBean.setUlbLogo(cityLogo);
        ackBean.setMunicipalityName(cityName);
        ackBean.setReceivedDate(new SimpleDateFormat("dd/MM/yyyy").format(propertyMutation.getMutationDate()));
        ackBean.setApplicationNo(propertyMutation.getApplicationNo());
        ackBean.setApplicationDate(propertyMutation.getMutationDate());
        ackBean.setApplicationName(propertyMutation.getFullTranfereeName());
        ackBean.setOwnerName(basicProperty.getFullOwnerName());
        ackBean.setOwnerAddress(basicProperty.getAddress().toString());
        ackBean.setNoOfDays(
                ptaxApplicationTypeService.findByNamedQuery(PtApplicationType.BY_CODE, TRANSFER).getResolutionTime().toString());
        ackBean.setLoggedInUsername(userService.getUserById(EgovThreadLocals.getUserId()).getName());

        final ReportRequest reportInput = new ReportRequest("transferProperty_ack", ackBean, reportParams);
        reportInput.setReportFormat(FileFormat.PDF);
        return reportService.createReport(reportInput);
    }

    public ReportOutput generateTransferNotice(final BasicProperty basicProperty, final PropertyMutation propertyMutation) {
        final PropertyAckNoticeInfo noticeBean = new PropertyAckNoticeInfo();
        final Map<String, Object> reportParams = new HashMap<String, Object>();
        noticeBean.setOldOwnerName(propertyMutation.getFullTranferorName());
        noticeBean.setOldOwnerParentName(propertyMutation.getFullTransferorGuardianName());
        noticeBean.setNewOwnerName(propertyMutation.getFullTranfereeName());
        noticeBean.setNewOwnerParentName(propertyMutation.getFullTransfereeGuardianName());
        noticeBean.setRegDocDate(new SimpleDateFormat("dd/MM/yyyy").format(propertyMutation.getDeedDate()));
        noticeBean.setRegDocNo(propertyMutation.getDeedNo());
        noticeBean.setCurrentInstallment(PropertyTaxUtil.getCurrentInstallment().getDescription());
        final ReportRequest reportInput = new ReportRequest("transferProperty_notice", noticeBean, reportParams);
        reportInput.setReportFormat(FileFormat.PDF);
        return reportService.createReport(reportInput);
    }

    private void checkAllMandatoryDocumentsAttached(final PropertyMutation propertyMutation) {
        for (final Document document : propertyMutation.getDocuments())
            if ((document.getType().isMandatory() || document.isEnclosed()) && document.getFiles().isEmpty())
                throw new ValidationException(new ValidationError("documents", "Please attach mandatory/marked enclosed documents."));
    }

    private void createUserIfNotExist(final List<User> transferees) {
        final List<User> newOwners = new ArrayList<>();
        transferees.forEach(transferee -> {
            if (transferee.isNew()) {
                getSession().setFlushMode(FlushMode.MANUAL);
                final User user = userService.getUserByAadhaarNumberAndType(transferee.getAadhaarNumber(), transferee.getType());
                if (user == null) {
                    if (UserType.CITIZEN.equals(transferee.getType())) {
                        final Citizen newOwner = new Citizen();
                        newOwner.setAadhaarNumber(transferee.getAadhaarNumber());
                        newOwner.setEmailId(transferee.getEmailId());
                        newOwner.setMobileNumber(transferee.getMobileNumber());
                        newOwner.setGender(transferee.getGender());
                        newOwner.setGuardian(transferee.getGuardian());
                        newOwner.setGuardianRelation(transferee.getGuardianRelation());
                        newOwner.setSalutation(transferee.getSalutation());
                        newOwner.setName(transferee.getName());
                        newOwner.setPassword("NOTSET");
                        newOwner.setUsername(transferee.getMobileNumber());
                        newOwners.add(newOwner);
                    }
                } else
                    newOwners.add(user);
            } else
                newOwners.add(transferee);
        });
        getSession().setFlushMode(FlushMode.AUTO);
        transferees.clear();
        transferees.addAll(newOwners);
    }

    private void processAndStoreDocument(final List<Document> documents) {
        documents.forEach(document -> {
            if (!document.getUploads().isEmpty()) {
                int fileCount = 0;
                for (final File file : document.getUploads()) {
                    final FileStoreMapper fileStore = fileStoreService.store(file, document.getUploadsFileName().get(fileCount),
                            document.getUploadsContentType().get(fileCount++), "PTIS");
                    document.getFiles().add(fileStore);
                }
            }
            document.setType(documentTypePersistenceService.load(document.getType().getId(), DocumentType.class));
        });
    }

    public BillReceiptInfo generateMiscReceipt(final BasicProperty basicProperty, final BigDecimal amount) {
        LOGGER.debug("Inside generateMiscReceipt method, Mutation Amount: " + amount);
        final org.egov.ptis.client.integration.impl.PropertyImpl property = new org.egov.ptis.client.integration.impl.PropertyImpl();
        final PropertyTaxBillable billable = new PropertyTaxBillable();
        billable.setBasicProperty(basicProperty);
        billable.setIsMiscellaneous(Boolean.TRUE);
        billable.setMutationFee(amount);
        billable.setCollectionType(DemandConstants.COLLECTIONTYPE_COUNTER);
        billable.setCallbackForApportion(Boolean.FALSE);
        billable.setUserId(Long.valueOf(EgovThreadLocals.getUserId()));
        billable.setReferenceNumber(propertyTaxNumberGenerator
                .generateBillNumber(basicProperty.getPropertyID().getWard().getBoundaryNum().toString()));
        property.setBillable(billable);
        final EgBill bill = property.createBill();
        final CollectionHelper collHelper = new CollectionHelper(bill);
        final Payment payment = preparePayment(amount);
        return collHelper.generateMiscReceipt(payment);
    }

    private Payment preparePayment(final BigDecimal amount) {
        LOGGER.debug("Inside preparePayment method, Mutation Amount: " + amount);
        final Map<String, String> payDetailMap = new HashMap<String, String>();
        payDetailMap.put(AMOUNT, String.valueOf(amount));
        final Payment payment = Payment.create(Payment.CASH, payDetailMap);
        LOGGER.debug("Exit from preparePayment method ");
        return payment;
    }

}
