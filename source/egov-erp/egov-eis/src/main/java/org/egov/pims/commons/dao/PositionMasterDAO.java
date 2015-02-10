package org.egov.pims.commons.dao;

import java.io.Serializable;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.pims.commons.Position;
import org.hibernate.Query;
import org.hibernate.Session;

// Referenced classes of package org.egov.infstr.commons.dao:
//            PositionMaster

public class PositionMasterDAO
    implements Serializable
{

    public final static Logger LOGGER = Logger.getLogger(PositionMasterDAO.class.getClass());
    private Session session;


    public PositionMasterDAO()
    {
        session = HibernateUtil.getCurrentSession();
    }

    private void openSession()
    {
        session = HibernateUtil.getCurrentSession();
    }

    public void createPositionMaster(Position position)
    {
        try
        {
            if(!session.isOpen())
            {
                openSession();
            }
            session.save(position);
           
        }
        catch(Exception e)
        {
        	
            throw new EGOVRuntimeException(e.getMessage(),e);
        }
    }

    public void updatePosition(Position position)
    {
        try
        {
            if(!session.isOpen())
            {
                openSession();
            }
            session.saveOrUpdate(position);
        }
        catch(Exception e)
        {
            throw new EGOVRuntimeException(e.getMessage(),e);
        }
    }

    public void removePosition(Position position)
    {
        try
        {
            if(!session.isOpen())
            {
                openSession();
            }
            if(position==null)
            {
            	throw new EGOVException("position.master.null");
            }
            session.delete(position);
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
            if(!session.isOpen())
            {
                openSession();
            }
            desig = (Position)session.get(Position.class, Integer.valueOf(posId));
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
            Query qry = session.createQuery((new StringBuilder("from ")).append(className).append(" CA where trim(upper(CA.positionName)) = :positionName ").toString());
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
            Query qry = session.createQuery("from position where trim(upper(positionName)) = :positionName ");
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
