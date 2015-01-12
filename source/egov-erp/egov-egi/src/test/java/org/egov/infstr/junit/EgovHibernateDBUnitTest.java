package org.egov.infstr.junit;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.ext.oracle.OracleDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.junit.utils.JndiObj;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.ReturningWork;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.File;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.sql.Connection;

public abstract class EgovHibernateDBUnitTest extends
		org.dbunit.DatabaseTestCase {
	private static final String DEFAULT_XML = "<?xml version='1.0' encoding='UTF-8'?><dataset></dataset>";
	static protected ApplicationContext applicationContext;
	static protected SessionFactory sessionFactory;
	static protected JndiObj jnObj;

	static {
		applicationContext = new ClassPathXmlApplicationContext(
				new String[] { "appContextTest.xml" });
		jnObj = (JndiObj) applicationContext.getBean("jndi");
		sessionFactory = (SessionFactory) applicationContext
				.getBean("sessionFactory");
		InitialContext context;
		try {
			context = new InitialContext() {
				@Override
				public Object lookup(final String name) throws NamingException {
					return sessionFactory;
				}

			};
			setHibernateUtilField("context", context);

		} catch (final Exception e) {
			throw new EGOVRuntimeException(e.getMessage(), e);
		}

	}

	private static void setHibernateUtilField(final String fieldName,
			final Object value) throws NoSuchFieldException,
			IllegalAccessException {
		final Field session = HibernateUtil.class.getDeclaredField(fieldName);
		session.setAccessible(true);
		session.set(HibernateUtil.class, value);
	}

	@Override
	protected DatabaseOperation getSetUpOperation() throws Exception {
		getConnection();
		return DatabaseOperation.REFRESH;
	}

	@Override
	protected DatabaseOperation getTearDownOperation() throws Exception {
		return DatabaseOperation.NONE;

	}

	protected void flush() {
		HibernateUtil.getCurrentSession().flush();
	}

	@Override
	protected IDatabaseConnection getConnection() throws Exception {
		EGOVThreadLocals.setJndiName(jnObj.getJndiName());
		EGOVThreadLocals.setHibFactName(jnObj.getHibFactName());
		EGOVThreadLocals.setDomainName("test.com");
		final String schema = jnObj.getSchema();
		final IDatabaseConnection conn = HibernateUtil.getCurrentSession()
				.doReturningWork(new ReturningWork<IDatabaseConnection>() {
					@Override
					public IDatabaseConnection execute(final Connection con) {
							return new DatabaseConnection(con, schema.toUpperCase());
					}
				});
		final DatabaseConfig config = conn.getConfig();
		config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY,
				new OracleDataTypeFactory());
		final String tableType[] = { "TABLE", "VIEW" };
		config.setProperty(DatabaseConfig.PROPERTY_TABLE_TYPE, tableType);
		return conn;
	}

	protected String dataSet() {
		return null;
	}

	@Override
	protected IDataSet getDataSet() throws Exception {
		// TODO Auto-generated method stub
		final File dataFile = new File(EgovHibernateDBUnitTest.class
				.getClassLoader().getResource(dataSet()).getFile());
		IDataSet loadedDataSet = null;
		if (dataSet() == null) {
			loadedDataSet = new FlatXmlDataSet(new StringReader(DEFAULT_XML));
		} else {
			loadedDataSet = new FlatXmlDataSet(dataFile);
		}
		return loadedDataSet;
	}
}