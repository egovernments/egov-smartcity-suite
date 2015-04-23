package org.egov.billsaccounting.dao;
/*
 * Created on Mar 5, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
import java.util.List;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.billsaccounting.model.EgwWorksDeductions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.egov.billsaccounting.model.Worksdetail; 
         
/**
 * @author Admin
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EgwWorksDeductionsHibernateDAO extends GenericHibernateDAO
{

	public EgwWorksDeductionsHibernateDAO(final Class persistentClass, final Session session)
	{
			super(persistentClass, session);
	}    
    
     public List<EgwWorksDeductions> getStatutoryDeductionsByWorksdetail(final Worksdetail worksdetail)
     {
         final Query qry =HibernateUtil.getCurrentSession().createQuery("from EgwWorksDeductions ewd where ewd.dedtype = 'S' and ewd.worksdetail=:worksdetail");
         qry.setEntity("worksdetail", worksdetail);
         return qry.list();
     }
      
      public List<EgwWorksDeductions> getNonStatutoryDeductionsByWorksdetail(final Worksdetail worksdetail)
      {
          final Query qry =HibernateUtil.getCurrentSession().createQuery("from EgwWorksDeductions ewd where ewd.dedtype = 'N' and ewd.worksdetail=:worksdetail");
          qry.setEntity("worksdetail", worksdetail);
          return qry.list();
      }

	public List getAllDeductionsByWorkorder(final String worksdetailid) {
		// TODO Auto-generated method stub
		return null;
	}
}