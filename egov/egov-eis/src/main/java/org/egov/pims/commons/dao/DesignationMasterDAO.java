/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.pims.commons.dao;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.exceptions.NoSuchObjectException;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.pims.commons.DesignationMaster;
import org.hibernate.Query;
import org.hibernate.Session;

// Referenced classes of package org.egov.infstr.commons.dao:
//            DesignationMaster

public class DesignationMasterDAO 
    implements Serializable
{

    public final static Logger LOGGER = Logger.getLogger(DesignationMasterDAO.class.getClass());
    
    @PersistenceContext
	private EntityManager entityManager;
    
	public Session  getCurrentSession() {
		return entityManager.unwrap(Session.class);
	}

    public void createDesignationMaster(DesignationMaster designation) throws EGOVException
    {
            if(designation==null)
            {
            	throw new EGOVException("designation.master.null");
            }
            getCurrentSession().saveOrUpdate(designation);
    }

    public void updateDesignationMaster(DesignationMaster designation) throws EGOVException
    {
            if(designation==null)
            {
            	throw new EGOVException("designation.master.null");
            }
            getCurrentSession().saveOrUpdate(designation);
    }

    public void removeDesignationMaster(DesignationMaster designation) throws EGOVException
    {
            if(designation==null)
            {
            	throw new EGOVException("designation.master.null");
            }
            getCurrentSession().delete(designation);
            
    }

    public DesignationMaster getDesignationMaster(int desigID) throws EGOVException
    {
        	DesignationMaster desig = null;
            if(Integer.valueOf(desigID)==null)
            {
            	throw new EGOVException("designation.id.null");
            }
            desig = (DesignationMaster)getCurrentSession().get(DesignationMaster.class, Integer.valueOf(desigID));
            return desig;
    }

    public Map getAllDesignationMaster() throws EGOVException
    {
            Query qry = getCurrentSession().createQuery("from DesignationMaster DM ");
            Map retMap = new LinkedHashMap();
            DesignationMaster designation=null;
            for(Iterator iter = qry.iterate(); iter.hasNext(); retMap.put(designation.getId(), designation.getName()))
            {
                designation = (DesignationMaster)(DesignationMaster)iter.next();
            }
            if(designation==null)
            {
            	throw new EGOVException("designation.notFound");
            }
            return retMap;
    }

    public boolean checkDuplication(String designationName, String className)
    {
        try
        {
            if(designationName==null)
            {
            	throw new EGOVException("designation.name.null");
            }
            if(className==null)
            {
            	throw new EGOVException("className.null");
            }
        	boolean b = false;
            Query qry = getCurrentSession().createQuery((new StringBuilder("from ")).append(className).append(" CA where trim(upper(CA.designationName)) = :designationName ").toString());
            qry.setString("designationName", designationName);
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
    
    /**
     * All comparisons are done after converting to uppercase.
     * @param designationName
     * @return Designation master object if a unique one was found for the input designationName, or null otherwise.
     * @throws NoSuchObjectException ,the input to this exception method is key i.e., defined in application resources
     * please make sure you are using this key and displaying appropriate messages to client. 
     */
    public DesignationMaster getDesignationByDesignationName(String designationName) throws NoSuchObjectException
    {
        
    	if(designationName==null)
        {
        	throw new EGOVRuntimeException("designation.name.null");
        }
    	try
        {
            
        	Query qry = getCurrentSession().createQuery("select d from  DesignationMaster d where trim(upper(d.designationName)) = :designationName ");
            qry.setString   ("designationName", designationName.toUpperCase());
            DesignationMaster desig =(DesignationMaster) qry.uniqueResult();
            if (desig == null) {
            	throw new NoSuchObjectException("designation.master.notFound");
            }
            return desig;
        }
        catch(Exception e)
        {
           throw new EGOVRuntimeException(e.getMessage(),e);
        }
    } 
}
