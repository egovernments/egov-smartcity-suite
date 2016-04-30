/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 * 	1) All versions of this program, verbatim or modified must carry this
 * 	   Legal Notice.
 *
 * 	2) Any misrepresentation of the origin of the material is prohibited. It
 * 	   is required that all modified versions of this material be marked in
 * 	   reasonable ways as different from the original version.
 *
 * 	3) This license does not grant any rights to any user of the program
 * 	   with regards to rights under trademark law for use of the trade names
 * 	   or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package com.exilant.exility.dataservice;

import org.apache.log4j.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.Statement;

public class DBHandler {

    private final static Logger LOGGER = Logger.getLogger(DBHandler.class);
    private final static String datasource = "java:/ezgovDatabasePool";
    private final static String errConnOpenString = "Unable to get a connection from Pool:" + datasource
            + ". Please make sure that the connection pool is set up properly";
    private final static String errStmtString = "Unable to Close the statement";
    private final static String errConnCloseString = "Unable to Close the statement";
    private final static String infoGetCon = "Requesting a new connection";
    private final static String infoRelCon = "Releasing connection";
    private final static String infoCrtCon = "Creating a new connection";
    private static javax.sql.DataSource ds = null;
    // cache the jndi look up
    static {
        InitialContext initCtx = null;
        try {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(infoGetCon);
            initCtx = new InitialContext();
            ds = (javax.sql.DataSource) initCtx.lookup(datasource);
        } catch (final Exception e) {
            LOGGER.fatal("Data Source not bound. Please check the jndi name.");
        } finally
        {
            try
            {
                if (initCtx != null)
                    initCtx.close();
            } catch (final NamingException ne)
            {
                if (LOGGER.isInfoEnabled())
                    LOGGER.info("Error closing context" + ne);
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
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(infoCrtCon);
            return ds.getConnection();
        } catch (final Exception exception)
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

    public static void returnConnection(final Connection connection, final Statement statement)
            throws DatabaseConnectionException
    {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(infoRelCon);
        try
        {
            if (statement != null)
                statement.close();
        } catch (final Exception exception)
        {
            LOGGER.error(errStmtString, exception);
            throw new DatabaseConnectionException(errStmtString, exception);
        }
        try
        {
            if (connection != null)
                connection.close();
        } catch (final Exception exception)
        {
            LOGGER.fatal(errConnOpenString, exception);
            throw new DatabaseConnectionException(errConnCloseString, exception);
        }
    }
}
