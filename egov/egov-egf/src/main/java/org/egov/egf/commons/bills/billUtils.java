/*
 * Created on Aug 6, 2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.egov.egf.commons.bills;

import org.apache.log4j.Logger;
import org.egov.commons.Relation;
import org.egov.commons.dao.RelationHibernateDAO;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.utils.EgovUtils;

/**
 * @author sapna
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class billUtils {
	public static  final Logger LOGGER = Logger.getLogger(billUtils.class);
	/**
	 * Get the relation Object 
	 * @param id
	 * @return
	 */
	static public Relation findRelationById(Integer id)
 	{
     	try {
     		if(LOGGER.isInfoEnabled())     LOGGER.info("id == "+id );
			RelationHibernateDAO relDAO= null;//This fix is for Phoenix Migration.CommonsDaoFactory.getDAOFactory().getRelationDAO();
			return (Relation)relDAO.findById(id,false);
		} catch (RuntimeException e) {
			EgovUtils.rollBackTransaction();
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
     }

}
