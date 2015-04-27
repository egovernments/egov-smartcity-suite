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
package org.egov.ptis.actions.reports;

import static org.egov.ptis.constants.PropertyTaxConstants.BILLTYPE_MANUAL;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.REPORT_TEMPLATENAME_DCBREPORT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.ZONE_BNDRY_TYPE;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.commons.Installment;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infstr.reporting.engine.ReportRequest.ReportDataSourceType;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.ptis.actions.common.CommonServices;
import org.egov.ptis.bean.ReportInfo;
import org.egov.ptis.nmc.util.PropertyTaxUtil;
import org.egov.web.actions.ReportFormAction;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;

@ParentPackage("egov")
public class DCBReportAction extends ReportFormAction {

	private final Logger LOGGER = Logger.getLogger(getClass());
	private List<ReportInfo> reportInfoList = new ArrayList<ReportInfo>();
	private Long zoneId;
	private Map<Long, String> ZoneBndryMap;
	List<Boundary> zoneList;
	
	@Autowired
	private BoundaryDAO boundaryDAO;
	
	private void prepareReportInfo() {
		StringBuffer query = new StringBuffer(1000);
		StringBuilder billQueryString = new StringBuilder();
		Installment currentInstallment = PropertyTaxUtil.getCurrentInstallment(); 
		List<Long> zoneParamList = new ArrayList<Long>();
		
		billQueryString.append("select propMatView.zone.id, propMatView.ward.id, propMatView.partNo, count(*) ")
				.append("from EgBill bill, PropertyMaterlizeView propMatView, PtNotice notice left join notice.basicProperty bp ")
				.append("where bp.propertyID.zone.id=propMatView.zone.id ")
				.append("and bp.propertyID.ward.id=propMatView.ward.id ")
				.append("and bill.is_History = 'N' ")
				.append("and bp.id = propMatView.basicPropertyID ")
				.append("and :FromDate <= bill.issueDate ")
				.append("and :ToDate >= bill.issueDate ")
				.append("and bill.egBillType.code = :BillType ")
				.append("and bill.billNo = notice.noticeNo ")
				.append("and notice.noticeType = 'Bill' ")
				.append("and notice.noticeFile is not null ")
				.append("and propMatView.zone.id in (:ZoneId) ")
				.append("group by propMatView.zone.id, propMatView.ward.id, propMatView.partNo ");
		
		query.append("select zone.id, ward.id, partNo, count(*) as totalProp, sum(aggrCurrDmd) as currDmd,")
			.append(" sum(aggrArrDmd) as arrDmd, sum(aggrCurrColl) as currColl, sum(aggrArrColl) as arrColl, ")
			.append(" (sum(aggrCurrDmd) + sum(aggrArrDmd)) as totalDmd, (sum(aggrCurrColl) + sum(aggrArrColl)) as totalColl ")
			.append(" from PropertyMaterlizeView ")
			.append(" where zone.id in (:ZoneId)")
		    .append(" group by zone.id, ward.id, partNo")
		    .append(" order by zone.id, ward.id, LPAD(partNo,'20',0)");
		
		if(!zoneId.equals(new Integer(-1)) && !zoneId.equals(new Integer(0))) {
			zoneParamList.add(zoneId);
		}
		else if(zoneId.equals(new Integer(0))) {
			for(Boundary bndry : zoneList) {
				zoneParamList.add(bndry.getId());
			}
		}
		
		Query propMatViewQuery = getPersistenceService().getSession().createQuery(query.toString());
		propMatViewQuery.setParameterList("ZoneId", zoneParamList);
		List<Object[]> propMatViewList = (List<Object[]>) propMatViewQuery.list();
		
		Query billQuery = getPersistenceService().getSession().createQuery(billQueryString.toString());
		billQuery.setDate("FromDate", currentInstallment.getFromDate());
		billQuery.setDate("ToDate", currentInstallment.getToDate());
		billQuery.setString("BillType", BILLTYPE_MANUAL);
		billQuery.setParameterList("ZoneId", zoneParamList);
		List<Object[]> billList = (List<Object[]>) billQuery.list();
		
		LOGGER.debug("DCB Report List size: "+propMatViewList.size());
		LOGGER.debug("Bills List size: "+billList.size());
		
		for(Object[] obj : propMatViewList) {
			ReportInfo repInfo = new ReportInfo();
			long mvZone, mvWard;
			
			mvZone = ((Integer)obj[0]).intValue();
			mvWard = ((Integer)obj[1]).intValue();
			String strZoneNum = (boundaryDAO.getBoundary(mvZone).getBoundaryNum()).toString();
			String strWardNum = (boundaryDAO.getBoundary(mvWard).getBoundaryNum()).toString();
			
			repInfo.setZoneNo(strZoneNum);
			repInfo.setWardNo(strWardNum);
			repInfo.setPartNo((String)obj[2]);
			repInfo.setTotalNoProps(Integer.valueOf(((Long) obj[3]).toString()));
			repInfo.setArrDmd(((BigDecimal)obj[5]).setScale(2, BigDecimal.ROUND_HALF_UP));
			repInfo.setCurrDmd(((BigDecimal)obj[4]).setScale(2, BigDecimal.ROUND_HALF_UP));
			repInfo.setArrColl(((BigDecimal)obj[7]).setScale(2, BigDecimal.ROUND_HALF_UP));
			repInfo.setCurrColl(((BigDecimal)obj[6]).setScale(2, BigDecimal.ROUND_HALF_UP));
			repInfo.setTotalDmd(((BigDecimal)obj[8]).setScale(2, BigDecimal.ROUND_HALF_UP));
			repInfo.setTotalColl(((BigDecimal)obj[9]).setScale(2, BigDecimal.ROUND_HALF_UP));
			
			for(Object[] billObj : billList) {
				int zoneNo, wardNo;
				String partNo;
				zoneNo = ((Integer) billObj[0]).intValue();
				wardNo = ((Integer) billObj[1]).intValue();
				partNo = (String) billObj[2];

				if(zoneNo==mvZone && wardNo==mvWard && partNo.equals(repInfo.getPartNo())) {
					repInfo.setTotalGenBills(Integer.valueOf(((Long) billObj[3]).toString()));
					break;
				}
			}
			LOGGER.debug("zoneNo:"+repInfo.getZoneNo()+", wardNo:"+repInfo.getWardNo()+", partNo:"+repInfo.getPartNo()
					+", Total Properties:"+repInfo.getTotalNoProps()+", Total bill generated:"+repInfo.getTotalGenBills()
					+", Arrear Demand:"+repInfo.getArrDmd()+", Current Demand:"+repInfo.getCurrDmd()
					+", Total Demand:"+repInfo.getTotalDmd()+", Arrear Recovery:"+repInfo.getArrColl()
					+", Current Recovery:"+repInfo.getCurrColl()+", Total Recovery:"+repInfo.getTotalColl());
	
			reportInfoList.add(repInfo);
		}
		
		setDataSourceType(ReportDataSourceType.JAVABEAN);
		setReportData(reportInfoList);
		super.report();
	}

	public String search() {
		return "search";
	}

	public String searchForm() {
		prepareReportInfo();
		return "report";
	}
	
	@Override
	public void prepare() {
		zoneList = persistenceService.findAllBy(
				"from BoundaryImpl BI where BI.boundaryType.name=? and BI.boundaryType.heirarchyType.name=? "
						+ "and BI.isHistory='N' order by BI.id", ZONE_BNDRY_TYPE, REVENUE_HIERARCHY_TYPE);
		setZoneBndryMap(CommonServices.getFormattedBndryMap(zoneList));
		ZoneBndryMap.put(0l, "All");
	}
	
	@Override
	public String criteria() {
		return null;
	}

	@Override
	protected String getReportTemplateName() {
		return REPORT_TEMPLATENAME_DCBREPORT;
	}

	public Long getZoneId() {
		return zoneId;
	}

	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
	}

	public Map<Long, String> getZoneBndryMap() {
		return ZoneBndryMap;
	}

	public void setZoneBndryMap(Map<Long, String> zoneBndryMap) {
		ZoneBndryMap = zoneBndryMap;
	}

	public List<Boundary> getZoneList() {
		return zoneList;
	}

	public void setZoneList(List<Boundary> zoneList) {
		this.zoneList = zoneList;
	}

}
