/*
 * @(#)Indexer.java 3.0, 17 Jun, 2013 2:50:32 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.models;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Indexer {
	public static void main(String[] args) {
		try{
			ClassPathXmlApplicationContext context= new ClassPathXmlApplicationContext(new String[]{"appContextTest.xml"});
			SessionFactory factory = (org.hibernate.SessionFactory) context.getBean("sessionFactory");
			final Session session = factory.openSession();
			session.beginTransaction();
			FullTextSession textSession = Search.getFullTextSession(session);
			for (String model: args) {
				List all = session.createCriteria(Class.forName(model)).list();
				for (Object object : all) {
					textSession.index(object);
				}
			}
			context.close();
			session.getTransaction().commit();
		}
		catch(ClassNotFoundException exp)
		{
		}
	}
}	
