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
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.web.actions.report;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.Fund;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.reporting.engine.ReportConstants.FileFormat;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.reporting.util.ReportUtil;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.report.FundFlowBean;
import org.egov.services.report.FundFlowService;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.springframework.transaction.annotation.Transactional;

@Results(value = {
		@Result(name = "PDF", type = "stream", location = "inputStream", params = {
				"inputName", "inputStream", "contentType", "application/pdf",
				"contentDisposition", "no-cache;filename=FundFlowReport.pdf" }),
		@Result(name = "XLS", type = "stream", location = "inputStream", params = {
				"inputName", "inputStream", "contentType", "application/xls",
				"contentDisposition", "no-cache;filename=FundFlowReport.xls" })

})
@Transactional(readOnly=true)
public class FundFlowAction extends BaseFormAction {
	private static Logger LOGGER = Logger.getLogger(FundFlowAction.class);
	private static final long serialVersionUID = 1L;
	EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
	private List<FundFlowBean> receiptList;
	private List<FundFlowBean> concurrancePaymentList;
	private List<FundFlowBean> outStandingPaymentList;
	private List<FundFlowBean> paymentList;
	private Long fund;
	private Date asOnDate;
	private List<FundFlowBean> total;
	private List<FundFlowBean> totalrepList = new ArrayList<FundFlowBean>();
	private List<FundFlowBean> totalpayList = new ArrayList<FundFlowBean>();
	private String jasperpath = "FundFlowReport";
	private InputStream inputStream;
	private ReportService reportService;
	SimpleDateFormat sqlformat = new SimpleDateFormat("dd-MMM-yyyy");
	Date openignBalanceCalculatedDate;
	private FundFlowService fundFlowService;
	private String mode;

	@Override
	public Object getModel() {
		return null;
	}

	public void prepare() {
		addDropdownData("fundList", masterCache.get("egi-fund"));
	}

@Action(value="/report/fundFlow-beforeSearch")
	public String beforeSearch() {
		loadDefalutDates();
		return NEW;
	}

