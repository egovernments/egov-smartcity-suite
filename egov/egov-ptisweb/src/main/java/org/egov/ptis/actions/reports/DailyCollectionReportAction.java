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
package org.egov.ptis.actions.reports;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.commons.dao.InstallmentDao;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.reporting.viewer.ReportViewerUtil;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.ptis.bean.CollectionInfo;
import org.egov.ptis.bean.ReceiptInfo;
import org.egov.ptis.bean.TaxCollectionInfo;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.math.BigDecimal.ZERO;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_EDUCATIONAL_CESS;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_GENERAL_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_LIBRARY_CESS;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_UNAUTHORIZED_PENALTY;
import static org.egov.ptis.constants.PropertyTaxConstants.GLCODEMAP_FOR_ARREARTAX;
import static org.egov.ptis.constants.PropertyTaxConstants.GLCODEMAP_FOR_CURRENTTAX;
import static org.egov.ptis.constants.PropertyTaxConstants.GLCODE_FOR_PENALTY;
import static org.egov.ptis.constants.PropertyTaxConstants.GLCODE_FOR_TAXREBATE;
import static org.egov.ptis.constants.PropertyTaxConstants.REPORT_TEMPLATENAME_DAILY_COLLECTION;

/**
 * 
 * @author subhash
 * 
 */
@ParentPackage("egov")
@ResultPath("/WEB-INF/jsp/")
@Namespace("/reports")
@Results({ @Result(name = "new", location = "reports/dailyCollectionReport-new.jsp")})
public class DailyCollectionReportAction extends BaseFormAction {

	private static final Logger LOGGER = Logger.getLogger(DailyCollectionReportAction.class);
	private static final String PAYMENT_MODE_CHEQUE = "cheque";
	private static final String PAYMENT_MODE_CASH = "cash";
	private static final String TOTAL_BY_CHEQUE = "Total By Cheque";
	private static final String TOTAL_BY_CASH = "Total By Cash";
	private static final String TOTAL = "Total";
	private static final long serialVersionUID = 1L;
	private String REPORT = "report";
	private String CURRENT = "Current";
	private String ARREAR = "Arrears";
	private String REBATE = "Rebate";
	@Autowired
	private BasicPropertyDAO basicPropertyDAO;
	private ReportService reportService;
	private String reportId;
	ReceiptInfo totalRcptInfo = new ReceiptInfo();
	ReceiptInfo totalCashRcptInfo = new ReceiptInfo();
	ReceiptInfo totalChequeRcptInfo = new ReceiptInfo();
	TaxCollectionInfo arrTotalCollInfo = new TaxCollectionInfo();
	TaxCollectionInfo currTotalCollInfo = new TaxCollectionInfo();
	TaxCollectionInfo rebateTotalCollInfo = new TaxCollectionInfo();
	TaxCollectionInfo arrTotalCashCollInfo = new TaxCollectionInfo();
	TaxCollectionInfo currTotalCashCollInfo = new TaxCollectionInfo();
	TaxCollectionInfo rebateTotalCashCollInfo = new TaxCollectionInfo();
	TaxCollectionInfo arrTotalChequeCollInfo = new TaxCollectionInfo();
	TaxCollectionInfo currTotalChequeCollInfo = new TaxCollectionInfo();
	TaxCollectionInfo rebateTotalChequeCollInfo = new TaxCollectionInfo();
	BigDecimal totalCashCollAmt = ZERO;
	BigDecimal totalChequeCollAmt = ZERO;
	BigDecimal totalOthersCollAmt = ZERO;
	BigDecimal arrTotalCashCollAmt = ZERO;
	BigDecimal arrTotalChequeCollAmt = ZERO;
	BigDecimal currTotalCashCollAmt = ZERO;
	BigDecimal currTotalChequeCollAmt = ZERO;
	BigDecimal rebateTotalCashCollAmt = ZERO;
	BigDecimal rebateTotalChequeCollAmt = ZERO;
	private Date fromDate;
	private Date toDate;
	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
	PropertyTaxUtil propTaxUtil = new PropertyTaxUtil();
	Boolean searchForm = Boolean.TRUE;
	String currInst = null;
	private String userId;

