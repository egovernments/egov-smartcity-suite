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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

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
import org.egov.infra.rest.client.SimpleRestClient;
import org.egov.infra.utils.ApplicationNumberGenerator;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infstr.services.PersistenceService;
import org.egov.portal.entity.Citizen;
import org.egov.ptis.client.integration.utils.CollectionHelper;
import org.egov.ptis.client.util.PropertyTaxNumberGenerator;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class TransferOwnerService extends PersistenceService<PropertyMutation, Long> {
    private static final Logger LOGGER = Logger.getLogger(TransferOwnerService.class);

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
    private UserService userService;

    @Autowired
    private SimpleRestClient simpleRestClient;

    @Autowired
    private ApplicationNumberGenerator applicationNumberGenerator;

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
        final PropertySource propertySource = basicProperty.getPropertyOwnerInfo().get(0).getSource();
        basicProperty.getPropertyOwnerInfo().clear();
        int order = 0;
        for (final User propertyOwner : propertyMutation.getTransfereeInfos()) {
            final PropertyOwnerInfo propertyOwnerInfo = new PropertyOwnerInfo(basicProperty, propertySource, propertyOwner,
                    order++);
            basicProperty.getPropertyOwnerInfo().add(propertyOwnerInfo);
        }
        basicProperty.setUnderWorkflow(false);
        basicPropertyService.persist(basicProperty);
    }

    public BigDecimal getWaterTaxDues(final String wtmsTaxDueRESTurl, final String upicNo) {
        final HashMap<String, Object> waterTaxInfo = simpleRestClient.getRESTResponseAsMap(wtmsTaxDueRESTurl);
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

    private void createUserIfNotExist(final List<User> transferees) {
        final List<User> newOwners = new ArrayList<>();
        transferees.forEach(transferee -> {
            User user = userService.getUserByAadhaarNumberAndType(transferee.getAadhaarNumber(), transferee.getType());
            if (user == null) {
                if (UserType.CITIZEN.equals(transferee.getType())) {
                    Citizen newOwner = new Citizen();
                    newOwner.setAadhaarNumber(transferee.getAadhaarNumber());
                    newOwner.setEmailId(transferee.getEmailId());
                    newOwner.setMobileNumber(transferee.getMobileNumber());
                    newOwner.setGender(transferee.getGender());
                    newOwner.setGuardian(transferee.getGuardian());
                    newOwner.setName(transferee.getName());
                    newOwner.setPassword("NOTSET");
                    newOwner.setUsername(transferee.getMobileNumber());
                    newOwners.add(newOwner);
                }
            } else {
                newOwners.add(user);
            }
        });
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
