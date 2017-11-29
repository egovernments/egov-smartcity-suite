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
package org.egov.collection.integration.services;

import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.RemittanceInstrument;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.collection.utils.FinancialsUtil;
import org.egov.commons.Bankaccount;
import org.egov.commons.CVoucherHeader;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.model.instrument.InstrumentType;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional(readOnly = true)
@Service
public class RemittanceSchedulerService {

    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;

    @Autowired
    @Qualifier("remittanceInstrumentService")
    private PersistenceService<RemittanceInstrument, Long> remittanceInstrumentService;

    @Autowired
    private FinancialsUtil financialsUtil;

    @Autowired
    private CollectionsUtil collectionsUtil;

    @SuppressWarnings("unchecked")
    @Transactional
    public void remittanceInstrumentProcess(final String instrumentType, Integer modulo) {
        StringBuilder queryString = new StringBuilder(200);
        queryString
                .append("select rt from org.egov.collection.entity.RemittanceInstrument rt where rt.reconciled=false and rt.instrumentHeader.instrumentType.type=:instType")
                .append(" and MOD(rt.id, ").append(CollectionConstants.QUARTZ_BULKBILL_JOBS).append(") = :modulo ");
        final Query qry = persistenceService.getSession().createQuery(queryString.toString()).setMaxResults(500);
        qry.setParameter("instType", instrumentType);
        qry.setParameter("modulo", modulo);
        final List<RemittanceInstrument> reconcileList = qry.list();
        Boolean voucherTypeForChequeDDCard = false;
        if (collectionsUtil.getAppConfigValue(CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
                CollectionConstants.APPCONFIG_VALUE_REMITTANCEVOUCHERTYPEFORCHEQUEDDCARD).equals(
                CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERTYPE))
            voucherTypeForChequeDDCard = true;
        final Map<String, Object> instrumentDepositMap = financialsUtil.prepareForUpdateInstrumentDepositSQL();
        if (!reconcileList.isEmpty())
            if (instrumentType.equals(CollectionConstants.INSTRUMENTTYPE_CASH))
                for (final RemittanceInstrument remittanceInstrument : reconcileList) {
                    if (remittanceInstrument.getRemittance().getVoucherHeader().getId() != null) {
                        final Map<String, Object> cashMap = constructInstrumentMap(instrumentDepositMap,
                                remittanceInstrument.getRemittance().getBankAccount(),
                                remittanceInstrument.getInstrumentHeader(), remittanceInstrument.getRemittance()
                                        .getVoucherHeader());
                        financialsUtil.updateCashDeposit(cashMap, remittanceInstrument.getRemittance()
                                .getVoucherHeader(), remittanceInstrument.getInstrumentHeader(), remittanceInstrument
                                .getRemittance().getBankAccount());
                    }
                    remittanceInstrument.setReconciled(Boolean.TRUE);
                    remittanceInstrumentService.persist(remittanceInstrument);
                }
            else
                for (final RemittanceInstrument bankRemit : reconcileList) {
                    if (bankRemit.getRemittance().getVoucherHeader().getId() != null) {
                        final Map<String, Object> chequeMap = constructInstrumentMap(instrumentDepositMap, bankRemit
                                .getRemittance().getBankAccount(), bankRemit.getInstrumentHeader(), bankRemit
                                .getRemittance().getVoucherHeader());
                        if (voucherTypeForChequeDDCard)
                            financialsUtil.updateCheque_DD_Card_Deposit_Receipt(chequeMap);
                        else
                            financialsUtil.updateCheque_DD_Card_Deposit(chequeMap, bankRemit.getRemittance()
                                    .getVoucherHeader(), bankRemit.getInstrumentHeader(), bankRemit.getRemittance()
                                    .getBankAccount());
                    }
                    bankRemit.setReconciled(Boolean.TRUE);
                    remittanceInstrumentService.persist(bankRemit);
                }
    }

    private Map<String, Object> constructInstrumentMap(final Map<String, Object> instrumentDepositMap,
                                                       final Bankaccount bankaccount, final InstrumentHeader instrumentHeader, final CVoucherHeader voucherHeader) {
        final InstrumentType instrumentType = (InstrumentType) persistenceService.find(
                "select it from InstrumentType it,InstrumentHeader ih where " + "ih.instrumentType=it.id and ih.id=?",
                instrumentHeader.getId());
        instrumentDepositMap.put("instrumentheader", instrumentHeader.getId());
        instrumentDepositMap.put("bankaccountid", bankaccount.getId());
        instrumentDepositMap.put("instrumentamount", instrumentHeader.getInstrumentAmount());
        instrumentDepositMap.put("instrumenttype", instrumentType.getType());
        instrumentDepositMap.put("depositdate", voucherHeader.getVoucherDate());
        instrumentDepositMap.put("createdby", voucherHeader.getCreatedBy().getId());
        instrumentDepositMap.put("ispaycheque", instrumentHeader.getIsPayCheque());
        instrumentDepositMap.put("payinid", voucherHeader.getId());
        return instrumentDepositMap;
    }

}
