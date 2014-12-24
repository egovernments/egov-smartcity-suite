package org.egov.ptis.actions.reports;

import static org.egov.ptis.nmc.constants.NMCPTISConstants.REPORT_TEMPLATENAME_BIGBUILDINGRECOVERY;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Installment;
import org.egov.infstr.reporting.engine.ReportRequest.ReportDataSourceType;
import org.egov.ptis.bean.ReportInfo;
import org.egov.ptis.domain.entity.property.InstDmdCollMaterializeView;
import org.egov.ptis.nmc.util.PropertyTaxUtil;
import org.egov.web.actions.ReportFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.hibernate.Criteria;

@ParentPackage("egov")
public class BigBuildingRecoveryReportAction extends ReportFormAction {
	private final Logger LOGGER = Logger.getLogger(getClass());
	private Date fromDate;
	private Date toDate;
	private BigDecimal arrBigBldgTaxColl = BigDecimal.ZERO;
	private BigDecimal currBigBldgTaxColl = BigDecimal.ZERO;
	private Map<String, BigDecimal> infoMap = new HashMap<String, BigDecimal>();
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
	
	@SkipValidation
	public String newForm() {
		return NEW;
	}
	
	public void validate() {
		if (getFromDate() == null) {
			addActionError(getText("mandatory.fromdate"));
		} 
		if (getToDate() == null) {
			addActionError(getText("mandatory.todate"));
		}
		if (getFromDate() != null && getFromDate().after(new Date())) {
			addActionError(getText("mandatory.fromDtBeforeCurrDt"));
		}
		if (getToDate() != null && getToDate().after(new Date())) {
			addActionError(getText("mandatory.toDtBeforeCurrDt"));
		}
	}

	@ValidationErrorPage(value=NEW)
	public String generateBigBldgRecStmt() {
		ReportInfo reportInfo = prepareReportInfo();
		setDataSourceType(ReportDataSourceType.JAVABEAN);
		setReportData(reportInfo);
		super.report();
		
		return "generate";
	}
	
	@SuppressWarnings("unchecked")
	private ReportInfo prepareReportInfo() {
		ReportInfo reportInfo = new ReportInfo();
		Integer totalNoProps = 0;
		
		getPersistenceService().setType(InstDmdCollMaterializeView.class);
		List<InstDmdCollMaterializeView> instDmdCollList = getPersistenceService()
				.getSession()
				.createQuery(
						"from InstDmdCollMaterializeView instDmdColl left join fetch instDmdColl.installment where instDmdColl.createdDate between ? and ? and instDmdColl.bigBldgTaxColl != 0 ")
				.setDate(0, fromDate).setDate(1, toDate).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
		Installment currInstallment = PropertyTaxUtil.getCurrentInstallment();		
		for (InstDmdCollMaterializeView instDmdColl : instDmdCollList) {
			if (instDmdColl.getInstallment().compareTo(currInstallment) < 0) {
				if (!instDmdColl.getBigBldgTaxColl().equals(BigDecimal.ZERO)) {
					arrBigBldgTaxColl = arrBigBldgTaxColl.add(instDmdColl.getBigBldgTaxColl());
				}
			} else {
				if (!instDmdColl.getBigBldgTaxColl().equals(BigDecimal.ZERO)) {
					currBigBldgTaxColl = currBigBldgTaxColl.add(instDmdColl.getBigBldgTaxColl());
					totalNoProps++;
				}
			}
		}
		String fdate = dateFormatter.format(fromDate);
		String tdate = dateFormatter.format(toDate);
		
		infoMap.put("arrBigBldgTaxColl", arrBigBldgTaxColl.setScale(2));
		infoMap.put("currBigBldgTaxColl", currBigBldgTaxColl.setScale(2));
		reportInfo.setDateString(fdate + " to " + tdate);
		reportInfo.setTotalNoProps(totalNoProps);
		reportInfo.setDemandCollMap(infoMap);
		
		return reportInfo;
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

	public BigDecimal getArrBigBldgTaxColl() {
		return arrBigBldgTaxColl;
	}

	public void setArrBigBldgTaxColl(BigDecimal arrBigBldgTaxColl) {
		this.arrBigBldgTaxColl = arrBigBldgTaxColl;
	}

	public BigDecimal getCurrBigBldgTaxColl() {
		return currBigBldgTaxColl;
	}

	public void setCurrBigBldgTaxColl(BigDecimal currBigBldgTaxColl) {
		this.currBigBldgTaxColl = currBigBldgTaxColl;
	}

	public Map<String, BigDecimal> getInfoMap() {
		return infoMap;
	}

	public void setInfoMap(Map<String, BigDecimal> infoMap) {
		this.infoMap = infoMap;
	}

	@Override
	public String criteria() {
		return null;
	}

	@Override
	protected String getReportTemplateName() {
		return REPORT_TEMPLATENAME_BIGBUILDINGRECOVERY;
	}

}
