/*
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pims.dao;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
	@PersistenceContext
	private EntityManager entityManager;
	    
	public Session  getCurrentSession() {
		return entityManager.unwrap(Session.class);
	}
	
	public void createStatusMaster(StatusMaster statusMaster)
	{
		try
		{
			getCurrentSession().save(statusMaster);
			

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
			getCurrentSession().saveOrUpdate(statusMaster);
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

			getCurrentSession().delete(statusMaster);
			

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

			StatusMaster sm=(StatusMaster)getCurrentSession().get(StatusMaster.class,Integer.valueOf(stID));

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
				Query qry = getCurrentSession().createQuery("from StatusMaster P where P.name =:name ");
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
