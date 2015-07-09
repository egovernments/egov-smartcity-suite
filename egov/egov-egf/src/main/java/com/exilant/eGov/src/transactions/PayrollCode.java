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
/** Created on May 17 dec, 2007

 *@author Mani
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.transactions;


import java.sql.Connection;
import org.apache.log4j.Logger;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infstr.utils.EGovConfig;
import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;

/**
 * @author Lakshmi
 *This class is used to extract Contractor Payable codes required 
 *in sqlTemplate id="getContractorDetails"
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PayrollCode extends AbstractTask
{ 
	private static final Logger LOGGER=Logger.getLogger(PayrollCode.class);
	private DataCollection dc;
	public void execute(String taskName,String gridName,DataCollection dCon,Connection cn,boolean errorOnNoData,boolean gridHasColumnHeading,String prefix)throws TaskFailedException
	{
		dc=dCon;
		String cityName=EgovThreadLocals.getDomainName();
		if(LOGGER.isDebugEnabled())     LOGGER.debug(cityName);
		String payrollCode=EGovConfig.getProperty("egf_config.xml",cityName,"0","PayRollCodes.PayRoll");
		dc.addValue("payrollCode",payrollCode);
		if(LOGGER.isDebugEnabled())     LOGGER.debug("payrollCode"+payrollCode); 
	}
	
}
