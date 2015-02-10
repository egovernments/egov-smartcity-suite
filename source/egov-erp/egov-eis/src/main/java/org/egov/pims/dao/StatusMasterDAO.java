/*
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pims.dao;

import java.io.Serializable;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.pims.model.StatusMaster;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
/**
 * @author deepak
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StatusMasterDAO implements Serializable
{
	private SessionFactory sessionFactory;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public StatusMasterDAO()
	{
		session = sessionFactory.getCurrentSession();
	}
	private Session session;
	private void openSession()
	{
		session = sessionFactory.getCurrentSession();
	}
	public void createStatusMaster(StatusMaster statusMaster)
	{
		try
		{
			if(! session.isOpen())
			{
				openSession();
			}
			session.save(statusMaster);
			

		}
		catch (Exception e)
		{
			
			throw new EGOVRuntimeException(STR_HIB_EXP+e.getMessage(),e);
		}

	}

	public void updateStatusMaster(StatusMaster statusMaster)
	{
		try
		{
			if(! session.isOpen())
			{
				openSession();
			}
			session.saveOrUpdate(statusMaster);
		}
		catch (Exception e)
		{
			
			throw new EGOVRuntimeException(STR_HIB_EXP+e.getMessage(),e);
		}

	}

	public void removeStatusMaster(StatusMaster statusMaster)
	{
		try
		{
			if(! session.isOpen())
			{
				openSession();
			}

			session.delete(statusMaster);
			

		}
		catch (Exception e)
		{
			
			throw new EGOVRuntimeException(STR_HIB_EXP+e.getMessage(),e);
		}

	}

	public StatusMaster getStatusMaster(int stID)
	{
		try
		{
			if(! session.isOpen())
			{
				openSession();
			}

			StatusMaster sm=(StatusMaster)session.get(StatusMaster.class,Integer.valueOf(stID));

			return sm ;
		}
		catch (Exception e)
		{
				
			throw new EGOVRuntimeException(STR_HIB_EXP+e.getMessage(),e);
		}


	}

	public StatusMaster getStatusMaster(String name)
		{
			try
			{
				if(! session.isOpen())
				{
					openSession();
				}
				Query qry = session.createQuery("from StatusMaster P where P.name =:name ");
				qry.setString("name", name);
				return (StatusMaster)qry.uniqueResult();
			}
			catch (Exception e)
			{
					throw new EGOVRuntimeException(STR_HIB_EXP+e.getMessage(),e);
			}


	}






	private final static String STR_HIB_EXP = "Hibernate Exception : ";




}
