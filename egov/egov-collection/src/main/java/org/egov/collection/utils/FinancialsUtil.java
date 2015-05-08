/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.collection.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.billsaccounting.services.CreateVoucher;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.collection.constants.CollectionConstants;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.dao.ChartOfAccountsDAO;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.model.instrument.InstrumentType;
import org.egov.model.instrument.InstrumentVoucher;
import org.egov.services.contra.ContraService;
import org.egov.services.instrument.InstrumentService;
import org.hibernate.SQLQuery;
import org.springframework.transaction.annotation.Transactional;

/**
 * Utility class for interfacing with financials. This class should be used for
 * calling any financials APIs from erp collections.
 */
@Transactional(readOnly=true)
public class FinancialsUtil {
	private InstrumentService instrumentService;
	private ContraService contraService;
	private CreateVoucher voucherCreator;
	private static final Logger LOGGER = Logger.getLogger(FinancialsUtil.class);

	/**
	 * @param instrumentService
	 *            the Instrument Service to set
	 */
	public void setInstrumentService(InstrumentService instrumentService) {
		this.instrumentService = instrumentService;
	}

	/**
	 * Fetches instrument type object for given instrument type as string
	 * 
	 * @param type
	 *            Instrument type as string e.g. cash/cheque
	 * @return Instrument type object for given instrument type as string
	 */
	public InstrumentType getInstrumentTypeByType(String type) {
		return instrumentService.getInstrumentTypeByType(type);
	}

	/**
	 * 
	 * @param headerdetails
	 * @param accountcodedetails
	 * @param subledgerdetails
	 * @param isVoucherApproved
	 * @return
	 */
	@Transactional
	public CVoucherHeader createVoucher(Map<String, Object> headerdetails,
			List<HashMap<String, Object>> accountcodedetails, List<HashMap<String, Object>> subledgerdetails,
			Boolean receiptBulkUpload, Boolean isVoucherApproved) {
		CVoucherHeader voucherHeader = null;

		LOGGER.debug("Logs For HandHeldDevice Permance Test : Voucher Creation Started....");

		if (!receiptBulkUpload) {
			if (isVoucherApproved != null && isVoucherApproved) {
				voucherHeader = createApprovedVoucher(headerdetails, accountcodedetails, subledgerdetails);
			}
			// if the isVoucherApproved value is false, create the voucher
			// in
			// pre approved state
			else {
				voucherHeader = createPreApprovalVoucher(headerdetails, accountcodedetails, subledgerdetails);
			}
		} else {
			voucherHeader = createApprovedVoucher(headerdetails, accountcodedetails, subledgerdetails);
		}
		LOGGER.info("Logs For HandHeldDevice Permance Test : Voucher Creation Ended...");
		return voucherHeader;

	}

	/**
	 * Get the pre-approval voucher created from financials
	 * 
	 * @param headerdetails
	 * @param accountcodedetails
	 * @param subledgerdetails
	 * @return CVoucherHeader
	 */
	@Transactional
	public CVoucherHeader createPreApprovalVoucher(Map<String, Object> headerdetails,
			List<HashMap<String, Object>> accountcodedetails, List<HashMap<String, Object>> subledgerdetails)
			throws EGOVRuntimeException {
		CVoucherHeader voucherHeaders = null;
		try {
			if (headerdetails instanceof HashMap) {
				voucherHeaders = voucherCreator.createPreApprovedVoucher((HashMap<String, Object>) headerdetails,
						accountcodedetails, subledgerdetails);
			}
		} catch (EGOVRuntimeException e) {
			LOGGER.error("Exception while creating voucher!", e);
			throw e;
		}
		return voucherHeaders;
	}

	@Transactional
	public CVoucherHeader createApprovedVoucher(Map<String, Object> headerdetails,
			List<HashMap<String, Object>> accountcodedetails, List<HashMap<String, Object>> subledgerdetails) {

		CVoucherHeader voucherHeaders = null;
		try {
			if (headerdetails instanceof HashMap) {

				// fetch from eg_modules once have master data in place
				headerdetails.put(VoucherConstant.MODULEID, "10");
				voucherHeaders = voucherCreator.createVoucher((HashMap<String, Object>) headerdetails,
						accountcodedetails, subledgerdetails);
			}
		} catch (EGOVRuntimeException e) {
			LOGGER.error("Exception while creating voucher!", e);
			throw e;
		}
		return voucherHeaders;

	}

	/**
	 * Get the reversal voucher for the voucher header
	 * 
	 * @param paramList
	 * @return CVoucherHeader
	 */
	public CVoucherHeader getReversalVoucher(List<HashMap<String, Object>> paramList) {
		CVoucherHeader voucherHeaders = null;
		try {
			voucherHeaders = voucherCreator.reverseVoucher(paramList);
		} catch (EGOVRuntimeException re) {
			LOGGER.error("Runtime Exception while creating reversal voucher!", re);
			throw re;
		} catch (Exception e) {
			LOGGER.error("Exception while creating reversal voucher!", e);
			throw new EGOVRuntimeException("Exception while creating reversal voucher!", e);
		}
		return voucherHeaders;
	}

	/**
	 * Update instrument type and return list of InstrumentVoucher
	 * 
	 * @param paramList
	 * @return
	 */
	@Transactional
	public List<InstrumentVoucher> updateInstrument(List<Map<String, Object>> paramList) {
		List<InstrumentVoucher> instrumentVoucherList = instrumentService.updateInstrumentVoucherReference(paramList);
		return instrumentVoucherList;
	}

