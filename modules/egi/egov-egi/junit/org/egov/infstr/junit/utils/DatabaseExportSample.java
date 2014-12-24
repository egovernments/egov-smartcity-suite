package org.egov.infstr.junit.utils;

import java.io.FileOutputStream;
import java.sql.Connection;

import javax.sql.DataSource;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.database.search.TablesDependencyHelper;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.ext.oracle.OracleDataTypeFactory;
import org.egov.exceptions.EGOVRuntimeException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DatabaseExportSample {
	static protected final IDatabaseConnection connection;
	static protected final ApplicationContext applicationContext;
	static protected final JndiObj jnObj;
	static {
		try {
			applicationContext = new ClassPathXmlApplicationContext(
					new String[] { "config/appContextTest.xml" });
			jnObj = (JndiObj) applicationContext.getBean("jndi");
			final DataSource dataSource = (DataSource) applicationContext
					.getBean("oracleDS");
			final Connection jdbcConnection = dataSource.getConnection();
			connection = new DatabaseConnection(jdbcConnection,
					jnObj.getSchema());
			final DatabaseConfig config = connection.getConfig();
			config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY,
					new OracleDataTypeFactory());
			final String tableType[] = { "TABLE", "VIEW" };
			config.setProperty(DatabaseConfig.PROPERTY_TABLE_TYPE, tableType);

		} catch (final Exception e) {
			// TODO Auto-generated catch block
			throw new EGOVRuntimeException(e.getMessage(), e);
		}

	}

	public static void getPartialDataSet(final String[] tablesNames) {

		QueryDataSet partialDataSet;
		try {
			// partial database export
			partialDataSet = new QueryDataSet(connection);
			for (final String tableName : tablesNames) {
				partialDataSet.addTable(tableName);
			}
			FlatXmlDataSet.write(partialDataSet, new FileOutputStream("junit/"
					+ jnObj.getDataSetPath()));

		} catch (final Exception e) {
			// TODO Auto-generated catch block
			throw new EGOVRuntimeException(e.getMessage(), e);
		}
	}

	public static void getCompleteDataSet() {
		try {
			// full database export
			final IDataSet fullDataSet = connection.createDataSet();
			FlatXmlDataSet.write(fullDataSet, new FileOutputStream("junit/"
					+ jnObj.getDataSetPath()));

			System.out.println("Dataset written");
		} catch (final Exception e) {
			// TODO Auto-generated catch block
			throw new EGOVRuntimeException(e.getMessage(), e);
		}

	}

	public static void getDependentDataSet(final String tableName) {
		try {
			// dependent tables database export: export table X and all tables
			// that
			// have a PK which is a FK on X, in the right order for insertion
			final String[] depTableNames = TablesDependencyHelper
					.getAllDependentTables(connection, tableName);
			final IDataSet depDataset = connection.createDataSet(depTableNames);
			FlatXmlDataSet.write(depDataset, new FileOutputStream("junit/"
					+ jnObj.getDataSetPath()));
		} catch (final Exception e) {
			// TODO Auto-generated catch block
			throw new EGOVRuntimeException(e.getMessage(), e);
		}
	}

}