	@Autowired
	private ModuleService moduleDao;

	@Autowired
	private InstallmentDao instalDao;

	@Autowired
	private UserService userService;

	@Autowired
	private ReportViewerUtil reportViewerUtil;

	@Override
	public Object getModel() {
		return null;
	}

	@Override
	public void prepare() {
		LOGGER.debug("Entered into prepare method");
		//FIXME Wrong according to the existing user role logic
		Query qry = persistenceService.getSession().createQuery(
				"select distinct UI FROM User UI left join UI.roles ur left join ur.role r "
						+ "where r.name = :roleName AND UI.active=1 AND ur.isHistory='N' order by UI.userName");
		qry.setParameter("roleName", PropertyTaxConstants.ROLE_OPERATOR);
		List<User> userList = qry.list();
		addDropdownData("userList", userList);
		LOGGER.debug("Exited from prepare method");
	}

	@SkipValidation
	@Action(value = "/dailyCollectionReport-newForm")
	public String newForm() {
		return NEW;
	}

	@Override
	public void validate() {
		if (fromDate == null || fromDate.equals("")) {
			addActionError(getText("mandatory.fromdate"));
		}
		if (toDate == null || toDate.equals("")) {
			addActionError(getText("mandatory.todate"));
		}
		if (userId == null || userId.equals("-1") || userId.equals("")) {
			addActionError(getText("mandatory.todate"));
		}
	}

