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
/**
 *
 */
package org.egov.services.financingsource;

import org.apache.log4j.Logger;
import org.egov.commons.Fundsource;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author manoranjan
 *
 */
public class FinancingSourceService extends PersistenceService<Fundsource, Integer> {
    private static final Logger LOGGER = Logger.getLogger(FinancingSourceService.class);

    public FinancingSourceService() {
        super(Fundsource.class);
    }

    public FinancingSourceService(final Class<Fundsource> type) {
        super(type);
    }

    /**
     * @description - returns the list of financial source objects based on the parameter subscheme id.
     * @param subSchemeId - the subscheme id.
     * @return listFundSource - returns the list of financial sources for the supplied subscheme.
     */
    @SuppressWarnings("unchecked")
    public List<Fundsource> getFinancialSourceBasedOnSubScheme(final Integer subSchemeId) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("FinancingSourceService | getFinancialSourceBasedOnSubScheme | Start ");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Received sub scheme id = " + subSchemeId);
        final Criteria criteria = getSession().createCriteria(Fundsource.class);
        criteria.add(Restrictions.eq("isactive", true));
        if (!subSchemeId.equals(-1)) {
            final Criterion subschmeNull = Restrictions.isNull("subSchemeId");
            final Criterion subschme = Restrictions.eq("subSchemeId.id", subSchemeId);
            final LogicalExpression orExp = Restrictions.or(subschme, subschmeNull);
            criteria.add(orExp);
            criteria.addOrder(Order.asc("name"));
        }
        final List<Fundsource> listFundSource = criteria.list();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("financial source list size = " + listFundSource.size());
        return listFundSource;
    }

    public List<Fundsource> getListOfSharedFinancialSource() {

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("FinancingSourceService | getListOfSharedFinancialSource | Start ");
        final Criteria criteria = getSession().createCriteria(Fundsource.class);
        criteria.add(Restrictions.eq("isactive", true));
        criteria.add(Restrictions.isNull("subSchemeId"));
        criteria.addOrder(Order.asc("name"));
        final List<Fundsource> listFundSource = criteria.list();
        return listFundSource;
    }
}