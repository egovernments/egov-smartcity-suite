/*
 * EGovFinFactory.java Created on Jul 18, 2006
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.exilant.eGov.src.common;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.utils.EGovConfig;

/**
 * TODO Brief Description of the puprpose of the class/interface
 * 
 * @author Elzan
 * @version 1.00 
 * @see	    
 * @see	    
 * @since   1.00
 */
public class EGovFinFactory
{
	private static final Logger LOGGER = Logger.getLogger(EGovFinFactory.class);
	public static EGovernCommon getCommonsInstance() 
	{
		LOGGER.info("Calling getCommonsInstance");
		EGovernCommon obj = null;
		String classStr = null;
		try
		{
			classStr = EGovConfig.getProperty("egf_config.xml","Commons",null,"FinClassFactory");
			
			LOGGER.info("Class is:" + classStr);
			obj = ((EGovernCommon)Class.forName(classStr).newInstance());
			LOGGER.info("Class is:" + obj);
		} 
		catch (ClassNotFoundException e)
		{
			LOGGER.debug(e.getMessage(), e);
			throw new EGOVRuntimeException("Class not found" + classStr + e.getMessage());
		}
		catch (InstantiationException e)
		{
			LOGGER.debug(e.getMessage(), e);
			throw new EGOVRuntimeException("Could not instantiate class" + classStr + e.getMessage());
			
		} 
		catch (IllegalAccessException e)
		{
			LOGGER.debug(e.getMessage(), e);
			throw new EGOVRuntimeException("Could not instantiate class" + classStr + e.getMessage());
		}
		catch (Exception e)
		{
			LOGGER.debug(e.getMessage(), e);
			throw new EGOVRuntimeException("Could not instantiate class" + classStr + e.getMessage());
		}
		return obj;
	}
}
