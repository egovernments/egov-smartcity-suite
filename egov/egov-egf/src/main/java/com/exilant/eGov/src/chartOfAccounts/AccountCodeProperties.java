
/*
 * AccountCodeProperties.java Created on june 6, 2006
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.exilant.eGov.src.chartOfAccounts;
/**
 * This class gets properties of glcode from eGovconfig.xml
 *
 * @author Tilak
 * @version 1.00
 */
import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;
import java.sql.Connection;
import org.egov.infstr.utils.EGovConfig;
public class AccountCodeProperties extends AbstractTask
{

	public void execute(String taskName,
				String gridName,
				DataCollection dc,
				Connection conn,
				boolean erroOrNoData,
			boolean gridHasColumnHeading, String prefix) throws TaskFailedException
	{
		String glcodelength=EGovConfig.getProperty("egf_config.xml","glcodeMaxLength","","AccountCode");
		String zeroFill=EGovConfig.getProperty("egf_config.xml","zerofill","","AccountCode");
		String minorValue=EGovConfig.getProperty("egf_config.xml","subminorvalue","","AccountCode");
		dc.addValue("glcodelength",glcodelength);
		dc.addValue("zeroFill",zeroFill);
		dc.addValue("minorValue",minorValue);
	}
}