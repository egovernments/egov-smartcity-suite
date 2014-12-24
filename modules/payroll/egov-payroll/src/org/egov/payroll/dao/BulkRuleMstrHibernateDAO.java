package org.egov.payroll.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.payroll.model.PayGenUpdationRule;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

public class BulkRuleMstrHibernateDAO extends GenericHibernateDAO implements BulkRuleMstrDAO
{
	public static final Logger logger = Logger.getLogger(BulkRuleMstrHibernateDAO.class.getClass());
	private Session session;	
	 public BulkRuleMstrHibernateDAO(Class persistentClass, Session session)
	    {
	        super(persistentClass, session);
	    }
	 
	public  PayGenUpdationRule checkRuleBasedOnSalCodeMonFnYrEmpGrp(Integer salaryCodeId,Integer monthId,Integer finId,Integer empGrpMstrId)
	{
		String mainStr = "from PayGenUpdationRule ruleObj where " +
				"ruleObj.salaryCodes.id =:salaryCodeId and ruleObj.month=:monthId and ruleObj.financialyear.id=:finId ";
		if(empGrpMstrId!=null && empGrpMstrId!=0)
		{
			mainStr+= " and ruleObj.empGroupMstrs.id=:empGrpMstrId";
		}
		Query qry = getSession().createQuery(mainStr);
        qry.setInteger("salaryCodeId",salaryCodeId);
        qry.setBigDecimal("monthId", new BigDecimal(monthId));
        qry.setLong("finId",Long.valueOf(finId));
        if(empGrpMstrId!=null && empGrpMstrId!=0)
		{
			qry.setInteger("empGrpMstrId",empGrpMstrId);
		}
        return (PayGenUpdationRule)qry.uniqueResult();
		
	}
	
	
	public PayGenUpdationRule getRuleMstrById(Integer ruleId)
	{
		Query qry = getSession().createQuery("from PayGenUpdationRule ruleObj where ruleObj.id=:ruleId");
		qry.setInteger("ruleId",ruleId);
		return (PayGenUpdationRule)qry.uniqueResult();
	}
	public List getRulemasterByMonFnYr(Integer monthId,Integer finId) {
		
		Query qry = getSession().createQuery("from PayGenUpdationRule ruleObj where ruleObj.month = :monthId and ruleObj.financialyear.id=:finId");
		 qry.setBigDecimal("monthId", new BigDecimal(monthId));
	     qry.setLong("finId",Long.valueOf(finId));
	     return qry.list();
		
	}
	public PayGenUpdationRule getLatestRulemasterAsOnDate(Integer salaryCodeId,Date payslipGenDate,Integer empGrpMstrsId) {
		logger.info("Inside getLatestRulemasterAsOnDate: payslipGenDate "+payslipGenDate);
		PayGenUpdationRule payGenUpdationRule = null;
		String mainStr = "from PayGenUpdationRule ruleObj where ruleObj.salaryCodes.id =:salaryCodeId and ruleObj.effectivedate <= :payslipGenDate ";
		if(empGrpMstrsId!=null && empGrpMstrsId!=0)
		{
			mainStr+= " and ruleObj.empGroupMstrs.id=:empGrpMstrsId ";
		}
		
		mainStr+= " order by effectivedate desc ";
		Query qry = getSession().createQuery(mainStr);
		qry.setInteger("salaryCodeId",salaryCodeId);
		qry.setDate("payslipGenDate", payslipGenDate);
		if( empGrpMstrsId!=null && empGrpMstrsId!=0 )
		{
			qry.setInteger("empGrpMstrsId",empGrpMstrsId);
		}
		List result = qry.list();
		if(result != null && !result.isEmpty())
		{	
			payGenUpdationRule=(PayGenUpdationRule)result.get(0);			
			logger.info("payGenUpdationRule.getEffectivedate= "+payGenUpdationRule.getEffectivedate());
		}
		return  payGenUpdationRule;		
	}
	
	//To Modify rule 
	public boolean checkRuleExists(Integer ruleId,Integer salaryCodeId,Integer monthId,Integer finId,Integer empGrpMstrId)
	{
		session = HibernateUtil.getCurrentSession();
		boolean b = false;
		Query qry =null;
		try
		{
			
				String main = "from PayGenUpdationRule ruleObj where " +
				"ruleObj.salaryCodes.id =:salaryCodeId and ruleObj.month=:monthId and " +
				"ruleObj.financialyear.id=:finId ";
				
				if(ruleId!=null)
		        {
					main+=" and ruleObj.id <>:ruleId";
		        }
				if(empGrpMstrId!=null && empGrpMstrId!=0)
				{
					main+=" and ruleObj.empGroupMstrs.id=:empGrpMstrId ";
				}
				qry=session.createQuery(main);
				qry.setInteger("salaryCodeId",salaryCodeId);
				qry.setBigDecimal("monthId", new BigDecimal(monthId));
		        qry.setLong("finId",Long.valueOf(finId));
		        if(empGrpMstrId!=null && empGrpMstrId!=0)
				{
					qry.setInteger("empGrpMstrId",empGrpMstrId);
				}
		        if(ruleId!=null)
		        {
		        	qry.setInteger("ruleId",ruleId);
		        }
		        b=(qry.list()!=null && !qry.list().isEmpty());
			
		}
		catch (HibernateException he) {
			logger.error(he.getMessage());
			throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
		} catch (Exception he) {
			logger.error(he.getMessage());
			  throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
		}
		return b;
	}
	
		
	
}
