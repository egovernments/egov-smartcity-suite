/*
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pims.dao;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.pims.model.TechnicalGradesMaster;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * @author deepak
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TechnicalGradesMasterDAO implements Serializable
{
	
	public TechnicalGradesMasterDAO()
	{
		session = HibernateUtil.getCurrentSession();
	}
	private Session session;
	private void openSession()
	{
		session = HibernateUtil.getCurrentSession();
	}
	public void createTechnicalGradesMaster(TechnicalGradesMaster technicalGradesMaster)
	{
		try
		{
			if(! session.isOpen())
			{
				openSession();
			}
			session.save(technicalGradesMaster);
			

		}
		catch (Exception e)
		{
			
			throw new EGOVRuntimeException(STR_HIB_EXP+e.getMessage(),e);
		}

	}

	public void updateTechnicalGradesMaster(TechnicalGradesMaster technicalGradesMaster)
	{
		try
		{
			if(! session.isOpen())
			{
				openSession();
			}
			session.saveOrUpdate(technicalGradesMaster);
		}
		catch (Exception e)
		{
			
			throw new EGOVRuntimeException(STR_HIB_EXP+e.getMessage(),e);
		}

	}

	public void removeTechnicalGradesMaster(TechnicalGradesMaster technicalGradesMaster)
	{
		try
		{
			if(! session.isOpen())
			{
				openSession();
			}

			session.delete(technicalGradesMaster);
			

		}
		catch (Exception e)
		{
			
			throw new EGOVRuntimeException(STR_HIB_EXP+e.getMessage(),e);
		}

	}

	public TechnicalGradesMaster getTechnicalGradesMaster(int tmID)
	{
		try
		{
			if(! session.isOpen())
			{
				openSession();
			}

			TechnicalGradesMaster tm=(TechnicalGradesMaster)session.get(TechnicalGradesMaster.class,Integer.valueOf(tmID));

			return tm ;
		}
		catch (Exception e)
		{
				
			throw new EGOVRuntimeException(STR_HIB_EXP+e.getMessage(),e);
		}


	}
	public Map getAllTechnicalGradesMaster()
	{
		try
		{
			Query qry = session.createQuery("from TechnicalGradesMaster TM ");
			Map<Integer,String> retMap = new LinkedHashMap<Integer,String>();
			for (Iterator iter = qry.iterate(); iter.hasNext();)
			{
				TechnicalGradesMaster technicalGradesMaster = (TechnicalGradesMaster)iter.next();
				retMap.put(technicalGradesMaster.getId(),technicalGradesMaster.getGradeName());
			}
			return retMap;
		}
		catch (Exception e)
		{
				
				throw new EGOVRuntimeException(STR_HIB_EXP+e.getMessage(),e);
		}
	}

	private final static String STR_HIB_EXP = "Hibernate Exception : ";

}