	@SuppressWarnings("unchecked")
	@ValidationErrorPage(value = NEW)
	/*
	 * * 1. get All Receipt Bank Accounts for the Fund--receiptList 2. get All
	 * Receipt Bank Accounts which has Contra Payments for the Date
	 * --btbPaymentList 3. set the Contra Payments to Receipt bank Accounts --
	 * receiptList 4. get All Payment Bank Accounts for the Fund--PaymentList 5.
	 * get All Payment Bank Accounts which has Contra Receipts for the Date
	 * --btbReceiptList 6. set the Contra Payments to Payment Bank Accounts --
	 * paymentList 7. get OutStanding Payments for Payment Bank Accounts --
	 * outStandingPaymentList 8. set the outStanding Payments to Payment Bank
	 * Accounts 9. get Previous day opening Balance+receipt as opening Balance
	 * for both type of accounts 10. get previous day Contra Payments for
	 * Receipt bank accounts --previousDaybtbPaymentList 11. Subtract the Contra
	 * Payment from opening balance --receiptList 12. get previous day Contra
	 * Receipt for Payment Bank Accounts --previousDaybtbReceiptList 13. Add the
	 * Contra Receipt to opening balance --PaymentList
	 */
	public String search() {
		StringBuffer alreadyExistsQryStr = new StringBuffer(100);
		alreadyExistsQryStr
				.append("select openingBalance From egf_fundflow ff,bankaccount ba where ba.id=ff.bankaccountid and to_date(reportdate)='"
						+ sqlformat.format(asOnDate) + "' ");
		if (fund != null && fund != -1) {
			alreadyExistsQryStr.append(" and ba.fundId=" + fund + " ");
		}
		Query alreadyExistsQry = HibernateUtil.getCurrentSession()
				.createSQLQuery(alreadyExistsQryStr.toString());
		List existsList = alreadyExistsQry.list();
		if (existsList.size() > 0) {
			paymentList = null;
			receiptList = null;
			throw new ValidationException(
					Arrays
							.asList(new ValidationError(
									"fundflow.report.already.generated",
									"Fund Flow report is already Generated for the Date and Fund. Open in modify Mode")));
		}

		@SuppressWarnings("unused")
		List<FundFlowBean> openingBalnaceAllList = new ArrayList<FundFlowBean>();
		
		
		receiptList = fundFlowService.getAllReceiptAccounts(fund);
		List<FundFlowBean> btbPaymentList = fundFlowService
				.getContraPaymentsForTheDay(asOnDate, fund);
		if(LOGGER.isDebugEnabled())     LOGGER.debug("all Bank accounts ------" + receiptList.size());
		for (FundFlowBean fall : receiptList) {
			for (FundFlowBean ftemp : btbPaymentList) {
				if (fall.getAccountNumber().equalsIgnoreCase(ftemp.getAccountNumber())) {
					fall.setBtbPayment(ftemp.getBtbPayment());
				}
			}

		}

		List<FundFlowBean> btbReceiptList = fundFlowService.getContraReceiptsForTheDay(asOnDate, fund);
		for (FundFlowBean fall : receiptList) {
			for (FundFlowBean ftemp : btbReceiptList) {
				if (fall.getAccountNumber().equalsIgnoreCase(ftemp.getAccountNumber())) {
					fall.setBtbReceipt(ftemp.getBtbReceipt());
				}
			}

		} 
		
		paymentList = fundFlowService.getAllpaymentAccounts(fund);
		for (FundFlowBean fall : paymentList) {
			for (FundFlowBean ftemp : btbReceiptList) {
				if (fall.getAccountNumber().equalsIgnoreCase(ftemp.getAccountNumber())) {
					fall.setBtbReceipt(ftemp.getBtbReceipt());
				}
			}

		}

		List<FundFlowBean> btbRceipt_PaymentList = fundFlowService.getContraPaymentsForTheDayFromPaymentBanks(asOnDate, fund);
		if(LOGGER.isDebugEnabled())     LOGGER.debug("all Bank accounts ------" + btbRceipt_PaymentList.size());
		for (FundFlowBean fall : paymentList) {
			for (FundFlowBean ftemp : btbRceipt_PaymentList) {
				if (fall.getAccountNumber().equalsIgnoreCase(ftemp.getAccountNumber())) {
					fall.setBtbPayment(ftemp.getBtbPayment());
				}
			}

		}

		getPreviousDayClosingBalance();
		concurrancePaymentList = fundFlowService.getConcurrancePayments(
				asOnDate, fund);

		for (FundFlowBean fBean : paymentList) {
			for (FundFlowBean fop : concurrancePaymentList) {
				if (fBean.getAccountNumber().equalsIgnoreCase(
						fop.getAccountNumber())) {
					fBean.setConcurranceBPV(fop.getConcurranceBPV());

				}
			}
		}
		for (FundFlowBean fBean : receiptList) {
			for (FundFlowBean fop : concurrancePaymentList) {
				if (fBean.getAccountNumber().equalsIgnoreCase(
						fop.getAccountNumber())) {
					fBean.setConcurranceBPV(fop.getConcurranceBPV());

				}
			}
		}
		outStandingPaymentList = fundFlowService.getOutStandingPayments(
				asOnDate, fund);

		for (FundFlowBean fBean : paymentList) {
			for (FundFlowBean fop : outStandingPaymentList) {
				if (fBean.getAccountNumber().equalsIgnoreCase(
						fop.getAccountNumber())) {
					fBean.setOutStandingBPV(fop.getOutStandingBPV());

				}
			}
		}
		for (FundFlowBean fBean : receiptList) {
			for (FundFlowBean fop : outStandingPaymentList) {
				if (fBean.getAccountNumber().equalsIgnoreCase(
						fop.getAccountNumber())) {
					fBean.setOutStandingBPV(fop.getOutStandingBPV());

				}
			}
		}
		setFundsAvailableTotalPay();
		setFundsAvailableTotalRep();
		return NEW;
	}

	/**
	 * set total fundavailable for receipt list
	 */
	public List<FundFlowBean> setFundsAvailableTotalRep() {
		if (fund == null) {
			BigDecimal openingBal = new BigDecimal("0");
			BigDecimal btbpay = new BigDecimal("0");
			BigDecimal btbrep = new BigDecimal("0");
			BigDecimal curReceipt = new BigDecimal("0");
			String fundNam = receiptList.get(0).getFundName();
			int lastIndr = 0, sizer = receiptList.size() - 1;
			for (FundFlowBean fBean : receiptList) {
				if (fBean.getFundName().equalsIgnoreCase(fundNam)) {
					totalrepList.add(fBean);
					btbpay = btbpay.add(fBean.getBtbPayment());
					btbrep = btbrep.add(fBean.getBtbReceipt());
					openingBal = openingBal.add(fBean.getOpeningBalance());
					curReceipt = curReceipt.add(fBean.getCurrentReceipt());
				} else {
					totalrepList.add(new FundFlowBean(fundNam, "Total",
							openingBal, curReceipt, btbpay,btbrep)); // add total value
					// of previous
					// fund
					totalrepList.add(fBean);
					btbpay = BigDecimal.ZERO;
					btbrep = BigDecimal.ZERO;
					openingBal = BigDecimal.ZERO;
					curReceipt = BigDecimal.ZERO;
					fundNam = fBean.getFundName();
					openingBal = openingBal.add(fBean.getOpeningBalance());
					curReceipt = curReceipt.add(fBean.getCurrentReceipt());
					btbpay = btbpay.add(fBean.getBtbPayment());
					btbrep = btbrep.add(fBean.getBtbReceipt());
				}
				lastIndr = receiptList.indexOf(fBean);
				if (lastIndr == sizer) {
					totalrepList.add(new FundFlowBean(fundNam, "Total",
							openingBal, curReceipt, btbpay,btbrep));
				}
			}
			receiptList = totalrepList;
		}
		return receiptList;
	}

