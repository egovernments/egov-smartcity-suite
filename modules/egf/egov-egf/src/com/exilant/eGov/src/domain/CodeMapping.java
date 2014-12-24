/*
 * Created on Mar 18, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package com.exilant.eGov.src.domain;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;
import org.apache.log4j.Logger;
/**
 * @author Rashmi MN
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


public class CodeMapping {

	private String eg_BoundaryID = null;
	private String cashInHand = null;
	private String chequeInHand = null;

	private String ID = "0";

	private String insertQuery;
	private boolean isId=false, isField=false;
	private String updateQuery="UPDATE CodeMapping SET";
	private static final Logger LOGGER=Logger.getLogger(CodeMapping.class);
	
	public void setID(String aID){ ID = aID;isId=true; }
	public void setegBoundaryID(String xegBoundaryID){ eg_BoundaryID = xegBoundaryID; insertQuery = insertQuery + " egBoundaryID=" +xegBoundaryID + ","; isField = true;}
	public void setcashInHand(String xcashInHand){ cashInHand = xcashInHand; updateQuery = updateQuery + " cashInHand=" +xcashInHand + ","; isField = true;}
	public void setchequeInHand(String xchequeInHand){ chequeInHand = xchequeInHand; updateQuery = updateQuery + " chequeInHand=" +xchequeInHand + ","; isField = true;}

	public void insert(Connection connection) throws SQLException
	{
		setID( String.valueOf(PrimaryKeyGenerator.getNextKey("CodeMapping")) );
		insertQuery = "insert into CodeMapping (id,eg_BoundaryID, cashInHand, chequeInHand)"+
		" values ("+ ID +"," + eg_BoundaryID + ", " + cashInHand + ", " + chequeInHand + ")";
		LOGGER.info(insertQuery);
		Statement statement = connection.createStatement();
		statement.executeUpdate(insertQuery);
	}
	
	public void update(Connection connection) throws SQLException{
		if(isId && isField)
		{
			updateQuery = updateQuery.substring(0,updateQuery.length()-1);
			updateQuery = updateQuery + " WHERE eg_BoundaryID = " + eg_BoundaryID;
			Statement statement = connection.createStatement();
			LOGGER.info(updateQuery);
			statement.executeUpdate(updateQuery);
			statement.close();
			updateQuery="UPDATE CodeMapping SET";
		}
	}
}
