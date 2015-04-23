/** Created on May 17 dec, 2007

 *@author Mani
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.transactions;


import java.sql.Connection;
import org.apache.log4j.Logger;
import org.egov.infstr.client.filter.EGOVThreadLocals;
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
		String cityName=EGOVThreadLocals.getDomainName();
		if(LOGGER.isDebugEnabled())     LOGGER.debug(cityName);
		String payrollCode=EGovConfig.getProperty("egf_config.xml",cityName,"0","PayRollCodes.PayRoll");
		dc.addValue("payrollCode",payrollCode);
		if(LOGGER.isDebugEnabled())     LOGGER.debug("payrollCode"+payrollCode); 
	}
	
}
