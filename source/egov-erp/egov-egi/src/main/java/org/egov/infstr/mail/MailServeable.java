/*
 * @(#)MailServeable.java 3.0, 17 Jun, 2013 2:41:55 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.mail;


public interface MailServeable {
	
	/**
	 * Send mail, email sending configuration values will be picked up from egov-config.xml.
	 * @param message the message
	 * @param subject the subject
	 * @return true, if successful
	 */
	boolean sendMail(String message,String subject);
}
