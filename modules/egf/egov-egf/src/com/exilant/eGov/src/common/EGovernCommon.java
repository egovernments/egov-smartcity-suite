/*
 * Created on Jan 7, 2005
 *
 */
package com.exilant.eGov.src.common;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVException;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.utils.EGovConfig;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infstr.utils.LabelValueBean;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.jdbc.Work;

import com.exilant.eGov.src.domain.Eg_Numbers;
import com.exilant.eGov.src.domain.VoucherHeader;
import com.exilant.eGov.src.domain.VoucherMIS;
import com.exilant.eGov.src.domain.egfRecordStatus;
import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;


/**
 * @author pushpendra.singh
 *
 * This class contains the common methods used for
 * E-Governments applciation
 */
public class EGovernCommon extends AbstractTask{
	private static String vouNumber;
	private static String vouNumberCess;
	private static String revNumber;
	private static int retVal=0;
	private static SimpleDateFormat sdfFormatddMMyyyy =new SimpleDateFormat("dd/MM/yyyy");
	private  SimpleDateFormat dtFormat = new SimpleDateFormat("dd-MMM-yyyy");
	private static final Logger LOGGER = Logger.getLogger(EGovernCommon.class);
	private static TaskFailedException taskExc;
	private static final String FUNDIDNSQL="SELECT identifier as \"fund_identi\" from fund where id=";
	private static final String  EXILRPERROR = "exilRPError";
	
	public void execute (String taskName,
			String gridName,
			DataCollection datacol,
			Connection con,
			boolean errorData,
			boolean gridHasCol, String prefix) throws TaskFailedException
			{
					EGovernCommon egobj=new EGovernCommon();
					datacol.addValue("voucherHeader_cgn",egobj.getCGNumber());
					if(datacol.getValue("hasSecondCGN").equalsIgnoreCase("true")){
						datacol.addValue("jv_cgn",egobj.getCGNumber());
					}
					datacol.addValue("databaseDate",egobj.getCurrentDate(con));
			}

	public long getCGNumber()
	{
		return PrimaryKeyGenerator.getNextKey("VoucherHeader");
	}

	/**
	 * This function returns the system date of the database server.
	 * @param connection
	 * @return
	 * @throws TaskFailedException
	 */
	@Deprecated
	public static String getCurrentDate(Connection connection)throws TaskFailedException
	{
		Statement statement=null;
		ResultSet resultset=null;
		String currentDate=null;

		try
		{
			statement = connection.createStatement();
			resultset = statement.executeQuery("select to_char(sysdate,'dd/MM/yyyy') as \"currentDate\" from dual");
			resultset.next();
			currentDate = resultset.getString("currentDate");
		}
		catch(Exception sqlex)
		{
			LOGGER.error(sqlex.getMessage(), sqlex);
			throw taskExc;
		}
		finally{
			try{
				resultset.close();
				statement.close();
			}catch(Exception e){LOGGER.error("Exception in finally...");}
		}
		return currentDate;
	}
	
