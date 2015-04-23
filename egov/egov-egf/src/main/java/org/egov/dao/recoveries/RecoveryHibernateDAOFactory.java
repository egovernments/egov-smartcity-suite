package org.egov.dao.recoveries;

import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.recoveries.EgDeductionDetails;
import org.egov.model.recoveries.Recovery;
import org.hibernate.Session;

public class RecoveryHibernateDAOFactory extends RecoveryDAOFactory {

	protected Session getCurrentSession() {
		// returns a reference to the current Session.	        
		return HibernateUtil.getCurrentSession();
    }
	
	public TdsHibernateDAO getTdsDAO() {
        return new TdsHibernateDAO(Recovery.class,getCurrentSession());
    }
	
	public EgDeductionDetailsHibernateDAO getEgDeductionDetailsDAO() {
       return new EgDeductionDetailsHibernateDAO(EgDeductionDetails.class,getCurrentSession());
	}
}
