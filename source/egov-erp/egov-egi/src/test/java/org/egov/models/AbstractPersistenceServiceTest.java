package org.egov.models;

import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.junit.After;
import org.junit.Before;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

import static org.junit.Assert.assertTrue;

public class AbstractPersistenceServiceTest<T, ID extends Serializable> {
	protected static org.hibernate.SessionFactory sessionFactory;
	protected PersistenceService<T, ID> service;
	protected Class type;
	protected Session session;
	protected PersistenceService genericService;
	protected FullTextSession textSession;
	private static ThreadLocal threadSession = new ThreadLocal();

	public static void setupFactory() {
		final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "appContextTest.xml" });
		sessionFactory = (org.hibernate.SessionFactory) context
				.getBean("sessionFactory");
	}

	protected static void setHibernateUtilField(final String fieldName,
			final Object value) throws NoSuchFieldException,
			IllegalAccessException {
		final Field session = HibernateUtil.class.getDeclaredField(fieldName);
		session.setAccessible(true);
		session.set(HibernateUtil.class, value);
	}

	@Before
	public void setup() {
		try {
			if (sessionFactory == null) {
				setupFactory();
			}
			this.session = sessionFactory.openSession();
			this.session.beginTransaction();
			this.textSession = Search.getFullTextSession(this.session);
			this.service = new PersistenceService<T, ID>();
			if (getClass().getGenericSuperclass() instanceof Class) {
				this.service.setType(this.type);
			} else if ((ParameterizedType) getClass().getGenericSuperclass() instanceof ParameterizedType) {
				final ParameterizedType parameterizedType = (ParameterizedType) getClass()
						.getGenericSuperclass();
				this.service.setType((Class) parameterizedType
						.getActualTypeArguments()[0]);
			}
			this.service.setSessionFactory(this.sessionFactory);
			this.genericService = new PersistenceService();
			this.genericService.setSessionFactory(sessionFactory);
			EGOVThreadLocals.setUserId("1");
			threadSession.set(this.session);
			setHibernateUtilField("threadSession", threadSession);
		} catch (final Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@After
	public void tearDown() {
		if (this.session.isOpen()
				&& !this.session.getTransaction().wasCommitted()) {
			this.session.getTransaction().rollback();
		}
	}

	protected boolean hasError(final ValidationException e, final String field,
			final String message) {
		return e.getErrors().contains(new ValidationError(field, message));
	}

	protected void assertHasError(final ValidationException e,
			final String field, final String msg) {
		assertTrue(hasError(e, field, msg));
	}

	protected int count(final Class<?> clazz) {
		final Query q = this.session.createQuery("select count(*) from "
				+ clazz.getName() + " clz");
		return Integer.parseInt(q.list().get(0).toString());
	}

	protected int countBy(final Class<?> clazz, final String whereClause) {
		final Query q = this.session.createQuery("select count(*) from "
				+ clazz.getName() + " where " + whereClause);
		return Integer.parseInt(q.list().get(0).toString());
	}

}
