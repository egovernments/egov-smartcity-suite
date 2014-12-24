package org.egov.infstr.junit;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import junit.framework.TestCase;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.junit.utils.JndiObj;
import org.egov.infstr.junit.utils.TestUtils;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class EgovHibernateTest extends TestCase {

	static protected ApplicationContext applicationContext;
	static protected SessionFactory sessionFactory;
	static protected DataSource dataSource;
	static protected ServletContext servletContext = null;
	static protected JndiObj j;
	static {
		try {
			applicationContext = new ClassPathXmlApplicationContext(
					new String[] { "appContextTest.xml" });
			j = (JndiObj) applicationContext.getBean("jndi");
			sessionFactory = (SessionFactory) applicationContext
					.getBean("sessionFactory");
			final Map<String, SessionFactory> hibernateFactory = new HashMap<String, SessionFactory>();
			hibernateFactory.put(j.getHibFactName(), sessionFactory);
			setHibernateUtilField("hibernateFactory", hibernateFactory);
		} catch (final Throwable e) {
			throw new EGOVRuntimeException(e.getMessage(), e);
		}

	}
	Session session;

	protected static void setHibernateUtilField(final String fieldName,
			final Object value) throws NoSuchFieldException,
			IllegalAccessException {
		final Field session = HibernateUtil.class.getDeclaredField(fieldName);
		session.setAccessible(true);
		final Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField
				.setInt(session, session.getModifiers() & ~Modifier.FINAL);
		session.set(HibernateUtil.class, value);
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		EGOVThreadLocals.setJndiName(j.getJndiName());
		EGOVThreadLocals.setHibFactName(j.getHibFactName());
		EGOVThreadLocals.setDomainName("test.com");
		HibernateUtil.beginTransaction();
		this.session = HibernateUtil.getCurrentSession();
		TestUtils.activateInitialContext();
	}

	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		if (this.session != null && this.session.isOpen()) {
			HibernateUtil.rollbackTransaction();
			HibernateUtil.closeSession();
		}
	}

	protected void flush() {
		HibernateUtil.getCurrentSession().flush();
	}

	protected int count(final Class<?> clazz) {
		final Query q = HibernateUtil.getCurrentSession().createQuery(
				"select count(*) from " + clazz.getName() + " clz");
		return Integer.parseInt(q.list().get(0).toString());
	}

}
