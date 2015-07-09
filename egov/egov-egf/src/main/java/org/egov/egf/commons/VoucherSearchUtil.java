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
package org.egov.egf.commons;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.exceptions.EGOVException;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infstr.config.dao.AppConfigValuesDAO;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly=true)
public class VoucherSearchUtil {
	private @Autowired AppConfigValuesDAO appConfigValuesDAO;
	private PersistenceService persistenceService;
	private static final Logger LOGGER=Logger.getLogger(VoucherSearchUtil.class);
	private FinancialYearDAO financialYearDAO;


	public List<CVoucherHeader> search(CVoucherHeader voucherHeader,Date fromDate, Date toDate, String mode) throws EGOVException,
	ParseException {

		System.out.println("Inside search ----- in voucher util");

		String sql = "", editModeQuery1, editModeQuery2;
		List<CVoucherHeader> voucherList = new ArrayList<CVoucherHeader>();
		sql=sql+voucherFilterQuery( voucherHeader,  fromDate,  toDate, mode);
		String statusExclude=excludeVoucherStatus();

		boolean modeIsNotBlank = (null != mode && !StringUtils.isBlank(mode));
		if (modeIsNotBlank) {
			if ("edit".equalsIgnoreCase(mode)) {
				sql = sql + " and vh.isConfirmed != 1";
			} else if ("reverse".equalsIgnoreCase(mode)) {
				sql = sql + " and vh.isConfirmed = 1";
			}
			statusExclude = statusExclude + ","+ FinancialConstants.REVERSEDVOUCHERSTATUS.toString() + ","+ FinancialConstants.REVERSALVOUCHERSTATUS;
		}
		
		if (modeIsNotBlank && "edit".equalsIgnoreCase(mode)	&& voucherHeader.getType().equalsIgnoreCase(FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL)) {
	/**
	 * Logic is as explained below
	 * 1.Get all vouchers created from JV screen (stateid null) and which does not have payments
	 * 2.Get all Vouchers which has payments (Any state 0,1,2,4,5)
	 * 3. Add 1 and 2 ==>A
	 * 4.Get all vouchers which has active payments
	 * 5.Get all vouchers which has active remittance payments
	 * 6. Add  4 and 5 ==>B
	 * 7. Remove All Bs from A
	 */
			String jvModifyCondition=" and vh.status in ("+FinancialConstants.CREATEDVOUCHERSTATUS+") and vh.moduleId is null ";

			// editModeQuery1  :Get all vouchers created from JV screen  and payments are not done 
			editModeQuery1 = " from CVoucherHeader vh where vh not in ( select billVoucherHeader from Miscbilldetail) "+jvModifyCondition;
			
			
			//  editModeQuery2 :-check for voucher for which payments are in created (any state)     
			editModeQuery2 = " select distinct(vh) from  Miscbilldetail misc left join misc.billVoucherHeader vh where misc.billVoucherHeader is not null"
				+ jvModifyCondition + sql;
			

			voucherList.addAll((List<CVoucherHeader>) persistenceService.findAllBy(editModeQuery1 + sql));
			if(LOGGER.isDebugEnabled())
			{
			if(LOGGER.isDebugEnabled())     LOGGER.debug("---NO Associated Payment");
			for(CVoucherHeader vh:voucherList)
			{
				if(LOGGER.isDebugEnabled())     LOGGER.debug("-----"+vh.getId());
			}
			}
			//Assumption:editModeQuery1 and editModeQuery2 will always return different list (not duplicate)
			voucherList.addAll((List<CVoucherHeader>) persistenceService.findAllBy(editModeQuery2));
			if(LOGGER.isDebugEnabled())
			{
			if(LOGGER.isDebugEnabled())     LOGGER.debug("---Associated Payment");
			for(CVoucherHeader vh:voucherList)
			{
				if(LOGGER.isDebugEnabled())     LOGGER.debug("-----"+vh.getId());
			}
			}
			
		//  editModeQuery3 :-check for voucher for for which payments are active 0,5 this will be removed from above two list
			String	editModeQuery3=	" select misc.billVoucherHeader.id from CVoucherHeader ph, Miscbilldetail misc,CVoucherHeader vh  where misc.payVoucherHeader=ph " +
			"and   misc.billVoucherHeader is not null and misc.billVoucherHeader=vh " +
			"and ph.status  in ("+FinancialConstants.CREATEDVOUCHERSTATUS+","+FinancialConstants.PREAPPROVEDVOUCHERSTATUS+") "+jvModifyCondition+sql;
			List<Long> vouchersHavingActivePayments =(List<Long>) persistenceService.findAllBy(editModeQuery3);	
		
			List<CVoucherHeader> vchList = voucherList;
			String uncancelledRemittances = " SELECT distinct(vh.id) FROM EgRemittanceDetail r, EgRemittanceGldtl rgd, Generalledgerdetail gld, CGeneralLedger gl, EgRemittance rd,"
				+ " CVoucherHeader vh ,Vouchermis billmis, CVoucherHeader remittedvh  WHERE r.egRemittanceGldtl=rgd AND rgd.generalledgerdetail=gld"
				+ " AND gld.generalledger=gl AND r.egRemittance=rd AND rd.voucherheader=remittedvh AND gl.voucherHeaderId =vh"
				+ " AND remittedvh =billmis.voucherheaderid and remittedvh.status!="
				+ FinancialConstants.CANCELLEDVOUCHERSTATUS + " ";
			List<Long> remittanceBillVhIdList = (List<Long>) persistenceService.findAllBy(uncancelledRemittances + sql);
			if(LOGGER.isDebugEnabled())
			{
			if(LOGGER.isDebugEnabled())     LOGGER.debug("---Active Bill  Remittance Payments");
			for(Long vh:remittanceBillVhIdList)
			{
				if(LOGGER.isDebugEnabled())     LOGGER.debug("-----"+vh);
			}
			}
			remittanceBillVhIdList.addAll(vouchersHavingActivePayments);
			List<CVoucherHeader> toBeRemovedList=new ArrayList<CVoucherHeader>();
			if (vchList != null && vchList.size() != 0	&& remittanceBillVhIdList != null && remittanceBillVhIdList.size() != 0) {
				for (int i = 0; i < vchList.size(); i++) {
					if (remittanceBillVhIdList.contains(vchList.get(i).getId()))
					{
						toBeRemovedList.add(voucherList.get(i));
					}
				}
				for(CVoucherHeader vh:toBeRemovedList)
				{
					voucherList.remove(vh);
				}
			}
			return voucherList;
		} else
			voucherList = (List<CVoucherHeader>) persistenceService
			.findAllBy(" from CVoucherHeader vh where vh.status not in ("
					+ statusExclude
					+ ") "
					+ sql
					+ " order by vh.voucherNumber ");
		return voucherList;
	}

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	/**
	 * @param voucherHeader
	 * @param date
	 * @param date2
	 * @param showMode
	 * @return
	 */
	public List<CVoucherHeader> searchNonBillVouchers(
			CVoucherHeader voucherHeader, Date fromDate, Date toDate,
			String mode) {

		String sql = "";
		List<CVoucherHeader> voucherList = new ArrayList<CVoucherHeader>();
		if (!voucherHeader.getType().equals("-1")) {
			sql = sql + " and vh.type='" + voucherHeader.getType() + "'";
		}

		if (voucherHeader.getName() != null
				&& !voucherHeader.getName().equalsIgnoreCase("-1")) {
			sql = sql + " and vh.name='" + voucherHeader.getName() + "'";
		} else {
			sql = sql + " and vh.name in ('"
			+ FinancialConstants.JOURNALVOUCHER_NAME_CONTRACTORJOURNAL
			+ "','"
			+ FinancialConstants.JOURNALVOUCHER_NAME_SUPPLIERJOURNAL
			+ "','"
			+ FinancialConstants.JOURNALVOUCHER_NAME_SALARYJOURNAL
			+ "')";
		}
		if (voucherHeader.getVoucherNumber() != null
				&& !voucherHeader.getVoucherNumber().equals("")) {
			sql = sql + " and vh.voucherNumber like '%"
			+ voucherHeader.getVoucherNumber() + "%'";
		}
		if (fromDate != null) {
			sql = sql + " and vh.voucherDate>='"
			+ Constants.DDMMYYYYFORMAT1.format(fromDate) + "'";
		}
		if (toDate != null) {
			sql = sql + " and vh.voucherDate<='"
			+ Constants.DDMMYYYYFORMAT1.format(toDate) + "'";
		}
		if (voucherHeader.getFundId() != null) {
			sql = sql + " and vh.fundId=" + voucherHeader.getFundId().getId();
		}
		if (voucherHeader.getVouchermis().getFundsource() != null) {
			sql = sql + " and vh.fundsourceId="
			+ voucherHeader.getVouchermis().getFundsource().getId();
		}
		if (voucherHeader.getVouchermis().getDepartmentid() != null) {
			sql = sql + " and vh.vouchermis.departmentid="
			+ voucherHeader.getVouchermis().getDepartmentid().getId();
		}
		if (voucherHeader.getVouchermis().getSchemeid() != null) {
			sql = sql + " and vh.vouchermis.schemeid="
			+ voucherHeader.getVouchermis().getSchemeid().getId();
		}
		if (voucherHeader.getVouchermis().getSubschemeid() != null) {
			sql = sql + " and vh.vouchermis.subschemeid="
			+ voucherHeader.getVouchermis().getSubschemeid().getId();
		}
		if (voucherHeader.getVouchermis().getFunctionary() != null) {
			sql = sql + " and vh.vouchermis.functionary="
			+ voucherHeader.getVouchermis().getFunctionary().getId();
		}
		if (voucherHeader.getVouchermis().getDivisionid() != null) {
			sql = sql + " and vh.vouchermis.divisionid="
			+ voucherHeader.getVouchermis().getDivisionid().getId();
		}
		String sql1 = sql
		+ " and  (vh.id  in (select voucherHeader.id from EgBillregistermis) and vh.id not in (select billVoucherHeader.id from Miscbilldetail where billVoucherHeader is not null and  payVoucherHeader  in (select id from CVoucherHeader where status not in (4,1) and type='Payment')) )";
		final List<AppConfigValues> appList = appConfigValuesDAO.getConfigValuesByModuleAndKey(
				"finance", "statusexcludeReport");
		String statusExclude = appList.get(0).getValue();
		statusExclude = statusExclude + ","
		+ FinancialConstants.REVERSEDVOUCHERSTATUS.toString() + ","
		+ FinancialConstants.REVERSALVOUCHERSTATUS;

		String finalQuery = " from CVoucherHeader vh where vh.status not in ("
			+ statusExclude + ") " + sql1 + "  ";

		voucherList = (List<CVoucherHeader>) persistenceService
		.findAllBy(finalQuery);
		return voucherList;

	}
	/*
	 * Forming query for filter search
	 */
	public String voucherFilterQuery(CVoucherHeader voucherHeader, Date fromDate, Date toDate,String mode){
		String sql="";
		if (!voucherHeader.getType().equals("-1")) {
			sql = sql + " and vh.type='" + voucherHeader.getType() + "'";
		}

		if (voucherHeader.getName() != null
				&& !voucherHeader.getName().equalsIgnoreCase("-1")) {
			sql = sql + " and vh.name='" + voucherHeader.getName() + "'";
		}
		if (voucherHeader.getVoucherNumber() != null
				&& !voucherHeader.getVoucherNumber().equals("")) {
			sql = sql + " and vh.voucherNumber like '%"
			+ voucherHeader.getVoucherNumber() + "%'";
		}
		if (fromDate != null) {
			sql = sql + " and vh.voucherDate>='"+ Constants.DDMMYYYYFORMAT1.format(fromDate) + "'";
		}
		if (toDate != null) {
			sql = sql + " and vh.voucherDate<='"
			+ Constants.DDMMYYYYFORMAT1.format(toDate) + "'";
		}
		if (voucherHeader.getFundId() != null) {
			sql = sql + " and vh.fundId=" + voucherHeader.getFundId().getId();
		}
		if (voucherHeader.getVouchermis().getFundsource() != null) {
			sql = sql + " and vh.fundsourceId="
			+ voucherHeader.getVouchermis().getFundsource().getId();
		}
		if (voucherHeader.getVouchermis().getDepartmentid() != null) {
			sql = sql + " and vh.vouchermis.departmentid="
			+ voucherHeader.getVouchermis().getDepartmentid().getId();
		}
		if (voucherHeader.getVouchermis().getSchemeid() != null) {
			sql = sql + " and vh.vouchermis.schemeid="
			+ voucherHeader.getVouchermis().getSchemeid().getId();
		}
		if (voucherHeader.getVouchermis().getSubschemeid() != null) {
			sql = sql + " and vh.vouchermis.subschemeid="
			+ voucherHeader.getVouchermis().getSubschemeid().getId();
		}
		if (voucherHeader.getVouchermis().getFunctionary() != null) {
			sql = sql + " and vh.vouchermis.functionary="
			+ voucherHeader.getVouchermis().getFunctionary().getId();
		}
		if (voucherHeader.getVouchermis().getDivisionid() != null) {
			sql = sql + " and vh.vouchermis.divisionid="
			+ voucherHeader.getVouchermis().getDivisionid().getId();
		}
		if (voucherHeader.getModuleId() != null) {
			// -2 For vouchers created from the financial module. -2 is set in
			// populateSourceMap() of VoucherSearchAction.
			if (voucherHeader.getModuleId() == -2) {
				sql = sql + " and vh.moduleId is null ";
			} else {
				sql = sql + " and vh.moduleId=" + voucherHeader.getModuleId();
			}
		}

		return sql;
	}
	public String excludeVoucherStatus(){
		final List<AppConfigValues> appList = appConfigValuesDAO.getConfigValuesByModuleAndKey(	"EGF", "statusexcludeReport");
		return appList.get(0).getValue();
	}

