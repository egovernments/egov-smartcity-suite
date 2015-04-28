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
package org.egov.ptis.domain.service.bill;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static org.egov.ptis.constants.PropertyTaxConstants.BILLTYPE_MANUAL;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.NOTICE_TYPE_BILL;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.REPORT_TEMPLATENAME_BILL_GENERATION;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.dateFormat;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentDao;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infstr.commons.Module;
import org.egov.infstr.commons.dao.ModuleDao;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.PropertyDAOFactory;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.service.notice.NoticeService;
import org.egov.ptis.nmc.bill.NMCPTBillServiceImpl;
import org.egov.ptis.nmc.bill.NMCPropertyTaxBillable;
import org.egov.ptis.nmc.model.PropertyBillInfo;
import org.egov.ptis.nmc.util.PropertyTaxNumberGenerator;
import org.egov.ptis.nmc.util.PropertyTaxUtil;
import org.egov.ptis.service.collection.DemandDetailsComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Provides API to Generate a Demand Notice or the Bill giving the break up of
 * the tax amounts
 */
public class BillService {

	private static final Logger LOGGER = Logger.getLogger(BillService.class);
	private static final String STR_TO = " to ";
	private static final String STR_BILL_SHORTCUT = "B";

	private ReportService reportService;
	private NoticeService noticeService;
	private PropertyTaxUtil propertyTaxUtil;
	private NMCPTBillServiceImpl nmcPtBillServiceImpl;
	private PropertyTaxNumberGenerator propertyTaxNumberGenerator;
	private Map<String, Map<String, BigDecimal>> reasonwiseDues;
	private String billNo;
	InputStream billPDF;
	@Autowired
	@Qualifier(value = "moduleDAO")
	private ModuleDao moduleDao;
	@Autowired
	private InstallmentDao installmentDao;

	/**
	 * Generates a Demand Notice or the Bill giving the break up of the tax
	 * amounts and the <code>EgBill</code>
	 *
	 * @see EgBill
	 * @param basicProperty
	 * @return
	 */
	public ReportOutput generateBill(BasicProperty basicProperty, Integer userId) {
		LOGGER.debug("Entered into generateBill BasicProperty : "
				+ basicProperty);

		Integer reportId = -1;
		ReportRequest reportRequest = null;
		PtDemandDao ptDemandDao = PropertyDAOFactory.getDAOFactory()
				.getPtDemandDao();
		EgDemand egDemand = ptDemandDao
				.getNonHistoryCurrDmdForProperty(basicProperty.getProperty());
		Module module = moduleDao
				.getModuleByName(PropertyTaxConstants.PTMODULENAME);
		List<EgDemandDetails> dmdDetailsList = new ArrayList<EgDemandDetails>(
				egDemand.getEgDemandDetails());
		Collections.sort(dmdDetailsList, new DemandDetailsComparator());
		Calendar calendar = Calendar.getInstance();
		Date startDate = dmdDetailsList.get(0).getEgDemandReason()
				.getEgInstallmentMaster().getFromDate();
		Date endDate = dmdDetailsList.get(dmdDetailsList.size() - 1)
				.getEgDemandReason().getEgInstallmentMaster().getFromDate();

		Installment currentInstall = installmentDao
				.getInsatllmentByModuleForGivenDate(module, new Date());
		String arrearsPeriod = null;

		Date firstSixMonthEndDate = DateUtils.add(
				DateUtils.add(currentInstall.getFromDate(), MONTH, 6),
				DAY_OF_MONTH, -1);

		String firstSixMonthsPeriod = dateFormat.format(currentInstall
				.getFromDate())
				+ STR_TO
				+ dateFormat.format(firstSixMonthEndDate);

		String secondSixMonthsPeriod = dateFormat.format(DateUtils.add(
				firstSixMonthEndDate, DAY_OF_MONTH, 1))
				+ STR_TO
				+ dateFormat.format(currentInstall.getToDate());

		if (!startDate.equals(currentInstall.getFromDate())) {
			calendar.setTime(startDate);
			int fromYear = calendar.get(Calendar.YEAR);
			calendar.setTime(endDate);
			int endYear = calendar.get(Calendar.YEAR);
			arrearsPeriod = fromYear + "-" + endYear;
		}

		calendar.setTime(currentInstall.getFromDate());
		int currFromYear = calendar.get(Calendar.YEAR);
		calendar.setTime(currentInstall.getToDate());
		int currToYear = calendar.get(Calendar.YEAR);
		String currentPeriod = currFromYear + "-" + currToYear;
		reasonwiseDues = propertyTaxUtil.getDemandDues(basicProperty
				.getUpicNo());
		setBillNo(propertyTaxNumberGenerator
				.generateManualBillNumber(basicProperty.getPropertyID()));

		int noOfBillGenerated = getNumberOfBills(basicProperty);

		if (noOfBillGenerated > 0) {
			setBillNo(getBillNo() + "/" + STR_BILL_SHORTCUT + noOfBillGenerated);
		}

		PropertyBillInfo propertyBillInfo = new PropertyBillInfo(
				reasonwiseDues, basicProperty, billNo);
		propertyBillInfo.setArrearsPeriod(arrearsPeriod);
		propertyBillInfo.setCurrentPeriod(currentPeriod);
		propertyBillInfo.setFirstSixMonthsPeriod(firstSixMonthsPeriod);
		propertyBillInfo.setSecondSixMonthsPeriod(secondSixMonthsPeriod);

		reportRequest = new ReportRequest(REPORT_TEMPLATENAME_BILL_GENERATION,
				propertyBillInfo, new HashMap<String, Object>());

		ReportOutput reportOutput = getReportService().createReport(
				reportRequest);

		if (reportOutput != null && reportOutput.getReportOutputData() != null) {
			billPDF = new ByteArrayInputStream(
					reportOutput.getReportOutputData());
		}

		saveEgBill(basicProperty, userId);// saving eg_bill

		noticeService.saveNotice(getBillNo(), NOTICE_TYPE_BILL, basicProperty,
				billPDF);

		LOGGER.debug("generateBill - reportId : " + reportId);
		LOGGER.debug("Exiting from generateBill");
		return reportOutput;
	}

