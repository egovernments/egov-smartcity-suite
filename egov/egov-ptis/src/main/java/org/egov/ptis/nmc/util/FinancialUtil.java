package org.egov.ptis.nmc.util;

import static org.egov.billsaccounting.services.VoucherConstant.VOUCHERNUMBER;
import static org.egov.ptis.constants.PropertyTaxConstants.PTIS_EG_MODULES_ID;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.ARREARS_DEMAND;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.CURRENT_DEMAND;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEFAULT_FUNCTIONARY_CODE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEFAULT_FUND_CODE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEPT_CODE_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.GLCODEMAP_FOR_ARREARTAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.GLCODEMAP_FOR_CURRENTTAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.GLCODEMAP_FOR_TAX_PAYABLE;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.billsaccounting.services.CreateVoucher;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.Installment;
import org.egov.commons.dao.CommonsDaoFactory;
import org.egov.commons.dao.InstallmentDao;
import org.egov.infstr.commons.Module;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.commons.dao.ModuleDao;
import org.egov.ptis.nmc.constants.NMCPTISConstants;

/**
 * @author subhash
 * 
 *         Provides API to create Voucher in financials whenever there is
 *         change(increase/decrease) in demand in PTIS.
 * 
 *         Use this API to create Voucher in the case of demand change ( either
 *         increment or decrement )
 */

public class FinancialUtil {
	private static final Logger LOGGER = Logger.getLogger(FinancialUtil.class);

	private static final String VOUCHERNAME = "JVoucher";

	private static final String VOUCHERTYPE = "Journal Voucher";

	private static final String URL_FOR_DCB = "/ptis/view/viewDCBProperty!displayPropInfo.action?propertyId=";

