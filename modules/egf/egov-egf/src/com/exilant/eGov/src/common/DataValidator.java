/*
 * Created on Jan 27, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

//import java.sql.*;
/**
 * @author siddhu
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DataValidator {
	public boolean checkDepartmentId(String deptId,Connection connection){
		try{
		Statement statement=connection.createStatement();
		ResultSet rset=statement.executeQuery("select dept_name from eg_department " +
				" where id_dept="+deptId);
		if(!rset.next()){ return false;}
		statement.close();
		}catch(Exception e){return false;}
		return true;
	}
	public boolean checkFunctionId(String funcId,Connection connection){
		try{
		Statement statement=connection.createStatement();
		ResultSet rset=statement.executeQuery("select name from function " +
				" where id="+funcId);
		if(!rset.next()){ return false;}
		rset.close();
		statement.close();
		}catch(Exception e){return false;}
		return true;
	}
	public boolean checkDepartmentName(String deptName,Connection connection){
		try{
			Statement statement=connection.createStatement();
			ResultSet rset=statement.executeQuery("select dept_name from eg_department" +
					" where dept_name='"+deptName+"'");
			if(!rset.next()){ return false;}
			statement.close();
			}catch(Exception e){return false;}
			return true;
	}
	public boolean checkFunctionName(String funcName,Connection connection){
		try{
			Statement statement=connection.createStatement();
			ResultSet rset=statement.executeQuery("select name from function" +
					" where name='"+funcName+"'");
			if(!rset.next()){return false;}
			statement.close();
			}catch(Exception e){return false;}
			return true;
	}
	public boolean checkOrganizationStructureId(String orgId,Connection connection){
		try{

			Statement statement=connection.createStatement();

			ResultSet rset=statement.executeQuery("select name from " +
					" organizationStructure where id="+orgId);
			if(!rset.next()){ return false;}

			statement.close();
			}catch(Exception e){return false;}
			return true;
	}
	public boolean taxCode(String taxCode,Connection connection){
		try{
			Statement statement=connection.createStatement();
			ResultSet rset=statement.executeQuery("select name from taxes " +
					" where code='"+taxCode+"'");
			if(!rset.next()){ return false;}
			statement.close();
			}catch(Exception e){return false;}
			return true;
	}
	public boolean checkBankAccount(String branchId,String accNumber,Connection connection){
		try{
			Statement statement=connection.createStatement();
			ResultSet rset=statement.executeQuery("select id from bankAccount  where branchId="+branchId);
					//" where branchId="+branchId+" and accountNumber='"+accNumber+"'");
			if(!rset.next()){ return false;}
			statement.close();
			}catch(Exception e){return false;}
			return true;
	}
	public boolean checkAccount(String id,Connection connection){
		try{
			Statement statement=connection.createStatement();
			ResultSet rset=statement.executeQuery("select id from bankAccount  where id="+id);
					//" where branchId="+branchId+" and accountNumber='"+accNumber+"'");
			if(!rset.next()){ return false;}
			statement.close();
			}catch(Exception e){return false;}
			return true;
	}
	public boolean checkBank(String bankId,Connection connection){
		try{
			Statement statement=connection.createStatement();
			ResultSet rset=statement.executeQuery("select name from bank " +
					" where id="+bankId);
			if(!rset.next()){ return false;}
			statement.close();
			}catch(Exception e){return false;}
			return true;
	}
	public boolean checkFundName(String fundName,Connection connection){
		try{
			Statement statement=connection.createStatement();
			ResultSet rset=statement.executeQuery("select name from fund where " +
					" name='"+fundName+"'");
			if(!rset.next()){ return false;}
			statement.close();
			}catch(Exception e){return false;}
			return true;
	}
	public boolean checkFundId(String fundId,Connection connection){
		try{
			Statement statement=connection.createStatement();
			ResultSet rset=statement.executeQuery("select name from fund where" +
					" id="+fundId);
			if(!rset.next()){ return false;}
			statement.close();
			}catch(Exception e){return false;}
			return true;
	}
	public boolean checkFundSourceName(String fundSourceName,Connection connection){
		try{
			Statement statement=connection.createStatement();
			ResultSet rset=statement.executeQuery("select name from fundsource where name='"+fundSourceName+"'");
			if(!rset.next()){ return false;}
			statement.close();
			}catch(Exception e){return false;}
			return true;
	}
	public boolean checkFundSourceId(String fundSourceId,Connection connection){
		try{
			Statement statement=connection.createStatement();
			ResultSet rset=statement.executeQuery("select name from fundsource where" +
					" id="+fundSourceId);
			if(!rset.next()){ return false;}
			statement.close();
			}catch(Exception e){return false;}
			return true;
	}
	public boolean checkSupplierId(String supplierId,Connection connection){
		try{
			Statement statement=connection.createStatement();
			ResultSet rset=statement.executeQuery("select name from supplier" +
					"  where id="+supplierId);
			if(!rset.next()){ return false;}
			statement.close();
			}catch(Exception e){return false;}
			return true;
	}
	public boolean checkContractorId(String contractorId,Connection connection){
		try{
			Statement statement=connection.createStatement();
			ResultSet rset=statement.executeQuery("select name from contractor " +
					" where id="+contractorId);
			if(!rset.next()){ return false;}
			statement.close();
			}catch(Exception e){return false;}
			return true;
	}
	public boolean checkBillCollectorId(String id,String type,Connection connection){
		try{
			Statement statement=connection.createStatement();
			ResultSet rset=statement.executeQuery("select name from billCollector " +
					" where id="+id+" and type='"+type+"'");

			if(!rset.next()){ return false;}
			statement.close();
			}catch(Exception e){return false;}
			return true;
	}
}
