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

import static org.egov.ptis.constants.PropertyTaxConstants.BILLTYPE_MANUAL;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_BILL;
import static org.egov.ptis.constants.PropertyTaxConstants.REPORT_TEMPLATENAME_BILL_GENERATION;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentDao;
import org.egov.demand.model.EgBill;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.ptis.client.bill.PTBillServiceImpl;
import org.egov.ptis.client.model.calculator.DemandNoticeInfo;
import org.egov.ptis.client.util.PropertyTaxNumberGenerator;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.bill.PropertyTaxBillable;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.service.notice.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;

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
	@Autowired
        private PropertyTaxUtil propertyTaxUtil;
	private PTBillServiceImpl nmcPtBillServiceImpl;
	private PropertyTaxNumberGenerator propertyTaxNumberGenerator;
	private Map<String, Map<String, BigDecimal>> reasonwiseDues;
	private String billNo;
	InputStream billPDF;
	@Autowired
	private ModuleService moduleDao;
	@Autowired
	private InstallmentDao installmentDao;
	@Autowired
	private PtDemandDao ptDemandDAO;
	@Autowired
	private PropertyTaxBillable propertyTaxBillable;

	/**
	 * Generates a Demand Notice or the Bill giving the break up of the tax
	 * amounts and the <code>EgBill</code>
	 *
	 * @see EgBill
	 * @param basicProperty
	 * @return
	 */
	public ReportOutput generateBill(BasicProperty basicProperty, Integer userId) {
		LOGGER.debug("Entered into generateBill BasicProperty : " + basicProperty);
		ReportOutput reportOutput=null;
		try{
        		setBillNo(propertyTaxNumberGenerator
                                .generateManualBillNumber(basicProperty.getPropertyID()));
        		int noOfBillGenerated = getNumberOfBills(basicProperty);
        		if (noOfBillGenerated > 0) {
        		    setBillNo(getBillNo() + "/" + STR_BILL_SHORTCUT + noOfBillGenerated);
        		}
        		//To generate Notice having installment and reasonwise balance for a property
                         DemandNoticeInfo demandNoticeInfo = new DemandNoticeInfo();
                         demandNoticeInfo.setBasicProperty(basicProperty);
                         demandNoticeInfo.setBillNo(getBillNo());
                         demandNoticeInfo.setDemandNoticeDetailsInfo(propertyTaxUtil.getDemandNoticeDetailsInfo(basicProperty));
                         
                         ReportRequest reportRequest = null;
                         reportRequest = new ReportRequest(REPORT_TEMPLATENAME_BILL_GENERATION,demandNoticeInfo,new HashMap<String, Object>());
                         reportOutput = getReportService().createReport(reportRequest); 
                         if (reportOutput != null && reportOutput.getReportOutputData() != null) {
                             billPDF = new ByteArrayInputStream(reportOutput.getReportOutputData());
                         }
        		saveEgBill(basicProperty, userId);// saving eg_bill 
        		noticeService.saveNotice(getBillNo(), NOTICE_TYPE_BILL, basicProperty, billPDF);// Save Notice
		} catch (final Exception e) {
	              throw new EGOVRuntimeException("Bill Generation Exception : " + e);
	        }
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
		Installment currentInstallment = propertyTaxUtil.getCurrentInstallment();

		Long count = (Long) HibernateUtil
				.getCurrentSession()
				.createQuery(
						"SELECT COUNT (*) FROM EgBill " + "WHERE module = ? "
								+ "AND egBillType.code = ? "
								+ "AND SUBSTRING(consumerId, 1, (LOCATE('(', consumerId)-1)) = ? "
								+ "AND is_Cancelled = 'N' " + "AND issueDate between ? and ? ")
				.setEntity(0, currentInstallment.getModule()).setString(1, BILLTYPE_MANUAL)
				.setString(2, basicProperty.getUpicNo())
				.setDate(3, currentInstallment.getFromDate())
				.setDate(4, currentInstallment.getToDate()).list().get(0);

		return count.intValue();
	}

	private void saveEgBill(BasicProperty basicProperty, Integer userId) {
		LOGGER.debug("Entered into saveEgBill");
		LOGGER.debug("saveEgBill : BasicProperty: " + basicProperty);
		propertyTaxBillable.setBasicProperty(basicProperty);
		propertyTaxBillable.setUserId(userId.longValue());
		propertyTaxBillable.setReferenceNumber(getBillNo());
		propertyTaxBillable.setBillType(propertyTaxUtil.getBillTypeByCode(BILLTYPE_MANUAL));
		propertyTaxBillable.setLevyPenalty(Boolean.TRUE);
		EgBill egBill = nmcPtBillServiceImpl.generateBill(propertyTaxBillable);
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

	public void setReasonwiseDues(Map<String, Map<String, BigDecimal>> reasonwiseDues) {
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

	public void setPropertyTaxNumberGenerator(PropertyTaxNumberGenerator propertyTaxNumberGenerator) {
		this.propertyTaxNumberGenerator = propertyTaxNumberGenerator;
	}

	public PTBillServiceImpl getNmcPtBillServiceImpl() {
		return nmcPtBillServiceImpl;
	}

	public void setNmcPtBillServiceImpl(PTBillServiceImpl nmcPtBillServiceImpl) {
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
