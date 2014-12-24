/*
 * Created on Feb 25, 2005 
 * @author pushpendra.singh 
 */

package com.exilant.eGov.src.domain;

import com.exilant.exility.updateservice.PrimaryKeyGenerator;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import org.apache.log4j.Logger;

public class GeneralLedgerDetail {
	private String id = null;
	private String glId = null;
	private String detailKeyId = null;
	private String detailTypeId = null;	
	private String detailAmt="0";
	private static final Logger LOGGER=Logger.getLogger(GeneralLedgerDetail.class);
	
	public void setId(String aId){ id = aId; }
	public void setGLId(String aGLId){ glId = aGLId; }
	public void setDetailKeyId(String aDetailKeyId){ detailKeyId = aDetailKeyId; }
	public void setDetailTypeId(String aDetailTypeId){ detailTypeId = aDetailTypeId; }
	public void setDetailAmt(String aDetailAmt){ detailAmt = aDetailAmt; }
	
	public int getId() {return Integer.valueOf(id).intValue(); }
	public String getGLId(){ return glId ;}
	public String getDetailKeyId(){return detailKeyId ; }
	public String getDetailTypeId(){ return detailTypeId; }
	public String getDetailAmt(){ return detailAmt; }
	
	public void insert(Connection connection) throws SQLException
	{						
		setId( String.valueOf(PrimaryKeyGenerator.getNextKey("GeneralLedgerDetail")) );
		
		String insertQuery = "INSERT INTO GeneralLedgerDetail (id, generalLedgerId, detailKeyId, detailTypeId,amount) " +		 
								"VALUES (" + id + ", " + glId + ", " + detailKeyId + ", " + detailTypeId + ","+detailAmt+")";		
		
	      Statement statement = connection.createStatement();
	      statement.executeUpdate(insertQuery);
	      LOGGER.info(insertQuery);
	      statement.close();
	}
}
