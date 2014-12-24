/*
 * Created on Jan 18, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.domain;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import com.exilant.exility.common.TaskFailedException;
import org.apache.log4j.Logger;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.jdbc.ReturningWork;

/**
 * @author pushpendra.singh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class Fund
{
	private String id =null;
	private String code = null;
	private String name = null;
	private String type = null;
	private String lLevel = null;
	private String parentId = null;
	private String openingDebitBalance = "0";
	private String openingCreditBalance = "0";
	private String transactionDebitAmount = "0";
	private String transactionCreditAmount = "0";
	private String isActive = "1";
	private String lastModified = "1-Jan-1900";
	private String created = "1-Jan-1900";
	private String modifiedBy = "0";
	private String payGlcodeId=null;
	private static TaskFailedException taskExc;
	private static final Logger LOGGER=Logger.getLogger(Fund.class);
	private String updateQuery="UPDATE fund SET";
	private boolean isId=false, isField=false;

	public Fund(){};
	public void setId(String aId){ id = aId; isId=true;}
	public int getId() {return Integer.valueOf(id).intValue(); }
	public String getPayGlcodeId() {return  payGlcodeId; }
	public void setCode(String aCode){ code = aCode; updateQuery = updateQuery + " code='" + code + "',"; isField = true;}
	public void setName(String aName){ name = aName; updateQuery = updateQuery + " name='" + name + "',"; isField = true;}
	public void setType(String aType){ type = aType; updateQuery = updateQuery + " type='" + type + "',"; isField = true;}
	public void setLlevel(String aLlevel){ lLevel = aLlevel; updateQuery = updateQuery + " lLevel=" + lLevel + ","; isField = true;}
	public void setParentId(String aParentId){ parentId = aParentId; updateQuery = updateQuery + " parentId=" + parentId + ","; isField = true;}
	public void setOpeningDebitBalance(String aOpeningDebitBalance){openingDebitBalance = aOpeningDebitBalance; updateQuery = updateQuery + " openingDebitBalance=openingDebitBalance" + openingDebitBalance + ","; isField = true;}
	public void setOpeningCreditBalance(String aOpeningCreditBalance){openingCreditBalance = aOpeningCreditBalance; updateQuery = updateQuery + " openingCreditBalance=openingCreditBalance" + openingCreditBalance + ","; isField = true;}
	public void setTranscDebitAmount(String aTransactionDebitAmount){transactionDebitAmount = aTransactionDebitAmount; updateQuery = updateQuery + " transactionDebitAmount=transactionDebitAmount+" + transactionDebitAmount + ","; isField = true;}
	public void setTranscCreditAmount(String aTransactionCreditAmount){transactionCreditAmount = aTransactionCreditAmount; updateQuery = updateQuery + " transactionCreditAmount=transactionCreditAmount+" + transactionCreditAmount + ","; isField = true;}
	public void setTransactionDebitAmount(String aTransactionDebitAmount){transactionDebitAmount = aTransactionDebitAmount; updateQuery = updateQuery + " transactionDebitAmount =" + transactionDebitAmount + ","; isField = true;}
	public void setTransactionCreditAmount(String aTransactionCreditAmount){transactionCreditAmount = aTransactionCreditAmount; updateQuery = updateQuery + " transactionCreditAmount=" + transactionCreditAmount + ","; isField = true;}
	public void setIsActive(String aIsActive){ isActive = aIsActive; updateQuery = updateQuery + " isActive=" + isActive + ","; isField = true;}
	public void setLastModified(String aLastModified){ lastModified = aLastModified; updateQuery = updateQuery + " lastModified='" + lastModified + "',"; isField = true;}
	public void setCreated(String aCreated){ created = aCreated; updateQuery = updateQuery + " created='" + created + "',"; isField = true;}
	public void setModifiedBy(String aModifiedBy){ modifiedBy = aModifiedBy; updateQuery = updateQuery + " modifiedBy=" + modifiedBy + ","; isField = true;}
	public void setPayGlcodeId(String apayGlcodeId){ payGlcodeId = apayGlcodeId; updateQuery = updateQuery + " payGlcodeId=" + payGlcodeId + ","; isField = true;}
	
	public String getInterfundCode(String fundid,Connection connection)throws SQLException,TaskFailedException
	{
		String retVal=null;
		Statement statement = connection.createStatement();
		ResultSet rs=statement.executeQuery("Select payGlcodeId from fund where id="+fundid);
		if(rs.next())
			retVal=rs.getString(1);
		return retVal;
	}
	
	public void insert(Connection connection) throws SQLException,TaskFailedException
	{
		EGovernCommon commommethods = new EGovernCommon();
		created = commommethods.getCurrentDate(connection);
		try
   		{
   			SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			created = formatter.format(sdf.parse( created ));
			EgovMasterDataCaching.getInstance().removeFromCache("egi-fund");
   		}catch(Exception e){
			LOGGER.error("Exp  in insert to fund"+e.getMessage());
			throw taskExc;
		}
		setCreated(created);
		setLastModified(created);
		setId( String.valueOf(PrimaryKeyGenerator.getNextKey("Fund")) );

		String insertQuery = "INSERT INTO Fund (Id, Code, Name, Type, Llevel, ParentId, OpeningDebitBalance, " +
						"OpeningCreditBalance, TransactionDebitAmount, TransactionCreditAmount, " +
						"IsActive, LastModified, Created, ModifiedBy) " +
						"VALUES (" + id + ", '" + code + "', '" + name + "', '" + type + "', '" + lLevel
						+ "', " + parentId + ", " + openingDebitBalance + ", " + openingCreditBalance
						+ ", " + transactionDebitAmount + ", " + transactionCreditAmount + ", "
						+ isActive + ", '" + lastModified + "', '" + created + "', " + modifiedBy + ")";

		LOGGER.info(insertQuery);
		Statement statement = connection.createStatement();
		statement.executeUpdate(insertQuery);
		statement.close();
	}

	public void update (Connection connection) throws SQLException,TaskFailedException
	{
		if(isId && isField)
		{
			EGovernCommon commommethods = new EGovernCommon();
			created = commommethods.getCurrentDate(connection);
			//LOGGER.debug("inside the update postInFund  --created"+created);
			try
	   		{
	   			SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
				created = formatter.format(sdf.parse(created));
				EgovMasterDataCaching.getInstance().removeFromCache("egi-fund");
	   		}catch(Exception e){
					LOGGER.error("exception in update fund"+e);
					throw taskExc;
			}
			setLastModified(created);
			updateQuery = updateQuery.substring(0,updateQuery.length()-1);
			updateQuery = updateQuery + "  WHERE id = " + id;
			Statement statement = connection.createStatement();
			LOGGER.info(updateQuery);
			statement.executeUpdate(updateQuery);
			statement.close();
			updateQuery="UPDATE fund SET";
		}
	}
	@Deprecated
	public HashMap getFundList(Connection con) throws Exception
	{
			String query="SELECT id, name FROM fund where isactive='1' and isnotleaf!='1' order by name";
			LOGGER.info("  query   "+query);
			HashMap hm=new HashMap();
			try
			{
				Statement statement = con.createStatement();
				ResultSet rs=statement.executeQuery(query);
				while (rs.next())
					hm.put(rs.getString(1),rs.getString(2));
				
			}catch(Exception e){
				LOGGER.error("Exp in getFundList"+e.getMessage());
				throw taskExc;
			}
			return hm;
	}
	
	public HashMap getFundList() throws Exception
	{
			
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<HashMap>() {

			@Override
			public HashMap execute(Connection con) throws SQLException {
				
				String query="SELECT id, name FROM fund where isactive='1' and isnotleaf!='1' order by name";
				LOGGER.info("  query   "+query);
				HashMap hm=new HashMap();
				try
				{
					Statement statement = con.createStatement();
					ResultSet rs=statement.executeQuery(query);
					while (rs.next())
						hm.put(rs.getString(1),rs.getString(2));
					HibernateUtil.release(statement, rs);
					
				}catch(Exception e){
					LOGGER.error("Exp in getFundList"+e.getMessage());
					
				}
				return hm;
			}
		});
	}
	@Deprecated
	public String getFundForAcc(String accId,Connection con) throws Exception
	{
			String query="SELECT fundid FROM bankaccount WHERE id="+accId;
			LOGGER.info("  query   "+query);
			String fundId="";
			try
			{
				Statement statement = con.createStatement();
				ResultSet rs=statement.executeQuery(query);
				if(rs.next())
					fundId=rs.getString(1);
			}catch(Exception e)	{
				LOGGER.error("Exp in getFundForAcc"+e.getMessage());
				throw taskExc;
			}
		return fundId;
	}
	
	public String getFundForAcc(final String accId) throws Exception
	{
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<String>() {

			@Override
			public String execute(Connection con) throws SQLException {
				
				String query="SELECT fundid FROM bankaccount WHERE id="+accId;
				LOGGER.info("  query   "+query);
				String fundId="";
				try
				{
					Statement statement = con.createStatement();
					ResultSet rs=statement.executeQuery(query);
					if(rs.next())
						fundId=rs.getString(1);
					HibernateUtil.release(statement, rs);
				}catch(Exception e)	{
					LOGGER.error("Exp in getFundForAcc"+e.getMessage());
					
				}
			return fundId;
			}
		});
			
	}
	
	@Deprecated
	public HashMap getFundSourceList(Connection con) throws Exception
	{
			String query="SELECT id, name FROM fundsource where isactive='1' and isnotleaf!='1' order by name";
			LOGGER.info("  query   "+query);
			HashMap hm=new HashMap();
			try
			{
				Statement statement = con.createStatement();
				ResultSet rs=statement.executeQuery(query);
				while (rs.next())
					hm.put(rs.getString(1),rs.getString(2));
			}catch(Exception e)	{
				LOGGER.error("Exp in getFundSourceList"+e.getMessage());
				throw taskExc;
			}
			return hm;
	}
	
	public HashMap getFundSourceList() throws Exception
	{
		
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<HashMap>() {

			@Override
			public HashMap execute(Connection con) throws SQLException {
				

				String query="SELECT id, name FROM fundsource where isactive='1' and isnotleaf!='1' order by name";
				LOGGER.info("  query   "+query);
				HashMap hm=new HashMap();
				try
				{
					Statement statement = con.createStatement();
					ResultSet rs=statement.executeQuery(query);
					while (rs.next())
						hm.put(rs.getString(1),rs.getString(2));
					HibernateUtil.release(statement, rs);
				}catch(Exception e)	{
					LOGGER.error("Exp in getFundSourceList"+e.getMessage());
					
				}
				return hm;
		
			}
			
		});
	}

}

