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
package org.egov.collection.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.billsaccounting.services.CreateVoucher;
import org.egov.collection.constants.CollectionConstants;
import org.egov.commons.Bankaccount;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.model.instrument.InstrumentType;
import org.egov.model.instrument.InstrumentVoucher;
import org.egov.services.contra.ContraService;
import org.egov.services.instrument.InstrumentService;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

/**
 * Utility class for interfacing with financials. This class should be used for
 * calling any financials APIs from erp collections.
 */
public class FinancialsUtil {
    private InstrumentService instrumentService;
    public PersistenceService<InstrumentHeader, Long> instrumentHeaderService;
    @Autowired
    @Qualifier("contraService")
    private ContraService contraService;
    @Autowired
    private CreateVoucher createVoucher;
    @Autowired
    private FinancialsVoucherUtil financialsVoucherUtil;
    @Autowired
    private ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO;
    private static final Logger LOGGER = Logger.getLogger(FinancialsUtil.class);

    /**
     * @param instrumentService
     *            the Instrument Service to set
     */
    public void setInstrumentService(final InstrumentService instrumentService) {
        this.instrumentService = instrumentService;
    }

    /**
     * Fetches instrument type object for given instrument type as string
     *
     * @param type
     *            Instrument type as string e.g. cash/cheque
     * @return Instrument type object for given instrument type as string
     */
    public InstrumentType getInstrumentTypeByType(final String type) {
        return instrumentService.getInstrumentTypeByType(type);
    }

    @Transactional
    public CVoucherHeader createRemittanceVoucher(final HashMap<String, Object> headerdetails,
            final List<HashMap<String, Object>> accountCodeList, final List<HashMap<String, Object>> subledgerList) {
        CVoucherHeader voucherHeaderCash = new CVoucherHeader();
        try {
            voucherHeaderCash = financialsVoucherUtil.createApprovedVoucher(headerdetails, accountCodeList,
                    subledgerList);
        } catch (final Exception e) {
            LOGGER.error("Error in createBankRemittance createPreApprovalVoucher when cash amount>0");
            throw new ApplicationRuntimeException(
                    "Error in createBankRemittance createPreApprovalVoucher when cash amount>0", e);
        }
        return voucherHeaderCash;
    }

    /**
     * @param headerdetails
     * @param accountcodedetails
     * @param subledgerdetails
     * @param isVoucherApproved
     * @return
     */

    public CVoucherHeader createVoucher(final Map<String, Object> headerdetails,
            final List<HashMap<String, Object>> accountcodedetails,
            final List<HashMap<String, Object>> subledgerdetails, final Boolean isVoucherApproved) {
        CVoucherHeader voucherHeader;

        LOGGER.debug("Logs For HandHeldDevice Permance Test : Voucher Creation Started....");

        if (isVoucherApproved != null && isVoucherApproved)
            voucherHeader = financialsVoucherUtil.createApprovedVoucher(headerdetails, accountcodedetails,
                    subledgerdetails);
        else
            voucherHeader = financialsVoucherUtil.createPreApprovalVoucher(headerdetails, accountcodedetails,
                    subledgerdetails);
        LOGGER.info("Logs For HandHeldDevice Permance Test : Voucher Creation Ended...");
        return voucherHeader;
    }

    /**
     * Get the reversal voucher for the voucher header
     *
     * @param paramList
     * @return CVoucherHeader
     */
    public CVoucherHeader getReversalVoucher(final List<HashMap<String, Object>> paramList) {
        CVoucherHeader voucherHeaders = null;
        try {
            voucherHeaders = createVoucher.reverseVoucher(paramList);
        } catch (final ApplicationRuntimeException re) {
            LOGGER.error("Runtime Exception while creating reversal voucher!", re);
            throw re;
        } catch (final Exception e) {
            LOGGER.error("Exception while creating reversal voucher!", e);
            throw new ApplicationRuntimeException("Exception while creating reversal voucher!", e);
        }
        return voucherHeaders;
    }

    /**
     * Update instrument type and return list of InstrumentVoucher
     *
     * @param paramList
     * @return
     */

    public List<InstrumentVoucher> updateInstrumentVoucher(final List<Map<String, Object>> paramList) {
        return instrumentService.updateInstrumentVoucherReference(paramList);
    }

    /**
     * Create Instrument Header for list of HashMap of instrument header
     * properties
     *
     * @param paramList
     * @return List of InstrumentHeader
     */
    public List<InstrumentHeader> createInstrument(final List<Map<String, Object>> paramList) {
        return instrumentService.addToInstrument(paramList);
    }

    /**
     * Update Cheque/DD/Card Instrument Status after creating Bank Remittance
     * Voucher(if the Bank Remittance voucher type is Contra)
     *
     * @param Map
     *            containing Instrument and PayInSlip voucher information
     */

    @Transactional
    public void updateCheque_DD_Card_Deposit(final Map<String, Object> instrumentMap) {
        contraService.updateCheque_DD_Card_Deposit(instrumentMap);
    }

