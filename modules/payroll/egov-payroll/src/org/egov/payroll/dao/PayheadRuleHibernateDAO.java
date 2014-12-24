package org.egov.payroll.dao;
/**
 * @author surya
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.payroll.model.PayheadRule;
import org.egov.payroll.model.SalaryCodes;
import org.hibernate.Query;
import org.hibernate.Session;



public class PayheadRuleHibernateDAO extends GenericHibernateDAO implements PayheadRuleDAO {


    public PayheadRuleHibernateDAO(Class persistentClass, Session session)
    {
        super(persistentClass, session);
    }
    
    
    public PayheadRule getEffectivePayheadRule(SalaryCodes salarycode, Date effectiveFrom){
    	PayheadRule payheadRule = null;    	
    	Query qry = getSession().createQuery("from PayheadRule pr where pr.salarycode = :salarycode and pr.effectiveFrom in" +
    										 "(select max(pr1.effectiveFrom) from PayheadRule pr1 where pr1.salarycode = pr.salarycode and  pr1.effectiveFrom <= :effectiveFrom)");
    	qry.setEntity("salarycode", salarycode);
    	qry.setDate("effectiveFrom", effectiveFrom);
    	payheadRule = (PayheadRule)qry.uniqueResult();     	
    	return payheadRule;
    }
    
	/**
	 * Get all payheadrule
	 */
	public List<PayheadRule> getAllPayheadRule(){
		List<PayheadRule> payheadRuleList = new ArrayList<PayheadRule>();
    	Query qry = getSession().createQuery("from PayheadRule PR order by PR.id");
    	payheadRuleList=qry.list();
		return payheadRuleList;
	}
	public List<PayheadRule> getAllPayheadRulesBySalCode(Integer id)
	{
		List<PayheadRule> ruleList = new ArrayList<PayheadRule>();
		Query qry= getSession().createQuery("from PayheadRule PR where PR.salarycode.id=:id");
		qry.setInteger("id",id);
		ruleList=qry.list();
		return ruleList;
	}
 
}