	/**
	 * This method creates a Voucher
	 * 
	 * @param indexNum
	 *            Property id for which the voucher is creating.
	 * @param amounts
	 *            Map of Installment and reason wise demand map demand map --
	 *            (key-demand reason, val-respective demand)
	 * @param transaction
	 *            Reason for voucher creation ( Property creation or
	 *            modification etc )
	 * @throws IOException
	 * 
	 */
	public void createVoucher(String indexNum, Map<Installment, Map<String, BigDecimal>> amounts, String transaction) {

		LOGGER.info("createVoucher: IndexNumber==>" + indexNum + " amounts==>" + amounts + "actionName==>"
				+ transaction);

		Map<String, Map<String, BigDecimal>> resultMap = prepareDemandForGlcode(amounts);
		Map<String, BigDecimal> arrearsDemandMap = resultMap.get(ARREARS_DEMAND);
		Map<String, BigDecimal> currentDemandMap = resultMap.get(CURRENT_DEMAND);

		HashMap<String, Object> headerdetails = createHeaderDetails(indexNum, transaction);
		List<HashMap<String, Object>> accountDetList = new ArrayList<HashMap<String, Object>>();
		try {

			for (Map.Entry<String, BigDecimal> arrearsDemand : arrearsDemandMap.entrySet()) {
				if (arrearsDemand.getValue().compareTo(BigDecimal.ZERO) == 1) {
					accountDetList.add(createAccDetailmap(GLCODEMAP_FOR_ARREARTAX.get(arrearsDemand.getKey()),
							arrearsDemand.getValue().abs(), BigDecimal.ZERO));
					accountDetList.add(createAccDetailmap(GLCODEMAP_FOR_TAX_PAYABLE.get(arrearsDemand.getKey()),
							BigDecimal.ZERO, arrearsDemand.getValue().abs()));

				} else if(arrearsDemand.getValue().compareTo(BigDecimal.ZERO) == -1){
					accountDetList.add(createAccDetailmap(GLCODEMAP_FOR_ARREARTAX.get(arrearsDemand.getKey()),
							BigDecimal.ZERO, arrearsDemand.getValue().abs()));
					accountDetList.add(createAccDetailmap(GLCODEMAP_FOR_TAX_PAYABLE.get(arrearsDemand.getKey()),
							arrearsDemand.getValue().abs(), BigDecimal.ZERO));
				}
			}
			for (Map.Entry<String, BigDecimal> currentDemand : currentDemandMap.entrySet()) {
				if (currentDemand.getValue().compareTo(BigDecimal.ZERO) == 1) {
					accountDetList.add(createAccDetailmap(GLCODEMAP_FOR_CURRENTTAX.get(currentDemand.getKey()),
							currentDemand.getValue().abs(), BigDecimal.ZERO));
					accountDetList.add(createAccDetailmap(GLCODEMAP_FOR_TAX_PAYABLE.get(currentDemand.getKey()),
							BigDecimal.ZERO, currentDemand.getValue().abs()));

				} else if(currentDemand.getValue().compareTo(BigDecimal.ZERO) == -1) {
					
					if (currentDemand.getKey().equalsIgnoreCase(NMCPTISConstants.DEMANDRSN_CODE_ADVANCE)) {
						accountDetList.add(createAccDetailmap(NMCPTISConstants.GLCODE_FOR_ADVANCE,
								BigDecimal.ZERO, currentDemand.getValue().abs()));
						accountDetList.add(createAccDetailmap(NMCPTISConstants.GLCODE_FOR_ADVANCE,
								currentDemand.getValue().abs(), BigDecimal.ZERO));
					} else {
						accountDetList.add(createAccDetailmap(GLCODEMAP_FOR_CURRENTTAX.get(currentDemand.getKey()),
								BigDecimal.ZERO, currentDemand.getValue().abs()));
						accountDetList.add(createAccDetailmap(GLCODEMAP_FOR_TAX_PAYABLE.get(currentDemand.getKey()),
								currentDemand.getValue().abs(), BigDecimal.ZERO));
					}

				}

			}

			CreateVoucher cv = new CreateVoucher();
			CVoucherHeader cvh = cv.createVoucher(headerdetails, accountDetList,
					new ArrayList<HashMap<String, Object>>());
			if (cvh == null) {
				LOGGER.error("Voucher Creation failed. CVoucherHeader is null.");
				throw new EGOVRuntimeException("Voucher Creation failed.");
			}
			LOGGER.info("createVoucherForPTIS(): Voucher is created for PTIS with the voucher number : "
					+ cvh.getVoucherNumber());
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
			throw new EGOVRuntimeException("Unable to create a voucher.", t);
		}
	}

	/**
	 * Creates Account Details map
	 * 
	 * @param glcode
	 *            GLCode for the account head.
	 * @param debitAmount
	 *            Debit amount for the account head
	 * @param creditAmount
	 *            Credit amount for the account head
	 * @return Map Map contains account details.
	 */
	private HashMap<String, Object> createAccDetailmap(String glcode, BigDecimal debitAmount, BigDecimal creditAmount) {
		HashMap<String, Object> accountdetailmap = new HashMap<String, Object>();
		accountdetailmap.put(VoucherConstant.GLCODE, glcode);
		accountdetailmap.put(VoucherConstant.DEBITAMOUNT, debitAmount);
		accountdetailmap.put(VoucherConstant.CREDITAMOUNT, creditAmount);
		accountdetailmap.put(VoucherConstant.FUNCTIONCODE, DEFAULT_FUNCTIONARY_CODE);
		return accountdetailmap;
	}