	public static String getCurrentDate()throws TaskFailedException
	{
		
		
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<String>() {

			@Override
			public String execute(Connection connection) throws SQLException {
				

				Statement statement=null;
				ResultSet resultset=null;
				String currentDate=null;

				try
				{
					statement = connection.createStatement();
					resultset = statement.executeQuery("select to_char(sysdate,'dd/MM/yyyy') as \"currentDate\" from dual");
					resultset.next();
					currentDate = resultset.getString("currentDate");
				}
				catch(Exception sqlex)
				{
					LOGGER.error(sqlex.getMessage(), sqlex);
					
				}
				finally{
					try{
						resultset.close();
						statement.close();
					}catch(Exception e){LOGGER.error("Exception in finally...");}
				}
				return currentDate;
			
			}
		});
	}


	/**
	 * 
	 * @param datacol
	 * @param connection
	 * @return
	 */
	public boolean isValidData(DataCollection datacol,
							   Connection connection)throws TaskFailedException
	{
		String[] fieldsToCheck={"department_id","organizationStructure_id","taxes_code","billCollector_id","bankAccount_accountNumber","bank_id","bankBranch_id","fund_id","supplier_id","contractor_id"};
		List formList=(ArrayList)getFormFields(datacol);
		String formControlName;
		for(int i=0;i<formList.size();i++){
			formControlName=(String)formList.get(i);
			for(int j=0;j<fieldsToCheck.length;j++){
				if(fieldsToCheck[j].equalsIgnoreCase(formControlName)){
					if(!isDataInDataBase(fieldsToCheck[j],(String)datacol.getValue(fieldsToCheck[j]),connection)){
						return false;
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @param datacol
	 * @return
	 */
	public List getFormFields(DataCollection datacol)
	{
		Set formSet=(Set) datacol.values.keySet();
		List formList=new ArrayList();
		Iterator itr=formSet.iterator();
		while(itr.hasNext()){
			formList.add(itr.next());
		}
		return formList;
	}
	
	/**
	 * 
	 * @param field
	 * @param data
	 * @param connection
	 * @return
	 */
	public boolean  isDataInDataBase(String fieldData,
									 String dbval,
									 Connection connection)throws TaskFailedException
	{
		String branchData;
		String dbval1="";
		ResultSet rset=null;
		Statement statement=null;
		if(fieldData.equalsIgnoreCase("bank_id")){
			branchData=dbval.substring(dbval.indexOf('-'),dbval.length());
			dbval1=dbval.substring(0,dbval.indexOf('-'));
			if(!isDataInDataBase("bankBranch_id",branchData,connection)){
				return false;
			}
		}
		try{
			statement =connection.createStatement();
			String table=fieldData.substring(0,fieldData.indexOf('_'));
			String fieldData1=fieldData.replaceAll("_",".");
			String query="select * from "+table+" where "+fieldData1+"="+dbval1;
		//	LOGGER.info(query);
			rset=statement.executeQuery(query);
			if(!rset.next())
			{
				return false;
			}
			else
				LOGGER.debug("Inside the else block");
			
		}catch(Exception e){
			LOGGER.error(e.getMessage(), e);			
			throw taskExc;
		 }
		finally{
			try{
				rset.close();  
				statement.close();
			}catch(Exception e){LOGGER.error("Exp in finally for isDateInDatabase");}			
		}
		return true;
	}

	public boolean noMatch(String tableName,
			String condition,
			Connection connection)throws TaskFailedException{
	boolean noMatch=true;
	ResultSet resultset =null;
	Statement statement =null;
	try{
		statement = connection.createStatement();
		String executeString = "select * from " + tableName + " where 1=1 " + condition;
		resultset = statement.executeQuery(executeString);
		noMatch = resultset.next() ? false : true;
	}
	catch(Exception sqlex){
		LOGGER.error(sqlex.getMessage(), sqlex);
		throw taskExc;
	}
	finally{
		try{
			resultset.close();
			statement.close();
		}catch(Exception e){
			LOGGER.error("Exp in nomatch..");
		}
	}

	return noMatch;
	}

	/**
	 * This function is to handle the single quotes.
	 * @param strToFormat
	 * @return
	 */
	public String formatString(String strToFormat){
		if(strToFormat!=null)
		{
		if(strToFormat.equalsIgnoreCase("")){ 
			return " ";
		}
		String valn1=strToFormat.replaceAll("\n", " ");
		String formtStr=valn1.replaceAll("\r"," " );
	    return formtStr.replaceAll("'","''");
	   }		
		else{
			return " ";
		}
	}
	/**
	 * This function will return the database date and time
	 * @param connection
	 * @return
	 * @throws TaskFailedException 
	 */
	public void validateScheme(String vdt,String sid,Connection connection) throws TaskFailedException
	{
		Statement statement=null;
		ResultSet resultset=null;
		try{
		SimpleDateFormat sdf =sdfFormatddMMyyyy;
		SimpleDateFormat formatter = dtFormat;//new SimpleDateFormat("dd-MMM-yyyy");
		String	vDate = formatter.format(sdf.parse(vdt));
		statement = connection.createStatement();
		String qry="select code,name from scheme where isactive=1 and  validFrom<='"+vDate+"' and validTo>='"+vDate+"' and id="+sid;
		LOGGER.debug("validating scheme"+qry);
		resultset=statement.executeQuery(qry);
		if(resultset.next()){
			return ;
		}else{
			throw new TaskFailedException("Scheme is not valid for the date "+vDate);
		}
		}catch(Exception e)
		{
			LOGGER.error(e.getMessage(), e);
			throw new TaskFailedException(e.getMessage()); 
		}finally{
			try{
				resultset.close();
				statement.close();
			}catch(Exception e){LOGGER.error("inside validatescheme");}
		}
	
	}
	public void validatesubScheme(String vdt,String ssid,Connection connection) throws TaskFailedException
	{
		Statement statement=null;
		ResultSet resultset=null;
	try{
		SimpleDateFormat sdf =sdfFormatddMMyyyy;
		SimpleDateFormat formatter = dtFormat;
		String	vDate = formatter.format(sdf.parse( vdt ));
		statement = connection.createStatement();
		String qry="select code,name from sub_scheme where validFrom<='"+vDate+"' and validTo>='"+vDate+"' and id="+ssid;
		LOGGER.debug("validating subscheme"+qry);
		resultset=statement.executeQuery(qry);
		if(resultset.next()){
			return;
		}else{
			throw new TaskFailedException("the subscheme is not valid for the date "+vDate);
		}
	}catch(Exception e)
	{
		LOGGER.error(e.getMessage(), e);
		throw taskExc; 
	}finally{
		try{
			resultset.close();
			statement.close();
		}catch(Exception e){
			LOGGER.error("in validatesubScheme");
		}
	}
	
	}
	@Deprecated
	public String getCurrentDateTime(Connection connection)throws TaskFailedException
	{
		Statement statement=null;
		ResultSet resultset=null;
		String currentDate=null;
		try
		{
			statement = connection.createStatement();
			resultset = statement.executeQuery("SELECT to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS') as \"currentDate\" FROM dual");
			resultset.next();
			currentDate = resultset.getString("currentDate");
			resultset.close();
			statement.close();
		}
		catch(Exception sqlex)
		{
			LOGGER.error("EGovernCommon->getCurrentDate " + sqlex.getMessage());
			throw taskExc;
		}finally{
			try{
				resultset.close();
				statement.close();
			}catch(Exception e){
				LOGGER.error("in getCurrentDateTime...");
			}
		}
		return currentDate;
	}
	
	
	public String getCurrentDateTime()throws TaskFailedException
	{
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<String>() {

			@Override
			public String execute(Connection connection) throws SQLException {
				Statement statement=null;
				ResultSet resultset=null;
				String currentDate=null;
				try
				{
					statement = connection.createStatement();
					resultset = statement.executeQuery("SELECT to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS') as \"currentDate\" FROM dual");
					resultset.next();
					currentDate = resultset.getString("currentDate");
					resultset.close();
					statement.close();
				}
				catch(Exception sqlex)
				{
					LOGGER.error("EGovernCommon->getCurrentDate " + sqlex.getMessage());
					
				}finally{
					try{
						resultset.close();
						statement.close();
					}catch(Exception e){
						LOGGER.error("in getCurrentDateTime...");
					}
				}
				return currentDate;
			}
		});
		
		
	}
/**
 * This is to get the balance of bank account. Not used now.
 * @param con
 * @param trnAmount
 * @param accId
 * @return
 * @throws TaskFailedException
 */
/*	public boolean bankBalanceAvaliable(Connection con,double trnAmount, String accId)throws TaskFailedException	{
	  	Statement statment1=null;
   	  	ResultSet rset=null;
   
	   	  if(accId==null || accId.length()<=0 && accId.equals("") || trnAmount==0){
	   	  	return true;
	   	  }
	   	  String sql="select currentbalance from bankaccount where id="+accId;
	   	  try{
	   	  	statment1=con.createStatement();
	   	  	rset=statment1.executeQuery(sql);
	   	  	String balance=null;
	   	  	while(rset.next()){
	   	  		balance=rset.getString(1);
	   	  	}
	   	  	if(Double.parseDouble(balance)<trnAmount){
	   	  		return false;
	   	  	}
	   	  }catch(Exception e){
	   	  	throw taskExc;
	   	  }
	   finally{
		try{
			rset.close();
			statment1.close();
		}catch(Exception e){
			LOGGER.error("in getCurrentDateTime...");
		}
	   }
	   	  return true;

	 }
*/	
	/**
	 * This function will return the vouchernumber.
	 * Fund identifier+ voucher type +user entered number
	 * @param datacol
	 * @param conn
	 * @param fund
	 * @param voucherNum
	 * @return
	 * @throws TaskFailedException,SQLException
	 */
	 public String vNumber(DataCollection datacol, Connection conn,String fund,String voucherNum) throws TaskFailedException,SQLException
	 	{	String fType="";
	 	 	Statement stmt=null;
	 	 	ResultSet rset=null;
	 	 	try{
	 	 		stmt = conn.createStatement();
		 	 	rset = stmt.executeQuery(FUNDIDNSQL+fund);
	 			if(rset.next()){
		 			fType = rset.getString("fund_identi");
	 			}
	 			rset.close();
				String tran=datacol.getValue("tType");
		 		vouNumber=fType+tran+voucherNum;
	 	 	}catch(Exception e){
	 	 		throw taskExc;
	 	 	}
		 	 	finally{
		 	 		try{
		 	 			rset.close();
		 	 			stmt.close();
		 	 		}catch(Exception e){LOGGER.error("inside the vNumber");}
		 	 	}
	 		return vouNumber;
 	}
	 
	 /**
	  * To generate Voucher number based on recordid [ uesd for Integeration ]
	  * @param type
	  * @param conn
	  * @param fund
	  * @param recordId
	  * @return
	  * @throws TaskFailedException,SQLException
	  */
 	public String vNumber(String type, Connection conn,String fund,int recordId) throws TaskFailedException,SQLException
	{		String fType="";
		 	Statement stmt=null;
		 	ResultSet rset=null;
		 	try{
		 	 	stmt = conn.createStatement();
		 	 	rset = stmt.executeQuery(FUNDIDNSQL+fund);

		 			if(rset.next()){
		 			fType = rset.getString("fund_identi");
		 			}
		 		rset.close();
				String tran=type;
				String rcdId=String.valueOf(recordId);
				for(int i=rcdId.length();i<5;i++)
				{
					rcdId="0"+rcdId;
				}
		 		vouNumber=fType+tran+rcdId;
	 	}catch(Exception e){
 	 		throw taskExc;
 	 	}
	 	 	finally{
	 	 		try{
	 	 			rset.close();
	 	 			stmt.close();
	 	 		}catch(Exception e){LOGGER.error("inside the vNumber2");}
	 	 	}

			 	 return vouNumber;
 	}
 	
 	 /**
	 * This function will return the vouchernumber.
	 * Fund identifier+ voucher type +user entered number		
	 * @param fund
	 * @param voucherNum
	 * @return
	 * @throws TaskFailedException,SQLException
	 */
	 public String vNumber(Connection conn, String fund, String voucherNum) throws TaskFailedException,SQLException
	 {	
		String vouNumber=""; 	
		String fType="";
		Statement st=null;
		ResultSet rs=null;
		try{			
			st = conn.createStatement();
			rs = st.executeQuery(FUNDIDNSQL+fund);
			if(rs.next()){
				fType = rs.getString("fund_identi");
			}
			rs.close();			
			vouNumber=fType+"P"+voucherNum;
	 	}catch(Exception e){
 	 		throw taskExc;
 	 	}
	 	 	finally{
	 	 		try{
	 	 			rs.close();
	 	 			st.close();
	 	 		}catch(Exception e){LOGGER.error("inside the vNumber3");}
	 	 	}

		return vouNumber;
 	}
	 
	 public String vNumber(String type, Connection conn,String fund,String vhNum) throws TaskFailedException,SQLException
		{		
	 		String vouNumber="";
	 		String fType="";
	 	 	Statement st=null;
	 	 	ResultSet rs=null;
	 	 	try{
		 	 	st = conn.createStatement();
			  	rs = st.executeQuery(FUNDIDNSQL+fund);
		 		if(rs.next()){
		 			fType = rs.getString("fund_identi");
		 		}
		 		rs.close();
				String tran=type;		
		 		vouNumber=fType+tran+vhNum;
		 	}catch(Exception e){
	 	 		throw taskExc;
	 	 	}finally{
		 	 		try{
		 	 			rs.close();
		 	 			st.close();
		 	 		}catch(Exception e){LOGGER.error("its finally vNumber4");}
		 	 }
		 	return vouNumber;
	 	}
 	
	 
	 public String getEGWStatusId(Connection con, String moduleType, String description) throws TaskFailedException
		{
			String statusId="0";
			Statement stmt = null;
			ResultSet rs = null;
			try
			{
				String sql = " select distinct id from egw_status where upper(moduletype)='"+moduleType.toUpperCase()+"' and upper(description)='"+description.toUpperCase()+"' ";
				LOGGER.debug("statement"+sql);
				stmt = con.createStatement();
				rs= stmt.executeQuery(sql);
				if(rs.next()){
					statusId =(rs.getString("id")==null)?"0":rs.getString("id");
				}

				LOGGER.debug("$$$$$$$$$$$$$$$$$$$statusId==="+statusId);
				if(statusId==null || statusId.equals("0")){
					throw taskExc;
				}
				
			}
			catch(Exception e)
			{
				LOGGER.error("Exception in getEGWStatusId=====:"+e.getMessage());
	    		throw taskExc;
			}
			finally
			{
				try { if(rs!=null)rs.close(); }catch(Exception e){throw taskExc;}
				try { if(stmt!=null)stmt.close(); }catch(Exception e){throw taskExc;}
			}
			return statusId;
		}
	 /**
		 * This function will return the vouchernumber.
		 * Fund identifier+ voucher type +user entered number
		 * @param type		
		 * @param fund
		 * @param voucherNum
		 * @return
		 * @throws TaskFailedException,SQLException
		 */
 	public String vNumber(final String type,final String fund,final String vhNum) throws TaskFailedException,SQLException
	{		
 		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<String>() {

			@Override
			public String execute(Connection conn) throws SQLException {
				String vouNumber="";
		 		String fType="";
		 	 	Statement st=null;
		 	 	ResultSet rs=null;
		 	 	try{
			 	 	st = conn.createStatement();
				  	rs = st.executeQuery(FUNDIDNSQL+fund);
			 		if(rs.next()){
			 			fType = rs.getString("fund_identi");
			 		}
			 		rs.close();
					String tran=type;		
			 		vouNumber=fType+tran+vhNum;
			 	}catch(Exception e){
		 	 		
		 	 	}finally{
			 	 		try{
			 	 			rs.close();
			 	 			st.close();
			 	 		}catch(Exception e){LOGGER.error("its finally vNumber4");}
			 	 }
			 	return vouNumber;
			}
		});
 		
 	
 	}

	 
 	/**
 	 * To generate Voucher number based on type(P for payment, R for receipt and others), fundid and max(vouchernumber)+1
 	 * @param type
 	 * @param conn
 	 * @param fund
 	 * @return
 	 * @throws TaskFailedException,SQLException
 	 */
 	@Deprecated
 	 public String maxVoucherNumber(String type, Connection conn,String fund) throws TaskFailedException,SQLException
	 {
 	 	String fType="";
		String vNum="";
		Statement st=null;
 	 	ResultSet rs=null;
 	 	try{
	  	 	st = conn.createStatement();
		  	rs = st.executeQuery(FUNDIDNSQL+fund);
			if(rs.next()){
	 			fType = rs.getString("fund_identi");
			}
	 		rs.close();
	 		st.close();
	
			st = conn.createStatement();
			String vType=fType+type;
			String query=" select max(to_number(translate(vouchernumber,'1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ;/-_()','1234567890'),'FM9999999999'))+1 "
					+" as \"vounumber\" from voucherheader where  vouchernumber like '"+vType+"%' and "
					+" length(translate(vouchernumber,'1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ;/-_()','1234567890'))<=10 ";
		  	LOGGER.debug("query  "+query);
			rs = st.executeQuery(query);
			if(rs.next()){
	 			vNum = rs.getString(1);
			}
			LOGGER.debug("Voucher Number from the query is :"+vNum);
	 		rs.close();
	 		st.close();
	 		if(StringUtils.isNotBlank(vNum)){ 		//if(vNum!=null && !vNum.equals("")){
	 			for(int i=vNum.length();i<5;i++){
	 				vNum="0"+vNum;
	 			}
	 		}
	 		else{
	 			vNum="00001";
	 		}
	 		LOGGER.debug("Voucher Number after the zero addition is :"+vNum);
	 		vouNumber=vType+vNum;
 	 	}catch(Exception e){
 	 		throw taskExc;
 	 	}
	 	 	finally{
	 	 		try{
	 	 			rs.close();
	 	 			st.close();
	 	 		}catch(Exception e){LOGGER.error("maxVoucherNumber....");}
	 	 	}

	 	return vouNumber;
 	}
 	 
 	public String maxVoucherNumber(final String type,final String fund) throws TaskFailedException,SQLException
	 {
 		
 		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<String>() {

			@Override
			public String execute(Connection conn) throws SQLException {
				

			 	String fType="";
				String vNum="";
				Statement st=null;
			 	ResultSet rs=null;
			 	try{
			  	 	st = conn.createStatement();
				  	rs = st.executeQuery(FUNDIDNSQL+fund);
					if(rs.next()){
			 			fType = rs.getString("fund_identi");
					}
			 		rs.close();
			 		st.close();
			
					st = conn.createStatement();
					String vType=fType+type;
					String query=" select max(to_number(translate(vouchernumber,'1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ;/-_()','1234567890'),'FM9999999999'))+1 "
							+" as \"vounumber\" from voucherheader where  vouchernumber like '"+vType+"%' and "
							+" length(translate(vouchernumber,'1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ;/-_()','1234567890'))<=10 ";
				  	LOGGER.debug("query  "+query);
					rs = st.executeQuery(query);
					if(rs.next()){
			 			vNum = rs.getString(1);
					}
					LOGGER.debug("Voucher Number from the query is :"+vNum);
			 		rs.close();
			 		st.close();
			 		if(StringUtils.isNotBlank(vNum)){ 		//if(vNum!=null && !vNum.equals("")){
			 			for(int i=vNum.length();i<5;i++){
			 				vNum="0"+vNum;
			 			}
			 		}
			 		else{
			 			vNum="00001";
			 		}
			 		LOGGER.debug("Voucher Number after the zero addition is :"+vNum);
			 		vouNumber=vType+vNum;
			 	}catch(Exception e){
			 		LOGGER.error("Esxception in maxVoucherNumber " + e);
			 	}
			 	 	finally{
			 	 		try{
			 	 			rs.close();
			 	 			st.close();
			 	 		}catch(Exception e){LOGGER.error("maxVoucherNumber....");}
			 	 	}

			 	return vouNumber;
			
				
			}
		});
	 }
 	 
 	 /**
 	  * This function will generate the voucher number for the cess JV
 	  * @param datacol
 	  * @param conn
 	  * @param fund
 	  * @return
 	  * @throws TaskFailedException,SQLException
 	  */
 	public String vNumberCess(DataCollection datacol, Connection conn,String fund) throws TaskFailedException,SQLException
 	{	String fType="";
 	 	Statement st=null;
 	 	ResultSet rs=null;
 	 	try{
 	 	st = conn.createStatement();
 	 	rs = st.executeQuery(FUNDIDNSQL+fund);
 		if(rs.next()) {
 			fType = rs.getString("fund_identi");
 				}
 		rs.close();
 	 	String tran=datacol.getValue("cvType");
 		vouNumberCess=fType+tran+datacol.getValue("jv_voucherNumber");
 	 	}
 	 	finally{
 	 		try{
 	 			rs.close();
 	 			st.close();
 	 		}catch(Exception e){LOGGER.error("vNumberCess....");}
 	 	}
 	 	return vouNumberCess;
 	}
 	
 	
 	 /**
 	  * Common method for getting the cgvn
 	  * @param datacol
 	  * @param conn
 	  * @param fund
 	  * @return
 	  * @throws TaskFailedException,SQLException
 	  */
 	public String getCgvnNo(Connection conn,String fund,String vnum,String vType) throws TaskFailedException,SQLException
 	{	String fType="",cgvnNumber="";
 	 	Statement st=null;
 	 	ResultSet rs=null;
 	 	try{
	 	 	st = conn.createStatement();
	 	 	rs = st.executeQuery(FUNDIDNSQL+fund);
	 		if(rs.next()) {
	 			fType = rs.getString("fund_identi");
	 				}
	 		cgvnNumber=fType+vType+vnum;
 	 	}catch(Exception e){
 	 		throw taskExc;
 	 	}finally{
 	 		try{
 	 			rs.close();
 	 			st.close();
 	 		}catch(Exception e){LOGGER.error("getCgvnNo finally...");}
 	 	}
 		return cgvnNumber;
 	}
 	
 	/**
 	 * This function will generate the reverse voucher number
 	 * @param datacol
 	 * @param conn
 	 * @param fund
 	 * @return
 	 * @throws TaskFailedException,SQLException
 	 */
 	 public String rvNumber(DataCollection datacol, Connection conn,String fund) throws TaskFailedException,SQLException
 	 {
		String fType="";
 	 	Statement st=null;
 	 	ResultSet rs=null;
 	 	try{
 	 		st = conn.createStatement();
 	  	  	rs = st.executeQuery(FUNDIDNSQL+fund);
 			if(rs.next()){
 			fType = rs.getString("fund_identi");
 			}
 			String tran=datacol.getValue("tType");
 			revNumber=fType+tran+datacol.getValue("voucherHeader_newVcNo");
 	 	}catch(Exception e){
 	 		throw taskExc;
 	 	}finally{
 	 		try{
 	 			rs.close();
 	 			st.close();
 	 		}catch(Exception e){LOGGER.error("rvNumber....");}
 	 	}
 	 	 	return revNumber;
 	}
 	 
 	 /**
 	  * Function to check if the voucher number is Unique
 	  * @param datacol
 	  * @param conn
 	  * @param vouNum
 	  * @return
 	  */
	 public boolean isUniqueVN(DataCollection datacol, Connection conn,String vouNum)throws TaskFailedException,SQLException,ParseException {
		String vcNum = vouNum;
		String vcDate = datacol.getValue("voucherHeader_voucherDate");
		SimpleDateFormat sdf =sdfFormatddMMyyyy;
		SimpleDateFormat formatter = dtFormat;
		vcDate = formatter.format(sdf.parse( vcDate ));
		
		return isUniqueVN(vcNum, vcDate, datacol, conn);
	}
	 
	 /**
	  * Function to check if the voucher number is Unique
	  * @param datacol
	  * @param conn
	  * @param vcNoField
	  * @param vcDateField
	  * @return
	  * @throws TaskFailedException,SQLException
	  */
	 public boolean isUniqueVN(DataCollection datacol, Connection conn,String  vcNoField, String vcDateField)throws TaskFailedException,SQLException,ParseException{
		String vcNum=vcNoField;
		String vcDate = datacol.getValue(vcDateField);
		SimpleDateFormat sdf =sdfFormatddMMyyyy;
		SimpleDateFormat formatter = dtFormat;
		vcDate = formatter.format(sdf.parse( vcDate ));
			
		return isUniqueVN(vcNum, vcDate, datacol, conn);
	}

	 /**
	  * Function to check if the voucher number is Unique
	  * @param vcNum
	  * @param vcDate
	  * @param datacol
	  * @param conn
	  * @return
	  */
	 public boolean isUniqueVN(String vcNum, String vcDate, DataCollection datacol, Connection conn)throws TaskFailedException,SQLException{
		boolean isUnique = false;
		vcNum=vcNum.toUpperCase();
		Statement st =null;
		ResultSet rs=null;
		String fyStartDate = "", fyEndDate = "";
		try{
			st = conn.createStatement();
			rs = st.executeQuery("SELECT to_char(startingDate, 'DD-Mon-YYYY') AS \"startingDate\", to_char(endingDate, 'DD-Mon-YYYY') AS \"endingDate\" FROM financialYear WHERE startingDate <= '"+vcDate+"' AND endingDate >= '"+vcDate+"'");
			if(rs.next()) {
				fyStartDate = rs.getString("startingDate");
				fyEndDate = rs.getString("endingDate");
			}
			rs.close();
			st.close();
			st = conn.createStatement();
			rs = st.executeQuery("SELECT id FROM voucherHeader WHERE voucherNumber = '" + vcNum + "' AND voucherDate>='" + fyStartDate + "' AND voucherDate<='" + fyEndDate + "' and status!=4");
			if(rs.next()){
				datacol.addMessage(EXILRPERROR, "duplicate voucher number");
			}else{
				isUnique = true;
			}
		}catch(Exception ex){
			datacol.addMessage(EXILRPERROR, "DataBase Error(isUniqueVN) : " + ex.toString());
			throw new TaskFailedException();
		}finally{
 	 		try{
 	 			rs.close();
 	 			st.close();
 	 		}catch(Exception e){LOGGER.error("isUniqueVN....");}
 	 	}
		return isUnique;
	}
	 
	 /**
	  * 
	  * @param vcNum
	  * @param vcDate
	  * @param conn
	  * @return
	  * @throws TaskFailedException,SQLException
	  */
	public boolean isUniqueVN(String vcNum, String vcDate,Connection conn)throws SQLException,TaskFailedException{
		boolean isUnique = false;
		String fyStartDate = "", fyEndDate = "";
		vcNum=vcNum.toUpperCase();
		Statement st = null;
		ResultSet rs =null;
		try{
			st = conn.createStatement();
			rs = st.executeQuery("SELECT to_char(startingDate, 'DD-Mon-YYYY') AS \"startingDate\", to_char(endingDate, 'DD-Mon-YYYY') AS \"endingDate\" FROM financialYear WHERE startingDate <= '"+vcDate+"' AND endingDate >= '"+vcDate+"'");
			if(rs.next()) {
				fyStartDate = rs.getString("startingDate");
				fyEndDate = rs.getString("endingDate");
			}
			rs.close();
			st.close();
			st = conn.createStatement();
			rs = st.executeQuery("SELECT id FROM voucherHeader WHERE voucherNumber = '" + vcNum + "' AND voucherDate>='" + fyStartDate + "' AND voucherDate<='" + fyEndDate + "' and status!=4");
			if (rs.next()) {
				LOGGER.debug("Duplicate Voucher Number");
			} else {
				isUnique = true;
			}
		}catch(Exception ex){
			LOGGER.error("error in finding unique VoucherNumber");
			throw taskExc;
		}finally{
 	 		try{
 	 			rs.close();
 	 			st.close();
 	 		}catch(Exception e){LOGGER.error("isUniqueVN....");}
 	 	}
		return isUnique;
	}
	
	
	/**
	 * This function will check if the cheque no is unique	 
	 */
	 public boolean isUniqueChequeNo(String Chequeno, String BankAccId, DataCollection datacol, Connection conn)throws TaskFailedException,SQLException{
		boolean isUnique = true;
		Statement st = null;
		ResultSet rs =null;
		try{
			st = conn.createStatement();
			st = conn.createStatement();
			String sql=" SELECT cd.id FROM chequedetail cd,voucherheader vh WHERE cd.accountnumberid = '"+BankAccId+"' AND cd.chequenumber='"+Chequeno+"' AND cd.ispaycheque='1' AND cd.chequetype='C' AND vh.id=cd.VOUCHERHEADERID AND vh.status<>4 ";
			LOGGER.debug(sql);
			rs = st.executeQuery(sql);
			if(rs.next()){
				datacol.addMessage(EXILRPERROR, "duplicate Cheque number");
				isUnique = false;
			}
			else {
				isUnique = true;
			}
		}catch(SQLException ex){
			datacol.addMessage(EXILRPERROR, "DataBase Error(isUniqueChequeNo) : " + ex.toString());
			throw taskExc;
		}finally{
 	 		try{
 	 			rs.close();
 	 			st.close();
 	 		}catch(Exception e){LOGGER.error("isUniqueChequeNo.");}
 	 	}
		
		LOGGER.debug("isUnique:"+isUnique);
		return isUnique; 
	} 
	 
	 /**
		 * This function will check if the cheque no is unique 
		 * without DataCollection 
		 * @param Chequeno
		 * @param BankAccId	
		 * @param conn
		 * @return
		 */
		 public boolean isUniqueChequeNo(String Chequeno, String BankAccId, Connection conn)throws TaskFailedException,SQLException
		 {
			boolean isUnique = false;
			Statement st = null;
			ResultSet rs =null;
			try
			{
				st = conn.createStatement();
				String sql=" SELECT cd.id FROM chequedetail cd,voucherheader vh WHERE cd.accountnumberid = '" + BankAccId + "' AND cd.chequenumber='" + Chequeno + "' AND cd.ispaycheque='1' AND cd.chequetype='C' AND vh.id=cd.VOUCHERHEADERID AND vh.status<>4 " +
						"  UNION SELECT egsc.id FROM EG_SURRENDERED_CHEQUES egsc,voucherheader vh WHERE egsc.BANKACCOUNTID = '" + BankAccId + "' AND egsc.chequenumber='" + Chequeno + "' AND vh.id=egsc.vhid AND vh.status<>4 "; 
				rs = st.executeQuery(sql);
				LOGGER.debug("Query:::"+sql);
				if(rs.next()) {
					throw new EGOVException("Error: duplicate Cheque number");
				}else{
					isUnique = true;
				}
			}
			catch(Exception e)
	    	{
				throw taskExc;
	    	}finally{
	 	 		try{
	 	 			rs.close();
	 	 			st.close();
	 	 		}catch(Exception e){LOGGER.error("isUniqueChequeNo....");}
	 	 	}
			return isUnique;
		}
		
	 
	 /**
	  * Checking for Cheque number is withing the Range
	  * @param Chequeno
	  * @param BankAccId
	  * @param datacol
	  * @param conn
	  * @return
	  */
	 public boolean isChqNoWithinRange(String Chequeno, String BankAccId, DataCollection datacol, Connection conn)throws TaskFailedException,SQLException{
		boolean isWithinRange = false;
		Statement st =null; 
		ResultSet rs =null;
		try{
			st = conn.createStatement();
			rs = st.executeQuery("SELECT id FROM EGF_ACCOUNT_CHEQUES WHERE BANKACCOUNTID = '" + BankAccId + "' AND fromchequenumber<=TO_NUMBER('" + Chequeno + "') and TO_NUMBER('"+Chequeno+"')<= tochequenumber");
			if(rs.next()){
				isWithinRange = true;
			}else{
				datacol.addMessage(EXILRPERROR, "Invalid Cheque number");
			}
		}catch(SQLException ex){
			datacol.addMessage(EXILRPERROR, "DataBase Error(isWithinRange) : " + ex.toString());
			throw taskExc;
		}finally{
 	 		try{
 	 			rs.close();
 	 			st.close();
 	 		}catch(Exception e){LOGGER.error("isUniqueVN....");}
 	 	}
		return isWithinRange;
	}
	 
	 /**
	  * Checking for Cheque number is withing the Range without DataCollection 
	  * @param Chequeno
	  * @param BankAccId	
	  * @param conn
	  * @return
	  */
	 public boolean isChqNoWithinRange(String Chequeno, String BankAccId, Connection conn)throws TaskFailedException,SQLException
	 {
		boolean isWithinRange = false;
		Statement st=null;
		ResultSet rs=null;
		try{
			st= conn.createStatement();		
			rs = st.executeQuery("SELECT id FROM EGF_ACCOUNT_CHEQUES WHERE BANKACCOUNTID = '" + BankAccId + "' AND fromchequenumber<=TO_NUMBER('" + Chequeno + "') and TO_NUMBER('"+Chequeno+"')<= tochequenumber");
			if(rs.next()) {
				isWithinRange = true;
			}else{
				throw new TaskFailedException("Error: Invalid Cheque number");
			}
		}
		catch(Exception e)
    	{
    		LOGGER.error("Exception in isChqNoWithinRange"+e.getMessage());
    		throw taskExc;
    	}
		finally{
			try{
				rs.close();
				st.close();
			}catch(Exception e){LOGGER.error("Exp in cheque range");}
		}
		return isWithinRange;
	}
	 
	 /**
	  * This function will check if the date enetered is within the todays' date 
	  * @param datacol
	  * @param conn
	  * @param VDate
	  * @return
	  */ 
	  public int isCurDate(DataCollection datacol, Connection conn,String VDate)throws TaskFailedException,SQLException{
		int isCurDate = 0;
		try{
			String today=getCurrentDate(conn);
			String[] dt2 = today.split("/");
			String[] dt1= VDate.split("/");
			int ret = (Integer.parseInt(dt2[2])>Integer.parseInt(dt1[2])) ? 1 : (Integer.parseInt(dt2[2])<Integer.parseInt(dt1[2])) ? -1 : (Integer.parseInt(dt2[1])>Integer.parseInt(dt1[1])) ? 1 : (Integer.parseInt(dt2[1])<Integer.parseInt(dt1[1])) ? -1 : (Integer.parseInt(dt2[0])>Integer.parseInt(dt1[0])) ? 1 : (Integer.parseInt(dt2[0])<Integer.parseInt(dt1[0])) ? -1 : 0 ;
			if(ret==-1 ){
				datacol.addMessage(EXILRPERROR, "Date Should be within the today date");
			}
			else{
				isCurDate=1;
			}

		}catch(Exception ex){
			datacol.addMessage(EXILRPERROR, "DataBase Error(iscurDate) : " + ex.toString());
			throw taskExc;
		}
		return isCurDate;
	}
	  
/**
 * This function will check if the Reverse date is prior or on the same date as that of the original voucher date
 * @param datacol
 * @param conn
 * @param VDate
 * @param RVDate
 * @return
 */
	 public int isValidDate(DataCollection datacol, Connection conn,String VDate,String RVDate)throws TaskFailedException,SQLException{
		int isCurDate = 0;
		try{
			String[]  dt1= VDate.split("/");
			String[]  dt2= RVDate.split("/");
			int ret = (Integer.parseInt(dt2[2])>Integer.parseInt(dt1[2])) ? 1 : (Integer.parseInt(dt2[2])<Integer.parseInt(dt1[2])) ? -1 : (Integer.parseInt(dt2[1])>Integer.parseInt(dt1[1])) ? 1 : (Integer.parseInt(dt2[1])<Integer.parseInt(dt1[1])) ? -1 : (Integer.parseInt(dt2[0])>Integer.parseInt(dt1[0])) ? 1 : (Integer.parseInt(dt2[0])<Integer.parseInt(dt1[0])) ? -1 : 0 ;
			if(ret==-1 ){
				datacol.addMessage(EXILRPERROR, "Date Should be more than original voucher date");
			}
			else{
				isCurDate=1;
			}
		}catch(Exception ex){
			datacol.addMessage(EXILRPERROR, "DataBase Error(isValidDate) : " + ex.toString());
			throw taskExc;
		}
		return isCurDate;
	}
	 
	 /**
	  * This function will check if the Reverse date is prior or on the same date as that of the original voucher date
	  * @param conn
	  * @param VDate
	  * @param RVDate
	  * @return
	  */
	 	 public int isValidDate(Connection conn,String VDate,String RVDate)throws TaskFailedException,SQLException
	 	 {
	 		int isCurDate = 0;
	 		try
	 		{
	 			String[]  dt1= VDate.split("/");
	 			String[]  dt2= RVDate.split("/");
	 			int ret = (Integer.parseInt(dt2[2])>Integer.parseInt(dt1[2])) ? 1 : (Integer.parseInt(dt2[2])<Integer.parseInt(dt1[2])) ? -1 : (Integer.parseInt(dt2[1])>Integer.parseInt(dt1[1])) ? 1 : (Integer.parseInt(dt2[1])<Integer.parseInt(dt1[1])) ? -1 : (Integer.parseInt(dt2[0])>Integer.parseInt(dt1[0])) ? 1 : (Integer.parseInt(dt2[0])<Integer.parseInt(dt1[0])) ? -1 : 0 ;
	 			if(ret==-1 ) {
	 				throw new TaskFailedException("Date Should be more than original voucher date");
	 			}
	 				isCurDate=1;
	 		}
	 		catch(Exception ex)
	 		{
	 			LOGGER.error("Inside the isValidDate.. "+ex.getMessage());
	 			throw taskExc;
	 		}
	 		return isCurDate;
	 	}

	 
/**
 * 
 * @param vouType
 * @param fiscialPeriod
 * @param conn
 * @return
 * @throws TaskFailedException,SQLException
 */	 
	@Deprecated
	public String getEg_Voucher(String vouType,String fiscialPeriod,Connection conn) throws TaskFailedException,SQLException
	{
		String voucherNumber="";
		Eg_Numbers en=new Eg_Numbers();
		Statement st =null;
		ResultSet rs = null;
		try{
			st = conn.createStatement();
			LOGGER.debug(" Value of fiscialPeriod is :"+fiscialPeriod);
			if(fiscialPeriod==null || fiscialPeriod.trim().equals("null")){
				LOGGER.debug(" Year not defined.");
				throw new TaskFailedException("Voucher Date not within an open period or Financial year not open for posting.");
			}
	
			rs = st.executeQuery("select id as \"key\", max(vouchernumber)  as \"vouchernumber\" from eg_numbers where vouchertype='"+vouType+"' and fiscialperiodid="+fiscialPeriod+" group by id");
			if(rs.next())
			{
				voucherNumber = rs.getString("vouchernumber");
				String idno=rs.getString("key");
				en.setId(idno);
				retVal=retVal+1;
				LOGGER.debug("voucherNumber  "+voucherNumber);
				voucherNumber=String.valueOf(Integer.parseInt(voucherNumber)+1);
				en.setVoucherNumber(voucherNumber);
				en.update(conn);
			}
			else
			{
				voucherNumber="1";
				voucherNumber=String.valueOf(Integer.parseInt(voucherNumber));
				en.setVoucherNumber(voucherNumber);
				en.setVoucherType(vouType);
				en.setFiscialPeriodId(fiscialPeriod);
				en.insert(conn);
			}
		}catch(Exception e)
		{
			LOGGER.error("Inside the voucher number generation"+e.getMessage());
			throw taskExc;
		}finally{
			try{
				rs.close();
				st.close();
			}catch(Exception e){
				LOGGER.error("Inside finally block for voucher generation.");
			}
		}
	LOGGER.debug("voucherNumber  "+voucherNumber+"  retVal "+retVal);
	return voucherNumber;
  }

	public String getEg_Voucher(final String vouType,final String fiscialPeriod)throws TaskFailedException,SQLException
	{
		
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<String>() {

			@Override
			public String execute(Connection conn) throws SQLException {
			

				String voucherNumber="";
				Eg_Numbers en=new Eg_Numbers();
				Statement st =null;
				ResultSet rs = null;
				try{
					st = conn.createStatement();
					LOGGER.debug(" Value of fiscialPeriod is :"+fiscialPeriod);
					if(fiscialPeriod==null || fiscialPeriod.trim().equals("null")){
						LOGGER.debug(" Year not defined.");
						throw new TaskFailedException("Voucher Date not within an open period or Financial year not open for posting.");
					}
			
					rs = st.executeQuery("select id as \"key\", max(vouchernumber)  as \"vouchernumber\" from eg_numbers where vouchertype='"+vouType+"' and fiscialperiodid="+fiscialPeriod+" group by id");
					if(rs.next())
					{
						voucherNumber = rs.getString("vouchernumber");
						String idno=rs.getString("key");
						en.setId(idno);
						retVal=retVal+1;
						LOGGER.debug("voucherNumber  "+voucherNumber);
						voucherNumber=String.valueOf(Integer.parseInt(voucherNumber)+1);
						en.setVoucherNumber(voucherNumber);
						en.update(conn);
					}
					else
					{
						voucherNumber="1";
						voucherNumber=String.valueOf(Integer.parseInt(voucherNumber));
						en.setVoucherNumber(voucherNumber);
						en.setVoucherType(vouType);
						en.setFiscialPeriodId(fiscialPeriod);
						en.insert(conn);
					}
				}catch(Exception e)
				{
					LOGGER.error("Inside the voucher number generation"+e.getMessage());
					
				}finally{
					try{
						rs.close();
						st.close();
					}catch(Exception e){
						LOGGER.error("Inside finally block for voucher generation.");
					}
				}
			LOGGER.debug("voucherNumber  "+voucherNumber+"  retVal "+retVal);
			return voucherNumber;
		  
				
			}
		});
		
	}
	
	/**
	 * This function will check the balance amount in a particular Bank Account(bankAccountId)
	 * @param datacol
	 * @param con
	 * @param bankAccountId
	 * @return
	 * @throws TaskFailedException
	 * First we need get the opening balance from transaction summary and then the
	 * sum(debitamount)-sum(creditamount) from the general ledger.
	 */
  public BigDecimal getAccountBalance(DataCollection datacol,Connection con,String bankAccountId) throws TaskFailedException
  {
  	
  	BigDecimal opeAvailable=BigDecimal.ZERO;
  	BigDecimal totalAvailable=BigDecimal.ZERO;
  	Statement statement=null;
  	ResultSet resultset=null;
	ResultSet resultset1=null;
	try{
	  	statement = con.createStatement();
	  	String vcDate = datacol.getValue("voucherHeader_voucherDate");
		SimpleDateFormat sdf =sdfFormatddMMyyyy;
		SimpleDateFormat formatter = dtFormat;
		vcDate = formatter.format(sdf.parse( vcDate ));

		String str="SELECT decode(sum(openingDebitBalance),null,0,sum(openingDebitBalance))- decode(sum(openingCreditBalance),null,0,sum(openingCreditBalance)) AS \"openingBalance\" "+
			"FROM transactionSummary WHERE financialYearId=( SELECT id FROM financialYear WHERE startingDate <='"+vcDate+"'" +
		   	"AND endingDate >='"+vcDate+"')  AND glCodeId =(select glcodeid from bankaccount where id="+bankAccountId+")";
	  	LOGGER.debug("getAccountBalance(EGovernCommon.java): "+str);

	  	resultset = statement.executeQuery(str);
	  	if(resultset.next()){
	  	 	opeAvailable = resultset.getBigDecimal("openingBalance");
	  	}
	  	else
	  		LOGGER.debug("Else resultset in getbalance");
	    LOGGER.debug("opening balance  "+opeAvailable);
		
   		String str1="SELECT (decode(sum(gl.debitAmount),null,0,sum(gl.debitAmount)) - decode(sum(gl.creditAmount),null,0,sum(gl.creditAmount))) + " +opeAvailable+
			" as \"totalAmount\" FROM   generalLedger gl, voucherHeader vh WHERE vh.id = gl.voucherHeaderId AND gl.glCodeid = (select glcodeid from bankaccount where id="+bankAccountId+") AND  "+
			" vh.voucherDate >=( SELECT TO_CHAR(startingDate, 'dd-Mon-yyyy') FROM financialYear WHERE startingDate <= '"+vcDate+"' AND endingDate >= '"+vcDate+"') AND vh.voucherDate <= '"+vcDate+"' and vh.status!=4";

		LOGGER.debug("Curr Yr Bal: "+str1);

		resultset1 = statement.executeQuery(str1);
 		if(resultset1.next()){
	   		 totalAvailable = resultset1.getBigDecimal("totalAmount");
 			LOGGER.debug("total balance  "+totalAvailable);
 		}
 		else
	  		LOGGER.debug("Else resultset in getbalance...");
 		totalAvailable=totalAvailable.setScale(2, BigDecimal.ROUND_HALF_UP);
 		LOGGER.debug("total balance before return "+totalAvailable);
 		return totalAvailable;
  	}catch(Exception e){
  		LOGGER.error(e.getMessage(), e);
  		throw taskExc;
  	}finally{
  		try{
  			resultset.close();
  			resultset1.close();
  			statement.close();
  		}catch(Exception e){LOGGER.error("finally in getbalance");}
  	}
}
  public BigDecimal getAccountBalance(Connection con,Date VoucherDate,String bankAccountId)throws TaskFailedException 
  {
	  	BigDecimal totalAvailable=BigDecimal.ZERO;
  		BigDecimal opeAvailable=BigDecimal.ZERO;
  		Statement statement=null;
	  	ResultSet resultset=null;
	  	ResultSet resultset1=null;
	  	try{
	  		statement = con.createStatement();
	  		SimpleDateFormat formatter = dtFormat;
	  		String vcDate = formatter.format(VoucherDate);

			String str="SELECT decode(sum(openingDebitBalance),null,0,sum(openingDebitBalance))- decode(sum(openingCreditBalance),null,0,sum(openingCreditBalance)) AS \"openingBalance\" "+
				"FROM transactionSummary WHERE financialYearId=( SELECT id FROM financialYear WHERE startingDate <='"+vcDate+"'" +
			   	"AND endingDate >='"+vcDate+"')  AND glCodeId =(select glcodeid from bankaccount where id="+bankAccountId+")";
		  	LOGGER.debug("getAccountBalance(EGovernCommon.java): "+str);

		  	resultset = statement.executeQuery(str);
		  	if(resultset.next()){
		  		opeAvailable = resultset.getBigDecimal("openingBalance");
		  	}else
		  		LOGGER.debug("Else resultset in getbalance");

		  	LOGGER.debug("opening balance  "+opeAvailable);
		  	//resultset.close();

		  	String str1="SELECT (decode(sum(gl.debitAmount),null,0,sum(gl.debitAmount)) - decode(sum(gl.creditAmount),null,0,sum(gl.creditAmount))) + " +opeAvailable+
			" as \"totalAmount\" FROM   generalLedger gl, voucherHeader vh WHERE vh.id = gl.voucherHeaderId AND gl.glCodeid = (select glcodeid from bankaccount where id="+bankAccountId+") AND  "+
			" vh.voucherDate >=( SELECT TO_CHAR(startingDate, 'dd-Mon-yyyy') FROM financialYear WHERE startingDate <= '"+vcDate+"' AND endingDate >= '"+vcDate+"') AND vh.voucherDate <= '"+vcDate+"' and vh.status!=4";

		  	LOGGER.debug("Curr Yr Bal: "+str1);

		  	resultset1 = statement.executeQuery(str1);
		  	if(resultset1.next()){
		  		totalAvailable = resultset1.getBigDecimal("totalAmount");
		  		LOGGER.debug("total balance  "+totalAvailable);
		  	}else
		  		LOGGER.debug("Else resultset in getbalance...");

		  	totalAvailable=totalAvailable.setScale(2, BigDecimal.ROUND_HALF_UP);
		  	LOGGER.debug("total balance before return "+totalAvailable);
 		
  	}catch(Exception e){
  		LOGGER.error(e.getMessage(), e);
  		throw taskExc;
  	}finally{
  		try{
  			resultset.close();
  			resultset1.close();
  			statement.close();
  		}catch(Exception e){LOGGER.error("finally in getbalance");}
  	}
  	return totalAvailable;
}
/*
	overloaded method with different argument type(without datacol)

*/
public BigDecimal getAccountBalance(String recDate,Connection con,String bankAccountId) throws TaskFailedException
{
  	
	BigDecimal opeAvailable=BigDecimal.ZERO;
	BigDecimal totalAvailable=BigDecimal.ZERO;
  	Statement statement=null;
  	ResultSet resultset=null;
  	ResultSet resultset1=null;
  	try{
	  	statement = con.createStatement();
		String str="SELECT decode(sum(openingDebitBalance),null,0,sum(openingDebitBalance))- decode(sum(openingCreditBalance),null,0,sum(openingCreditBalance)) AS \"openingBalance\" "+
			"FROM transactionSummary WHERE financialYearId=( SELECT id FROM financialYear WHERE startingDate <='"+recDate+"'" +
		   	"AND endingDate >='"+recDate+"')  AND glCodeId =(select glcodeid from bankaccount where id="+bankAccountId+")";
	  	LOGGER.debug("getAccountBalance(EGovernCommon.java): "+str);

	  	resultset = statement.executeQuery(str);
	  	if(resultset.next()){
	  	 	opeAvailable = resultset.getBigDecimal("openingBalance");
	  	}else
	  		LOGGER.debug("Else resultset in getAccountBalance...");

	    LOGGER.debug("opening balance  "+opeAvailable);

   		String str1="SELECT (decode(sum(gl.debitAmount),null,0,sum(gl.debitAmount)) - decode(sum(gl.creditAmount),null,0,sum(gl.creditAmount))) + " +opeAvailable+
			" as \"totalAmount\" FROM   generalLedger gl, voucherHeader vh WHERE vh.id = gl.voucherHeaderId and gl.glCodeid = (select glcodeid from bankaccount where id="+bankAccountId+") AND  "+
			" vh.voucherDate >=( SELECT TO_CHAR(startingDate, 'dd-Mon-yyyy') FROM financialYear WHERE startingDate <= '"+recDate+"' AND endingDate >= '"+recDate+"') AND vh.voucherDate <= '"+recDate+"' and vh.status!=4" ;

		LOGGER.debug("Curr Yr Bal: "+str1);

		resultset1 = statement.executeQuery(str1);
 		if(resultset1.next()){
	   		 totalAvailable = resultset1.getBigDecimal("totalAmount");
 			LOGGER.debug("total balance  "+totalAvailable);
 		}else
	  		LOGGER.debug("Else resultset in getAccountBalance...");
 		
 		totalAvailable=totalAvailable.setScale(2, BigDecimal.ROUND_HALF_UP);
 		LOGGER.debug("total balance before return "+totalAvailable);
 		return totalAvailable;
  	}catch(Exception e){
  		LOGGER.error(e.getMessage(), e);
  		throw taskExc;
  	}finally{
  		try{
  			resultset.close();
  			resultset1.close();
  			statement.close();
  		}catch(Exception e){LOGGER.error("finally in getbalance");}
  	}
}
	/**
	 * This function will check the cash balance amount
	 * @param datacol
	 * @param con
	 * @param cashIdValue
	 * @return
	 * @throws TaskFailedException
	 * First we need get the opening balance from transaction summary and then the
	 * sum(debitamount)-sum(creditamount) from the general ledger.
	 */
  public BigDecimal getCashBalance(String vcDate,Connection con,String cashIdValue,String fundId) throws TaskFailedException
  {

	BigDecimal opeAvailable=BigDecimal.ZERO;
	BigDecimal totalAvailable=BigDecimal.ZERO;
  	Statement statement=null;
  	ResultSet resultset=null;
	try{
	   	statement = con.createStatement();
	  	String vcDateFmt="";
		SimpleDateFormat sdf =sdfFormatddMMyyyy;
		SimpleDateFormat formatter = dtFormat;
		vcDateFmt = formatter.format( sdf.parse( vcDate ));

		String str="SELECT decode(sum(openingDebitBalance),null,0,sum(openingDebitBalance))- decode(sum(openingCreditBalance),null,0,sum(openingCreditBalance)) AS \"openingBalance\" "+
			"FROM transactionSummary WHERE financialYearId=( SELECT id FROM financialYear WHERE startingDate <='"+vcDateFmt+"'" +
		   	"AND endingDate >='"+vcDateFmt+"')  AND glCodeId ="+cashIdValue+" and fundid="+fundId;
	  	LOGGER.debug(str);

	  	resultset = statement.executeQuery(str);
	  	if(resultset.next()){
	  	 	opeAvailable = resultset.getBigDecimal("openingBalance");
	  	}else
	  		LOGGER.debug("Else resultset in getCashAccountBalance...");
	    LOGGER.debug("opening balance  "+opeAvailable);
   		String str1="SELECT (decode(sum(gl.debitAmount),null,0,sum(gl.debitAmount)) - decode(sum(gl.creditAmount),null,0,sum(gl.creditAmount))) + " +opeAvailable+
			" as \"totalAmount\" FROM   generalLedger gl, voucherHeader vh WHERE vh.id = gl.voucherHeaderId AND gl.glCodeid = "+cashIdValue+" AND  "+
			" vh.voucherDate >=( SELECT TO_CHAR(startingDate, 'dd-Mon-yyyy') FROM financialYear WHERE startingDate <= '"+vcDateFmt+"' AND endingDate >= '"+vcDateFmt+"') AND vh.voucherDate <= '"+vcDateFmt+"' and vh.status!=4 and fundid="+fundId;

		LOGGER.debug(str1);

		resultset = statement.executeQuery(str1);
 		if(resultset.next()){
	   		 totalAvailable = resultset.getBigDecimal("totalAmount");
 			LOGGER.debug("total cash balance  "+totalAvailable);
 		}else
	  		LOGGER.debug("Else resultset in getCashAccountBalance");
 		totalAvailable=totalAvailable.setScale(2, BigDecimal.ROUND_HALF_UP);
 		LOGGER.debug("total cash balance  "+totalAvailable);
 		return totalAvailable;
  	}
  	catch(Exception e){
  		LOGGER.error(e.getMessage(), e);
  		throw taskExc;
  	}finally{
  		try{
  			resultset.close();
  			statement.close();
  		}catch(Exception e){LOGGER.error("finally in getCashBalance");}
  	}

}

	 public String getEffectiveDateFilter(Connection con,String val) throws TaskFailedException,SQLException{
		String returnVal="";
		if(val==null){
			returnVal=getCurrentDateTime(con);
			returnVal=" and TO_DATE(TO_CHAR(vh.effectivedate,'dd-Mon-yyyy HH24:MI:SS'),'dd-Mon-yyyy HH24:MI:SS')<" +
				" TO_DATE('"+returnVal+"','dd-Mon-yyyy HH24:MI:SS')";

		}else{
			returnVal=" and TO_DATE(TO_CHAR(vh.effectivedate,'dd-Mon-yyyy HH24:MI:SS'),'dd-Mon-yyyy HH24:MI:SS')<" +
			" TO_DATE('"+val+"','dd-Mon-yyyy HH24:MI:SS')";
		}
		return returnVal;
	}
	 
	 public String getCurDateTime(Connection con)throws TaskFailedException,SQLException{
		 Statement st=null;
		 ResultSet rset=null;
		 String val="";
		 try{
			 st=con.createStatement();
			 String query=" select TO_CHAR(sysdate,'dd-Mon-yyyy HH24:MI:SS') from dual " ;
			 rset=st.executeQuery(query);
			 while(rset.next()){
				 val=rset.getString(1);
			 }
		 }catch(Exception e){
		  		LOGGER.error(e.getMessage(), e);
		  		throw taskExc;
		  	}finally{
		  		try{
		  			rset.close();
		  			st.close();
		  		}catch(Exception e){LOGGER.error("finally in getCurDateTime");}
		  	}
		return val;
	 }

	 /**
	  * Function to get the Fundid
	  * @param datacol
	  * @param con
	  * @return
	  * @author Elzan Mathew
	  */
	 public String getFund(DataCollection datacol,Connection con) throws TaskFailedException{
	 	String retVal="";
	 	Statement stmt=null;
	 	ResultSet rs= null;
	 	try{
	 		stmt=con.createStatement();
	 		rs=stmt.executeQuery("select fundid from voucherheader where id="+datacol.getValue("voucherHeader_id"));
	 		if(rs.next()){
	 			retVal=rs.getString(1);
	 		}else
	 			LOGGER.debug("Inside if for getFund");
 		}catch(Exception e){
 			LOGGER.error("Exception in getting fund.."+e);
 			throw taskExc;
 		}finally{
	  		try{
	  			rs.close();
	  			stmt.close();
	  		}catch(Exception e){LOGGER.error("finally in getFund");}
	  	}
	 	return retVal;
	 }

/**
 * @author Elzan Mathew
 * This function will be called from all the transactions for inserting into the status table
 */
	 //private String id = "0";//Added by sumit for EGF_RECORD_STATUS table
	 //public void setId(String aId){ id = aId; }//Added by sumit for EGF_RECORD_STATUS table

	public void UpdateVoucherStatus(DataCollection datacol,String type,Connection connection,int voucherHeaderId)throws TaskFailedException {
	 	egfRecordStatus egfstatus = new egfRecordStatus();
	 	String today;
	 	try
	 	{
	 		int recStatus =0;
	 		String code=EGovConfig.getProperty("egf_config.xml","confirmoncreate","","JournalVoucher");
	 		if(code.equalsIgnoreCase("N")){
	 			recStatus=1;
	 		}
	 		else{
	 			recStatus=0;
	 		}
	 		EGovernCommon cm=new EGovernCommon();
	 		SimpleDateFormat sdf =sdfFormatddMMyyyy;
			SimpleDateFormat formatter = dtFormat;
			today=cm.getCurrentDateTime(connection);

			egfstatus.setEffectiveDate(formatter.format(sdf.parse( today )));
	 		egfstatus.setVoucherheaderId(String.valueOf(voucherHeaderId));
	 		egfstatus.setRecord_Type(type);
	 		egfstatus.setStatus(String.valueOf(recStatus));
	 		egfstatus.setUserId(datacol.getValue("current_UserID"));
	 		egfstatus.insert(connection);
	 	}catch(Exception e){
	 		datacol.addMessage(EXILRPERROR,"Voucher Status not inserted");
	 		LOGGER.error(e.getMessage(), e);
	 		throw taskExc;
	 	}
	 }
	 
/**
 * Overloaded UpdateVoucherStatus method without datacol parameter
 */
	public void UpdateVoucherStatus(String type,Connection connection,int voucherHeaderId,int userId)throws TaskFailedException,SQLException,ParseException
	{
	 	try
	 	{
	 		egfRecordStatus egfstatus = new egfRecordStatus();
		 	String today;
	 		int recStatus =0;
	 		String code=EGovConfig.getProperty("egf_config.xml","confirmoncreate","","JournalVoucher");
	 		LOGGER.debug("code:"+code);
	 		if(code.equalsIgnoreCase("N")){
	 			recStatus=1;
	 		}
	 		else{
	 			recStatus=0;
	 		}
	 		SimpleDateFormat sdf =sdfFormatddMMyyyy;
			SimpleDateFormat formatter = dtFormat;
			EGovernCommon cm=new EGovernCommon();
			today=cm.getCurrentDate(connection);
			egfstatus.setEffectiveDate(formatter.format(sdf.parse( today )));
	 		egfstatus.setVoucherheaderId(String.valueOf(voucherHeaderId));
	 		egfstatus.setRecord_Type(type);
	 		egfstatus.setStatus(String.valueOf(recStatus));
	 		egfstatus.setUserId(String.valueOf(userId));
	 		egfstatus.insert(connection);
	 	}
	 	catch(Exception e)
	 	{
	 		LOGGER.error(e.getMessage(), e);
	 		throw taskExc;
	 	}

	 }
	@Deprecated
	 public String getStartDate(Connection connection, String finId) throws TaskFailedException
		{
			Statement statement=null;
			ResultSet resultset=null;
			String startDate=null;
			try
			{
				statement = connection.createStatement();
				resultset = statement.executeQuery("select startingdate as \"startDate\" from financialyear where id ="+finId );
				resultset.next();
				startDate = resultset.getString("startDate");
			}
			catch(SQLException sqlex)
			{
				LOGGER.error("EGovernCommon->getStartDate " + sqlex.getMessage());
				throw taskExc;
			}finally{
				try{
					resultset.close();
					statement.close();
				}catch(Exception e){LOGGER.error("finally block getStartDate");}
			}
			return startDate;
		}
	 
	 public static String getStartDate(final String finId) 
		{
		 
		 return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<String>() {

			@Override
			public String execute(Connection connection) throws SQLException {
				
				Statement statement=null;
				ResultSet resultset=null;
				String startDate=null;
				try
				{
					statement = connection.createStatement();
					resultset = statement.executeQuery("select startingdate as \"startDate\" from financialyear where id ="+finId );
					resultset.next();
					startDate = resultset.getString("startDate");
				}
				catch(SQLException sqlex)
				{
					LOGGER.error("EGovernCommon->getStartDate " + sqlex.getMessage());
					
				}finally{
					try{
						resultset.close();
						statement.close();
					}catch(Exception e){LOGGER.error("finally block getStartDate");}
				}
				return startDate;
				
			}
		});
			
		}
	/**
	 * @author Lakshmi
	 * This function will be called from those transactions that need
	 * balance amount from bank during modification of the BankPayment
	 * @param datacol
	 * @param con
	 * @param balAvailable
	 * @return accountBalanceCr
	 * @throws TaskFailedException
	 * Get the accountBalanceCr amount as totalBalance+oldTransactionAmout-newAmount if accountNumber is same,
	 * else accountBalanceCr=totalBalance+oldTransactionAmout-newAmount.
	 * @Date 08-May-2006
	 */
	 public BigDecimal getAccountBalanceInModify(DataCollection datacol,Connection con,String bankAccId,String accountBalance,int voucherId)throws TaskFailedException
		{
			Statement st=null;
			ResultSet rs=null;
			
			BigDecimal oldAmt=BigDecimal.ZERO;
			BigDecimal accBalance=new BigDecimal(accountBalance);
			LOGGER.debug("  accBalance  "+accBalance);
			BigDecimal balAvailable=getAccountBalance(datacol,con,bankAccId);
			LOGGER.debug("  balAvailable  "+balAvailable);
			BigDecimal accountBalanceCr = balAvailable.subtract(accBalance);
			LOGGER.debug("  accountBalanceCr  "+accountBalanceCr);
			String query="select bankaccountid,amount from bankreconciliation where voucherheaderid="+voucherId+" and transactionType='Cr'";
			try
			{
				st=con.createStatement();
				rs=st.executeQuery(query);
			    if(rs.next())
			    {
			      	oldAmt=rs.getBigDecimal(2);
			    }else
			    	LOGGER.debug("Inside the else block for getbalance in modify");
			    accountBalanceCr=accountBalanceCr.add(oldAmt);
				LOGGER.debug("accountBalanceCr(if a/c same): "+accountBalanceCr);
			}
			catch(SQLException e){throw taskExc;}
			finally{
				try{
					rs.close();
					st.close();
				}catch(Exception e){LOGGER.error("finally block getAccountBalanceInModify");}
			}
			accountBalanceCr=accountBalanceCr.setScale(2, BigDecimal.ROUND_HALF_UP);
			LOGGER.debug("Before return accountBalanceCr :"+accountBalanceCr);
			return accountBalanceCr;
		}

	 public String getBillNumber() throws TaskFailedException,SQLException
	 {
		 throw new TaskFailedException("Method Not Supported Exception");
	 }

	 /**
	  * This method will insert/update the informations to egf_vouchermis table based on the value of mode.
	  * @param conn
	  * @param mode
	  * @param voucherheaderid
	  * @param field
	  * @throws TaskFailedException
	  */
	 public void updateVoucherMIS(String mode,Connection conn,int voucherheaderid,String field,String fundSourceId,String schemeid,String subschemeid,String vdt) throws TaskFailedException
	 {
		if(schemeid!=null && schemeid.length()>0) { validateScheme(vdt,schemeid,conn);}
		if(subschemeid != null && subschemeid.length()>0){ validatesubScheme(vdt,subschemeid,conn);}
	 	LOGGER.debug("Inside the VoucherMIS update Fn()..");
	 	String strVHID=voucherheaderid+"";
	 	VoucherMIS misobj=new VoucherMIS();
	 	misobj.setDivisionId(field);
	 	misobj.setVoucherheaderid(strVHID);
	 	misobj.setFundsourceid(fundSourceId);
		misobj.setScheme(schemeid);
	 	misobj.setSubscheme(subschemeid);
		if ("I".equals(mode)){
	 		misobj.insert(conn);}
	 	else if("U".equals(mode)){
	 		misobj.update(conn);
	 	}
	 }
	 /**
	  * This method will insert/update the informations to egf_vouchermis table based on the value of mode.
	  * @param conn
	  * @param mode
	  * @param voucherheaderid
	  * @param field
	  * @throws TaskFailedException
	  */
	 public void updateVoucherMIS(String mode,Connection conn,int voucherheaderid,String field,String fundSourceId,String schemeid,
			 String subschemeid,String vdt,String departmentId,String functionary,String sourcepath) throws TaskFailedException
	 {
		if(schemeid!=null && schemeid.length()>0) { validateScheme(vdt,schemeid,conn);}
		if(subschemeid != null && subschemeid.length()>0){ validatesubScheme(vdt,subschemeid,conn);}
	 	LOGGER.debug("Inside the VoucherMIS update Fn()..");
	 	String strVHID=voucherheaderid+"";
	 	VoucherMIS misobj=new VoucherMIS();
	 	misobj.setDivisionId(field);
	 	misobj.setVoucherheaderid(strVHID);
	 	misobj.setFundsourceid(fundSourceId);
		misobj.setScheme(schemeid);
	 	misobj.setSubscheme(subschemeid);
	 	misobj.setDepartmentId(departmentId);
	 	misobj.setFunctionary(functionary);
	 	misobj.setSourcePath(sourcepath);
	 	if("I".equals(mode)){
	 		misobj.insert(conn);}
	 	else if("U".equals(mode)){
	 		misobj.update(conn);
	 	}
	 }	  
      /*
       * input finYear, type is dummy or empty string
       */
         public String getFinYearID(String finYear,Connection con,String type) throws SQLException,TaskFailedException
	     {
	        String finyearid="";
	        Statement statement=null;
            ResultSet rs=null;
            try{
            	if(!StringUtils.isNotBlank(finYear)){
            		throw new TaskFailedException("financialyear is empty or null");
            	}
            	String sql="select ID as \"financialYearID\" from financialyear where financialyear='"+finYear+"'";
	            LOGGER.debug(sql);
	            statement=con.createStatement();
	            rs=statement.executeQuery(sql);
	            if(rs.next()){
	            	finyearid=rs.getString("financialYearID");
	            }
            }catch(Exception e){
            	throw taskExc;
            }finally{
            	try{
            		rs.close();
            		statement.close();
            	}catch(Exception e){LOGGER.error("getFinYearID here");}
            }
	        return finyearid;
	 }

	public int getDetailTypeId(String glCode,Connection connection) throws TaskFailedException,SQLException
	{
			int detailTypeId=0;
			ResultSet rs=null;
			Statement st=null;
			try{
				st=connection.createStatement();
				String qryDetailType="Select detailtypeid from chartofaccountdetail where glcodeid=(select id from chartofaccounts where glcode='"+glCode+"')";
				LOGGER.debug("  qryDetailType  "+qryDetailType);
				rs=st.executeQuery(qryDetailType);
				if(rs.next())
				{
					detailTypeId=rs.getInt(1);
				}
			}catch(Exception e){
            	throw taskExc;
            }finally{
            	try{
            		rs.close();
            		st.close();
            	}catch(Exception e){LOGGER.error("getDetailTypeId is here in finally..");}
            }
			return detailTypeId;
	}
	
	
	public int getDetailTypeIdById(String glCodeid,Connection connection) throws TaskFailedException,SQLException
	{
		int detailTypeId=0;
		ResultSet rs=null;
		Statement st=null;
		try{
			st=connection.createStatement();
			String qryDetailType="Select detailtypeid from chartofaccountdetail where glcodeid='"+glCodeid+"'";
			LOGGER.debug("  qryDetailType  "+qryDetailType);
			rs=st.executeQuery(qryDetailType);
			if(rs.next())
			{
				detailTypeId=rs.getInt(1);
			}
		}catch(Exception e){
        	throw taskExc;
        }finally{
        	try{
        		rs.close();
        		st.close();
        	}catch(Exception e){LOGGER.error("getDetailTypeIdById here");}
        }
		return detailTypeId;
	}
	
	
	public void cancelVouchers(String voucherHeaderId, Connection conn) throws TaskFailedException,SQLException,ParseException
	{
	    PreparedStatement ps=null;
		ResultSet rs=null;
		String today;
		VoucherHeader vh = new VoucherHeader();
		String getRefVoucher="SELECT a.id,a.vouchernumber,a.cgn FROM voucherheader a,voucherheader b "+
					"WHERE a.CGN=b.REFCGNO AND b.id=?";
		LOGGER.debug("getRefVoucher  "+getRefVoucher);
		try{
			ps=conn.prepareStatement(getRefVoucher);
			vh.setId(voucherHeaderId);
			egfRecordStatus egfstatus= new egfRecordStatus();
			SimpleDateFormat sdf =sdfFormatddMMyyyy;
			SimpleDateFormat formatter = dtFormat;
			EGovernCommon cm=new EGovernCommon();
			today=cm.getCurrentDate(conn);
	
			LOGGER.debug("Update the egf_record_status table of original voucher");
			egfstatus.setEffectiveDate(formatter.format(sdf.parse( today )));
			egfstatus.setStatus("4");
			egfstatus.setVoucherheaderId(voucherHeaderId);
			egfstatus.update(conn);
			LOGGER.debug("Update the original voucher");
			vh.setStatus(""+4);
			vh.update(conn);
	
			//Check if there is any related vouchers
			ps.clearParameters();
			ps.setString(1,voucherHeaderId);
			rs=ps.executeQuery();
			if(rs.next())
			{
				egfRecordStatus egfstatusRef= new egfRecordStatus();
				String refVhid=rs.getString(1);
				vh.setId(refVhid);
				egfstatusRef.setEffectiveDate(formatter.format(sdf.parse( today )));
				egfstatusRef.setStatus("4");
				egfstatusRef.setVoucherheaderId(refVhid);
				egfstatusRef.update(conn);
				vh.setStatus(""+4); LOGGER.debug("before voucher update");
				vh.update(conn);
			}
		}catch(Exception e){
        	throw taskExc;
        }finally{
        	try{
        		rs.close();
        		ps.close();
        	}catch(Exception e){LOGGER.error("cancel voucher in finally block");}
        }
	}
	
	public String getAccountdetailtypeAttributename(Connection connection) throws TaskFailedException,SQLException
	{
		ResultSet rs=null;
		Statement st=null;
		String retval="";
		StringBuffer attrName=new StringBuffer();
		try{
			st=connection.createStatement();
			String accQuery="select id,attributename from accountdetailtype where name='Creditor' ";
			LOGGER.debug("  accQuery  "+accQuery);
			rs=st.executeQuery(accQuery);
			if(rs.next())
			{
				attrName=attrName.append(rs.getString(1));
				attrName=attrName.append("#").append(rs.getString(2));
				LOGGER.debug(">>>attrName "+attrName);
			}
			}catch(Exception e)
			{
				LOGGER.error("Exception in payment:"+e);
				throw taskExc;
			}finally{
				try{
					rs.close();
					st.close();				
				}catch(Exception e){LOGGER.error("inside getAccountdetailtypeAttributename");}
			}
			retval=attrName.toString();
			return retval;
	}
	/**
	  * This method will retrieve the VoucherHeader Object based on the voucherheaderid passed
	  * @author Sapna
	  * @param conn
	  * @param vhId
	  * @return
	  */
	 public VoucherHeader getVoucherHeader(Connection conn,String vhId)throws TaskFailedException
	 {
	 	VoucherHeader vhrHdr = new VoucherHeader();
	 	Statement statement;
		ResultSet resultset;
		try
		{
			statement = conn.createStatement();
			resultset = statement.executeQuery("select type as \"recType\",cgn as \"cgn\",name as \"name\",vouchernumber as \"voucherNum\",status as \"status\",fundid as \"fundid\",lastmodifieddate as \"lastModDate\",createdby as \"createdby\" from voucherheader where id="+vhId);
			resultset.next();
			vhrHdr.setId(vhId);
			vhrHdr.setType(resultset.getString("recType"));
			vhrHdr.setCgn(resultset.getString("cgn"));
			vhrHdr.setName(resultset.getString("name"));
			vhrHdr.setVoucherNumber(resultset.getString("voucherNum"));
			vhrHdr.setStatus(resultset.getString("status"));
			vhrHdr.setFundId(resultset.getString("fundid"));
			vhrHdr.setLastModifiedDate(resultset.getString("lastModDate"));
			vhrHdr.setCreatedby(resultset.getString("createdby"));
			LOGGER.debug("vhrHdr type- "+ vhrHdr.getType() + " vhrHdr.getId()- "+vhrHdr.getId() );
			LOGGER.debug("vhrHdr.setCgn()-"+ vhrHdr.getCgn()+" vhrHdr.setName-"+vhrHdr.getName()+" vhrHdr.setVoucherNumber-"+ vhrHdr.getVoucherNumber() );
			LOGGER.debug(" vhrHdr.setStatus-"+ vhrHdr.getStatus()+ " vhrHdr.setFundId -"+ vhrHdr.getFundId()+" vhrHdr.setLastModifiedDate-  "+ vhrHdr.getlastModifiedBy() +" vhrHdr.setCreatedby()-"+ vhrHdr.getCreatedby());
			resultset.close();
			statement.close();
		}
		catch(SQLException sqlex)
		{
				LOGGER.debug(sqlex.getMessage(), sqlex);
				throw new TaskFailedException(sqlex.getMessage());
		}
	 	return vhrHdr;
	 }
	 /**
	       * This api returns the SQL date format .
	       * @author Iliyaraja
	       * @param bill_date
	       * @return
	       */
	      public String getSQLDateFormat(String bill_date)
	  	{
	  		SimpleDateFormat sdf =sdfFormatddMMyyyy;
	  		SimpleDateFormat formatter = dtFormat;
	  //		Date dt=new Date();
	  		String dateFormat = null;
	  		try
	  		{
	  //		dt = sdf.parse( bill_date );
	  		dateFormat = formatter.format(sdf.parse( bill_date ));
	  		LOGGER.debug("dateFormat ---"+ dateFormat);
}
	  		catch(Exception e)
	  		{
	  			LOGGER.error(e.getMessage(), e);
	
	  		}
	  		return dateFormat;
	  	}
	      /**
	       * This api returns the SQL date & time format .
	       * @author Iliyaraja
	       * @param bill_date
	       * @return
	       */
	  	public String getSQLDateTimeFormat(String bill_date)
	  	{
	  		SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	  		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
	  //		Date dt=new Date();
	  		String dateFormat = null;
	  		try
	  		{
	  	//	dt = sdf.parse(bill_date);
	  		dateFormat = formatter.format(sdf.parse(bill_date));
	  		LOGGER.debug("dateFormat ---"+ dateFormat);
	  		}
	  		catch(Exception e)
	  		{
	  			LOGGER.debug(e.getMessage(), e);
	  		}
	  		return dateFormat;
 	}
	  	
	  	/**
	  	 * This API returns the fiscialperiodid for the date passed
	  	 * @param vDate
	  	 * @param con
	  	 * @return
	  	 * @throws TaskFailedException
	  	 */
	  	public String getFiscalPeriod(final String vDate)throws TaskFailedException{
	  	
	  		
	  		
	  		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<String>()  {

				@Override
				public String execute(Connection con) throws SQLException {
					
					String fiscalPeriodID=null;
			  		String sql="select id from fiscalperiod  where '"+vDate+"' between startingdate and endingdate";
			  		
			  			Statement st=con.createStatement();
			  			ResultSet rset=st.executeQuery(sql);
			  			if(rset.next()){
			  				fiscalPeriodID=rset.getString(1);}
			  		
			  			return fiscalPeriodID;
				}
			});
	  		
	  		
	  	}
	  	
	  	/**
	  	 * This API returns the Name of the accountcode when passing the glcode.
	  	 * @param code
	  	 * @param con
	  	 * @return
	  	 * @throws TaskFailedException
	  	 */
	  	public String getCodeName(String code,Connection con) throws TaskFailedException
		{
			String name="";
			try{
				Statement st=con.createStatement();
				ResultSet rset=st.executeQuery("select name from chartofaccounts where glcode='"+code+"'");
				if(rset.next()){
					name=rset.getString(1);
				}
			}catch(Exception e){
				LOGGER.error("error  "+ e.toString());
				throw new TaskFailedException(e.getMessage());
			}
			return name;
		}
	  	 /**
	  	  * This function will return value if data!=null && data is not emtpy string;  
	  	  * else return defaultValue
	  	  * @param data
	  	  * @param defaultValue
	  	  * @return 
		  */
	  	
	  	
	  	
	  	public String getCodeNameById(String codeid,Connection con) throws TaskFailedException
		{
			String name="";
			try{
				Statement st=con.createStatement();
				ResultSet rset=st.executeQuery("select name from chartofaccounts where id='"+codeid+"'");
				if(rset.next()){
					name=rset.getString(1);
				}
			}catch(Exception e){
				LOGGER.error("error  "+ e.toString());
				throw new TaskFailedException(e.getMessage());
			}
			return name;
		}
	  	public String assignValue(String data,String defaultValue)
	  	{
	  		if(StringUtils.isNotBlank(data)){ return "'"+trimChar(formatString(data),"'".charAt(0))+"'";}
	  		else if(StringUtils.isNotBlank(defaultValue)){ return "'"+trimChar(defaultValue,"'".charAt(0))+"'";}
	  		else{ return defaultValue;}
	  	}
	  	
	  	/**
	  	 * this function trims ch in string
	  	 * @param str
	  	 * @param ch
	  	 * @return
	  	 */
	  	public String trimChar(String str,char ch)
	  	{
	  		Boolean b=true,e=true;
	  		str=str.trim();
	  		while (str.length()>0 && (b || e))
	  		{
	  			if (str.charAt(0) == ch) {
	  				str = str.substring(1, str.length());
				} else {
					b = false;
				}
	  			if(str.charAt(str.length()-1)==ch){
	  				str=str.substring(0,str.length()-1);
	  			} else{
	  				e=false;
	  			}
	  		}
	  		return str;
	  	}
	  	
	  	/**
		 * Return next available cheque number
		 * @param con
		 * @param accId
		 * @param noChqs--number cheque number need to be generated
		 * @return
		 * @throws TaskFailedException
		 */
		public String[] getNextChequeNo(Connection con,String accId,int noChqs,int allotid) throws TaskFailedException
		{
		/*	Map chqRange=new HashMap();
			long  availChqs[]=new long[noChqs+1];*/
			String strChqs[]=new String[noChqs+1];
		/*	Statement stmt=null;
			ResultSet rs=null;
			long availChqNo=0;
			int iNumChqBooks=0;
			
			String query="select fromchequenumber,tochequenumber  from egf_account_cheques where bankaccountid="+accId+" and allotedto="+allotid+" order by id";
			try{
				stmt= con.createStatement();
				LOGGER.debug(query);
				rs=stmt.executeQuery(query);
				while(rs.next())
				{
					iNumChqBooks++;
					Map temp=new HashMap();
					temp.put(FROMCHQNO,rs.getString(1));
					temp.put(TOCHQNO,rs.getString(2));
					chqRange.put(iNumChqBooks,temp);

				}
				rs.close();
				stmt.close();
				LOGGER.debug("chequeRange:"+chqRange);
				//decode if cheque number is null, it should be set to the first cheque number of the chq book
				//Also checking thhat the first character of the cheque number is a number
				query="select decode(max(to_number(cd.chequenumber)),null,?,max(to_number(cd.chequenumber))) as \"ChqNo\" from chequedetail cd where cd.accountnumberid="+accId
				+ " and cd.chequenumber between ? and ? and ascii(chequenumber)<=57 and ispaycheque='1' AND LENGTH(TO_CHAR(CD.CHEQUENUMBER))=LENGTH(TO_CHAR(?))"
				+ " UNION "
				+ " select decode(max(to_number(sc.chequenumber)),null,?,max(to_number(sc.chequenumber))) from eg_surrendered_cheques sc" 
				+ " where sc.bankaccountid="+accId+" and sc.chequenumber between ? and ? and ascii(chequenumber)<=57 AND LENGTH(TO_CHAR(sc.CHEQUENUMBER))=LENGTH(TO_CHAR(?)) order by \"ChqNo\" desc";
				PreparedStatement ps= con.prepareStatement(query);
				LOGGER.debug("maxChqNo query:"+query);
				int count=1;
				int j=1;
				LOGGER.debug("check1:"+j+":"+iNumChqBooks+":"+count+":"+noChqs);
				String toCheqeNo="",fromChequeNo="",StrAvailChqNo="";
				while(j<=iNumChqBooks && count<=noChqs)
				{
					LOGGER.debug("inside while:"+j+":"+iNumChqBooks+":"+count+":"+noChqs);
					int firstNo=Integer.parseInt(((HashMap)chqRange.get(j)).get(FROMCHQNO).toString())-1;
					fromChequeNo=((HashMap)chqRange.get(j)).get(FROMCHQNO).toString();
					toCheqeNo=((HashMap)chqRange.get(j)).get(TOCHQNO).toString();
					ps.setString(1,firstNo+"");
					ps.setString(2,((HashMap)chqRange.get(j)).get(FROMCHQNO).toString());
					ps.setString(3,((HashMap)chqRange.get(j)).get(TOCHQNO).toString());
					ps.setString(4,((HashMap)chqRange.get(j)).get(FROMCHQNO).toString());
					ps.setString(5,firstNo+"");
					ps.setString(6,((HashMap)chqRange.get(j)).get(FROMCHQNO).toString());
					ps.setString(7,((HashMap)chqRange.get(j)).get(TOCHQNO).toString());
					ps.setString(8,((HashMap)chqRange.get(j)).get(FROMCHQNO).toString());
					ResultSet res=ps.executeQuery();
					if(res.next()){
						availChqNo=Integer.parseInt(res.getString(1))+1;
					}
					LOGGER.debug("availChqNo:count:noChqs"+availChqNo+":"+Integer.parseInt(toCheqeNo)+":"+count+":"+noChqs);
					while((availChqNo<=Integer.parseInt(toCheqeNo)) && (count<=noChqs))
					{
						LOGGER.debug("availChqNo:count:noChqs"+availChqNo+":"+Integer.parseInt(toCheqeNo)+":"+count+":"+noChqs);
						LOGGER.debug("From Cheque No. len "+fromChequeNo.length()+" Available chq len.."+(availChqNo+"").length());
						StrAvailChqNo=availChqNo+"";
						int diff=(fromChequeNo.length()-(availChqNo+"").length());
						if(diff>0)						
							for(int k=0;k<diff;k++){
								StrAvailChqNo="0"+StrAvailChqNo;
							}						
						strChqs[count]=StrAvailChqNo;
						availChqs[count]=availChqNo;
						if(count!=noChqs)availChqNo++;
						count++;
					}
					j++;
					res.close();

				}
				ps.close();
				if(toCheqeNo !=null && !toCheqeNo.equals(""))
					LOGGER.debug(availChqNo+":"+Integer.parseInt(toCheqeNo));
				for(int k=1;k<=availChqs.length-1;k++){LOGGER.debug("chqs["+k+"]:"+availChqs[k]+" String cheque no :"+strChqs[k]);}
				if(toCheqeNo !=null && !toCheqeNo.equals(""))
				{
					if((count<noChqs) || (availChqNo>Integer.parseInt(toCheqeNo)))
					{ 
						//datacol.addValue("success",false); datacol.addMessage("eGovFailure","Sufficient no.of cheques are not available"); 
						throw new EGOVException("Sufficient no.of cheques are not available for this account");
					}
				}
			}
			catch(Exception e)
	    	{
	    		LOGGER.error("Exception in creating statement ");
	    		 throw new TaskFailedException("Exception in generating Cheque no:"+e.getMessage());
	    	}			
			//return availChqs;*/
			return strChqs;
		}
		
		public List getBankAndBranch(Connection con,String fundId) throws TaskFailedException,SQLException
		{
	        Statement st=null;
	        ResultSet rs=null;
	        String query=null; 
	        List bankBranchList=new ArrayList();
	        String fundCondition="";
	        if(fundId!=null && !fundId.equals(""))
	        	fundCondition=" and branch.id in(select branchid from bankaccount where fundid=" + fundId + ")";
	        
	    	query="select branch.ID as \"bankBranchId\", concat(concat(ba.name, ' - '),branch.branchName) as \"bankBranchName\" "
	    		+ " FROM bank ba, bankBranch branch WHERE branch.bankId=ba.ID AND ba.isActive='1' AND branch.isActive = '1' "
	    		+fundCondition+" order by LOWER(ba.name) ";
	    	LOGGER.debug("query:"+query);
	    	try 
	    	{    		
	    		st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);   	
	    		rs = st.executeQuery(query);
	    		LabelValueBean obj=null;
	    		if(rs!=null)
	    		{ 
	    			while(rs.next())
	    			{
	    				obj=new LabelValueBean();
	    				obj.setId(rs.getInt("bankBranchId"));
	    				obj.setName(rs.getString("bankBranchName"));
	    				bankBranchList.add(obj);
	    			}
	    		}
	    	}
	    	catch(Exception e)
	    	{
	    		LOGGER.error("Exception in creating statement:"+st);
	    		throw new TaskFailedException(e.getMessage()); 
	    	}
	    	finally
	        {
	        	rs.close();        	
	        }
	    	return bankBranchList;
		}
	  	
		public List getCashierNameList(Connection con) throws TaskFailedException,SQLException
		{
	        Statement st=null;
	        ResultSet rs=null;
	        String query=null; 
	        List cashierList=new ArrayList();
	    	query="SELECT id, name FROM billcollector WHERE lower(type) = 'cashier' and isactive='1'";
	    	LOGGER.debug("query:"+query);
	    	try 
	    	{    		
	    		st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);   	
	    		rs = st.executeQuery(query);
	    		LabelValueBean obj=null;
	    		if(rs!=null)
	    		{ 
	    			while(rs.next())
	    			{
	    				obj=new LabelValueBean();
	    				obj.setId(rs.getInt("ID"));
	    				obj.setName(rs.getString("NAME"));
	    				cashierList.add(obj);
	    			}
	    		}
	    	}
	    	catch(Exception e)
	    	{
	    		LOGGER.error("Exception in creating statement:"+st);
	    		throw new TaskFailedException(e.getMessage()); 
	    	}
	    	finally
	        {
	        	rs.close();        	
	        }
	    	return cashierList;
		}
		
		public String getBillCollectorName(Connection con, String billCollectorId) throws TaskFailedException,SQLException
		{
	        Statement st=null;
	        ResultSet rs=null;
	        String query=null; 
	        String name=null;
	    	query="SELECT name FROM billcollector WHERE id = "+billCollectorId;
	    	LOGGER.debug("query:"+query);
	    	try 
	    	{    		
	    		st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);   	
	    		rs = st.executeQuery(query);	    		
	    		if(rs!=null && rs.next() )
	    		{ 
    				name= rs.getString("NAME");	    				
	    		}
	    	}
	    	catch(Exception e)
	    	{
	    		LOGGER.error("Exception in creating statement:"+st);
	    		throw taskExc; 
	    	}
	    	finally
	        {
	        	rs.close();        	
	        }
	    	return name;
		}
		
	/*	public String getDeptIdFromVoucherMis(Connection con, String voucherId) throws TaskFailedException,SQLException
		{
	        Statement st=null;
	        ResultSet rs=null;
	        String query=null; 
	        String deptId=null;
	    	query="SELECT departmentid FROM vouchermis WHERE voucherheaderid = "+voucherId;
	    	LOGGER.debug("query:"+query);
	    	try 
	    	{    		
	    		st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);   	
	    		rs = st.executeQuery(query);	    		
	    		if(rs!=null && rs.next())
	    		{ 
    				deptId= rs.getString("DEPARTMENTID");	    				
	    		}
	    	}
	    	catch(Exception e)
	    	{
	    		LOGGER.error("Exception in creating statement:"+st);
	    		throw taskExc;
	    	}
	    	finally
	        {
	        	rs.close();        	
	        }
	    	return deptId;
		}*/
		
		/**
		 * checking for duplicate account code and Function Code Combination
		 * @param tableObj
		 * @param funIdIdx
		 * @param glCodeIdx
		 * @return true/false
		 */
		public boolean checkDuplicatesForFunction_AccountCode(DataCollection datacol,String[][] tableObj, int funIdIdx,int glCodeIdx){ 
			String  funcIdtemp1,glcodeTemp1,funcIdtemp2,glcodeTemp2;
			for(int i=0; i<tableObj.length ; i++){ 
				funcIdtemp1=tableObj[i][funIdIdx];
				glcodeTemp1=tableObj[i][glCodeIdx];
				if(funcIdtemp1.equalsIgnoreCase("")&&glcodeTemp1.equalsIgnoreCase(""))continue;
				if(glcodeTemp1.trim().equalsIgnoreCase("")) {datacol.addMessage(EXILRPERROR, "delete "+i+"th row in "+tableObj+" grid"); return false;}
				for(int j=i+1; j<tableObj.length; j++){ 
					funcIdtemp2=tableObj[j][funIdIdx];
					glcodeTemp2=tableObj[j][glCodeIdx];
					LOGGER.debug("funcIdtemp1:"+funcIdtemp1);
					LOGGER.debug("glcodeTemp1:"+glcodeTemp1);
					LOGGER.debug("funcIdtemp2:"+funcIdtemp2);
					LOGGER.debug("glcodeTemp2:"+glcodeTemp2);
					 if((glcodeTemp1.equalsIgnoreCase(glcodeTemp2))  && (funcIdtemp1.equalsIgnoreCase(funcIdtemp2) )){
						 datacol.addMessage(EXILRPERROR,"Same Account Code & Function Name can not appear more than once...CHECK ACCOUNT : " + glcodeTemp1);
						return false;
			  		}
			 	}
			}
			return true;
		}
		
		/**
		 * add blank row to the grid  at 0th position
		 * @param grid
		 * @return
		 */
		public String[][] insertBlankRow(String[][] grid)
		{
			int length=0;
			boolean hasFirstEmptyRow=true;
			if(grid.length>0)
				length = grid[0].length;
			else return grid;
			LOGGER.debug("entered file>>>");
			for(int i=0;i<length;i++)
			{
				if(!grid[0][i].trim().equalsIgnoreCase("")) { hasFirstEmptyRow=false; break;}
			}
			if(hasFirstEmptyRow) return grid;
			
			//adding  empty row
			String[][] newGrid=new String[grid.length+1][length];
			for(int i=0;i<length;i++){
				newGrid[0][i]="" ;
			
			}
			for(int i=0;i<grid.length;i++){
				for(int j=0;j<length;j++){
					newGrid[i+1][j]=grid[i][j] ;
				}
			}
			return newGrid;
		}
		
		//to Get Scheme List for Particular Fund
		public List getSchemeList(Connection con, int fundId)throws TaskFailedException,SQLException
		{
	        Statement st=null;
	        ResultSet rs=null;
	        String query=null; 
	        List schemeList=new ArrayList();
	    	query="select id, name from scheme where fundid="+fundId+" order by name";
	    	LOGGER.debug("query:"+query);
	    	try 
	    	{    		
	    		st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);   	
	    		rs = st.executeQuery(query);
	    		LabelValueBean obj=null;
	    		if(rs!=null)
	    		{ 
	    			while(rs.next())
	    			{
	    				obj=new LabelValueBean();
	    				obj.setId(rs.getInt("ID"));
	    				obj.setName(rs.getString("NAME"));
	    				schemeList.add(obj);
	    			}
	    		}
	    	}
	    	catch(Exception e)
	    	{
	    		LOGGER.error("Exception in creating statement:"+st);
	    		throw new TaskFailedException(e.getMessage());
	    	}
	    	finally
	        {
	        	rs.close();
	        	st.close();
	        }
	    	return schemeList;
		}
		
		/**
		 * To get the EGW_STATUS id
		 * @param con
		 * @param moduleType
		 * @param description
		 * @return statusId
		 */
		public String getEGWStatusId(final String moduleType,final String description) throws TaskFailedException
		{
			return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<String>() {

				@Override
				public String execute(Connection con) throws SQLException {
					String statusId="0";
					Statement stmt = null;
					ResultSet rs = null;
					try
					{
						String sql = " select distinct id from egw_status where upper(moduletype)='"+moduleType.toUpperCase()+"' and upper(description)='"+description.toUpperCase()+"' ";
						LOGGER.debug("statement"+sql);
						stmt = con.createStatement();
						rs= stmt.executeQuery(sql);
						if(rs.next()){
							statusId =(rs.getString("id")==null)?"0":rs.getString("id");
						}

						LOGGER.debug("$$$$$$$$$$$$$$$$$$$statusId==="+statusId);
						if(statusId==null || statusId.equals("0")){
							throw taskExc;
						}
						
					}
					catch(Exception e)
					{
						LOGGER.error("Exception in getEGWStatusId=====:"+e.getMessage());
			    		
					}
					finally
					{
						try { if(rs!=null)rs.close(); }catch(Exception e){}
						try { if(stmt!=null)stmt.close(); }catch(Exception e){}
					}
					return statusId;
				}
			});
			
		
			
		}
		
		public String getVoucherNumber(String fundId,String type,String voucherno,Connection conn)throws TaskFailedException,SQLException
		{
 			if(type==null || type.equals(""))
				throw new TaskFailedException("Configuration setting for voucher numbering is not done");

			String vouchernumber="",fType="";
	 	 	Statement st;
	 	 	ResultSet rs;
	 	 	try
	 	 	{
		 	 	st = conn.createStatement();
		 	  	rs = st.executeQuery(FUNDIDNSQL+fundId);
	 			if(rs.next())
		 			fType = rs.getString("fund_identi");
	 			
	 			if(fType==null || fType.trim().equals("")){
	 				throw new TaskFailedException("Fund identiefier is null or empty");
	 			}
	 			if(rs!=null){
	 				rs.close();
	 			}
	 			
	 			vouchernumber = fType+type+voucherno;
	 	 	}
	 	 	catch(Exception e)
	 	 	{
	 	 		throw new TaskFailedException("Exp in getVoucherNumber()=="+e.getMessage());
	 	 	}
			return vouchernumber;
		}

		
		/**
		  * This function is used to delete a Journal from the system
		  * @param cgn
		  * @param con
		  * @throws TaskFailedException
		  */
		public void deleteJournal(String cgn,Connection con) throws TaskFailedException
		{
			try{
					Statement stmt=con.createStatement();
					Statement stmt1=con.createStatement();
					int vhid=0;
					ResultSet rs=stmt1.executeQuery("Select id from voucherheader where cgn='"+cgn+"'");
					if(rs.next())
						vhid=rs.getInt(1);
					else
						throw new TaskFailedException("Cannot find the journal voucher");
					rs.close();
					
					stmt.clearBatch();
					stmt.addBatch("Delete from egf_record_status where voucherheaderid="+vhid);
					stmt.addBatch("Delete from generalledgerdetail where generalledgerid in (select id from generalledger where voucherheaderid="+vhid+")");
					stmt.addBatch("Delete from generalledger where voucherheaderid="+vhid);
					stmt.addBatch("Delete from voucherdetail where voucherheaderid="+vhid);
					stmt.addBatch("Delete from vouchermis where voucherheaderid="+vhid);
					stmt.addBatch("Delete from vouchermis where voucherheaderid="+vhid);
					stmt.addBatch("Delete from voucherheader where id="+vhid);
					stmt.executeBatch();
			}catch(Exception e){
				LOGGER.error(e.getMessage(), e);
				throw taskExc;
			}
		}
		
	public static void  isCurDate(final String VDate) throws ValidationException{
			try {
				
			
			HibernateUtil.getCurrentSession().doWork(new Work() {
				
				@Override
				public void execute(Connection conn) throws SQLException {
					
					try{
						String today=getCurrentDate(conn);
						String[] dt2 = today.split("/");
						String[] dt1= VDate.split("/");
						int ret = (Integer.parseInt(dt2[2])>Integer.parseInt(dt1[2])) ? 1 : (Integer.parseInt(dt2[2])<Integer.parseInt(dt1[2])) ? -1 : (Integer.parseInt(dt2[1])>Integer.parseInt(dt1[1])) ? 1 : (Integer.parseInt(dt2[1])<Integer.parseInt(dt1[1])) ? -1 : (Integer.parseInt(dt2[0])>Integer.parseInt(dt1[0])) ? 1 : (Integer.parseInt(dt2[0])<Integer.parseInt(dt1[0])) ? -1 : 0 ;
						if(ret==-1 ){
							throw taskExc;
						}
					}catch(Exception ex){
						LOGGER.error("Exception in isCurDate "+ex);
						List<ValidationError> errors = new ArrayList<ValidationError>();
						errors.add(new ValidationError("date", "Date Should be within the today's date"));
						throw new ValidationException(errors);
						
					}
					
				}
			});
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
	
	
	 public static String getFinYearStartDate(final String endDate) throws Exception
     {
		
		 
		 	return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<String>() {
		 		 String startingDate;
				@Override
				public String execute(Connection con) throws SQLException {
					try
		            {
						
		                Statement stmt = con.createStatement();
		                ResultSet rs=null;
		                String query = "SELECT TO_CHAR(startingDate, 'dd-Mon-yyyy') AS \"startingDate\" " +
		                               "FROM financialYear WHERE startingDate <= '"+endDate+"' AND endingDate >= '"+endDate+"'";
		                LOGGER.info("query: "+query);
		                rs = stmt.executeQuery(query);
		                if(rs.next()){
		                	startingDate =   rs.getString("startingDate");
		                  
		                }
		                else
		                	throw new Exception("Reports not defined for this financial year");
		               rs.close();
		              
		            }
		            catch(Exception  ex)
		            {
		                LOGGER.error("Error in getting Starting date"+ex.getMessage());
		                // Throwing the same exception
		              
		            }
					return startingDate;
				}
				
			});
		 	 
     	
       
     }
}