	@SuppressWarnings("unchecked")
	@ValidationErrorPage("new")
	@Action(value = "/dialyCollectionReport-generateReport", results = { @Result(name = NEW, location = "/dialyCollectionReport-new.jsp") })
	public String generateReport() {
		LOGGER.debug("Eneterd into generateReport method");
		Long reportStartTime = System.currentTimeMillis();
		Module module = moduleDao.getModuleByName(PropertyTaxConstants.PTMODULENAME);
		// UserDAO userDao = new UserDAO();
		currInst = instalDao.getInsatllmentByModuleForGivenDate(module, new Date()).getDescription();
		StringBuilder qryString = new StringBuilder("from org.egov.erpcollection.models.ReceiptHeader rh")
				.append(" left join fetch rh.receiptInstrument rcptInst")
				.append(" left join fetch rcptInst.instrumentType").append(" left join fetch rh.receiptDetails")
				.append(" left join fetch rh.receiptPayeeDetails").append(" left join fetch rh.receiptMisc")
				.append(" left join fetch rh.onlinePayment").append(" left join fetch rh.challan")
				.append(" where rh.manualreceiptnumber is null and rh.manualreceiptdate is null")
				.append(" and rh.status.description != 'Cancelled' and rh.service.serviceName = 'Property Tax'")
				.append(" and rh.createdBy.id = :userId").append(" and rh.createdDate >= :fromDate")
				.append(" and rh.createdDate <= :toDate").append(" order by rh.modifiedDate desc");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(toDate);
		calendar.add(Calendar.DATE, 1);
		Query qry = persistenceService.getSession().createQuery(qryString.toString())
				.setParameter("fromDate", fromDate).setParameter("toDate", calendar.getTime())
				.setParameter("userId", Integer.valueOf(userId))
				.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		Long qryStartTime = System.currentTimeMillis();
		List<ReceiptHeader> rcptHeaderList = qry.list();
		LOGGER.debug("Main Query took " + (System.currentTimeMillis() - qryStartTime) / 1000 + "sec(s)..!");
		LOGGER.debug("Number of records: " + rcptHeaderList.size());
		if (rcptHeaderList != null && !rcptHeaderList.isEmpty()) {
			Long loopStartTime = System.currentTimeMillis();
			List<ReceiptInfo> rcptInfoList = new ArrayList<ReceiptInfo>();
			CollectionInfo cashCollInfo = new CollectionInfo();
			initializeTotalsInfo();
			for (ReceiptHeader rcptHeader : rcptHeaderList) {
				ReceiptInfo rcptInfo = new ReceiptInfo();
				rcptInfo.setReceiptNo(rcptHeader.getReceiptnumber());
				String indexNo = org.apache.commons.lang.StringUtils
						.trim(rcptHeader.getConsumerCode().contains("(") ? rcptHeader.getConsumerCode().substring(0,
								rcptHeader.getConsumerCode().indexOf('(')) : rcptHeader.getConsumerCode());
				rcptInfo.setIndexNo(indexNo);
				rcptInfo.setWardNo(rcptHeader.getConsumerCode().substring(
						rcptHeader.getConsumerCode().lastIndexOf(":") + 1, rcptHeader.getConsumerCode().indexOf(")")));
				// FIX ME rcptHeader.getReceiptPayeeDetails() is not available
				// in collection
				// rcptInfo.setHouseNo(rcptHeader.getReceiptPayeeDetails().getPayeeAddress().split(",")[0]);
				StringBuffer payMode = new StringBuffer();
				Set<String> paymentModes = new HashSet<String>();
				StringBuffer instrumentDetails = new StringBuffer();
				int i = 1, j = 1;
				for (InstrumentHeader instrumentHead : rcptHeader.getReceiptInstrument()) {
					if (i > 1) {
						instrumentDetails.append("|| ");
					}

					if (instrumentHead.getInstrumentNumber() != null) {
						instrumentDetails
								.append(instrumentHead.getInstrumentNumber())
								.append(", ")
								.append(instrumentHead.getInstrumentDate() != null ? dateFormat.format(instrumentHead
										.getInstrumentDate()) : " ")
								.append(", ")
								.append(instrumentHead.getBankId() != null ? instrumentHead.getBankId().getName() : " ");
					}

					paymentModes.add(instrumentHead.getInstrumentType().getType());
					i++;
				}
				for (String paymentMode : paymentModes) {
					if (j > 1) {
						payMode.append(", ");
					}
					payMode.append(paymentMode);
					j++;
				}
				rcptInfo.setInstrumentDetails(instrumentDetails.toString());
				rcptInfo.setPaymentMode(payMode.toString());
				rcptInfo.setPayeeName(rcptHeader.getPaidBy());
				rcptInfo.setCollInfoList(getCollectionInfoList(rcptHeader));
				rcptInfoList.add(rcptInfo);
			}
			List<TaxCollectionInfo> cashTotalsList = new ArrayList<TaxCollectionInfo>();
			cashTotalsList.add(currTotalCashCollInfo);
			cashTotalsList.add(arrTotalCashCollInfo);
			cashTotalsList.add(rebateTotalCashCollInfo);
			totalCashRcptInfo.setCollInfoList(cashTotalsList);
			totalCashRcptInfo.setReceiptNo(TOTAL_BY_CASH);
			List<TaxCollectionInfo> chequeTotalsList = new ArrayList<TaxCollectionInfo>();
			chequeTotalsList.add(currTotalChequeCollInfo);
			chequeTotalsList.add(arrTotalChequeCollInfo);
			chequeTotalsList.add(rebateTotalChequeCollInfo);
			totalChequeRcptInfo.setCollInfoList(chequeTotalsList);
			totalChequeRcptInfo.setReceiptNo(TOTAL_BY_CHEQUE);
			List<TaxCollectionInfo> totalsList = new ArrayList<TaxCollectionInfo>();
			totalsList.add(currTotalCollInfo);
			totalsList.add(arrTotalCollInfo);
			totalsList.add(rebateTotalCollInfo);
			totalRcptInfo.setCollInfoList(totalsList);
			totalRcptInfo.setReceiptNo(TOTAL);
			rcptInfoList.add(totalCashRcptInfo);
			rcptInfoList.add(totalChequeRcptInfo);
			rcptInfoList.add(totalRcptInfo);
			cashCollInfo.setCollByCash(totalCashCollAmt);
			cashCollInfo.setCollByCheque(totalChequeCollAmt);
			cashCollInfo.setOtherColl(totalOthersCollAmt);
			cashCollInfo.setEduEgsArrColl(arrTotalCollInfo.getEduCess().add(arrTotalCollInfo.getEgsCess()));
			cashCollInfo.setEduEgsCurrColl(currTotalCollInfo.getEduCess().add(currTotalCollInfo.getEgsCess()));
			cashCollInfo.setTotalArrColl(arrTotalCollInfo.getTotal());
			cashCollInfo.setTotalCurrColl(currTotalCollInfo.getTotal().subtract(rebateTotalCollInfo.getTotal()));
			cashCollInfo.setGrandTotal(arrTotalCollInfo.getTotal().add(currTotalCollInfo.getTotal())
					.subtract(rebateTotalCollInfo.getTotal()));
			cashCollInfo.setRcptInfoList(rcptInfoList);
			User user = userService.getUserById(Long.valueOf(userId));
			cashCollInfo.setOperator(user.getUsername());
			LOGGER.debug("Loop took " + (System.currentTimeMillis() - loopStartTime) / 1000 + " sec(s)..!");
			ReportRequest reportInput = new ReportRequest(REPORT_TEMPLATENAME_DAILY_COLLECTION, cashCollInfo, null);
			reportInput.setPrintDialogOnOpenReport(true);
			reportId = reportViewerUtil.addReportToTempCache(reportService.createReport(reportInput));
			LOGGER.debug("Report took " + (System.currentTimeMillis() - reportStartTime) / 1000 + " sec(s)..!");
			LOGGER.debug("Exited from generateReport method(if block)");
			return REPORT;
		} else {
			searchForm = Boolean.FALSE;
			LOGGER.debug("Exited from generateReport method(else block)");
			return NEW;
		}
	}