	/**
	 * Creates Voucher Header details
	 * 
	 * @param indexNumber
	 *            Property id
	 * @param transaction
	 *            Voucher creation reason ( Property creation or modification
	 *            etc)
	 * @return Map Contains voucher header details
	 */
	private HashMap<String, Object> createHeaderDetails(String indexNumber, String transaction) {
		SimpleDateFormat sdf = new SimpleDateFormat("MMM-dd-yyyy HH:mm:ss");
		String description = "PTIS / " + indexNumber + " / " + transaction + " / " + sdf.format(new Date());
		String sourceURL = URL_FOR_DCB + indexNumber;

		HashMap<String, Object> headerdetails = new HashMap<String, Object>();
		headerdetails.put(VoucherConstant.VOUCHERNAME, VOUCHERNAME);
		headerdetails.put(VoucherConstant.VOUCHERTYPE, VOUCHERTYPE);
		headerdetails.put(VoucherConstant.DESCRIPTION, description);
		headerdetails.put(VoucherConstant.VOUCHERNUMBER, VOUCHERNUMBER);
		headerdetails.put(VoucherConstant.VOUCHERDATE, new Date());
		headerdetails.put(VoucherConstant.STATUS, 0);
		headerdetails.put(VoucherConstant.MODULEID, PTIS_EG_MODULES_ID);
		headerdetails.put(VoucherConstant.DEPARTMENTCODE, DEPT_CODE_TAX);
		headerdetails.put(VoucherConstant.FUNDCODE, DEFAULT_FUND_CODE);
		headerdetails.put(VoucherConstant.SOURCEPATH, sourceURL);
		return headerdetails;
	}

	/**
	 * Creates a map of map contains GLCODE and the aggregate amount for GLCODE
	 * 
	 * @param amounts
	 *            Map of Installment and reason wise demand map demand map --
	 *            (key-demand reason, val-respective demand)
	 * @return Map Map of map contains demand reason and the aggregate amount
	 *         for demand reason
	 * @throws IOException
	 */
	private Map<String, Map<String, BigDecimal>> prepareDemandForGlcode(
			Map<Installment, Map<String, BigDecimal>> amounts) {

		ModuleDao moduleDao = GenericDaoFactory.getDAOFactory().getModuleDao();
		InstallmentDao isntalDao = CommonsDaoFactory.getDAOFactory().getInstallmentDao();
		Module module = moduleDao.getModuleByName(PTMODULENAME);
		Installment currentInstall = isntalDao.getInsatllmentByModuleForGivenDate(module, new Date());

		Map<String, Map<String, BigDecimal>> demandForGlcode = new HashMap<String, Map<String, BigDecimal>>();
		Map<String, BigDecimal> arrearsDemand = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> currentDemand = new HashMap<String, BigDecimal>();
		BigDecimal taxAmount = BigDecimal.ZERO;
		BigDecimal amount = BigDecimal.ZERO;
		String demandReason = "";
		for (Entry<Installment, Map<String, BigDecimal>> amountsRecord : amounts.entrySet()) {

			String instDesc = amountsRecord.getKey().getDescription();
			Map<String, BigDecimal> demandReasonMap = amountsRecord.getValue();

			if (!instDesc.equalsIgnoreCase(currentInstall.toString())) {
				for (Map.Entry<String, BigDecimal> demandReasonRecord : demandReasonMap.entrySet()) {

					demandReason = demandReasonRecord.getKey();
					amount = demandReasonRecord.getValue();
					taxAmount = BigDecimal.ZERO;

					if (arrearsDemand.get(demandReason) == null) {
						arrearsDemand.put(demandReason, amount);
					} else {
						taxAmount = arrearsDemand.get(demandReason);
						arrearsDemand.put(demandReason, taxAmount.add(amount));
					}
				}
			} else {
				for (Map.Entry<String, BigDecimal> demandReasonRecord : demandReasonMap.entrySet()) {
					demandReason = demandReasonRecord.getKey();
					amount = demandReasonRecord.getValue();
					taxAmount = BigDecimal.ZERO;

					if (currentDemand.get(demandReason) == null) {
						currentDemand.put(demandReason, amount);
					} else {
						taxAmount = currentDemand.get(demandReason);
						currentDemand.put(demandReason, taxAmount.add(amount));
					}
				}
			}
		}

		demandForGlcode.put(ARREARS_DEMAND, arrearsDemand);
		demandForGlcode.put(CURRENT_DEMAND, currentDemand);
		return demandForGlcode;
	}
}