	/**
	 * Set fundsAvailableTotal for payment list
	 */
	public List<FundFlowBean> setFundsAvailableTotalPay() {
		if (fund == null) {
			BigDecimal openingBalp = new BigDecimal("0");
			BigDecimal btbpayp = new BigDecimal("0");
			BigDecimal curReceiptp = new BigDecimal("0");
			BigDecimal btbReceiptp = new BigDecimal("0");
			BigDecimal conBpvp = new BigDecimal("0");
			BigDecimal outBpvp = new BigDecimal("0");
			int lastIndex = 0, sizep = paymentList.size() - 1, lastInd = 0;
			String fndNamp = paymentList.get(0).getFundName();
			for (FundFlowBean fBean : paymentList) {
				if (fBean.getFundName().equalsIgnoreCase(fndNamp)) {
					totalpayList.add(fBean);
					openingBalp = openingBalp.add(fBean.getOpeningBalance());
					btbpayp = btbpayp.add(fBean.getBtbPayment());
					curReceiptp = curReceiptp.add(fBean.getCurrentReceipt());
					btbReceiptp = btbReceiptp.add(fBean.getBtbReceipt());
					conBpvp = conBpvp.add(fBean.getConcurranceBPV());
					outBpvp = outBpvp.add(fBean.getOutStandingBPV());

					lastIndex++;
				} else {
					totalpayList.add(new FundFlowBean(fndNamp, "Total",
							openingBalp, curReceiptp, btbpayp, btbReceiptp,
							conBpvp, outBpvp));
					openingBalp = BigDecimal.ZERO;
					btbpayp = BigDecimal.ZERO;
					curReceiptp = BigDecimal.ZERO;
					btbReceiptp = BigDecimal.ZERO;
					conBpvp = BigDecimal.ZERO;
					outBpvp = BigDecimal.ZERO;
					totalpayList.add(fBean);
					openingBalp = openingBalp.add(fBean.getOpeningBalance());
					btbpayp = btbpayp.add(fBean.getBtbPayment());
					curReceiptp = curReceiptp.add(fBean.getCurrentReceipt());
					btbReceiptp = btbReceiptp.add(fBean.getBtbReceipt());
					conBpvp = conBpvp.add(fBean.getConcurranceBPV());
					outBpvp = outBpvp.add(fBean.getOutStandingBPV());
					fndNamp = fBean.getFundName();
				}
				lastInd = paymentList.indexOf(fBean);
				if (lastInd == sizep) {
					totalpayList.add(new FundFlowBean(fndNamp, "Total",
							openingBalp, curReceiptp, btbpayp, btbReceiptp,
							conBpvp, outBpvp));
				}
			}
			paymentList = totalpayList;
		}
		return totalpayList;
	}

	/**
	 * get Prevous Day closing Balance As current day Opening Balance
	 * 
	 * Opening balance for Receipt bank = Opening balance for the previous day +
	 * Current Receipts (for the previous day) - CSL Transfer to Payment Banks
	 * (on the previous day) Opening balance for Payment bank = Opening balance
	 * for the previous day + Current Receipts (for the previous day) + CSL
	 * Transfer from Receipt Banks (on the previous day) - Concurrence for BPVs
	 * (for which concurrence has been done on the previous day)
	 * 
	 */

	public String recalculateOpeningBalance() {
		getPreviousDayClosingBalance();
		return EDIT;
	}