	private List<TaxCollectionInfo> getCollectionInfoList(ReceiptHeader rcptHeader) {
		LOGGER.debug("Entered into getCollectionInfoList method");
		List<TaxCollectionInfo> collInfoList = new ArrayList<TaxCollectionInfo>();
		Set<ReceiptDetail> rcptDetails = rcptHeader.getReceiptDetails();
		Set<InstrumentHeader> instHeaderList = rcptHeader.getReceiptInstrument();
		String paymentMode = null;
		for (InstrumentHeader instHeader : instHeaderList) {
			paymentMode = instHeader.getInstrumentType().getType();
		}
		TaxCollectionInfo currCollInfo = new TaxCollectionInfo();
		TaxCollectionInfo arrCollInfo = new TaxCollectionInfo();
		TaxCollectionInfo rebateCollInfo = new TaxCollectionInfo();
		currCollInfo.setTaxType(CURRENT);
		arrCollInfo.setTaxType(ARREAR);
		rebateCollInfo.setTaxType(REBATE);
		BigDecimal totalCurrentTax = ZERO;
		BigDecimal totalArrearTax = ZERO;
		BigDecimal totalRebate = ZERO;

		for (ReceiptDetail rcptDetail : rcptDetails) {
			String glcode = rcptDetail.getAccounthead().getGlcode();
			if (GLCODE_FOR_PENALTY.equals(glcode)) {
				if (currInst.equals(rcptDetail.getDescription().substring(16, rcptDetail.getDescription().length()))) {

					currCollInfo.setMiscTax(currCollInfo.getMiscTax().add(rcptDetail.getCramount()));
					currTotalCollInfo.setMiscTax(currTotalCollInfo.getMiscTax().add(rcptDetail.getCramount()));

					if (PAYMENT_MODE_CASH.equals(paymentMode)) {
						currTotalCashCollInfo.setMiscTax(currTotalCashCollInfo.getMiscTax().add(
								rcptDetail.getCramount()));
					} else if (PAYMENT_MODE_CHEQUE.equals(paymentMode)) {
						currTotalChequeCollInfo.setMiscTax(currTotalChequeCollInfo.getMiscTax().add(
								rcptDetail.getCramount()));
					}

					totalCurrentTax = totalCurrentTax.add(rcptDetail.getCramount() != null ? rcptDetail.getCramount()
							: ZERO);
				} else {
					arrCollInfo.setMiscTax(arrCollInfo.getMiscTax().add(rcptDetail.getCramount()));
					arrTotalCollInfo.setMiscTax(arrTotalCollInfo.getMiscTax().add(rcptDetail.getCramount()));

					if (PAYMENT_MODE_CASH.equals(paymentMode)) {
						arrTotalCashCollInfo
								.setMiscTax(arrTotalCashCollInfo.getMiscTax().add(rcptDetail.getCramount()));
					} else if (PAYMENT_MODE_CHEQUE.equals(paymentMode)) {
						arrTotalChequeCollInfo.setMiscTax(arrTotalChequeCollInfo.getMiscTax().add(
								rcptDetail.getCramount()));
					}
					totalArrearTax = totalArrearTax.add(rcptDetail.getCramount() != null ? rcptDetail.getCramount()
							: ZERO);
				}
			} else if (GLCODE_FOR_TAXREBATE.equals(glcode)) {

				rebateCollInfo.setGeneralTax(rebateCollInfo.getGeneralTax().add(
						rcptDetail.getDramount() != null ? rcptDetail.getDramount() : ZERO));

				totalRebate = totalRebate.add(rcptDetail.getDramount() != null ? rcptDetail.getDramount() : ZERO);
				rebateTotalCollInfo.setGeneralTax(rebateTotalCollInfo.getGeneralTax().add(rcptDetail.getDramount()));

				if (PAYMENT_MODE_CASH.equals(paymentMode)) {
					rebateTotalCashCollInfo.setGeneralTax(rebateTotalCashCollInfo.getGeneralTax().add(
							rcptDetail.getDramount()));
				} else if (PAYMENT_MODE_CHEQUE.equals(paymentMode)) {
					rebateTotalChequeCollInfo.setGeneralTax(rebateTotalChequeCollInfo.getGeneralTax().add(
							rcptDetail.getDramount()));
				}
			} else if (GLCODEMAP_FOR_ARREARTAX.containsValue(glcode)) {
				if (GLCODEMAP_FOR_ARREARTAX.get(DEMANDRSN_CODE_GENERAL_TAX).equals(glcode)) {

					arrCollInfo.setGeneralTax(arrCollInfo.getGeneralTax().add(
							rcptDetail.getCramount() != null ? rcptDetail.getCramount() : ZERO));
					arrTotalCollInfo.setGeneralTax(arrTotalCollInfo.getGeneralTax().add(rcptDetail.getCramount()));

					if (PAYMENT_MODE_CASH.equals(paymentMode)) {
						arrTotalCashCollInfo.setGeneralTax(arrTotalCashCollInfo.getGeneralTax().add(
								rcptDetail.getCramount()));
					} else if (PAYMENT_MODE_CHEQUE.equals(paymentMode)) {
						arrTotalChequeCollInfo.setGeneralTax(arrTotalChequeCollInfo.getGeneralTax().add(
								rcptDetail.getCramount()));
					}
				} else if (GLCODEMAP_FOR_ARREARTAX.get(DEMANDRSN_CODE_EDUCATIONAL_CESS).equals(glcode)) {

					arrCollInfo.setSewerageTax(arrCollInfo.getSewerageTax().add(
							rcptDetail.getCramount() != null ? rcptDetail.getCramount() : ZERO));
					arrTotalCollInfo.setSewerageTax(arrTotalCollInfo.getSewerageTax().add(rcptDetail.getCramount()));

					if (PAYMENT_MODE_CASH.equals(paymentMode)) {
						arrTotalCashCollInfo.setSewerageTax(arrTotalCashCollInfo.getSewerageTax().add(
								rcptDetail.getCramount()));
					} else if (PAYMENT_MODE_CHEQUE.equals(paymentMode)) {
						arrTotalChequeCollInfo.setSewerageTax(arrTotalChequeCollInfo.getSewerageTax().add(
								rcptDetail.getCramount()));
					}
				} else if (GLCODEMAP_FOR_ARREARTAX.get(DEMANDRSN_CODE_LIBRARY_CESS).equals(glcode)) {

					arrCollInfo.setLightTax(arrCollInfo.getLightTax().add(
							rcptDetail.getCramount() != null ? rcptDetail.getCramount() : ZERO));
					arrTotalCollInfo.setLightTax(arrTotalCollInfo.getLightTax().add(rcptDetail.getCramount()));

					if (PAYMENT_MODE_CASH.equals(paymentMode)) {
						arrTotalCashCollInfo.setLightTax(arrTotalCashCollInfo.getLightTax().add(
								rcptDetail.getCramount()));
					} else if (PAYMENT_MODE_CHEQUE.equals(paymentMode)) {
						arrTotalChequeCollInfo.setLightTax(arrTotalChequeCollInfo.getLightTax().add(
								rcptDetail.getCramount()));
					}
				} else if (GLCODEMAP_FOR_ARREARTAX.get(DEMANDRSN_CODE_UNAUTHORIZED_PENALTY).equals(glcode)) {

					arrCollInfo.setWaterTax(arrCollInfo.getWaterTax().add(
							rcptDetail.getCramount() != null ? rcptDetail.getCramount() : ZERO));
					arrTotalCollInfo.setWaterTax(arrTotalCollInfo.getWaterTax().add(rcptDetail.getCramount()));

					if (PAYMENT_MODE_CASH.equals(paymentMode)) {
						arrTotalCashCollInfo.setWaterTax(arrTotalCashCollInfo.getWaterTax().add(
								rcptDetail.getCramount()));
					} else if (PAYMENT_MODE_CHEQUE.equals(paymentMode)) {
						arrTotalChequeCollInfo.setWaterTax(arrTotalChequeCollInfo.getWaterTax().add(
								rcptDetail.getCramount()));
					}
				}

				totalArrearTax = totalArrearTax.add(rcptDetail.getCramount() != null ? rcptDetail.getCramount() : ZERO);
			} else {
				if (GLCODEMAP_FOR_CURRENTTAX.get(DEMANDRSN_CODE_GENERAL_TAX).equals(glcode)) {

					currCollInfo.setGeneralTax(rcptDetail.getCramount());
					currTotalCollInfo.setGeneralTax(currTotalCollInfo.getGeneralTax().add(rcptDetail.getCramount()));

					if (PAYMENT_MODE_CASH.equals(paymentMode)) {
						currTotalCashCollInfo.setGeneralTax(currTotalCashCollInfo.getGeneralTax().add(
								rcptDetail.getCramount()));
					} else if (PAYMENT_MODE_CHEQUE.equals(paymentMode)) {
						currTotalChequeCollInfo.setGeneralTax(currTotalChequeCollInfo.getGeneralTax().add(
								rcptDetail.getCramount()));
					}
				} else if (GLCODEMAP_FOR_CURRENTTAX.get(DEMANDRSN_CODE_EDUCATIONAL_CESS).equals(glcode)) {

					currCollInfo.setSewerageTax(rcptDetail.getCramount());
					currTotalCollInfo.setSewerageTax(currTotalCollInfo.getSewerageTax().add(rcptDetail.getCramount()));

					if (PAYMENT_MODE_CASH.equals(paymentMode)) {
						currTotalCashCollInfo.setSewerageTax(currTotalCashCollInfo.getSewerageTax().add(
								rcptDetail.getCramount()));
					} else if (PAYMENT_MODE_CHEQUE.equals(paymentMode)) {
						currTotalChequeCollInfo.setSewerageTax(currTotalChequeCollInfo.getSewerageTax().add(
								rcptDetail.getCramount()));
					}

				} else if (GLCODEMAP_FOR_CURRENTTAX.get(DEMANDRSN_CODE_LIBRARY_CESS).equals(glcode)) {

					currCollInfo.setLightTax(rcptDetail.getCramount());
					currTotalCollInfo.setLightTax(currTotalCollInfo.getLightTax().add(rcptDetail.getCramount()));

					if (PAYMENT_MODE_CASH.equals(paymentMode)) {
						currTotalCashCollInfo.setLightTax(currTotalCashCollInfo.getLightTax().add(
								rcptDetail.getCramount()));
					} else if (PAYMENT_MODE_CHEQUE.equals(paymentMode)) {
						currTotalChequeCollInfo.setLightTax(currTotalChequeCollInfo.getLightTax().add(
								rcptDetail.getCramount()));
					}
				} else if (GLCODEMAP_FOR_CURRENTTAX.get(DEMANDRSN_CODE_UNAUTHORIZED_PENALTY).equals(glcode)) {

					currCollInfo.setWaterTax(rcptDetail.getCramount());
					currTotalCollInfo.setWaterTax(currTotalCollInfo.getWaterTax().add(rcptDetail.getCramount()));

					if (PAYMENT_MODE_CASH.equals(paymentMode)) {
						currTotalCashCollInfo.setWaterTax(currTotalCashCollInfo.getWaterTax().add(
								rcptDetail.getCramount()));
					} else if (PAYMENT_MODE_CHEQUE.equals(paymentMode)) {
						currTotalChequeCollInfo.setWaterTax(currTotalChequeCollInfo.getWaterTax().add(
								rcptDetail.getCramount()));
					}
				}

				totalCurrentTax = totalCurrentTax.add(rcptDetail.getCramount() != null ? rcptDetail.getCramount()
						: ZERO);
			}
		}
		arrTotalCollInfo.setTotal(arrTotalCollInfo.getTotal().add(totalArrearTax));
		currTotalCollInfo.setTotal(currTotalCollInfo.getTotal().add(totalCurrentTax));
		rebateTotalCollInfo.setTotal(rebateTotalCollInfo.getTotal().add(totalRebate));
		if (PAYMENT_MODE_CASH.equals(paymentMode)) {
			totalCashCollAmt = totalCashCollAmt.add(totalCurrentTax.add(totalArrearTax).subtract(totalRebate));
			arrTotalCashCollAmt = arrTotalCashCollAmt.add(totalArrearTax);
			currTotalCashCollAmt = currTotalCashCollAmt.add(totalCurrentTax);
			rebateTotalCashCollAmt = rebateTotalCashCollAmt.add(totalRebate);
		} else if (PAYMENT_MODE_CHEQUE.equals(paymentMode)) {
			totalChequeCollAmt = totalChequeCollAmt.add(totalCurrentTax.add(totalArrearTax).subtract(totalRebate));
			arrTotalChequeCollAmt = arrTotalChequeCollAmt.add(totalArrearTax);
			currTotalChequeCollAmt = currTotalChequeCollAmt.add(totalCurrentTax);
			rebateTotalChequeCollAmt = rebateTotalChequeCollAmt.add(totalRebate);
		} else {
			totalOthersCollAmt = totalOthersCollAmt.add(totalCurrentTax.add(totalArrearTax).subtract(totalRebate));
		}
		currTotalCashCollInfo.setTotal(currTotalCashCollAmt);
		arrTotalCashCollInfo.setTotal(arrTotalCashCollAmt);
		rebateTotalCashCollInfo.setTotal(rebateTotalCashCollAmt);
		currTotalChequeCollInfo.setTotal(currTotalChequeCollAmt);
		arrTotalChequeCollInfo.setTotal(arrTotalChequeCollAmt);
		rebateTotalChequeCollInfo.setTotal(rebateTotalChequeCollAmt);
		arrCollInfo.setTotal(totalArrearTax);
		currCollInfo.setTotal(totalCurrentTax);
		rebateCollInfo.setTotal(totalRebate);
		collInfoList.add(currCollInfo);
		collInfoList.add(arrCollInfo);
		collInfoList.add(rebateCollInfo);
		LOGGER.debug("Exited from getCollectionInfoList method");
		return collInfoList;
	}

