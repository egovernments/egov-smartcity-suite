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
import java.sql.Connection;

import org.egov.infstr.utils.EGovConfig;
import org.springframework.transaction.annotation.Transactional;

import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;
@Transactional(readOnly=true)
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
