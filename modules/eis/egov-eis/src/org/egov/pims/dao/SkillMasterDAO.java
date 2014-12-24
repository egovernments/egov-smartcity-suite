/*
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pims.dao;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.pims.model.SkillMaster;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
/**
 * @author deepak
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SkillMasterDAO implements Serializable
{
	public final static Logger LOGGER = Logger.getLogger(SkillMasterDAO.class.getClass());
	public SkillMasterDAO()
	{
		session = HibernateUtil.getCurrentSession();
	}
	private Session session;
	private void openSession()
	{
		session = HibernateUtil.getCurrentSession();
	}
	public void createSkillMaster(SkillMaster skillMaster)
	{
		try
		{
			if(! session.isOpen())
			{
				openSession();
			}
			session.save(skillMaster);
			

		}
		catch (Exception e)
		{
			
			throw new EGOVRuntimeException(STR_HIB_EXP+e.getMessage(),e);
		}

	}

	public void updateSkillMaster(SkillMaster skillMaster)
	{
		try
		{
			if(! session.isOpen())
			{
				openSession();
			}
			session.saveOrUpdate(skillMaster);
		}
		catch (Exception e)
		{
			
			throw new EGOVRuntimeException(STR_HIB_EXP+e.getMessage(),e);
		}

	}

	public void removeSkillMaster(SkillMaster skillMaster)
	{
		try
		{
			if(! session.isOpen())
			{
				openSession();
			}
			session.delete(skillMaster);
			

		}
		catch (Exception e)
		{
			
			throw new EGOVRuntimeException(STR_HIB_EXP+e.getMessage(),e);
		}

	}

	public SkillMaster getSkillMaster(int smID)
	{
		try
		{
			if(! session.isOpen())
			{
				openSession();
			}
			SkillMaster sm=(SkillMaster)session.get(SkillMaster.class,Integer.valueOf(smID));

			return sm ;
		}
		catch (Exception e)
		{
				
			throw new EGOVRuntimeException(STR_HIB_EXP+e.getMessage(),e);
		}


	}
	public Map getAllSkillMaster()
	{
		try
		{
			Query qry = session.createQuery("from SkillMaster SM ");
			Map<Integer,String> retMap = new LinkedHashMap<Integer,String>();
			for (Iterator iter = qry.iterate(); iter.hasNext();)
			{
				SkillMaster skillMaster = (SkillMaster)iter.next();
				retMap.put(skillMaster.getId(),skillMaster.getName());
			}
			return retMap;
		}
		catch (Exception e)
		{
				
				throw new EGOVRuntimeException(STR_HIB_EXP+e.getMessage(),e);
		}
	}

	public Map getAllGradesForSkill()
	{
		try
		{
			Query qry = session.createQuery("from SkillMaster SM ");
			Map<Integer,String> retMap = new LinkedHashMap<Integer,String>();
			for (Iterator iter = qry.iterate(); iter.hasNext();)
			{
				SkillMaster skillMaster = (SkillMaster)iter.next();
				retMap.put(skillMaster.getId(),skillMaster.getName());
			}
			return retMap;
		}
		catch (Exception e)
		{
				
				throw new EGOVRuntimeException(STR_HIB_EXP+e.getMessage(),e);
		}
	}
	public boolean checkDuplication(String name,String className)
	{
		try
		{
			boolean b = false;
			Query qry = session.createQuery("from "+className+" CA where trim(upper(CA.name)) = :name ");
			qry.setString("name", name);
			Iterator iter = qry.iterate();
			LOGGER.info("iter"+iter);
			if(iter.hasNext())
			{
				LOGGER.info("iter"+iter.hasNext());
				b = true;
			}
			return b;
		}
		catch (Exception e)
		{
				
				throw new EGOVRuntimeException(STR_HIB_EXP+e.getMessage(),e);
		}
	}



	private final static String STR_HIB_EXP = "Hibernate Exception : ";


}
