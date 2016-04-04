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
package org.egov.ptis.service.collection;

import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_REVENUE_OFFICER_APPROVAL_PENDING;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.collection.integration.models.ReceiptAmountInfo;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.integration.TaxCollection;
import org.egov.demand.model.EgBill;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.property.PropertyMutation;
import org.egov.ptis.domain.service.transfer.PropertyTransferService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public void updateDemandDetails(final BillReceiptInfo bri) {
        final PropertyMutation propertyMutation = propertyTransferService.getPropertyMutationByApplicationNo(getEgBill(
                bri.getBillReferenceNum()).getConsumerId());
        propertyMutation.setReceiptDate(bri.getReceiptDate());
        propertyMutation.setReceiptNum(bri.getReceiptNum());
        propertyMutation.transition(true).withSenderName(propertyMutation.getState().getSenderName()).withDateInfo(new Date())
        .withOwner(propertyMutation.getState().getOwnerPosition())
                .withStateValue(PropertyTaxConstants.TRANSFER_FEE_COLLECTED)
        .withNextAction(WF_STATE_REVENUE_OFFICER_APPROVAL_PENDING);
        propertyMutationService.persist(propertyMutation);
        propertyMutationService.getSession().flush();
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String constructAdditionalInfoForReceipt(final BillReceiptInfo billReceiptInfo) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ReceiptAmountInfo receiptAmountBifurcation(final BillReceiptInfo billReceiptInfo) {
        return new ReceiptAmountInfo();
    }

}