	/* Adding search query to return Query object for implementing paginated list
	 * for view voucher
	 */
	public List<Query> voucherSearchQuery(CVoucherHeader voucherHeader,Date fromDate, Date toDate,String mode){
		List<Query> queryList=new ArrayList<Query>();
		String statusExclude=excludeVoucherStatus();
		String sql=voucherFilterQuery( voucherHeader,  fromDate,  toDate, mode);
		String sqlQuery="from CVoucherHeader vh where vh.status not in ("
			+ statusExclude
			+ ") "
			+ sql
			+ " order by vh.voucherNumber ,vh.voucherDate,vh.name ";
		Query query1 = HibernateUtil.getCurrentSession().createQuery(sqlQuery);
		queryList.add(query1);
		Query query2= HibernateUtil.getCurrentSession().createQuery("select count(*) from CVoucherHeader vh where vh.status not in ("
				+ statusExclude
				+ ") "
				+ sql);
		queryList.add(query2);
		return queryList;
	}

	public boolean validateFinancialYearForPosting(Date fromDate, Date toDate) {
    	return financialYearDAO.isFinancialYearActiveForPosting(fromDate, toDate);
	}

	public void setFinancialYearDAO(FinancialYearDAO financialYearDAO) {
		this.financialYearDAO = financialYearDAO;
	}

	

}
