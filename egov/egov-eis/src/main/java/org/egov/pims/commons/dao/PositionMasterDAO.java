/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
package org.egov.pims.commons.dao;

import org.apache.log4j.Logger;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.pims.commons.Position;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.Iterator;

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
            	throw new ApplicationException("position.master.null");
            }
            getCurrentSession().delete(position);
        }
        catch(Exception e)
        {
        	throw new ApplicationRuntimeException(e.getMessage(),e);
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
        	throw new ApplicationRuntimeException(e.getMessage(),e);
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
        	throw new ApplicationRuntimeException(e.getMessage(),e);
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
        	throw new ApplicationRuntimeException(e.getMessage(),e);
        }
    }
    
    

}