    @Transactional
    public void updateCheque_DD_Card_Deposit(final Map instrumentMap, final CVoucherHeader cVoucherHeader,
            final InstrumentHeader instrumentHeader, final Bankaccount bankaccount) {
        contraService.updateCheque_DD_Card_Deposit(instrumentMap, cVoucherHeader, instrumentHeader, bankaccount);
    }

    /**
     * Update Cheque/DD/Card Instrument Status after creating Bank Remittance
     * Voucher(if the Bank Remittance voucher type is Receipt)
     *
     * @param Map
     *            containing Instrument and PayInSlip voucher information
     */

    @Transactional
    public void updateCheque_DD_Card_Deposit_Receipt(final Map<String, Object> instrumentMap) {
        contraService.updateCheque_DD_Card_Deposit_Receipt(instrumentMap);
    }

    /**
     * Update Cash Instrument Status after creating Pay in Slip Voucher
     *
     * @param Map
     *            containing Instrument and PayInSlip voucher information
     */
    @Deprecated
    @Transactional
    public void updateCashDeposit(final Map<String, Object> instrumentMap) {
        contraService.updateCashDeposit(instrumentMap);
    }

    @Transactional
    public void updateCashDeposit(final Map<String, Object> instrumentMap, final CVoucherHeader cVoucherHeader,
            final InstrumentHeader instrumentHeader, final Bankaccount bankaccount) {
        contraService.updateCashDeposit(instrumentMap, cVoucherHeader, instrumentHeader, bankaccount);
    }

    /**
     * @param contraService
     *            the contraService to set
     */
    public void setContraService(final ContraService contraService) {
        this.contraService = contraService;
    }

    /**
     * Checks whether given account is a revenue account (cash/cheque in hand)
     *
     * @param coa
     *            the account object
     * @return true if the account is a revenue account, else false
     */
    @SuppressWarnings("unchecked")
    public static boolean isRevenueAccountHead(final CChartOfAccounts coa, final List<CChartOfAccounts> bankCOAList,
            final PersistenceService persistenceService) {
        final Long purposeId = coa.getPurposeId();

        // In case of bank payment, to check if the chartofaccounts exist in the
        // list of chartofacccounts mapped to bankaccounts.
        if (bankCOAList.contains(coa))
            return true;
        if (purposeId != null)
            try {
                final SQLQuery query = persistenceService.getSession().createSQLQuery(
                        "SELECT NAME FROM EGF_ACCOUNTCODE_PURPOSE WHERE ID = " + purposeId);
                final List<String> purposeNames = query.list();
                if (purposeNames != null && purposeNames.size() == 1) {
                    final String purposeName = purposeNames.get(0);
                    if (purposeName.equals(CollectionConstants.PURPOSE_NAME_CASH_IN_HAND)
                            || purposeName.equals(CollectionConstants.PURPOSE_NAME_CHEQUE_IN_HAND)
                            || purposeName.equals(CollectionConstants.PURPOSE_NAME_CASH_IN_TRANSIT)
                            || purposeName.equals(CollectionConstants.PURPOSE_NAME_CREDIT_CARD)
                            || purposeName.equals(CollectionConstants.PURPOSE_NAME_ATM_ACCOUNTCODE)
                            || purposeName.equals(CollectionConstants.PURPOSE_NAME_INTERUNITACCOUNT))
                        return true;
                }
            } catch (final Exception e) {
                throw new ApplicationRuntimeException("Exception in fetching purpose name for id [" + purposeId + "]",
                        e);
            }
        return false;
    }

    @Transactional
    public void updateInstrumentHeader(final List<InstrumentHeader> instrumentHeaderList, final EgwStatus status,
            final Bankaccount depositedBankAccount) {
        for (final InstrumentHeader iHeader : instrumentHeaderList)
            instrumentHeaderService.persist(updateInstrumentHeaderStatus(iHeader, status, depositedBankAccount));

    }

    public InstrumentHeader updateInstrumentHeaderStatus(final InstrumentHeader instrumentHeaderObj,
            final EgwStatus status, final Bankaccount depositedBankAccount) {
        instrumentHeaderObj.setStatusId(status);
        instrumentHeaderObj.setBankAccountId(depositedBankAccount);
        return instrumentHeaderObj;

    }

    @Transactional
    public void updateInstrumentHeader(final InstrumentHeader instrumentHeader) {
        instrumentHeaderService.persist(instrumentHeader);
    }

    /**
     * This API return list of ChartOfAccount mapped with bank accounts
     *
     * @return List of CChartOfAccounts
     */
    public List<CChartOfAccounts> getBankChartofAccountCodeList() {
        return chartOfAccountsHibernateDAO.getBankChartofAccountCodeList();
    }

    public Map<String, Object> prepareForUpdateInstrumentDepositSQL() {
        return contraService.prepareForUpdateInstrumentDepositSQL();
    }

    public void setInstrumentHeaderService(final PersistenceService<InstrumentHeader, Long> instrumentHeaderService) {
        this.instrumentHeaderService = instrumentHeaderService;
    }

}