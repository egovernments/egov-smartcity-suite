package org.egov.payroll.utils;

import org.apache.log4j.Logger;

public class PayslipAlreadyExistException extends Exception{
	
	/**
	 * @author surya
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(PayslipAlreadyExistException.class);
	
	public PayslipAlreadyExistException(String msg)	{
		super(msg);
		LOGGER.error(msg);
	}
	
}
