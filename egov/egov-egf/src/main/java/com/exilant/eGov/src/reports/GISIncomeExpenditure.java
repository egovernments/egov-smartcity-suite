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
/**
 * @author Administrator
 * This
 */
package com.exilant.eGov.src.reports;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.EGovConfig;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;

import com.exilant.eGov.src.common.EGovernCommon;

public class GISIncomeExpenditure {
	private static final Logger LOGGER = Logger
			.getLogger(GISIncomeExpenditure.class);
	EGovernCommon egc = new EGovernCommon();
	Map hmAcccodeMap = null;

	String fyear = EGovConfig.getProperty("egf_config.xml", "getFinancialyear",
			"", "GISReports");
	String majorcode = EGovConfig.getProperty("egf_config.xml",
			"getSubstringValue", "", "GISReports");
	Query pst = null;
	List<Object[]> rs = null;

	public Map GISReportData(String reportType) {

		hmAcccodeMap = new TreeMap();
		String expenseQuery = "select c.name,(sum(a.debitamount)-sum(a.creditamount))/100000 from generalledger a, chartofaccounts c,voucherheader b"
				+ "	where substr(a.glcode,0,?) = c.glcode and c.type = ? and a.voucherheaderid=b.id and "
				+ "b.fiscalperiodid IN (SELECT id FROM fiscalperiod WHERE financialyearid= ? ) and "
				+ "	a.debitamount > a.creditamount  group by c.name";
		String incomeQuery = "select c.name,(sum(a.creditamount)-sum(a.debitamount))/100000 from generalledger a, chartofaccounts c,voucherheader b"
				+ "	where substr(a.glcode,0,?) = c.glcode and c.type = ? and a.voucherheaderid=b.id and "
				+ "b.fiscalperiodid IN (SELECT id FROM fiscalperiod WHERE financialyearid= ? ) and "
				+ "	a.creditamount > a.debitamount  group by c.name";
		String repQuery = null;
		try {

			if (reportType.equalsIgnoreCase("E")) {
				repQuery = expenseQuery;
				pst = HibernateUtil.getCurrentSession().createSQLQuery(repQuery);
				pst.setString(1, majorcode);
				pst.setString(2, reportType);
				pst.setString(3, fyear);
			} else {
				repQuery = incomeQuery;
				pst = HibernateUtil.getCurrentSession().createSQLQuery(repQuery);
				pst.setString(1, majorcode);
				pst.setString(2, reportType);
				pst.setString(3, fyear);
			}
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Type is :" + reportType + " Query :" + repQuery);

			rs = pst.list();
			for(Object[] element : rs){
				String accCode = element[0].toString();
				String amount = element[1].toString();
				// if(LOGGER.isDebugEnabled())     LOGGER.debug(">>>>>accCode="+accCode+"  , amount="+amount);
				hmAcccodeMap.put(accCode, amount);
			}
			// if(LOGGER.isDebugEnabled())     LOGGER.debug(">>>>>>hmAcccodeMap="+hmAcccodeMap);

		} catch (Exception e) {
			LOGGER.error("Exp=" + e.getMessage(),e);
		} 
		return hmAcccodeMap;
	}

}