	/**
	 * Create Instrument Header for list of HashMap of instrument header
	 * properties
	 * 
	 * @param paramList
	 * @return List of InstrumentHeader
	 */
	public List<InstrumentHeader> createInstrument(List<Map<String, Object>> paramList) {
		List<InstrumentHeader> instrumentHeaderList = instrumentService.addToInstrument(paramList);
		return instrumentHeaderList;
	}

	/**
	 * Update Cheque/DD/Card Instrument Status after creating Bank Remittance
	 * Voucher(if the Bank Remittance voucher type is Contra)
	 * 
	 * @param payInId
	 * @param toBankaccountGlcode
	 * @param instrumentHeader
	 */
	@Transactional
	public void updateCheque_DD_Card_Deposit(Long payInId, String toBankaccountGlcode, InstrumentHeader instrumentHeader,Map<String, Object> instrumentMap) {
		contraService.updateCheque_DD_Card_Deposit(payInId, toBankaccountGlcode, instrumentHeader,instrumentMap);
	}

	/**
	 * Update Cheque/DD/Card Instrument Status after creating Bank Remittance
	 * Voucher(if the Bank Remittance voucher type is Receipt)
	 * 
	 * @param receiptId
	 * @param toBankaccountGlcode
	 * @param instrumentHeader
	 */
	@Transactional
	public void updateCheque_DD_Card_Deposit_Receipt(Long receiptId, String toBankaccountGlcode,
			InstrumentHeader instrumentHeader,Map<String, Object> instrumentMap) {
		contraService.updateCheque_DD_Card_Deposit_Receipt(receiptId, toBankaccountGlcode, instrumentHeader,instrumentMap);
	}

	/**
	 * Update Cash Instrument Status after creating Pay in Slip Voucher
	 * 
	 * @param payInId
	 * @param toBankaccountGlcode
	 * @param instrumentHeader
	 */
	@Transactional
	public void updateCashDeposit(Long payInId, String toBankaccountGlcode, InstrumentHeader instrumentHeader,Map<String, Object> instrumentMap) {
		contraService.updateCashDeposit(payInId, toBankaccountGlcode, instrumentHeader,instrumentMap);
	}

	/**
	 * @return the contraService
	 */
	public ContraService getContraService() {
		return contraService;
	}

	/**
	 * @param contraService
	 *            the contraService to set
	 */
	public void setContraService(ContraService contraService) {
		this.contraService = contraService;
	}

	/**
	 * @param voucherCreator
	 *            the Voucher Creator to set
	 */
	public void setVoucherCreator(CreateVoucher voucherCreator) {
		this.voucherCreator = voucherCreator;
	}

	/**
	 * Checks whether given account is a revenue account (cash/cheque in hand)
	 * 
	 * @param coa
	 *            the account object
	 * @return true if the account is a revenue account, else false
	 */
	@SuppressWarnings("unchecked")
	public static boolean isRevenueAccountHead(CChartOfAccounts coa, List<CChartOfAccounts> bankCOAList) {
		String purposeId = coa.getPurposeId();
		
		// In case of bank payment, to check if the chartofaccounts exist in the
		// list of chartofacccounts mapped to bankaccounts.
		if (bankCOAList.contains(coa)) {
			return true;
		}
		if (purposeId != null) {
			try {
				SQLQuery query = HibernateUtil.getCurrentSession().createSQLQuery(
						"SELECT NAME FROM EGF_ACCOUNTCODE_PURPOSE WHERE ID = " + purposeId);
				List<String> purposeNames = (List<String>) query.list();
				if (purposeNames != null && purposeNames.size() == 1) {
					String purposeName = purposeNames.get(0);
					if (purposeName.equals(CollectionConstants.PURPOSE_NAME_CASH_IN_HAND)
							|| purposeName.equals(CollectionConstants.PURPOSE_NAME_CHEQUE_IN_HAND)
							|| purposeName.equals(CollectionConstants.PURPOSE_NAME_BANK_CODES)
							|| purposeName.equals(CollectionConstants.PURPOSE_NAME_CREDIT_CARD)
							|| purposeName.equals(CollectionConstants.PURPOSE_NAME_ATM_ACCOUNTCODE)
							|| purposeName.equals(CollectionConstants.PURPOSE_NAME_INTERUNITACCOUNT)) {
						return true;
					}
				}
			} catch (Exception e) {
				throw new EGOVRuntimeException("Exception in fetching purpose name for id [" + purposeId + "]", e);
			}
		}
		return false;
	}
	
	/**
	 * This API return list of ChartOfAccount mapped with bank accounts
	 * @return List of CChartOfAccounts
	 */
	public static List<CChartOfAccounts> getBankChartofAccountCodeList(){
		ChartOfAccountsDAO chartOfAccoutsDAO = new ChartOfAccountsHibernateDAO(CChartOfAccounts.class,
				HibernateUtil.getCurrentSession());
		return chartOfAccoutsDAO.getBankChartofAccountCodeList();
	}
	
	public Map<String, Object> prepareForUpdateInstrumentDepositSQL() {
		return contraService.prepareForUpdateInstrumentDepositSQL();
	}
}