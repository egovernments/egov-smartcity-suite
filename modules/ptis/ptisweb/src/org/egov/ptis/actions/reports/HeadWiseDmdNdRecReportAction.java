package org.egov.ptis.actions.reports;

import static java.util.Calendar.YEAR;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.REPORT_TEMPLATENAME_HEADWISEDMDCOLL;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.commons.Installment;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.reporting.viewer.ReportViewerUtil;
import org.egov.ptis.bean.ReportInfo;
import org.egov.ptis.domain.entity.property.InstDmdCollMaterializeView;
import org.egov.ptis.nmc.util.PropertyTaxUtil;
import org.egov.web.actions.BaseFormAction;
import org.hibernate.Query;

@ParentPackage("egov")
public class HeadWiseDmdNdRecReportAction extends BaseFormAction{
	private final Logger LOGGER = Logger.getLogger(getClass());
	private ReportService reportService;
	private Integer reportId = -1;
	private BigDecimal arrDmdTotal = BigDecimal.ZERO;
	private BigDecimal currDmdTotal = BigDecimal.ZERO;
	private BigDecimal arrCollTotal = BigDecimal.ZERO;
	private BigDecimal currCollTotal = BigDecimal.ZERO;
	private BigDecimal arrDmdGrandTotal = BigDecimal.ZERO;
	private BigDecimal currDmdGrandTotal = BigDecimal.ZERO;
	private BigDecimal arrCollGrandTotal = BigDecimal.ZERO;
	private BigDecimal currCollGrandTotal = BigDecimal.ZERO;
	private Map<String, BigDecimal> dmdCollMap = new HashMap<String, BigDecimal>();
	
	private List arrInstDmdCollList = new ArrayList();
	private List currInstDmdCollList = new ArrayList();
	private Installment currentInstallment;
	
	public String generateHeadWiseDmdColl() {
		ReportRequest reportRequest = null;
		ReportInfo reportInfo = new ReportInfo();
		getPersistenceService().setType(InstDmdCollMaterializeView.class);
		
		currentInstallment = PropertyTaxUtil.getCurrentInstallment();
		
		prepareReasonWiseCurrDmdColl();
		prepareReasonWiseArrDmdColl();
		
		Calendar installmentFromDate = Calendar.getInstance();
		installmentFromDate.setTime(currentInstallment.getFromDate());
		
		Calendar installmentToDate = Calendar.getInstance();
		installmentToDate.setTime(currentInstallment.getToDate());
		
		reportInfo.setCurrInstallment("Year " + installmentFromDate.get(YEAR) + "-" + installmentToDate.get(YEAR));
		reportInfo.setDemandCollMap(dmdCollMap);
		
		reportRequest = new ReportRequest(REPORT_TEMPLATENAME_HEADWISEDMDCOLL, reportInfo, new HashMap<String, Object>());
		reportRequest.setPrintDialogOnOpenReport(true);
		reportId = ReportViewerUtil.addReportToSession(reportService.createReport(reportRequest), getSession());
		
		return "generate";
	}
	
