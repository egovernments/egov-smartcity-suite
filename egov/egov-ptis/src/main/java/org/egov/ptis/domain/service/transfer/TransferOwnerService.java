/*******************************************************************************
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
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_ISACTIVE;
import static org.egov.ptis.constants.PropertyTaxConstants.TRANSFER;

import java.io.File;
import java.math.BigDecimal;
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
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infstr.services.PersistenceService;
import org.egov.portal.entity.Citizen;
import org.egov.ptis.client.integration.utils.CollectionHelper;
import org.egov.ptis.client.util.PropertyTaxNumberGenerator;
import org.egov.ptis.domain.bill.PropertyTaxBillable;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.dao.property.PropertyMutationMasterDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;
import org.egov.ptis.domain.entity.property.Document;
import org.egov.ptis.domain.entity.property.DocumentType;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyMutation;
import org.egov.ptis.domain.entity.property.PropertyMutationMaster;
import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;
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
    
    @Transactional
    public void initiatePropertyTransfer(PropertyMutation propertyMutation, String upicNo) {
        BasicProperty basicProperty = getBasicPropertyByUpicNo(upicNo);
        propertyMutation.setBasicProperty(basicProperty);
        propertyMutation.setProperty(basicProperty.getActiveProperty());
        propertyMutation.getTransferorInfos().addAll(basicProperty.getPropertyOwnerInfo());
        createUserIfNotExist(propertyMutation.getTransfereeInfos());
        basicProperty.getPropertyMutations().add(propertyMutation);
        basicProperty.setUnderWorkflow(true);
        //propertyMutation.transition().start();
        //basicProperty.getPropertyOwnerInfo().clear();
        processAndStoreDocument(propertyMutation.getDocuments());
        basicPropertyService.persist(basicProperty);
    }
    

    public PropertyImpl getActiveProperty(String upicNo) {
        return propertyImplService.findByNamedQuery("getPropertyByUpicNoAndStatus", upicNo, STATUS_ISACTIVE);
    }
    
    public BasicPropertyImpl getBasicPropertyByUpicNo(String upicNo) {
        return (BasicPropertyImpl)basicPropertyDAO.getBasicPropertyByPropertyID(upicNo);
    }
    
    public String getCurrentPropertyTax(Property propertyImpl) {
        return ptDemandDAO.getDemandCollMap(propertyImpl).get(CURR_DMD_STR).toString();
    }
    
    public List<DocumentType> getPropertyTransferDocumentTypes() {
        return documentTypePersistenceService.findAllByNamedQuery(DocumentType.DOCUMENTTYPE_BY_MODULE_AND_SUBMODULE, "PTIS", TRANSFER);
    }
    
    public List<PropertyMutationMaster> getPropertyTransferReasons() {
        return propertyMutationMasterDAO.getAllPropertyMutationMastersByType(TRANSFER);
    }
    
    private void createUserIfNotExist(List<PropertyOwnerInfo> transferees) {
        transferees.forEach(transferee -> {
            User user = userService.getUserByAadhaarNumberAndType(transferee.getOwner().getAadhaarNumber(), transferee.getOwnerType());
            if (user == null) {
                User tmpUser = transferee.getOwner();
                if (UserType.CITIZEN.equals(transferee.getOwnerType())) {
                    user = new Citizen();
                    user.setAadhaarNumber(tmpUser.getAadhaarNumber());
                    user.setEmailId(tmpUser.getEmailId());
                    user.setMobileNumber(tmpUser.getMobileNumber());
                    user.setGender(tmpUser.getGender());
                    user.setName(tmpUser.getName());
                    user.setPassword("NOTSET");
                    user.setUsername(user.getMobileNumber());
                }
            } 
            transferee.setOwner(user);
        });
    }
    
    private void processAndStoreDocument(List<Document> documents) {
        documents.forEach(document -> {
            if (!document.getUploads().isEmpty()) {
                int fileCount = 0;
                for (File file : document.getUploads()) {
                        FileStoreMapper fileStore = fileStoreService
                                        .store(file, document.getUploadsFileName().get(fileCount),
                                                document.getUploadsContentType().get(fileCount++), "PTIS");
                        document.getFiles().add(fileStore);
                }
            }
            document.setType(documentTypePersistenceService.load(document.getType().getId(), DocumentType.class));
        });
    }
    
    public BillReceiptInfo generateMiscReceipt(BasicProperty basicProperty, BigDecimal amount) {
        LOGGER.debug("Inside generateMiscReceipt method, Mutation Amount: " + amount);
        org.egov.ptis.client.integration.impl.PropertyImpl property = new org.egov.ptis.client.integration.impl.PropertyImpl();
        PropertyTaxBillable billable = new PropertyTaxBillable();
        billable.setBasicProperty(basicProperty);
        billable.setIsMiscellaneous(Boolean.TRUE);
        billable.setMutationFee(amount);
        billable.setCollectionType(DemandConstants.COLLECTIONTYPE_COUNTER);
        billable.setCallbackForApportion(Boolean.FALSE);
        billable.setUserId(Long.valueOf(EgovThreadLocals.getUserId()));
        billable.setReferenceNumber(propertyTaxNumberGenerator
                .generateBillNumber(basicProperty.getPropertyID().getWard().getBoundaryNum().toString()));
        property.setBillable(billable);
        EgBill bill = property.createBill();
        CollectionHelper collHelper = new CollectionHelper(bill);
        Payment payment = preparePayment(amount);
        return collHelper.generateMiscReceipt(payment);
    }

    private Payment preparePayment(BigDecimal amount) {
        LOGGER.debug("Inside preparePayment method, Mutation Amount: " + amount);
        Map<String, String> payDetailMap = new HashMap<String, String>();
        payDetailMap.put(AMOUNT, String.valueOf(amount));
        Payment payment = Payment.create(Payment.CASH, payDetailMap);
        LOGGER.debug("Exit from preparePayment method ");
        return payment;
    }

}
