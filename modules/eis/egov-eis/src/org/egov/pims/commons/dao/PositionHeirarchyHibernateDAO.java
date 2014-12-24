/*
 * Created on Jan 17, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.egov.pims.commons.dao;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.commons.ObjectType;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.pims.commons.Position;
import org.egov.pims.commons.PositionHeirarchy;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * 
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PositionHeirarchyHibernateDAO extends GenericHibernateDAO implements
PositionHeirarchyDAO
{

	
	public PositionHeirarchyHibernateDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);

	}

	public Set getSetOfPosHeirForObjType(Integer objTypeId)
	{
		Set<PositionHeirarchy> set = new HashSet<PositionHeirarchy>();
		Query qry = getSession().createQuery("from PositionHeirarchy P where P.objectType =:objTypeId ");
		qry.setInteger("objTypeId", objTypeId);
		if(qry.list()!=null&&!qry.list().isEmpty())
		{
			Collection c = (Collection)qry.list();
			set.addAll(c);
		}
		return set;
	}
	public Position getHigherPos(Position position,ObjectType obType)
	{
		
		Query qry = getSession().createQuery("from PositionHeirarchy P where P.posFrom =:position and  P.objectType = :obType ");
		qry.setEntity("position", position);
		qry.setEntity("obType", obType);
		PositionHeirarchy positionHeirarchy = new PositionHeirarchy();
		if(qry.list()!=null&&!qry.list().isEmpty())
		{
			positionHeirarchy = (PositionHeirarchy)qry.list().get(0);
		}
		return positionHeirarchy.getPosTo();
		
	}
	
	/** 
	 * This method returns a position below the current position
	 * @param position
	 * @param obType
	 * @return
	 */
	public Position getLowerPos(Position position,ObjectType obType)
	{
		
		Query qry = getSession().createQuery("from PositionHeirarchy P where P.posTo =:position and  P.objectType = :obType ");
		qry.setEntity("position", position);
		qry.setEntity("obType", obType);
		PositionHeirarchy positionHeirarchy = new PositionHeirarchy();
		if(qry.list()!=null&&!qry.list().isEmpty())
		{
			positionHeirarchy = (PositionHeirarchy)qry.list().get(0);
		}
		return positionHeirarchy.getPosFrom();
		
	}
}
