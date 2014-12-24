/*
 * @(#)AjaxQueryUtil.java 3.0, 10 Jun, 2013 11:24:46 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.utils;

import java.util.List;
import java.util.Map;

import org.egov.exceptions.EGOVRuntimeException;
import org.hibernate.SQLQuery;

public class AjaxQueryUtil {
	public List executeAjaxQuery(final String xmlFileName, final String levelOfQuery, final String queryName, final Map map) {
		List list = null;
		if (xmlFileName == null || levelOfQuery == null || queryName == null) {
			throw new EGOVRuntimeException("Named query is Missing");
		} else {
			final String queryValue = EGovConfig.getProperty(xmlFileName, levelOfQuery, "", queryName);
			if (queryValue == null || queryValue.trim().equals("")) {
				throw new EGOVRuntimeException("Named query is Missing");
			}

			list = evaluateQuery(queryValue, map);
		}
		return list;
	}

	public List evaluateQuery(final String query, final Map parameterMap) {

		final SQLQuery qry = HibernateUtil.getCurrentSession().createSQLQuery(query);
		if (!query.contains(":text") && parameterMap != null) {
			parameterMap.remove("text");
		}
		if (parameterMap != null) {
			qry.setProperties(parameterMap);

		}
		return qry.list();
	}

}
