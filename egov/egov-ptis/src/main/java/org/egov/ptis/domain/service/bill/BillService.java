package org.egov.ptis.domain.service.bill;

import static org.egov.ptis.constants.PropertyTaxConstants.BILLTYPE_MANUAL;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.commons.Installment;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.service.notice.NoticeService;
import org.egov.ptis.nmc.bill.NMCPTBillServiceImpl;
import org.egov.ptis.nmc.util.PropertyTaxNumberGenerator;
import org.egov.ptis.nmc.util.PropertyTaxUtil;

/**
 * Provides API to Generate a Demand Notice or the Bill giving the break up of the tax amounts
 *
 * @author nayeem
 *
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

	/**
	 * Generates a Demand Notice or the Bill giving the break up of the tax amounts and the <code>EgBill</code>
	 *
	 * @see EgBill
	 * @param basicProperty
	 * @return
	 */
	//TODO -- Fix this once demand code is available
	/*public ReportOutput generateBill(BasicProperty basicProperty, Integer userId) {
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

		Date firstSixMonthEndDate = DateUtils.add(DateUtils.add(currentInstall.getFromDate(), MONTH, 6), DAY_OF_MONTH, -1);

		String firstSixMonthsPeriod = dateFormat.format(currentInstall.getFromDate())
				+ STR_TO + dateFormat.format(firstSixMonthEndDate);

		String secondSixMonthsPeriod = dateFormat.format(DateUtils.add(firstSixMonthEndDate, DAY_OF_MONTH, 1))
				+ STR_TO + dateFormat.format(currentInstall.getToDate());

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
		setBillNo(propertyTaxNumberGenerator.generateManualBillNumber(basicProperty.getPropertyID()));

		int noOfBillGenerated = getNumberOfBills(basicProperty);

		if (noOfBillGenerated > 0) {
			setBillNo(getBillNo() + "/" + STR_BILL_SHORTCUT + noOfBillGenerated);
		}

		PropertyBillInfo propertyBillInfo = new PropertyBillInfo(reasonwiseDues, basicProperty, billNo);
		propertyBillInfo.setArrearsPeriod(arrearsPeriod);
		propertyBillInfo.setCurrentPeriod(currentPeriod);
		propertyBillInfo.setFirstSixMonthsPeriod(firstSixMonthsPeriod);
		propertyBillInfo.setSecondSixMonthsPeriod(secondSixMonthsPeriod);

		reportRequest = new ReportRequest(REPORT_TEMPLATENAME_BILL_GENERATION, propertyBillInfo,
				new HashMap<String, Object>());

		ReportOutput reportOutput = getReportService().createReport(reportRequest);

		if (reportOutput != null && reportOutput.getReportOutputData() != null) {
			billPDF = new ByteArrayInputStream(reportOutput.getReportOutputData());
		}

		saveEgBill(basicProperty, userId);// saving eg_bill

		noticeService.saveNotice(getBillNo(), NOTICE_TYPE_BILL, basicProperty, billPDF);

		LOGGER.debug("generateBill - reportId : " + reportId);
		LOGGER.debug("Exiting from generateBill");
		return reportOutput;
	}*/

	/**
	 * Gives the count of generated bills
	 *
	 * @param basicProperty
	 * @return
	 */
	private int getNumberOfBills(BasicProperty basicProperty) {
		Installment currentInstallment = PropertyTaxUtil.getCurrentInstallment();

		Long count = (Long) HibernateUtil.getCurrentSession().createQuery("SELECT COUNT (*) FROM EgBill " +
				        "WHERE module = ? " +
						"AND egBillType.code = ? " +
						"AND SUBSTRING(consumerId, 1, (LOCATE('(', consumerId)-1)) = ? " +
						"AND is_Cancelled = 'N' " +
						"AND issueDate between ? and ? ")
						.setEntity(0, currentInstallment.getModule())
						.setString(1, BILLTYPE_MANUAL)
						.setString(2, basicProperty.getUpicNo())
						.setDate(3, currentInstallment.getFromDate())
						.setDate(4, currentInstallment.getToDate()).list().get(0);

		return count.intValue();
	}

	private void saveEgBill(BasicProperty basicProperty, Integer userId) {
		//TODO -- Fix this once demand code is available
		/*LOGGER.debug("Entered into saveEgBill");
		LOGGER.debug("saveEgBill : BasicProperty: " + basicProperty);

		NMCPropertyTaxBillable nmcPTBill = new NMCPropertyTaxBillable();
		nmcPTBill.setBasicProperty(basicProperty);
		nmcPTBill.setUserId(userId.longValue());
		nmcPTBill.setReferenceNumber(getBillNo());
		nmcPTBill.setBillType(propertyTaxUtil.getBillTypeByCode(BILLTYPE_MANUAL));
		nmcPTBill.setLevyPenalty(Boolean.TRUE);
		EgBill egBill = nmcPtBillServiceImpl.generateBill(nmcPTBill);

		LOGGER.debug("Exit from saveEgBill, EgBill: " + egBill);*/
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
