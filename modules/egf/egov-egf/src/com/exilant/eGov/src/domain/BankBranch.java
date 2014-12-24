/*
 * Created on Jan 18, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.domain;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.jdbc.ReturningWork;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;

/**
 * @author pushpendra.singh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


public class BankBranch
{
	private String id = null;
	private String branchCode = null;
	private String branchName = null;
	private String branchAddress1 = null;
	private String branchAddress2 = null;
	private String branchCity = null;
	private String branchState = null;
	private String branchPin = null;
	private String branchPhone = null;
	private String branchFax = null;
	private String bankId = null;
	private String contactPerson =null;
	private String isActive = "1";
	private String created = "1-Jan-1900";
	private String modifiedBy = null;
	private String lastModified = "1-Jan-1900";
	private String narration = null;
	private TaskFailedException taskExc;
	private final static Logger LOGGER=Logger.getLogger(BankBranch.class);
	private String updateQuery="UPDATE bankbranch SET";
	private boolean isId=false, isField=false;
	EGovernCommon cm=new EGovernCommon();
	private SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
	private SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
	

	public void setId(String aId){ id = aId; isId=true;}
	public String getId() {return id; }
	public void setBranchCode(String aBranchCode){ branchCode = cm.assignValue(aBranchCode,branchCode); updateQuery = updateQuery + " branchCode=" + branchCode + ","; isField = true;}
	public void setBranchName(String aBranchName){ branchName = cm.assignValue(aBranchName,branchName); updateQuery = updateQuery + " branchName=" + branchName + ","; isField = true;}
	public void setBranchAddress1(String aBranchAddress1){ branchAddress1 = cm.assignValue(aBranchAddress1,branchAddress1); updateQuery = updateQuery + " branchAddress1=" + branchAddress1 + ","; isField = true;}
	public void setBranchAddress2(String aBranchAddress2){ branchAddress2 = cm.assignValue(aBranchAddress2,branchAddress2); updateQuery = updateQuery + " branchAddress2=" + branchAddress2 + ","; isField = true;}
	public void setBranchCity(String aBranchCity){ branchCity = cm.assignValue(aBranchCity,branchCity); updateQuery = updateQuery + " branchCity=" + branchCity + ","; isField = true;}
	public void setBranchState(String aBranchState){ branchState = cm.assignValue(aBranchState,branchState); updateQuery = updateQuery + " branchState=" + branchState + ","; isField = true;}
	public void setBranchPin(String aBranchPin){ branchPin = cm.assignValue(aBranchPin,branchPin); updateQuery = updateQuery + " branchPin=" + branchPin + ","; isField = true;}
	public void setBranchPhone(String aBranchPhone){ branchPhone = cm.assignValue(aBranchPhone,branchPhone); updateQuery = updateQuery + " branchPhone=" + branchPhone + ","; isField = true;}
	public void setBranchFax(String aBranchFax){ branchFax = cm.assignValue(aBranchFax,branchFax); updateQuery = updateQuery + " branchFax=" + branchFax + ","; isField = true;}
	public void setBankId(String aBankId){ bankId = cm.assignValue(aBankId,bankId); updateQuery = updateQuery + " bankId=" + bankId + ","; isField = true;}
	public void setContactPerson(String aContactPerson){ contactPerson = cm.assignValue(aContactPerson,contactPerson); updateQuery = updateQuery + " contactPerson=" + contactPerson + ","; isField = true;}
	public void setIsActive(String aIsActive){ isActive = cm.assignValue(aIsActive,isActive); updateQuery = updateQuery + " isActive=" + isActive + ","; isField = true;}
	public void setCreated(String aCreated) { created = cm.assignValue(aCreated,created); updateQuery = updateQuery + " created=" + created + ","; isField = true;}
	public void setModifiedBy(String aLastModifiedBy){ modifiedBy = cm.assignValue(aLastModifiedBy,modifiedBy); updateQuery = updateQuery + " modifiedBy=" + modifiedBy + ","; isField = true;}
	public void setLastModified(String aLastModifiedAt){ lastModified = cm.assignValue(aLastModifiedAt,lastModified); updateQuery = updateQuery + " lastModified=" + lastModified + ","; isField = true;}
	public void setNarration(String aNarration){ narration = cm.assignValue(aNarration,narration); updateQuery = updateQuery + " narration=" + narration + ","; isField = true;}

	/**
	 * This is Insert API
	 * @param connection
	 * @throws TaskFailedException
	 * @throws SQLException
	 */
	public void insert(Connection connection) throws TaskFailedException,SQLException
	{
		EGovernCommon commommethods = new EGovernCommon();
		created = commommethods.getCurrentDate(connection);
		Statement statement = null;
		try
   		{
   			created = cm.assignValue(formatter.format(sdf.parse( created )),created);
			setLastModified(created);
			setId( String.valueOf(PrimaryKeyGenerator.getNextKey("BankBranch")) );
	        setBranchCode(id);	
			String insertQuery = "INSERT INTO bankbranch (Id, BranchCode, BranchName, BranchAddress1, BranchAddress2, " +
							"BranchCity, BranchState, BranchPin, BranchPhone, BranchFax, BankId, ContactPerson, " + "" +
							"IsActive, Created, ModifiedBy, LastModified, Narration) " +
							"VALUES (" + id + ", " + branchCode + ", " + branchName + ", " + branchAddress1 + ", "
							+ branchAddress2 + ", " + branchCity + ", " + branchState + ", " + branchPin
							+ ", " + branchPhone + ", " + branchFax + ", " + bankId + ", " + contactPerson
							+ ", " + isActive + "," + created + ", " + modifiedBy + ", " + lastModified + ", " + narration + ")";
	
			LOGGER.debug(insertQuery);
			statement = connection.createStatement();
			statement.executeUpdate(insertQuery);
   		}catch(Exception e){
   			LOGGER.error("Exp in insert"+e.getMessage());
   			throw taskExc;}
		finally{
			try{
				statement.close();
			}catch(Exception e){LOGGER.error("Inside finally block of insert");}
		}	
			}

	/**
	 * This is the update API
	 * @param connection
	 * @throws TaskFailedException
	 * @throws SQLException
	 */
	public void update (Connection connection) throws TaskFailedException,SQLException
	{
		if(isId && isField)
		{
			EGovernCommon commommethods = new EGovernCommon();
			created = commommethods.getCurrentDate(connection);
			Statement statement = null;
			try
	   		{

	   			created = formatter.format(sdf.parse( created ));
				setCreated(created);	   					
				setLastModified(created);							
				updateQuery = updateQuery.substring(0,updateQuery.length()-1);
				updateQuery = updateQuery + " WHERE id = " + id;
				LOGGER.debug(updateQuery);
				statement = connection.createStatement();
				statement.executeUpdate(updateQuery);
				updateQuery="UPDATE bankbranch SET";
	   		}catch(Exception e){
	   			LOGGER.error("Exp in update: "+e.getMessage());
	   			throw taskExc;}
	   		finally{
	   			try{
	   				statement.close();
	   			}catch(Exception e){LOGGER.error("Inside finally block of update");}
	   		}	
		}
	}
	

	/**
	 * This API gives the details for all the branches
	 * @param con
	 * @return
	 * @throws TaskFailedException
	 */
	@Deprecated
	public HashMap getBankBranch(Connection con) throws TaskFailedException
	{
		String query="SELECT  CONCAT(CONCAT(bankBranch.bankId, '-'),bankBranch.ID) as \"bankBranchID\",concat(concat(bank.name , ' '),bankBranch.branchName) as \"bankBranchID\" FROM bank, bankBranch where"
					+ " bank.isactive='1' and bankBranch.isactive='1' and bank.ID = bankBranch.bankId ";
		LOGGER.info("  query   "+query);
		Map hm=new HashMap();
		Statement statement =null;
		ResultSet rs=null;

		try
		{
			statement = con.createStatement();
			rs=statement.executeQuery(query);
			while (rs.next())
			{
				hm.put(rs.getString(1),rs.getString(2));
			}
			
		}
		catch(Exception e)
		{
			LOGGER.error("Error in getBankBranch ",e);
			throw taskExc;
		}finally{
   			try{
   				rs.close();
   				statement.close();
   			}catch(Exception e){LOGGER.error("Inside finally block of getBankBranch");}
   		}
		return (HashMap)hm;
	}
	
	public HashMap getBankBranch() throws TaskFailedException
	{
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<HashMap>() {

			@Override
			public HashMap execute(Connection con) throws SQLException {
				
				
				String query="SELECT  CONCAT(CONCAT(bankBranch.bankId, '-'),bankBranch.ID) as \"bankBranchID\",concat(concat(bank.name , ' '),bankBranch.branchName) as \"bankBranchID\" FROM bank, bankBranch where"
						+ " bank.isactive=1 and bankBranch.isactive=1 and bank.ID = bankBranch.bankId ";
			LOGGER.info("  query   "+query);
			Map hm=new HashMap();
			Statement statement =null;
			ResultSet rs=null;

			try
			{
				statement = con.createStatement();
				rs=statement.executeQuery(query);
				while (rs.next())
				{
					hm.put(rs.getString(1),rs.getString(2));
				}
				
			}
			catch(Exception e)
			{
				LOGGER.error("Error in getBankBranch ",e);
				
			}finally{
	   			try{
	   				rs.close();
	   				statement.close();
	   			}catch(Exception e){LOGGER.error("Inside finally block of getBankBranch");}
	   		}
			return (HashMap)hm;
			}
		});
		
		
	}
	
	/**
	 * This API will give the list of Bankaccounts for any Bank branch
	 * @param bankBranchId
	 * @param con
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public HashMap getAccNumber(String bankBranchId,Connection con) throws TaskFailedException
	{
		String id[]=bankBranchId.split("-");
		String branchId=id[1];
		Statement statement = null;
		ResultSet rs=null;
	
		String query="SELECT  ID, accountNumber from bankAccount WHERE branchId= "+branchId+" and isactive=1  ORDER BY ID";
		LOGGER.debug("  query   "+query);
	    Map hm=new HashMap();
		try
		{
			statement = con.createStatement();
			 rs=statement.executeQuery(query);
			while (rs.next())
			{
				hm.put(rs.getString(1),rs.getString(2));
			}
		}
		catch(Exception e)
		{
			LOGGER.error("Error in getAccNumber ",e);
			throw taskExc;
		}finally{
   			try{
   				rs.close();
   				statement.close();
   			}catch(Exception e){LOGGER.error("Inside finally block of getAccNumber");}
   		}
		
		return (HashMap)hm;
	}
	
	public HashMap getAccNumber(final String bankBranchId) throws TaskFailedException
	{
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<HashMap>() {

			@Override
			public HashMap execute(Connection con) throws SQLException {
				String id[]=bankBranchId.split("-");
				String branchId=id[1];
				Statement statement = null;
				ResultSet rs=null;
			
				String query="SELECT  ID, accountNumber from bankAccount WHERE branchId= "+branchId+" and isactive=1  ORDER BY ID";
				LOGGER.debug("  query   "+query);
			    Map hm=new HashMap();
				try
				{
					statement = con.createStatement();
					 rs=statement.executeQuery(query);
					while (rs.next())
					{
						hm.put(rs.getString(1),rs.getString(2));
					}
				}
				catch(Exception e)
				{
					LOGGER.error("Error in getAccNumber ",e);
					
				}finally{
		   			try{
		   				rs.close();
		   				statement.close();
		   			}catch(Exception e){LOGGER.error("Inside finally block of getAccNumber");}
		   		}
				
				return (HashMap)hm;
			
				
			}
		});
	}		
}

