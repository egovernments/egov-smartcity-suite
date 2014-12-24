package org.egov.payroll.services.providentfund;

import java.util.List;
import org.egov.payroll.model.providentfund.PFDetails;
import org.egov.payroll.model.providentfund.PFHeader;


/**
 * @author Jagadeesan
 *
 */
public interface PFService
{
	public void createPFHeader(PFHeader header)  throws Exception;
	public void updatePFHeader(PFHeader header) throws Exception;
	public void createPFDetails(PFDetails details) throws Exception;
	public void updatePFDetails(PFDetails details) throws Exception;
	public void deletePFDetails(PFDetails details) throws Exception;
	public List getPFHeaderInfo();
	public List getPFDetails();
	public void deleteAllPFDetails(Integer pfHeaderId);
}
