package org.egov.payroll.dao;

import java.util.List;



/**
 * <p>This is an interface which would be implemented by the
 * Individual Frameworks  for all the CRUD (create, read, update, delete) basic data
 * access operations for Advance
 *
 * @author Jagadeesan
 * @version 2.00
 * 
 */

public interface PFDAO extends org.egov.infstr.dao.GenericDAO
{
	public List getPFHeaderInfo();
	public List getPFDetails();
	public void deleteAllPFDetails(Integer pfHeaderId);
}
