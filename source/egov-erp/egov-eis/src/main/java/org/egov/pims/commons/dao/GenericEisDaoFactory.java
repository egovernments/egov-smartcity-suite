 /*
 * Created on Oct 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.egov.pims.commons.dao;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.EGovConfig;

/**
 * @author Venkatesh.M.J
 * @version 2.0
 * @since 2.0
*/

public abstract class GenericEisDaoFactory {

	/**
	 *
	 */
	public GenericEisDaoFactory() {   
		super();
		// TODO Auto-generated constructor stub
	}

	private static final Logger LOGGER = Logger.getLogger(GenericEisDaoFactory.class);

    private static final GenericEisDaoFactory EJB3_PERSISTENCE = null;

    private static final GenericEisDaoFactory HIBERNATE = new GenericEisHibernateDaoFactory ();

    private static final GenericEisDaoFactory RETFAC = resolveDAOFactory();

    public static GenericEisDaoFactory getDAOFactory()
    {
    	LOGGER.debug("getDAOFactory called."+RETFAC);
    	return RETFAC;
    }

    private static GenericEisDaoFactory resolveDAOFactory()
    {
    	
    	LOGGER.debug("Resolving GenericDaoFactory.");
    	String method = EGovConfig.getProperty("COMMONS-FACTORY-IMPL","HIBERNATE","PTIS");
    	LOGGER.debug("Factory implementation name:"+method+".");
    	if(method != null)
    	{
    		if(method.trim().equalsIgnoreCase("HIBERNATE"))
    		{
    			LOGGER.debug("Returning Hibernate Implementation of GenericDaoFactory");
    			return HIBERNATE;
    		}
    		else
    		{
    			LOGGER.debug("Returning EJB3_PERSISTENCE Implementation of GenericDaoFactory");
    			return EJB3_PERSISTENCE;
    		}
    	}
    	LOGGER.debug("Returning null. GenericDaoFactory could not be resolved.");
    	return  null;
    }
    
}
