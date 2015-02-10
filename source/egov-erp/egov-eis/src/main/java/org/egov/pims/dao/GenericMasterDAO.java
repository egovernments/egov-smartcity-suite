/*
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pims.dao;

import java.io.Serializable;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.pims.model.GenericMaster;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
/**
 * @author deepak
 *
 */
public class GenericMasterDAO implements Serializable
{
	
	private SessionFactory sessionFactory;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public GenericMasterDAO()
	{
		session = sessionFactory.getCurrentSession();

	}
	private Session session;
	private void openSession()
	{
		session = sessionFactory.getCurrentSession();
	}
	public void create(GenericMaster genericMaster) 
	{
		try
		{
			if(! session.isOpen())
			{
				openSession();
			}
			session.save(genericMaster);
			

		}
		catch (HibernateException e)
		{
			
			throw  new EGOVRuntimeException(STR_HIB_EXP+e.getMessage(),e);
		}

	}

	public void update(GenericMaster genericMaster)
	{
		try
		{
			if(! session.isOpen())
			{
				openSession();
			}
			session.saveOrUpdate(genericMaster);
		}
		catch (HibernateException e)
		{
			throw  new EGOVRuntimeException(STR_HIB_EXP+e.getMessage(),e);

		}

	}

	public void remove(GenericMaster genericMaster)
	{
		try
		{
			if(! session.isOpen())
			{
				openSession();
			}

			session.delete(genericMaster);
			

		}
		catch (HibernateException e)
		{
			
			throw  new EGOVRuntimeException(STR_HIB_EXP+e.getMessage(),e);

		}

	}

	public GenericMaster getGenericMaster(int Id,String className)
	{
		try
		{
			if(! session.isOpen())
			{
				openSession();
			}
			String srt = "org.egov.pims.model."+className;
			GenericMaster imp = (GenericMaster)session.get(Class.forName(srt), Integer.valueOf(Id));

			return imp ;
		}catch (HibernateException e)
		{
			throw  new EGOVRuntimeException(STR_HIB_EXP+e.getMessage(),e);

		}
		catch (ClassNotFoundException e)
		{
				
			throw  new EGOVRuntimeException(STR_HIB_EXP+e.getMessage(),e);

		}


	}
	
	private final static String STR_HIB_EXP = "Hibernate Exception : ";


}
