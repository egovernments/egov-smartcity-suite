/*
 * @(#)SessionFactory.java 3.0, 17 Jun, 2013 3:08:18 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.services;

import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Session;

public class SessionFactory {

	public Session getSession() {
		return HibernateUtil.getCurrentSession();
	}
}