	public void getPreviousDayClosingBalance() {
		List<FundFlowBean> openingBalnaceAllList;
		openingBalnaceAllList = getOpeningBalance(asOnDate, fund);

		for (FundFlowBean fBean : receiptList) {
			for (FundFlowBean fop : openingBalnaceAllList) {
				if (fBean.getAccountNumber().equalsIgnoreCase(
						fop.getAccountNumber())) {
					fBean.setOpeningBalance(fop.getOpeningBalance());
				}
			}
		}

		for (FundFlowBean fBean : paymentList) {
			for (FundFlowBean fop : openingBalnaceAllList) {
				if (fBean.getAccountNumber().equalsIgnoreCase(
						fop.getAccountNumber())) {
					fBean.setOpeningBalance(fop.getOpeningBalance());

				}
			}
		}
		List<FundFlowBean> previousDaybtbPaymentList = fundFlowService
				.getContraPaymentsForTheDay(openignBalanceCalculatedDate, fund);
		for (FundFlowBean fBean : receiptList) {
			for (FundFlowBean fop : previousDaybtbPaymentList) {
				if (fBean.getAccountNumber().equalsIgnoreCase(
						fop.getAccountNumber())) {
					fBean.setOpeningBalance(fBean.getOpeningBalance().subtract(
							fop.getBtbPayment()));
				}
			}
		}

		for (FundFlowBean fBean : paymentList) {
			for (FundFlowBean fop : previousDaybtbPaymentList) {
				if (fBean.getAccountNumber().equalsIgnoreCase(
						fop.getAccountNumber())) {
					fBean.setOpeningBalance(fBean.getOpeningBalance().subtract(
							fop.getBtbPayment()));
				}
			}
		}
//this portion is not required since we are fetching for all type of bankaccounts at the same time
	/*	List<FundFlowBean> previousDaybtbPaymentFromPaymentBankList = fundFlowService
				.getContraPaymentsForTheDayFromPaymentBanks(
						openignBalanceCalculatedDate, fund);
		for (FundFlowBean fBean : paymentList) {
			for (FundFlowBean fop : previousDaybtbPaymentFromPaymentBankList) {
				if (fBean.getAccountNumber().equalsIgnoreCase(
						fop.getAccountNumber())) {
					fBean.setOpeningBalance(fBean.getOpeningBalance().subtract(
							fop.getBtbPayment()));
				}
			}
		}

		for (FundFlowBean fBean : paymentList) {
			for (FundFlowBean fop : previousDaybtbPaymentList) {
				if (fBean.getAccountNumber().equalsIgnoreCase(
						fop.getAccountNumber())) {
					fBean.setOpeningBalance(fBean.getOpeningBalance().subtract(
							fop.getBtbPayment()));
				}
			}
		}*/

		List<FundFlowBean> previousDaybtbReceiptList = fundFlowService
				.getContraReceiptsForTheDay(openignBalanceCalculatedDate, fund);
		for (FundFlowBean fBean : paymentList) {
			for (FundFlowBean fop : previousDaybtbReceiptList) {
				if (fBean.getAccountNumber().equalsIgnoreCase(
						fop.getAccountNumber())) {
					fBean.setOpeningBalance(fBean.getOpeningBalance().add(
							fop.getBtbReceipt()));
				}
			}
		}
		for (FundFlowBean fBean : receiptList) {
			for (FundFlowBean fop : previousDaybtbReceiptList) {
				if (fBean.getAccountNumber().equalsIgnoreCase(
						fop.getAccountNumber())) {
					fBean.setOpeningBalance(fBean.getOpeningBalance().add(
							fop.getBtbReceipt()));
				}
			}
		}

		concurrancePaymentList = fundFlowService.getConcurrancePayments(
				openignBalanceCalculatedDate, fund);

		for (FundFlowBean fBean : paymentList) {
			for (FundFlowBean fop : concurrancePaymentList) {
				if (fBean.getAccountNumber().equalsIgnoreCase(
						fop.getAccountNumber())) {
					// fBean.setConcurranceBPV(fop.getConcurranceBPV());
					fBean.setOpeningBalance(fBean.getOpeningBalance().subtract(
							fop.getConcurranceBPV()));
				}
			}
		}
		for (FundFlowBean fBean : receiptList) {
			for (FundFlowBean fop : concurrancePaymentList) {
				if (fBean.getAccountNumber().equalsIgnoreCase(
						fop.getAccountNumber())) {
					// fBean.setConcurranceBPV(fop.getConcurranceBPV());
					fBean.setOpeningBalance(fBean.getOpeningBalance().subtract(
							fop.getConcurranceBPV()));
				}
			}
		}
		outStandingPaymentList = fundFlowService.getOutStandingPayments(
				asOnDate, fund);

		for (FundFlowBean fBean : paymentList) {
			for (FundFlowBean fop : outStandingPaymentList) {
				if (fBean.getAccountNumber().equalsIgnoreCase(
						fop.getAccountNumber())) {
					fBean.setOutStandingBPV(fop.getOutStandingBPV());

				}
			}
		}
		for (FundFlowBean fBean : receiptList) {
			for (FundFlowBean fop : outStandingPaymentList) {
				if (fBean.getAccountNumber().equalsIgnoreCase(
						fop.getAccountNumber())) {
					fBean.setOutStandingBPV(fop.getOutStandingBPV());

				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public String create() {
		// merge two list to get sigle without duplicates
		StringBuffer alreadyExistsQryStr = new StringBuffer(100);
		alreadyExistsQryStr
				.append("select openingBalance From egf_fundflow ff,bankaccount ba where ba.id=ff.bankaccountid and to_date(reportdate)='"
						+ sqlformat.format(asOnDate) + "' ");
		if (fund != null && fund != -1) {
			alreadyExistsQryStr.append("and ba.fundId=" + fund + " ");
		}
		Query alreadyExistsQry = HibernateUtil.getCurrentSession()
				.createSQLQuery(alreadyExistsQryStr.toString());
		List existsList = alreadyExistsQry.list();
		if (existsList.size() > 0) {
			throw new ValidationException(
					Arrays
							.asList(new ValidationError(
									"fundflow.report.already.generated",
									"Fund Flow report is already Generated for the Date and Fund. Open in modify Mode")));
		}
		List<FundFlowBean> finalList = merge(receiptList, paymentList);
		persistenceService.setType(FundFlowBean.class);
		for (FundFlowBean fbean : finalList) {
			fbean.setReportDate(asOnDate);
			persistenceService.persist(fbean);
		}
		addActionMessage(getText("fundflowreport.create.succesful"));
		mode = NEW;
		return "report";
	}

@Action(value="/report/fundFlow-beforeEditSearch")
	public String beforeEditSearch() {
		loadDefalutDates();
		return EDIT;
	}

	/**
	 * get Opening Balance,currentReceipt and id from egf_Fundflow get Contra
	 * Receipts and Contra Payments get ConcurranceBPV for the date
	 * 
	 * @return
	 */
	public String beforeEdit() {

		List<FundFlowBean> openingBalnaceAllList = new ArrayList<FundFlowBean>();

		openingBalnaceAllList = getCurrentDayOpeningBalance(asOnDate, fund);
		if (openingBalnaceAllList == null || openingBalnaceAllList.size() == 0) {
			throw new ValidationException(Arrays
							.asList(new ValidationError(
									"fundflow.report.not.generated",
									"Fund Flow report is not Generated for the Date and Fund. open in create Mode")));
		}
		receiptList = fundFlowService.getAllReceiptAccounts(fund);
		List<FundFlowBean> btbPaymentList = fundFlowService.getContraPaymentsForTheDay(asOnDate, fund);
		if(LOGGER.isDebugEnabled())     LOGGER.debug("all Bank accounts ------" + receiptList.size());
		for (FundFlowBean fall : receiptList) {
			for (FundFlowBean ftemp : btbPaymentList) {
				if (fall.getAccountNumber().equalsIgnoreCase(ftemp.getAccountNumber())) {
					fall.setBtbPayment(ftemp.getBtbPayment());
				}
			}

		}
		List<FundFlowBean> btbReceiptList = fundFlowService.getContraReceiptsForTheDay(asOnDate, fund);
		for (FundFlowBean fall : receiptList) {
			for (FundFlowBean ftemp : btbReceiptList) {
				if (fall.getAccountNumber().equalsIgnoreCase(ftemp.getAccountNumber())) {
					fall.setBtbReceipt(ftemp.getBtbReceipt());
				}
			}

		}
		paymentList = fundFlowService.getAllpaymentAccounts(fund);
		for (FundFlowBean fall : paymentList) {
			for (FundFlowBean ftemp : btbReceiptList) {
				if (fall.getAccountNumber().equalsIgnoreCase(ftemp.getAccountNumber())) {
					fall.setBtbReceipt(ftemp.getBtbReceipt());
				}
			}

		}

		List<FundFlowBean> btbReceipt_PaymentList = fundFlowService.getContraPaymentsForTheDayFromPaymentBanks(asOnDate, fund);
		if(LOGGER.isDebugEnabled())     LOGGER.debug("all Bank accounts ------" + receiptList.size());
		for (FundFlowBean fall : paymentList) {
			for (FundFlowBean ftemp : btbReceipt_PaymentList) {
				if (fall.getAccountNumber().equalsIgnoreCase(
						ftemp.getAccountNumber())) {
					fall.setBtbPayment(ftemp.getBtbPayment());
				}
			}

		}

		for (FundFlowBean fBean : receiptList) {
			for (FundFlowBean fop : openingBalnaceAllList) {
				if (fBean.getAccountNumber().equalsIgnoreCase(
						fop.getAccountNumber())) {
					fBean.setOpeningBalance(fop.getOpeningBalance());
					fBean.setCurrentReceipt(fop.getCurrentReceipt());
					fBean.setBankAccountId(fop.getBankAccountId());
					fBean.setId(fop.getId());
				}
			}
		}

		for (FundFlowBean fBean : paymentList) {
			for (FundFlowBean fop : openingBalnaceAllList) {
				if (fBean.getAccountNumber().equalsIgnoreCase(
						fop.getAccountNumber())) {
					fBean.setOpeningBalance(fop.getOpeningBalance());
					fBean.setCurrentReceipt(fop.getCurrentReceipt());
					fBean.setBankAccountId(fop.getBankAccountId());
					fBean.setId(fop.getId());
				}
			}
		}

		concurrancePaymentList = fundFlowService.getConcurrancePayments(
				asOnDate, fund);

		for (FundFlowBean fBean : paymentList) {
			for (FundFlowBean fop : concurrancePaymentList) {
				if (fBean.getAccountNumber().equalsIgnoreCase(
						fop.getAccountNumber())) {
					fBean.setConcurranceBPV(fop.getConcurranceBPV());
					// fBean.setOpeningBalance(fBean.getOpeningBalance().subtract(fop.getConcurranceBPV()));
				}
			}
		}
		for (FundFlowBean fBean : receiptList) {
			for (FundFlowBean fop : concurrancePaymentList) {
				if (fBean.getAccountNumber().equalsIgnoreCase(
						fop.getAccountNumber())) {
					fBean.setConcurranceBPV(fop.getConcurranceBPV());
					// fBean.setOpeningBalance(fBean.getOpeningBalance().subtract(fop.getConcurranceBPV()));
				}
			}

		}
		outStandingPaymentList = fundFlowService.getOutStandingPayments(
				asOnDate, fund);

		for (FundFlowBean fBean : paymentList) {
			for (FundFlowBean fop : outStandingPaymentList) {
				if (fBean.getAccountNumber().equalsIgnoreCase(
						fop.getAccountNumber())) {
					fBean.setOutStandingBPV(fop.getOutStandingBPV());

				}
			}
		}
		for (FundFlowBean fBean : receiptList) {
			for (FundFlowBean fop : outStandingPaymentList) {
				if (fBean.getAccountNumber().equalsIgnoreCase(
						fop.getAccountNumber())) {
					fBean.setOutStandingBPV(fop.getOutStandingBPV());

				}
			}
		}
		setFundsAvailableTotalPay();
		setFundsAvailableTotalRep();
		return EDIT;
	}

	@SuppressWarnings("unchecked")
	public String edit() {
		// Connection conn = null;
		List<FundFlowBean> finalList = merge(receiptList, paymentList);
		persistenceService.setType(FundFlowBean.class);
		for (FundFlowBean fbean : finalList) {

			fbean.setReportDate(asOnDate);
			// HibernateUtil.getCurrentSession().evict(fbean);
			persistenceService.persist(fbean);
		}
		addActionMessage(getText("fundflowreport.update.succesful"));
		mode = EDIT;
		return "report";
	}

	/**
	 * @param
	 * @return previous day opening balance+receipt add or subtracting csl is in
	 *         the main code
	 */
	@SuppressWarnings("unchecked")
	private List<FundFlowBean> getOpeningBalance(Date asPerDate, Long fundId) {
		Date reportDate = asPerDate;
		int i = 0;
		List<FundFlowBean> openingBalnaceList = new ArrayList<FundFlowBean>();
		while (openingBalnaceList.size() == 0) {
			try {
				// get previous day
				reportDate = sqlformat.parse(sqlformat.format((reportDate
						.getTime() - (1000 * 24 * 60 * 60))));
			} catch (ParseException e) {
				throw new ValidationException(Arrays
						.asList(new ValidationError("parserExeception",
								"parser exception")));
			}
			StringBuffer openingBalanceQryStr = new StringBuffer(100);
			openingBalanceQryStr
					.append("select ba.id as bankAccountId,ba.accountnumber as accountNumber,ff.openingBalance+ff.currentReceipt as openingBalance from egf_fundflow ff,bankaccount ba   "
							+ "   where ff.bankaccountid=ba.id ");
			if (fundId != null && fundId != -1) {
				openingBalanceQryStr.append("and ba.fundid=" + fundId);
			}
			openingBalanceQryStr.append(" and to_date(reportdate)='"
					+ sqlformat.format(reportDate) + "'");
			if(LOGGER.isDebugEnabled())     LOGGER.debug("getting Opening Balance for " + reportDate
					+ "    sqlformat.format(reportDate)"
					+ sqlformat.format(reportDate));

			if(LOGGER.isDebugEnabled())     LOGGER.debug(" Opening Balance Qry "
					+ openingBalanceQryStr.toString());
			Query openingBalanceQry = HibernateUtil.getCurrentSession()
					.createSQLQuery(openingBalanceQryStr.toString()).addScalar(
							"bankAccountId").addScalar("accountNumber")
					.addScalar("openingBalance").setResultTransformer(
							Transformers.aliasToBean(FundFlowBean.class));
			openingBalnaceList = openingBalanceQry.list();
			i++;
			if (i >= 100) {
				receiptList = null;
				paymentList = null;
				throw new ValidationException(
						Arrays
								.asList(new ValidationError(
										"fundflow.not.done.for.more.than.100",
										"fundflow  not done for more than 100 Days please start from last date")));

			}

		}
		if(LOGGER.isInfoEnabled())     LOGGER.info("////////////////////////////////////////");
		LOGGER
				.info("--------------------------Last Entry for Fund Flow Data is on "
						+ reportDate + "and this report is for " + asPerDate);
		if(LOGGER.isInfoEnabled())     LOGGER.info("///////////////////////////////////////");
		setOpenignBalanceCalculatedDate(reportDate);
		return openingBalnaceList;
	}

	/**
	 * get openingbalance,currentreceipt,bankaccountid from egf_fundflow table
	 * for modify
	 * 
	 * @param asOnDate2
	 * @param fund2
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<FundFlowBean> getCurrentDayOpeningBalance(Date asOnDate2,Long fund2) {

		StringBuffer currentOpbAndRcptQryStr = new StringBuffer(100);
		currentOpbAndRcptQryStr
				.append("select ff.openingBalance as openingBalance,ff.currentreceipt as currentReceipt,ff.id as id ,ba.accountNumber as accountNumber,ff.bankAccountId as bankAccountId From egf_fundflow ff,bankaccount ba where ba.id=ff.bankaccountid and to_date(reportdate)='"
						+ sqlformat.format(asOnDate2) + "' ");
		if (fund2 != null && fund2 != -1) {
			currentOpbAndRcptQryStr.append(" and ba.fundId=" + fund2 + " ");
		}

		Query currentOpbAndRcptQry = HibernateUtil.getCurrentSession()
				.createSQLQuery(currentOpbAndRcptQryStr.toString()).addScalar(
						"openingBalance").addScalar("currentReceipt")
				.addScalar("id", LongType.INSTANCE).addScalar("accountNumber")
				.addScalar("bankAccountId").setResultTransformer(
						Transformers.aliasToBean(FundFlowBean.class));
		return currentOpbAndRcptQry.list();

	}

	/**
	 * @param receiptList2
	 * @param paymentList2
	 * @return
	 */

	private List<FundFlowBean> merge(List<FundFlowBean> receiptList2,
			List<FundFlowBean> paymentList2) {

		List<FundFlowBean> finalList = new ArrayList<FundFlowBean>();
		if (receiptList2 == null) {
			receiptList2 = new ArrayList<FundFlowBean>();
		}
		for(FundFlowBean fBean : receiptList2){
			boolean insert = true;
			if (fBean.getAccountNumber().contains("Total")) {
				insert=false; continue;
			}if (insert == true) {
				finalList.add(fBean);
			}
		}
		if(LOGGER.isInfoEnabled())     LOGGER.info("Starting Merging..................................... ");
		for (FundFlowBean fBean : paymentList2) {
			//if(LOGGER.isInfoEnabled())     LOGGER.info("Payment Bean is :" + fBean.toString());
			boolean insert = true;
			if (fBean.getAccountNumber().contains("Total")) {
				continue;
			}
			inner: for (FundFlowBean fop : receiptList2) {
				//if(LOGGER.isInfoEnabled())     LOGGER.info("Receipt Bean is :" + fop.toString());
				if (fop.getAccountNumber().contains("Total")) {
					continue;
				}
				if (fBean.getAccountNumber().equalsIgnoreCase(
						fop.getAccountNumber())) {
					insert = false;
					break inner;
				}
			}
			if (insert == true) {
				//if(LOGGER.isInfoEnabled())     LOGGER.info("inserting " + fBean.toString());
				finalList.add(fBean);
			}

		}
		if(LOGGER.isInfoEnabled())     LOGGER.info("Completed Merging....................");
		return finalList;
	}

	public String exportPdf() {
		generateReport();
		return "PDF";
	}

	public String exportXls() {
		updateListsForTotals();
		ReportRequest reportInput = new ReportRequest(jasperpath, receiptList,
				getParamMap());
		reportInput.setReportFormat(FileFormat.XLS);
		ReportOutput reportOutput = reportService.createReport(reportInput);
		inputStream = new ByteArrayInputStream(reportOutput
				.getReportOutputData());
		return "XLS";
	}

	private void generateReport() {

		updateListsForTotals();
		ReportRequest reportInput = new ReportRequest(jasperpath, receiptList,
				getParamMap());
		ReportOutput reportOutput = reportService.createReport(reportInput);
		inputStream = new ByteArrayInputStream(reportOutput
				.getReportOutputData());
	}

	/**
	 * will add total rows for PDF and Excels
	 */
	private void updateListsForTotals() {

		// receiptList.add();
		if (total.get(0) != null) {
			total.get(0).setAccountNumber("Total (A)");
			total.get(0).setBankName(" ");
			total.get(0).setGlcode(null);
			receiptList.add(total.get(0));
		}

		if (total.get(1) != null) {
			total.get(1).setAccountNumber("Total (B)");
			total.get(1).setBankName(" ");
			total.get(1).setGlcode(null);
			paymentList.add(total.get(1));
		}
		total.get(2).setAccountNumber("Grand Total (A+B)");
		total.get(2).setBankName(" ");
		total.get(2).setGlcode(null);
		total.get(2).setCurrentReceipt(null);
		total.get(2).setBtbPayment(null);
		total.get(2).setBtbReceipt(null);
		total.get(2).setConcurranceBPV(null);
		paymentList.add(total.get(2));

	}

	/**
	 * @return
	 */
	private Map<String, Object> getParamMap() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("FundFlowReport_paymentDetailsJasper", ReportUtil
				.getTemplateAsStream("FundFlowReport_paymentDetails.jasper"));
		// paramMap.put("FundFlowReport_paymentDetailsJasper",
		// this.getClass().getResourceAsStream("/reports/templates/FundFlowReport_paymentDetails.jasper"));

		paramMap.put("receiptList", receiptList);
		paramMap.put("paymentList", paymentList);
		if (fund != null && fund != -1) {
			Fund fundObj = (Fund) persistenceService.find("from Fund where id="
					+ fund);
			paramMap.put("fundName", fundObj.getName());
		} else {
			paramMap.put("fundName", "");
		}
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		paramMap.put("asOnDate", sdf.format(asOnDate));

		paramMap.put("ulbName", getUlbName());

		return paramMap;
	}

	// load current date
	private void loadDefalutDates() {
		final Date currDate = new Date();
		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			asOnDate = sdf.parse(sdf.format(currDate));
		} catch (final ParseException e) {
			throw new ValidationException(Arrays.asList(new ValidationError(
					"Exception while formatting as on date",
					"Transaction failed")));
		}
	}

	public List<FundFlowBean> getOutStandingPaymentList() {
		return outStandingPaymentList;
	}

	public void setOutStandingPaymentList(
			List<FundFlowBean> outStandingPaymentList) {
		this.outStandingPaymentList = outStandingPaymentList;
	}

	public List<FundFlowBean> getPaymentList() {
		return paymentList;
	}

	public void setPaymentList(List<FundFlowBean> paymentList) {
		this.paymentList = paymentList;
	}

	public Long getFund() {
		return fund;
	}

	public List<FundFlowBean> getReceiptList() {
		return receiptList;
	}

	public void setReceiptList(List<FundFlowBean> receiptList) {
		this.receiptList = receiptList;
	}

	public void setFund(Long fund) {
		this.fund = fund;
	}

	public Date getAsOnDate() {
		return asOnDate;
	}

	public void setAsOnDate(Date asOnDate) {
		this.asOnDate = asOnDate;
	}

	public List<FundFlowBean> getTotal() {
		return total;
	}

	public void setTotal(List<FundFlowBean> total) {
		this.total = total;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public ReportService getReportService() {
		return reportService;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public FundFlowService getFundFlowService() {
		return fundFlowService;
	}

	public void setFundFlowService(FundFlowService fundFlowService) {
		this.fundFlowService = fundFlowService;
	}

	public List<FundFlowBean> getTotalrepList() {
		return totalrepList;
	}

	public void setTotalrepList(List<FundFlowBean> totalrepList) {
		this.totalrepList = totalrepList;
	}

	public List<FundFlowBean> getTotalpayList() {
		return totalpayList;
	}

	public void setTotalpayList(List<FundFlowBean> totalpayList) {
		this.totalpayList = totalpayList;
	}

	public Date getOpenignBalanceCalculatedDate() {
		return openignBalanceCalculatedDate;
	}

	public void setOpenignBalanceCalculatedDate(
			Date openignBalanceCalculatedDate) {
		this.openignBalanceCalculatedDate = openignBalanceCalculatedDate;
	}

	public List<FundFlowBean> getConcurrancePaymentList() {
		return concurrancePaymentList;
	}

	public void setConcurrancePaymentList(
			List<FundFlowBean> concurrancePaymentList) {
		this.concurrancePaymentList = concurrancePaymentList;
	}

	@SuppressWarnings("unchecked")
	private String getUlbName() {
		SQLQuery query = HibernateUtil.getCurrentSession().createSQLQuery(
				"select name from companydetail");
		List<String> result = query.list();
		if (result != null)
			return result.get(0);
		return "";
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
}
