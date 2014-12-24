package org.egov.payroll.services.payslip;

import org.apache.log4j.Logger;

public class PayslipFailureException extends Exception{
	
	/**
	 * @author surya
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(PayslipFailureException.class);
	
	public PayslipFailureException(String msg)	{
		super(msg);
		logger.error(msg);
	}
	
}
