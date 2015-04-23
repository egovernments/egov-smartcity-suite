/**
 * 
 */
package org.egov.services.financingsource;

import java.util.List;

import org.apache.log4j.Logger;
import org.egov.commons.Fundsource;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * @author manoranjan
 *
 */
public class FinancingSourceService extends PersistenceService<Fundsource, Integer>{
	private static final Logger	LOGGER	= Logger.getLogger(FinancingSourceService.class);
	
	/**
	 * @description - returns the list of financial source objects based on the parameter subscheme id.
	 * @param subSchemeId - the subscheme id.
	 * @return listFundSource - returns the list of financial sources for the supplied subscheme. 
	 */
	@SuppressWarnings("unchecked")
	public List<Fundsource> getFinancialSourceBasedOnSubScheme(Integer subSchemeId){
		if(LOGGER.isDebugEnabled())     LOGGER.debug("FinancingSourceService | getFinancialSourceBasedOnSubScheme | Start ");
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Received sub scheme id = "+ subSchemeId);
		Criteria criteria = null;//This fix is for Phoenix Migration.HibernateUtil.getCurrentSession().createCriteria(Fundsource.class);
		criteria.add(Restrictions.eq("isactive", true));
		if(!subSchemeId.equals(-1)){
			Criterion subschmeNull = Restrictions.isNull("subSchemeId");
			Criterion subschme = Restrictions.eq("subSchemeId.id", subSchemeId);
			LogicalExpression orExp = Restrictions.or(subschme,subschmeNull);
			criteria.add(orExp);
			criteria.addOrder(Order.asc("name"));
		}
		List<Fundsource> listFundSource = criteria.list();
		if(LOGGER.isDebugEnabled())     LOGGER.debug("financial source list size = "+ listFundSource.size());
		return listFundSource;
	}
	
	public List<Fundsource> getListOfSharedFinancialSource(){
		
		if(LOGGER.isDebugEnabled())     LOGGER.debug("FinancingSourceService | getListOfSharedFinancialSource | Start ");
		Criteria criteria =null;//This fix is for Phoenix Migration.HibernateUtil.getCurrentSession().createCriteria(Fundsource.class);
		criteria.add(Restrictions.eq("isactive", true));
		criteria.add(Restrictions.isNull("subSchemeId"));
		criteria.addOrder(Order.asc("name"));
		List<Fundsource> listFundSource = criteria.list();
		return listFundSource;
	}
}
