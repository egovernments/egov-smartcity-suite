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
package org.egov.dao.bills;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.EGovConfig;

public abstract class BillsDaoFactory {
	public BillsDaoFactory()
	{
		super();
	}
	private static final Logger logger = Logger.getLogger(BillsDaoFactory.class);

    private static final BillsDaoFactory EJB3_PERSISTENCE = null;

    private static final BillsDaoFactory HIBERNATE = new BillsHibernateDaoFactory ();

    private static final BillsDaoFactory retFac = resolveDAOFactory();

    public static BillsDaoFactory getDAOFactory()
    {
    	if(logger.isDebugEnabled())     logger.debug("getDAOFactory called."+retFac);
    	return retFac;
    }

    private static BillsDaoFactory resolveDAOFactory()
    {
    	
    	if(logger.isDebugEnabled())     logger.debug("Resolving CommonsDaoFactory.");
    	String method = EGovConfig.getProperty("COMMONS-FACTORY-IMPL","HIBERNATE","PTIS");
    	if(logger.isDebugEnabled())     logger.debug("Factory implementation name:"+method+".");
    	if(method != null)
    	{
    		if(method.trim().equalsIgnoreCase("HIBERNATE"))
    		{
    			if(logger.isDebugEnabled())     logger.debug("Returning Hibernate Implementation of CommonsDaoFactory");
    			return HIBERNATE;
    		}
    		else
    		{
    			if(logger.isDebugEnabled())     logger.debug("Returning EJB3_PERSISTENCE Implementation of CommonsDaoFactory");
    			return EJB3_PERSISTENCE;
    		}
    	}
    	if(logger.isDebugEnabled())     logger.debug("Returning null. CommonsDaoFactory could not be resolved.");
    	return  null;
    }
    
    public abstract EgBillRegisterHibernateDAO getEgBillRegisterHibernateDAO();
    public abstract EgBilldetailsDAO getEgBilldetailsDAO();   
}