	private void prepareReasonWiseCurrDmdColl() {
		LOGGER.info("Enter prepareReasonWiseCurrDmdColl");
		StringBuffer currDmdCollBuff = new StringBuffer(1500);
		currDmdCollBuff.append("select")
			.append(" dcb.installment.id, sum(dcb.generalTax), sum(dcb.generalTaxColl),")
			.append(" sum(dcb.egsTax), sum(dcb.egsTaxColl),")
			.append(" sum(dcb.eduCessResdTax) + sum(dcb.eduCessNonResdTax),")
			.append(" sum(dcb.eduCessResdTaxColl) + sum(dcb.eduCessNonResdTaxColl),")
			.append(" sum(dcb.waterTax), sum(dcb.waterTaxColl),")
			.append(" sum(dcb.fireTax), sum(dcb.fireTaxColl),")
			.append(" sum(dcb.sewerageTax), sum(dcb.sewerageTaxColl),")
			.append(" sum(dcb.lightTax), sum(dcb.lightTaxColl),")
			.append(" sum(dcb.bigBldgTax), sum(dcb.bigBldgTaxColl),")
			.append(" sum(dcb.chqBouncePenalty), sum(dcb.chqBuncPnltyColl),")
			.append(" sum(dcb.penaltyFine), sum(dcb.penaltyFineColl)")
			.append(" from InstDmdCollMaterializeView dcb, Installment inst, Module module")
			.append(" where")
			.append(" inst.module.id = module.id")
			.append(" and module.moduleName = 'Property Tax'")
			.append(" and inst.fromDate <= sysdate")
			.append(" and inst.toDate >= sysdate")
			.append(" and dcb.installment.id = inst.id")
			.append(" group by dcb.installment.id");
			
		Query qry = getPersistenceService().getSession().createQuery(currDmdCollBuff.toString());
		currInstDmdCollList = qry.list();
		Object[] currInst = (Object[]) currInstDmdCollList.get(0);
		
		dmdCollMap.put("currSewerageTax", ((BigDecimal) currInst[11]));
		dmdCollMap.put("currWaterTax", ((BigDecimal) currInst[7]));
		dmdCollMap.put("currGenTax", ((BigDecimal) currInst[1]));
		dmdCollMap.put("currFireTax", ((BigDecimal) currInst[9]));
		dmdCollMap.put("currLightTax", ((BigDecimal) currInst[13]));
		dmdCollMap.put("currEduTax", ((BigDecimal) currInst[5]));
		dmdCollMap.put("currEgsTax", ((BigDecimal) currInst[3]));
		dmdCollMap.put("currBigBldgTax", ((BigDecimal) currInst[15]));
		dmdCollMap.put("currSewerageTaxColl", ((BigDecimal) currInst[12]));
		dmdCollMap.put("currWaterTaxColl", ((BigDecimal) currInst[8]));
		dmdCollMap.put("currGenTaxColl", ((BigDecimal) currInst[2]));
		dmdCollMap.put("currFireTaxColl", ((BigDecimal) currInst[10]));
		dmdCollMap.put("currLightTaxColl", ((BigDecimal) currInst[14]));
		dmdCollMap.put("currEduTaxColl", ((BigDecimal) currInst[6]));
		dmdCollMap.put("currEgsTaxColl", ((BigDecimal) currInst[4]));
		dmdCollMap.put("currBigBldgTaxColl", ((BigDecimal) currInst[16]));
		
		dmdCollMap.put("currChqBuncPenalty", ((BigDecimal) currInst[17]));
		dmdCollMap.put("currPenaltyFine", ((BigDecimal) currInst[19]));
		dmdCollMap.put("currChqBncPntyColl", ((BigDecimal) currInst[18]));
		dmdCollMap.put("currPnltyFineColl", ((BigDecimal) currInst[20]));
		
		currDmdTotal = currDmdTotal.add((BigDecimal) currInst[11]).add((BigDecimal) currInst[7])
				.add((BigDecimal) currInst[1]).add((BigDecimal) currInst[9]).add((BigDecimal) currInst[13])
				.add((BigDecimal) currInst[5]).add((BigDecimal) currInst[3]).add((BigDecimal) currInst[15])
				.add((BigDecimal) currInst[17]).add((BigDecimal) currInst[19]);
		currCollTotal = currCollTotal.add((BigDecimal) currInst[12]).add((BigDecimal) currInst[8])
				.add((BigDecimal) currInst[2]).add((BigDecimal) currInst[10]).add((BigDecimal) currInst[14])
				.add((BigDecimal) currInst[6]).add((BigDecimal) currInst[4]).add((BigDecimal) currInst[16])
				.add((BigDecimal) currInst[18]).add((BigDecimal) currInst[20]);
		currDmdGrandTotal = currDmdGrandTotal.add(currDmdTotal);
		currCollGrandTotal = currCollGrandTotal.add(currCollTotal);
		
		dmdCollMap.put("currDmdTotal", currDmdTotal);
		dmdCollMap.put("currCollTotal", currCollTotal);
		dmdCollMap.put("currDmdGrandTotal", currDmdGrandTotal);
		dmdCollMap.put("currCollGrandTotal", currCollGrandTotal);
		
		LOGGER.info("Exit prepareReasonWiseCurrDmdColl");
	}
	
