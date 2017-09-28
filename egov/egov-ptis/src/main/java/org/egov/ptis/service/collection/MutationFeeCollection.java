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
package org.egov.ptis.service.collection;

import static org.egov.ptis.constants.PropertyTaxConstants.ADDTIONAL_RULE_FULL_TRANSFER;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;
import static org.egov.ptis.constants.PropertyTaxConstants.TRANSFER_FEE_COLLECTED;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_CLOSED;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.collection.integration.models.ReceiptAmountInfo;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.integration.TaxCollection;
import org.egov.demand.model.EgBill;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.domain.entity.property.PropertyMutation;
import org.egov.ptis.domain.service.transfer.PropertyTransferService;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

public class MutationFeeCollection extends TaxCollection {

    @Autowired
    private ModuleService moduleDao;

    @Autowired
    private PropertyTransferService propertyTransferService;

    @Autowired
    private PersistenceService<PropertyMutation, Long> propertyMutationService;

    @Autowired
    private EgBillDao egBillDAO;

    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<PropertyMutation> transferWorkflowService;
    
    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;
    
    private static final Logger LOGGER = Logger.getLogger(MutationFeeCollection.class);

    @Override
    @Transactional
    public void updateDemandDetails(final BillReceiptInfo bri) {
        final PropertyMutation propertyMutation = propertyTransferService.getPropertyMutationByApplicationNo(getEgBill(
                bri.getBillReferenceNum()).getConsumerId());
        if (bri.getEvent().equals(EVENT_RECEIPT_CREATED)) {
            if (propertyMutation != null && propertyMutation.getReceiptDate() == null
                    && propertyMutation.getReceiptNum() == null) {
                propertyMutation.setReceiptDate(bri.getReceiptDate());
                propertyMutation.setReceiptNum(bri.getReceiptNum());
            } else if (propertyMutation != null && propertyMutation.getReceiptDate() != null
                    && !propertyMutation.getReceiptNum().isEmpty()
                    && propertyTaxCommonUtils.isReceiptCanceled(propertyMutation.getReceiptNum())) {
                propertyMutation.setReceiptDate(bri.getReceiptDate());
                propertyMutation.setReceiptNum(bri.getReceiptNum());
            } else {
                LOGGER.error(
                        "Mutation fee is already paid with  receipt : " + propertyMutation.getReceiptNum() + " for assessment : "
                                + propertyMutation.getBasicProperty().getUpicNo());
                throw new ValidationException();
            }
        }
        if (!WF_STATE_CLOSED.equalsIgnoreCase(propertyMutation.getCurrentState().getValue())) {
            updateTransitionForFullTransfer(bri, propertyMutation);
        } else {
            LOGGER.error("Mutation workflow is already closed for the receipt : " + propertyMutation.getReceiptNum()
                    + " payed for assessment : "
                    + propertyMutation.getBasicProperty().getUpicNo());
            throw new ValidationException();
        }
        propertyMutationService.persist(propertyMutation);
        propertyMutationService.getSession().flush();
    }

    private void updateTransitionForFullTransfer(final BillReceiptInfo bri, final PropertyMutation propertyMutation) {
        if (bri.getEvent().equals(EVENT_RECEIPT_CREATED)
                && propertyMutation.getType().equalsIgnoreCase(ADDTIONAL_RULE_FULL_TRANSFER)
                && !(TRANSFER_FEE_COLLECTED).equalsIgnoreCase(propertyMutation.getCurrentState().getValue())) {
            final WorkFlowMatrix wFMatrix = transferWorkflowService.getWfMatrix(propertyMutation.getStateType(),
                    null, null, propertyMutation.getType(), propertyMutation.getCurrentState().getValue(), null);
            propertyMutation.transition().progressWithStateCopy().withSenderName(propertyMutation.getState().getSenderName())
                    .withDateInfo(new Date())
                    .withOwner(propertyMutation.getState().getOwnerPosition())
                    .withStateValue(TRANSFER_FEE_COLLECTED)
                    .withNextAction(wFMatrix.getNextAction());
        }
    }

    @Override
    protected Module module() {
        return moduleDao.getModuleByName(PTMODULENAME);
    }

    private EgBill getEgBill(final String billRefNo) {
        return egBillDAO.findById(Long.valueOf(billRefNo), false);
    }

    @Override
    public List<ReceiptDetail> reconstructReceiptDetail(final String billReferenceNumber, final BigDecimal actualAmountPaid,
            final List<ReceiptDetail> receiptDetailList) {

        return null;
    }

    @Override
    public String constructAdditionalInfoForReceipt(final BillReceiptInfo billReceiptInfo) {

        return null;
    }

    @Override
    public ReceiptAmountInfo receiptAmountBifurcation(final BillReceiptInfo billReceiptInfo) {
        return new ReceiptAmountInfo();
    }

}
