package org.egov.ptis.domain.service.bill;

import static org.egov.ptis.constants.PropertyTaxConstants.BILLTYPE_MANUAL;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.NOTICE_TYPE_BILL;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.REPORT_TEMPLATENAME_BILL_GENERATION;

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

import org.apache.log4j.Logger;
import org.egov.commons.Installment;
import org.egov.commons.dao.CommonsDaoFactory;
import org.egov.commons.dao.InstallmentDao;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infstr.commons.Module;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.commons.dao.ModuleDao;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
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
import org.egov.ptis.notice.PtNotice;
import org.egov.ptis.service.collection.DemandDetailsComparator;

/**
 * Provides API to Generate a Demand Notice or the Bill giving the break up of the tax amounts
 * 
 * @author nayeem
 * 
 */
public class BillService {

	private static final Logger LOGGER = Logger.getLogger(BillService.class);		
	private ReportService reportService;
	private NoticeService noticeService;
	private PropertyTaxUtil propertyTaxUtil;
	private NMCPTBillServiceImpl nmcPtBillServiceImpl;
	private PropertyTaxNumberGenerator propertyTaxNumberGenerator;

	private Map<String, Map<String, BigDecimal>> reasonwiseDues;
	private String billNo;

	InputStream billPDF;

	/**
	 * Generates a Demand Notice or the Bill giving the break up of the tax amounts and the <code>EgBill</code>
	 * 
	 * @see EgBill
	 * @param basicProperty
	 * @return
	 */
	public ReportOutput generateBill(BasicProperty basicProperty, Integer userId) {
		LOGGER.debug("Entered into generateBill BasicProperty : " + basicProperty);

		Integer reportId = -1;
		ReportRequest reportRequest = null;
		PtDemandDao ptDemandDao = PropertyDAOFactory.getDAOFactory().getPtDemandDao();
		EgDemand egDemand = ptDemandDao.getNonHistoryCurrDmdForProperty(basicProperty.getProperty());
		InstallmentDao isntalDao = CommonsDaoFactory.getDAOFactory().getInstallmentDao();
		ModuleDao moduleDao = GenericDaoFactory.getDAOFactory().getModuleDao();
		Module module = moduleDao.getModuleByName(PropertyTaxConstants.PTMODULENAME);
		List<EgDemandDetails> dmdDetailsList = new ArrayList<EgDemandDetails>(egDemand.getEgDemandDetails());
		Collections.sort(dmdDetailsList, new DemandDetailsComparator());
		Calendar calendar = Calendar.getInstance();
		Date startDate = dmdDetailsList.get(0).getEgDemandReason().getEgInstallmentMaster().getFromDate();
		Date endDate = dmdDetailsList.get(dmdDetailsList.size() - 1).getEgDemandReason().getEgInstallmentMaster().getFromDate();
		
		Installment currentInstall = isntalDao.getInsatllmentByModuleForGivenDate(module, new Date());
		String arrearsPeriod = null;
		
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
		reasonwiseDues = propertyTaxUtil.getDemandDues(basicProperty.getUpicNo());
		setBillNo(propertyTaxNumberGenerator.generateBillNumber(basicProperty.getPropertyID().getWard()
				.getBoundaryNum().toString()));

		PropertyBillInfo propertyBillInfo = new PropertyBillInfo(reasonwiseDues, basicProperty, billNo);
		propertyBillInfo.setArrearsPeriod(arrearsPeriod);
		propertyBillInfo.setCurrentPeriod(currentPeriod);
		reportRequest = new ReportRequest(REPORT_TEMPLATENAME_BILL_GENERATION, propertyBillInfo,
				new HashMap<String, Object>());

		ReportOutput reportOutput = getReportService().createReport(reportRequest);
		if (reportOutput != null && reportOutput.getReportOutputData() != null) {
			billPDF = new ByteArrayInputStream(reportOutput.getReportOutputData());
		}
		saveEgBill(basicProperty, userId);// saving eg_bill
		
		@SuppressWarnings("unused")
		PtNotice ptNotice = getNoticeService().saveNoticeToDMS(userId, billPDF, getBillNo(), NOTICE_TYPE_BILL,
				basicProperty);
		LOGGER.debug("generateBill - reportId : " + reportId);
		LOGGER.debug("Exiting from generateBill");
		return reportOutput;
	}

	private void saveEgBill(BasicProperty basicProperty, Integer userId) {
		LOGGER.debug("Entered into saveEgBill");
		LOGGER.debug("saveEgBill : BasicProperty: " + basicProperty);

		NMCPropertyTaxBillable nmcPTBill = new NMCPropertyTaxBillable();
		nmcPTBill.setBasicProperty(basicProperty);
		nmcPTBill.setUserId(userId.longValue());
		nmcPTBill.setReferenceNumber(getBillNo());
		nmcPTBill.setBillType(propertyTaxUtil.getBillTypeByCode(BILLTYPE_MANUAL));
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

	public NMCPTBillServiceImpl getNmcPtBillServiceImpl() {
		return nmcPtBillServiceImpl;
	}

	public void setNmcPtBillServiceImpl(NMCPTBillServiceImpl nmcPtBillServiceImpl) {
		this.nmcPtBillServiceImpl = nmcPtBillServiceImpl;
	}

	public NoticeService getNoticeService() {
		return noticeService;
	}

	public void setNoticeService(NoticeService noticeService) {
		this.noticeService = noticeService;
	}

	public PropertyTaxUtil getPropertyTaxUtil() {
		return propertyTaxUtil;
	}

	public void setPropertyTaxUtil(PropertyTaxUtil propertyTaxUtil) {
		this.propertyTaxUtil = propertyTaxUtil;
	}
}
