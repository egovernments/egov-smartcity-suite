package org.egov.pims.commons.dao;

import java.io.Serializable;
import java.util.Iterator;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.pims.commons.Position;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

// Referenced classes of package org.egov.infstr.commons.dao:
//            PositionMaster

@Repository
public class PositionMasterDAO
    implements Serializable
{

    public final static Logger LOGGER = Logger.getLogger(PositionMasterDAO.class.getClass());
    
    @PersistenceContext
	private EntityManager entityManager;
    
	public Session  getCurrentSession() {
		return entityManager.unwrap(Session.class);
	}


    public void createPositionMaster(Position position)
    {
            getCurrentSession().save(position);
    }

    public void updatePosition(Position position)
    {
    	getCurrentSession().saveOrUpdate(position);
    }

    public void removePosition(Position position)
    {
        try
        {
            if(position==null)
            {
            	throw new EGOVException("position.master.null");
            }
            getCurrentSession().delete(position);
        }
        catch(Exception e)
        {
        	throw new EGOVRuntimeException(e.getMessage(),e);
        }
    }

    public Position getPosition(int posId)
    {
        try
        {
        	Position desig = null;
            desig = (Position)getCurrentSession().get(Position.class, Integer.valueOf(posId));
            return desig;
        }
        catch(Exception e)
        {
        	throw new EGOVRuntimeException(e.getMessage(),e);
        }
    }

    public boolean checkDuplication(String positionName, String className)
    {
        try
        {
            boolean b = false;
            Query qry = getCurrentSession().createQuery((new StringBuilder("from ")).append(className).append(" CA where trim(upper(CA.position)) = :positionName ").toString());
            qry.setString("positionName", positionName);
            Iterator iter = qry.iterate();
            LOGGER.info((new StringBuilder("iter")).append(iter).toString());
            if(iter.hasNext())
            {
                LOGGER.info((new StringBuilder("iter")).append(iter.hasNext()).toString());
                b = true;
            }
            return b;
        }
        catch(Exception e)
        {
        	throw new EGOVRuntimeException(e.getMessage(),e);
        }
    }
    
    
    public Integer getPositionIdByPositionName(String positionName)
    {
        try
        {
            Integer positionId = 0;
            Query qry = getCurrentSession().createQuery("from position where trim(upper(name)) = :positionName ");
            qry.setString("positionName", positionName);
            Iterator iter = qry.iterate();
            LOGGER.info((new StringBuilder("iter")).append(iter).toString());
            if(iter.hasNext())
            {
               
                positionId = (Integer)iter.next();
            }
            return positionId;
        }
        catch(Exception e)
        {
        	throw new EGOVRuntimeException(e.getMessage(),e);
        }
    }
    
    

}
