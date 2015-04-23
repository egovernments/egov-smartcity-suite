package com.exilant.exility.dataservice;

import java.sql.Connection;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.log4j.Logger;



public class DBHandler {

	private final static Logger LOGGER = Logger.getLogger(DBHandler.class);
	private final static String datasource = "java:/ezgovDatabasePool";
	private final static String errConnOpenString = "Unable to get a connection from Pool:" + datasource + ". Please make sure that the connection pool is set up properly";
	private final static String errStmtString = "Unable to Close the statement";
	private final static String errConnCloseString = "Unable to Close the statement";
	private final static String infoGetCon = "Requesting a new connection";
	private final static String infoRelCon = "Releasing connection";
	private final static String infoCrtCon= "Creating a new connection";
	private static javax.sql.DataSource ds = null;
	// cache the jndi look up
	static {
		InitialContext initCtx = null;
		try {
			if(LOGGER.isDebugEnabled())     LOGGER.debug(infoGetCon);
			initCtx = new InitialContext();
			ds = (javax.sql.DataSource) initCtx.lookup(datasource);
		}
		catch (Exception e) {
            LOGGER.fatal("Data Source not bound. Please check the jndi name.");
       }
		finally
		{
			try
			{
				if(initCtx != null)
				initCtx.close();
			}
			catch(NamingException ne)
			{
				if(LOGGER.isInfoEnabled())     LOGGER.info("Error closing context" + ne);
				throw new DatabaseConnectionException(ne.getMessage());
			}
		}

	}

	/**
	* This method returns a Database connection to the callee.
	* @return Connection - Database connection
	* @exception DatabaseConnectionException - Throws a runtime exception.
	*/
	public static Connection getConnection() throws DatabaseConnectionException
	{
		try
		{
			if(LOGGER.isDebugEnabled())     LOGGER.debug(infoCrtCon);
			return ds.getConnection();
		}
		catch(Exception exception)
		{
			LOGGER.fatal(errConnOpenString, exception);
			throw new DatabaseConnectionException(errConnOpenString, exception);
		}
	}

	/**
	* This method releases the resources of the connection and the statement
	* @param connection The existing connection object to be closed
	* @param statement The Statement object that needs to be closed.
	* @return void
	* @exception DatabaseConnectionException - Throws a runtime exception.
	*/

	public static void returnConnection(Connection connection, Statement statement) throws DatabaseConnectionException
	{
		if(LOGGER.isDebugEnabled())     LOGGER.debug(infoRelCon);
		try
		{
			if(statement != null)
				statement.close();
		}
		catch(Exception exception)
		{
			LOGGER.error(errStmtString, exception);
			throw new DatabaseConnectionException(errStmtString, exception);
		}
		try
		{
			if(connection != null)
				connection.close();
		}
		catch(Exception exception)
		{
			LOGGER.fatal(errConnOpenString, exception);
				throw new DatabaseConnectionException(errConnCloseString, exception);
		}
	}
}