	private void initializeTotalsInfo() {
		LOGGER.debug("Entered into initializeTotalsInfo");
		arrTotalCollInfo.setGeneralTax(ZERO);
		arrTotalCollInfo.setWaterTax(ZERO);
		arrTotalCollInfo.setBigBuildingCess(ZERO);
		arrTotalCollInfo.setEduCess(ZERO);
		arrTotalCollInfo.setEgsCess(ZERO);
		arrTotalCollInfo.setFireTax(ZERO);
		arrTotalCollInfo.setLightTax(ZERO);
		arrTotalCollInfo.setMiscTax(ZERO);
		arrTotalCollInfo.setSewerageTax(ZERO);
		arrTotalCollInfo.setTotal(ZERO);
		arrTotalCollInfo.setTaxType(ARREAR);

		currTotalCollInfo.setGeneralTax(ZERO);
		currTotalCollInfo.setWaterTax(ZERO);
		currTotalCollInfo.setBigBuildingCess(ZERO);
		currTotalCollInfo.setEduCess(ZERO);
		currTotalCollInfo.setEgsCess(ZERO);
		currTotalCollInfo.setFireTax(ZERO);
		currTotalCollInfo.setLightTax(ZERO);
		currTotalCollInfo.setMiscTax(ZERO);
		currTotalCollInfo.setSewerageTax(ZERO);
		currTotalCollInfo.setTotal(ZERO);
		currTotalCollInfo.setTaxType(CURRENT);

		rebateTotalCollInfo.setGeneralTax(ZERO);
		rebateTotalCollInfo.setTotal(ZERO);
		rebateTotalCollInfo.setTaxType(REBATE);

		arrTotalCashCollInfo.setGeneralTax(ZERO);
		arrTotalCashCollInfo.setWaterTax(ZERO);
		arrTotalCashCollInfo.setBigBuildingCess(ZERO);
		arrTotalCashCollInfo.setEduCess(ZERO);
		arrTotalCashCollInfo.setEgsCess(ZERO);
		arrTotalCashCollInfo.setFireTax(ZERO);
		arrTotalCashCollInfo.setLightTax(ZERO);
		arrTotalCashCollInfo.setMiscTax(ZERO);
		arrTotalCashCollInfo.setSewerageTax(ZERO);
		arrTotalCashCollInfo.setTotal(ZERO);
		arrTotalCashCollInfo.setTaxType(ARREAR);

		currTotalCashCollInfo.setGeneralTax(ZERO);
		currTotalCashCollInfo.setWaterTax(ZERO);
		currTotalCashCollInfo.setBigBuildingCess(ZERO);
		currTotalCashCollInfo.setEduCess(ZERO);
		currTotalCashCollInfo.setEgsCess(ZERO);
		currTotalCashCollInfo.setFireTax(ZERO);
		currTotalCashCollInfo.setLightTax(ZERO);
		currTotalCashCollInfo.setMiscTax(ZERO);
		currTotalCashCollInfo.setSewerageTax(ZERO);
		currTotalCashCollInfo.setTotal(ZERO);
		currTotalCashCollInfo.setTaxType(CURRENT);

		rebateTotalCashCollInfo.setGeneralTax(ZERO);
		rebateTotalCashCollInfo.setTotal(ZERO);
		rebateTotalCashCollInfo.setTaxType(REBATE);

		arrTotalChequeCollInfo.setGeneralTax(ZERO);
		arrTotalChequeCollInfo.setWaterTax(ZERO);
		arrTotalChequeCollInfo.setBigBuildingCess(ZERO);
		arrTotalChequeCollInfo.setEduCess(ZERO);
		arrTotalChequeCollInfo.setEgsCess(ZERO);
		arrTotalChequeCollInfo.setFireTax(ZERO);
		arrTotalChequeCollInfo.setLightTax(ZERO);
		arrTotalChequeCollInfo.setMiscTax(ZERO);
		arrTotalChequeCollInfo.setSewerageTax(ZERO);
		arrTotalChequeCollInfo.setTotal(ZERO);
		arrTotalChequeCollInfo.setTaxType(ARREAR);

		currTotalChequeCollInfo.setGeneralTax(ZERO);
		currTotalChequeCollInfo.setWaterTax(ZERO);
		currTotalChequeCollInfo.setBigBuildingCess(ZERO);
		currTotalChequeCollInfo.setEduCess(ZERO);
		currTotalChequeCollInfo.setEgsCess(ZERO);
		currTotalChequeCollInfo.setFireTax(ZERO);
		currTotalChequeCollInfo.setLightTax(ZERO);
		currTotalChequeCollInfo.setMiscTax(ZERO);
		currTotalChequeCollInfo.setSewerageTax(ZERO);
		currTotalChequeCollInfo.setTotal(ZERO);
		currTotalChequeCollInfo.setTaxType(CURRENT);

		rebateTotalChequeCollInfo.setGeneralTax(ZERO);
		rebateTotalChequeCollInfo.setTotal(ZERO);
		rebateTotalChequeCollInfo.setTaxType(REBATE);
		LOGGER.debug("Exited from initializeTotalsInfo method");
	}

	public ReportService getReportService() {
		return reportService;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public String getReportId() {
		return reportId;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Boolean getSearchForm() {
		return searchForm;
	}

	public void setSearchForm(Boolean searchForm) {
		this.searchForm = searchForm;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
