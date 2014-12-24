package org.egov.pims.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.exceptions.NoSuchObjectException;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.pims.model.GradeMaster;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

public class GradeMasterHibernateDAO extends GenericHibernateDAO implements GradeMasterDao 
{
	private final static Logger LOGGER = Logger.getLogger(GradeMasterHibernateDAO.class.getClass());
	private Session session;
    /*
     *
	 * @param persistentClass
	 * @param session
	 */
	public GradeMasterHibernateDAO(Class persistentClass, Session session) {
		super(persistentClass, session);
		
	}
	public  GradeMaster getGradeMstrById(Integer gradeId)
	{
		session = HibernateUtil.getCurrentSession();
		if(gradeId==null)
        {
        	throw new EGOVRuntimeException("Grade id is null");
        }
    	try
        {
            
        	Query qry = session.createQuery("select g from  GradeMaster g where g.id=:gradeId ");
            qry.setInteger("gradeId", gradeId);
            GradeMaster gradeMstrById =(GradeMaster) qry.uniqueResult();
            if (gradeMstrById == null) {
            	throw new NoSuchObjectException("Grade master not found");
            }
            return gradeMstrById;
        }
        catch(Exception e)
        {
           throw new EGOVRuntimeException("system.error", e);
        }
		
	}
	public List getAllGradeMstr() throws Exception {

		List gradeList =null;
		
		try
		{
			StringBuffer query=new StringBuffer("from GradeMaster order by orderNo asc");
			Query qry = HibernateUtil.getCurrentSession().createQuery(query.toString());
			gradeList = qry.list();
			
	}catch (HibernateException he) {
		LOGGER.error(he.getMessage());
		throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(),he);
	} catch (Exception he) {
		LOGGER.error(he.getMessage());
		throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(),he);
	}
			return gradeList;

		
	}
	public List getAllDesgBasedOnGrade(Integer gradeId) throws Exception {
		
		List gradeList = null;
		try
		{
			StringBuffer query=new StringBuffer("from DesignationMaster dg where dg.gradeMstr.id=:gradeId");
			
			Query qry = HibernateUtil.getCurrentSession().createQuery(query.toString());
			qry.setInteger("gradeId", gradeId);
			gradeList = qry.list();
			
	}catch (HibernateException he) {
		LOGGER.error(he.getMessage());
		throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(),he);
	} catch (Exception he) {
		LOGGER.error(he.getMessage());
		throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(),he);
	}
	return gradeList;
	}

	
	private final static String STR_EXCEPTION = "Exception:";
	
}
