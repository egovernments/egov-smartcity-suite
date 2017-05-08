/*
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
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
 */
package org.egov.ptis.actions.reports;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Installment;
import org.egov.infra.reporting.engine.ReportDataSourceType;
import org.egov.infra.web.struts.actions.ReportFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.ptis.bean.ReportInfo;
import org.egov.ptis.domain.entity.property.InstDmdCollMaterializeView;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.hibernate.Criteria;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.egov.ptis.constants.PropertyTaxConstants.REPORT_TEMPLATENAME_BIGBUILDINGRECOVERY;

@ParentPackage("egov")
public class BigBuildingRecoveryReportAction extends ReportFormAction {
	private final Logger LOGGER = Logger.getLogger(getClass());
	private Date fromDate;
	private Date toDate;
	private BigDecimal arrBigBldgTaxColl = BigDecimal.ZERO;
	private BigDecimal currBigBldgTaxColl = BigDecimal.ZERO;
	private Map<String, BigDecimal> infoMap = new HashMap<String, BigDecimal>();
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
	
	@Autowired
	private PropertyTaxCommonUtils propertyTaxCommonUtils;
	
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
		
		List<InstDmdCollMaterializeView> instDmdCollList = (List<InstDmdCollMaterializeView>) getPersistenceService()
				.getSession()
				.createQuery(
						"from InstDmdCollMaterializeView instDmdColl left join fetch instDmdColl.installment where instDmdColl.createdDate between ? and ? and instDmdColl.bigBldgTaxColl != 0 ")
				.setDate(0, fromDate).setDate(1, toDate).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
		Installment currInstallment = propertyTaxCommonUtils.getCurrentInstallment();		
		for (InstDmdCollMaterializeView instDmdColl : instDmdCollList) {
		    // TODO : commented as part of DCB report story. 	
		    /*if (instDmdColl.getInstallment().compareTo(currInstallment) < 0) {
				if (!instDmdColl.getBigBldgTaxColl().equals(BigDecimal.ZERO)) {
					arrBigBldgTaxColl = arrBigBldgTaxColl.add(instDmdColl.getBigBldgTaxColl());
				}
			} else {
				if (!instDmdColl.getBigBldgTaxColl().equals(BigDecimal.ZERO)) {
					currBigBldgTaxColl = currBigBldgTaxColl.add(instDmdColl.getBigBldgTaxColl());
					totalNoProps++;
				}
			}*/
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
