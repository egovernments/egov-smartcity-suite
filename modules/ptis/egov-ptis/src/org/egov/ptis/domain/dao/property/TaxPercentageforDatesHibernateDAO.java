/* TaxPercentageforDatesHibernateDAO.java Created on june 13, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.ptis.domain.dao.property;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;


/**
 * This Class implememets the TaxPercentageforDatesDAO for the Hibernate specific 
 * Implementation 
 * 
 * @author Lokesh
 * @version 2.00
 */

public class TaxPercentageforDatesHibernateDAO extends GenericHibernateDAO implements TaxPercentageforDatesDAO
{
	private static final Logger LOGGER = Logger.getLogger(TaxPercentageforDatesHibernateDAO.class);
	
	/**
	 * @param persistentClass
	 * @param session
	 */
	
	public TaxPercentageforDatesHibernateDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	}
	
	public List getTaxPercentageforDates(Integer type, BigDecimal amount,Date installmentStartDate,Date installmentEndDate)
	{
	
		Connection connection = null;
		PreparedStatement preparedstatement = null;
		ResultSet resultset=null;
		try
		{
		    //TODO Not used in NMC. SO COMMENTING OUT connection() call. Might be needed for other clients.  
			connection = null; // HibernateUtil.getCurrentSession().connection();  
			String myQuery=("select * from EGPT_TAXRATES er where  er.TO_DATE BETWEEN to_date(?) " +
					"and to_date(?) and er.FROM_AMOUNT<? and type = ?"); 	
			preparedstatement = connection.prepareStatement(myQuery);
			LOGGER.info("after preparestatement");
			preparedstatement.setDate(1,new java.sql.Date(installmentStartDate.getTime()));
			preparedstatement.setDate(2,new java.sql.Date(installmentEndDate.getTime()));
            preparedstatement.setBigDecimal(3, amount);
            preparedstatement.setInt(4, type);
			resultset = preparedstatement.executeQuery(); 
			 ArrayList retList = new ArrayList();
	            while(resultset.next())
	            {
	                int count = resultset.getInt(1);
	                retList.add(count);
				}
	            LOGGER.info("size of the retList array is "+retList.size());
	       
			  return retList;
			
		}catch (HibernateException e)
		{
			LOGGER.info("Exception in  getTaxPercentageforDates--- TaxPercentageforDatesHibernateDao--"+e.getMessage());
			throw new EGOVRuntimeException("Hibernate Exception : "+e.getMessage(),e);
		}
		catch (Exception e1)
		{
			LOGGER.info("Exception in  getTaxPercentageforDates--- TaxPercentageforDatesHibernateDao--"+e1.getMessage());
			throw new EGOVRuntimeException("Exception : "+e1.getMessage(),e1);
		}
	}
}	