	/**
	 * Gives the count of generated bills
	 *
	 * @param basicProperty
	 * @return
	 */
	private int getNumberOfBills(BasicProperty basicProperty) {
		Installment currentInstallment = PropertyTaxUtil
				.getCurrentInstallment();

		Long count = (Long) HibernateUtil
				.getCurrentSession()
				.createQuery(
						"SELECT COUNT (*) FROM EgBill "
								+ "WHERE module = ? "
								+ "AND egBillType.code = ? "
								+ "AND SUBSTRING(consumerId, 1, (LOCATE('(', consumerId)-1)) = ? "
								+ "AND is_Cancelled = 'N' "
								+ "AND issueDate between ? and ? ")
				.setEntity(0, currentInstallment.getModule())
				.setString(1, BILLTYPE_MANUAL)
				.setString(2, basicProperty.getUpicNo())
				.setDate(3, currentInstallment.getFromDate())
				.setDate(4, currentInstallment.getToDate()).list().get(0);

		return count.intValue();
	}

	private void saveEgBill(BasicProperty basicProperty, Integer userId) {
		LOGGER.debug("Entered into saveEgBill");
		LOGGER.debug("saveEgBill : BasicProperty: " + basicProperty);

		NMCPropertyTaxBillable nmcPTBill = new NMCPropertyTaxBillable();
		nmcPTBill.setBasicProperty(basicProperty);
		nmcPTBill.setUserId(userId.longValue());
		nmcPTBill.setReferenceNumber(getBillNo());
		nmcPTBill.setBillType(propertyTaxUtil
				.getBillTypeByCode(BILLTYPE_MANUAL));
		nmcPTBill.setLevyPenalty(Boolean.TRUE);
		EgBill egBill = nmcPtBillServiceImpl.generateBill(nmcPTBill);

		LOGGER.debug("Exit from saveEgBill, EgBill: " + egBill);
	}

	public ReportService getReportService() {
		return reportService;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public Map<String, Map<String, BigDecimal>> getReasonwiseDues() {
		return reasonwiseDues;
	}

	public void setReasonwiseDues(
			Map<String, Map<String, BigDecimal>> reasonwiseDues) {
		this.reasonwiseDues = reasonwiseDues;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public InputStream getBillPDF() {
		return billPDF;
	}

	public void setBillPDF(InputStream billPDF) {
		this.billPDF = billPDF;
	}

	public PropertyTaxNumberGenerator getPropertyTaxNumberGenerator() {
		return propertyTaxNumberGenerator;
	}

	public void setPropertyTaxNumberGenerator(
			PropertyTaxNumberGenerator propertyTaxNumberGenerator) {
		this.propertyTaxNumberGenerator = propertyTaxNumberGenerator;
	}

	public NMCPTBillServiceImpl getNmcPtBillServiceImpl() {
		return nmcPtBillServiceImpl;
	}

	public void setNmcPtBillServiceImpl(
			NMCPTBillServiceImpl nmcPtBillServiceImpl) {
		this.nmcPtBillServiceImpl = nmcPtBillServiceImpl;
	}

	public PropertyTaxUtil getPropertyTaxUtil() {
		return propertyTaxUtil;
	}

	public void setPropertyTaxUtil(PropertyTaxUtil propertyTaxUtil) {
		this.propertyTaxUtil = propertyTaxUtil;
	}

	public void setNoticeService(NoticeService noticeService) {
		this.noticeService = noticeService;
	}
}
