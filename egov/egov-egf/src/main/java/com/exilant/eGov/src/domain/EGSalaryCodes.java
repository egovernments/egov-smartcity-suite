 /*
   * EGSalaryCodes.java Created on July 17, 2007
   *
   * Copyright 2005 eGovernments Foundation. All rights reserved.
   * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
  package com.exilant.eGov.src.domain;

  import java.sql.Connection;
  import java.sql.Statement;
  import java.sql.SQLException;
  import org.apache.log4j.Logger;
  import com.exilant.exility.common.TaskFailedException;
  import com.exilant.exility.updateservice.PrimaryKeyGenerator;


  /**
  * @author Iliyaraja
  *
  * TODO To change the template for this generated type comment go to
  * Window - Preferences - Java - Code Style - Code Templates
  */

  public class EGSalaryCodes
  {

	  private static final Logger LOGGER=Logger.getLogger(EGSalaryCodes.class);

	private String id=null;
	private String head=null;
	private String glcodeId=null;
	private String salType=null;
	private String Createdby=null;
	private String createdDate="";
	private String lastModifiedBy=null;
	private String lastModifiedDate="";
	private String updateQuery="UPDATE EG_SALARYCODES SET";
	private boolean isId=false,isField=false;


	public EGSalaryCodes() {}

	public void setId(String aId){ id = aId; isId=true;isField = true;}
	public int getId() {return Integer.valueOf(id).intValue(); }

	public void setHead(String aHead){ head = aHead; updateQuery = updateQuery + " head='" + head + "',"; isField = true;}
	public void setGlcodeId(String aGlcodeId){glcodeId = aGlcodeId; updateQuery = updateQuery + " glcodeId=" + glcodeId + ","; isField = true;}
	public void setSalType(String aSalType){ salType = aSalType; updateQuery = updateQuery +" salType='" + salType + "',"; isField = true;}

	public void setCreatedby(String createdby) {Createdby = createdby;updateQuery = updateQuery + " CREATEDBY=" + Createdby + ","; isField = true;}
	public void setCreatedDate(String createdDate) {this.createdDate = createdDate;updateQuery = updateQuery + " createdDate='" + createdDate + "',"; isField = true;}

	public void setLastModifiedBy(String alastModifiedBy) {lastModifiedBy = alastModifiedBy;updateQuery = updateQuery + " lastModifiedBy=" + lastModifiedBy + ","; isField = true;}
	public void setLastModifiedDate(String aLastModifiedDate){ lastModifiedDate = aLastModifiedDate; updateQuery = updateQuery + " lastModifiedDate=to_date('" + lastModifiedDate + "','dd-Mon-yyyy HH24:MI:SS')"+","; isField = true;}

	public void insert(Connection connection) throws SQLException,TaskFailedException
	{
	//	EGovernCommon commommethods = new EGovernCommon();
		setId( String.valueOf(PrimaryKeyGenerator.getNextKey("EG_SALARYCODES")));

		String insertQuery = "INSERT INTO EG_SALARYCODES (Id, head,GlcodeId, " +
						"SalType, Createdby,createdDate) " +
						"VALUES (" + id + ", '" + head + "'," + glcodeId + ",'" + salType + "'," + Createdby + ",to_date('"+this.createdDate+"','dd-Mon-yyyy HH24:MI:SS'))";
		if(LOGGER.isInfoEnabled())     LOGGER.info(insertQuery);
		Statement statement = connection.createStatement();
		statement.executeUpdate(insertQuery);
		statement.close();
	}

	public void update (Connection connection) throws SQLException,TaskFailedException
	{
		if(isId && isField)
		{
			updateQuery = updateQuery.substring(0,updateQuery.length()-1);
			updateQuery = updateQuery + " WHERE id = " + id;

			if(LOGGER.isInfoEnabled())     LOGGER.info(updateQuery);
			Statement statement = connection.createStatement();
			statement.executeUpdate(updateQuery);
			statement.close();
			updateQuery="UPDATE EG_SALARYCODES SET";
		}
	}

}