	private void prepareReasonWiseArrDmdColl() {
		LOGGER.info("Enter prepareReasonWiseArrDmdColl");
		
		StringBuffer arrDmdCollBuff = new StringBuffer(1500);
		
		arrDmdCollBuff.append("select")
			.append(" dcb.installment.id, sum(dcb.generalTax), sum(dcb.generalTaxColl),")
			.append(" sum(dcb.egsTax), sum(dcb.egsTaxColl), " )
			.append(" sum(dcb.eduCessResdTax) + sum(dcb.eduCessNonResdTax),")
			.append(" sum(dcb.eduCessResdTaxColl) + sum(dcb.eduCessNonResdTaxColl),")
			.append(" sum(dcb.waterTax), sum(dcb.waterTaxColl), sum(dcb.fireTax),")
			.append(" sum(dcb.fireTaxColl), sum(dcb.sewerageTax), sum(dcb.sewerageTaxColl),")
			.append(" sum(dcb.lightTax), sum(dcb.lightTaxColl), sum(dcb.bigBldgTax),")
			.append(" sum(dcb.bigBldgTaxColl), sum(dcb.chqBouncePenalty),")
			.append(" sum(dcb.chqBuncPnltyColl), sum(dcb.penaltyFine),")
			.append(" sum(dcb.penaltyFineColl)")
			.append(" from InstDmdCollMaterializeView dcb, Installment inst, Module module")
			.append(" where")
			.append(" inst.module.id = module.id")
			.append(" and module.moduleName = 'Property Tax'")
			.append(" and inst.toDate < :currInstFromDate")
			.append(" and dcb.installment.id = inst.id")
			.append(" group by dcb.installment.id");
		
		Query qry = getPersistenceService().getSession().createQuery(arrDmdCollBuff.toString());
		qry.setDate("currInstFromDate", currentInstallment.getFromDate());
		arrInstDmdCollList = qry.list();
		Object[] arrInst = (Object[]) arrInstDmdCollList.get(0);
		
		dmdCollMap.put("arrSewerageTax", ((BigDecimal) arrInst[11]));
		dmdCollMap.put("arrWaterTax", ((BigDecimal) arrInst[7]));
		dmdCollMap.put("arrGenTax", ((BigDecimal) arrInst[1]));
		dmdCollMap.put("arrFireTax", ((BigDecimal) arrInst[9]));
		dmdCollMap.put("arrLightTax", ((BigDecimal) arrInst[13]));
		dmdCollMap.put("arrEduTax", ((BigDecimal) arrInst[5]));
		dmdCollMap.put("arrEgsTax", ((BigDecimal) arrInst[3]));
		dmdCollMap.put("arrBigBldgTax", ((BigDecimal) arrInst[15]));
		dmdCollMap.put("arrSewerageTaxColl", ((BigDecimal) arrInst[12]));
		dmdCollMap.put("arrWaterTaxColl", ((BigDecimal) arrInst[8]));
		dmdCollMap.put("arrGenTaxColl", ((BigDecimal) arrInst[2]));
		dmdCollMap.put("arrFireTaxColl", ((BigDecimal) arrInst[10]));
		dmdCollMap.put("arrLightTaxColl", ((BigDecimal) arrInst[14]));
		dmdCollMap.put("arrEduTaxColl", ((BigDecimal) arrInst[6]));
		dmdCollMap.put("arrEgsTaxColl", ((BigDecimal) arrInst[4]));
		dmdCollMap.put("arrBigBldgTaxColl", ((BigDecimal) arrInst[16]));
		
		dmdCollMap.put("arrChqBuncPenalty", ((BigDecimal) arrInst[17]));
		dmdCollMap.put("arrPenaltyFine", ((BigDecimal) arrInst[19]));
		dmdCollMap.put("arrChqBuncPnltyColl", ((BigDecimal) arrInst[18]));
		dmdCollMap.put("arrPenaltyFineColl", ((BigDecimal) arrInst[20]));
		
		arrDmdTotal = arrDmdTotal.add((BigDecimal) arrInst[11]).add((BigDecimal) arrInst[7]).add((BigDecimal) arrInst[1])
					.add((BigDecimal) arrInst[9]).add((BigDecimal) arrInst[13]).add((BigDecimal) arrInst[5])
					.add((BigDecimal) arrInst[3]).add((BigDecimal) arrInst[15])
					.add((BigDecimal) arrInst[17]).add((BigDecimal) arrInst[19]);
		arrCollTotal = arrCollTotal.add((BigDecimal) arrInst[12]).add((BigDecimal) arrInst[8]).add((BigDecimal) arrInst[2])
					.add((BigDecimal) arrInst[10]).add((BigDecimal) arrInst[14]).add((BigDecimal) arrInst[6])
					.add((BigDecimal) arrInst[4]).add((BigDecimal) arrInst[16])
					.add((BigDecimal) arrInst[18]).add((BigDecimal) arrInst[20]);
		arrDmdGrandTotal = arrDmdGrandTotal.add(arrDmdTotal);
		arrCollGrandTotal = arrCollGrandTotal.add(arrCollTotal);
		
		dmdCollMap.put("arrDmdTotal", arrDmdTotal);
		dmdCollMap.put("arrCollTotal", arrCollTotal);
		dmdCollMap.put("arrDmdGrandTotal", arrDmdGrandTotal);
		dmdCollMap.put("arrCollGrandTotal", arrCollGrandTotal);
		
		LOGGER.info("Exit from prepareReasonWiseArrDmdColl");
	}
	
	public ReportService getReportService() {
		return reportService;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public Integer getReportId() {
		return reportId;
	}

	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}

	@Override
	public Object getModel() {
		return null;
	}
	
}
