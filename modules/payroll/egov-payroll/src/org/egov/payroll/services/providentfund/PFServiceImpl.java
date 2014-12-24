package org.egov.payroll.services.providentfund;

import java.util.List;
import org.egov.payroll.dao.PFDAO;
import org.egov.payroll.dao.PayrollDAOFactory;
import org.egov.payroll.model.providentfund.PFDetails;
import org.egov.payroll.model.providentfund.PFHeader;


/**
 * @author Jagadeesan
 *
 */

public class PFServiceImpl implements PFService
{
	
	
	/**
	 * @param header
	 * @throws Exception
	 */
	public void createPFHeader(PFHeader header) throws Exception{
		PFDAO pfHeaderDAO = PayrollDAOFactory.getDAOFactory().getPFHeaderDAO();
		pfHeaderDAO.create(header);
	}
	
	/**
	 * @param header
	 * @throws Exception
	 */
	public void updatePFHeader(PFHeader header) throws Exception{
		PFDAO pfHeaderDAO = PayrollDAOFactory.getDAOFactory().getPFHeaderDAO();
		pfHeaderDAO.update(header);
	}
	
	
	public void createPFDetails(PFDetails details) throws Exception{
		PFDAO pfDetailDAO = PayrollDAOFactory.getDAOFactory().getPFDetailsDAO();
		pfDetailDAO.create(details);
	}
	
	
	public void updatePFDetails(PFDetails details) throws Exception{
		PFDAO pfDetailDAO = PayrollDAOFactory.getDAOFactory().getPFDetailsDAO();
		pfDetailDAO.update(details);
	}
	
	/**
	 * @param details
	 * @throws Exception
	 */
	public void deletePFDetails(PFDetails details) throws Exception{
		PFDAO pfDetailDAO = PayrollDAOFactory.getDAOFactory().getPFDetailsDAO();
		pfDetailDAO.delete(details);
	}
	
	public void deleteAllPFDetails(Integer pfHeaderId)
	{
		PFDAO pfDetailDAO = PayrollDAOFactory.getDAOFactory().getPFDetailsDAO();
		pfDetailDAO.deleteAllPFDetails(pfHeaderId);
	}
	
	public List getPFHeaderInfo()
	{
		PFDAO pfHeaderDAO  = PayrollDAOFactory.getDAOFactory().getPFHeaderDAO();
		return pfHeaderDAO.getPFHeaderInfo();
	}
	
	public List getPFDetails()
	{
		PFDAO pfDetailDAO  = PayrollDAOFactory.getDAOFactory().getPFDetailsDAO();
		return pfDetailDAO.getPFDetails();
	}

}
