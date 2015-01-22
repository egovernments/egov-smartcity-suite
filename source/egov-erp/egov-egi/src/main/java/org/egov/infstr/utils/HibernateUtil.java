/*
 * @(#)HibernateUtil.java 3.0, 18 Jun, 2013 12:05:53 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.utils;

import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * Basic Hibernate helper class, handles SessionFactory, Session and Transaction.
 * <p>
 * Uses a static initializer to read startup options and initialize <tt>Configuration</tt> and <tt>SessionFactory</tt>.
 * <p>
 * This class tries to figure out if either ThreadLocal handling of the <tt>Session</tt> and <tt>Transaction</tt> should be used,
 * for resource local transactions or BMT, or if CMT with automatic <tt>Session</tt> handling is enabled.
 * <p>
 * To keep your DAOs free from any of this, just call <tt>HibernateUtil.getCurrentSession()</tt>in the constructor of each DAO.,
 * The recommended way to set resource local or BMT transaction boundaries is an interceptor, or a request filter.
 * <p>
 * This class also tries to figure out if JNDI binding of the <tt>SessionFactory</tt>is used, otherwise it falls back to a global static variable (Singleton).
 * <p>
 * If you want to assign a global interceptor, set its fully qualified class name with the system (or hibernate.properties/hibernate.cfg.xml) property
 * <tt>hibernate.util.interceptor_class</tt>. It will be loaded and instantiated on static initialization of HibernateUtil;
 * it has to have a no-argument constructor. You can call <tt>getInterceptor()</tt> if you need to provide settings before using the interceptor.
 * <p>
 */
@Deprecated
public class HibernateUtil {

	private static final Logger LOG = LoggerFactory.getLogger(HibernateUtil.class);
	private static final Map<String, SessionFactory> hibernateFactory = new HashMap<String, SessionFactory>();
	private static ThreadLocal<Session> threadSession = new ThreadLocal<Session>();
	private static ThreadLocal<Transaction> threadTransaction = new ThreadLocal<Transaction>();
	private static ThreadLocal<Object> markForRollback = new ThreadLocal<Object>();
	private static boolean useThreadLocal = true;
	private static TypeFilter[] entityTypeFilters = new TypeFilter[] {
			new AnnotationTypeFilter(Entity.class, false),
			new AnnotationTypeFilter(Embeddable.class, false),
			new AnnotationTypeFilter(MappedSuperclass.class, false) };

	private static synchronized SessionFactory createSessionFactory(final String jndiName) {

		SessionFactory sessionFact = hibernateFactory.get(jndiName);
		if (sessionFact == null) {
			final Configuration configuration = new Configuration();
			resolveAndConfigureMappingJars(configuration);
			resolveAndConfigureAnnotedClass(configuration);
			configuration.setProperty(AvailableSettings.DATASOURCE, EGOVThreadLocals.getJndiName());
			configuration.configure();
			sessionFact = configuration.buildSessionFactory(new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry());
			hibernateFactory.put(jndiName, sessionFact);
		}

		return sessionFact;

	}

	private static void resolveAndConfigureMappingJars(final Configuration configuration) {
		final String filePath = Thread.currentThread().getContextClassLoader().getResource("hibernate.cfg.xml").getPath();
		final String mappingJarsLocation = filePath.substring(0, filePath.indexOf("/lib/")) + "/lib";
		final File mappingJarsLocationFile = new File(mappingJarsLocation);
		final String[] jars = mappingJarsLocationFile.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.startsWith("egov-") && name.endsWith(".jar");
			}
		});

		configuration.addDirectory(mappingJarsLocationFile);
		final String mappingssJarPath = mappingJarsLocationFile.getAbsolutePath() + File.separator;
		for (final String jar : jars) {
			configuration.addJar(new File(mappingssJarPath + jar));
		}
	}

	/**
	 * Will search and find all the javax.persistence annotted class from org.egov package. and it to hibernate configuration.
	 **/
	private static void resolveAndConfigureAnnotedClass(final Configuration configuration) {
		try {
			final ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
			final Resource[] resources = resourcePatternResolver.getResources("classpath*:org/egov/**/*.class");
			final MetadataReaderFactory readerFactory = new SimpleMetadataReaderFactory(resourcePatternResolver);
			for (final Resource resource : resources) {
				if (resource.isReadable()) {
					final MetadataReader reader = readerFactory.getMetadataReader(resource);
					final String className = reader.getClassMetadata().getClassName();
					if (matchesFilter(reader, readerFactory)) {
						configuration.addAnnotatedClass(resourcePatternResolver.getClassLoader().loadClass(className));
					}
				}
			}
		} catch (final Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/*
	 * Check whether any of the configured entity type filters matches the current class descriptor contained in the metadata reader.
	 */
	private static boolean matchesFilter(final MetadataReader reader, final MetadataReaderFactory readerFactory) throws IOException {
		if (entityTypeFilters != null) {
			for (final TypeFilter filter : entityTypeFilters) {
				if (filter.match(reader, readerFactory)) {
					return true;
				}
			}
		}
		return false;
	}

	@Deprecated
	public static SessionFactory getSessionFactory() {
		final String jndiName = EGOVThreadLocals.getHibFactName();
		final SessionFactory sessionFact = hibernateFactory.get(jndiName);
		return sessionFact != null ? sessionFact : createSessionFactory(jndiName);
	}

	@Deprecated
	public static Session getCurrentSession() {
		if (useThreadLocal) {
			Session s = threadSession.get();
			if (s == null || !s.isOpen()) {
				s = getSessionFactory().openSession();
				threadSession.set(s);
			}
			return s;
		} else {
			return getSessionFactory().getCurrentSession();
		}
	}

	public static void beginTransaction() {
		if (useThreadLocal) {
			Transaction tx = threadTransaction.get();
			if (tx == null) {
				tx = getCurrentSession().beginTransaction();
				threadTransaction.set(tx);
			}
		}
	}

	public static void commitTransaction() {
		if (useThreadLocal) {
			final Transaction tx = threadTransaction.get();
			try {
				if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack()) {
					if (isMarkedForRollback()) {
						rollbackTransaction();
					} else {
						tx.commit();
					}
				}
				threadTransaction.set(null);
				clearMarkedForRollback();
			} catch (final RuntimeException ex) {
				LOG.error("Error occurred while committing Trasaction", ex);
				rollbackTransaction();
				throw ex;
			}
		}
	}

	public static void rollbackTransaction() {
		if (useThreadLocal) {
			final Transaction tx = threadTransaction.get();
			try {
				threadTransaction.set(null);
				if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack() && tx.isActive()) {
					tx.rollback();
				}
			} catch (final RuntimeException ex) {
				throw new RuntimeException("Might swallow original cause, check ERROR log!", ex);
			} finally {
				clearMarkedForRollback();
				closeSession();
			}
		}
	}

	public static void closeSession() {
		if (useThreadLocal) {
			final Session session = threadSession.get();
			threadSession.set(null);
			if (session != null && session.isOpen() && session.isConnected() && session.getTransaction().isActive()) {
				session.close();
			}
		}
	}

	public static void markForRollback() {
		markForRollback.set(new Object());
	}

	public static boolean isMarkedForRollback() {
		return markForRollback.get() != null;
	}

	public static void clearMarkedForRollback() {
		markForRollback.set(null);
	}

	public static void release(Statement statement, ResultSet resultSet) throws SQLException {
		if (statement != null) {
			statement.close();
		}

		if (resultSet != null) {
			resultSet.close();
		}

	}

}
