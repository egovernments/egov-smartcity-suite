<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>

<%@page import="org.hibernate.jdbc.ReturningWork"%>
<%@ page language="java" contentType="text/plain; charset=UTF-8" pageEncoding="UTF-8"
import="org.egov.infstr.security.utils.SecurityUtils,java.sql.*,java.util.HashMap,java.util.Date,java.text.*,
org.egov.infstr.utils.*,org.egov.infstr.utils.HibernateUtil,org.egov.infra.exception.ApplicationRuntimeException" %>
<%!org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger("Process.jsp");

public String processResultSet(ResultSet rs) {
	int i = 0;
	StringBuffer result = new StringBuffer();
	try {
		while(rs.next()){
			if(i > 0) {
				result.append("+");
				result.append(rs.getString("code"));
			} else {
				result.append(rs.getString("code"));
				i++;
			}
		}
		result.append("^");
	} catch (Exception e) {
		LOG.error(e);
	}	
	return result.toString();
}

public String simpleExecute(final String query) {
	return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<String>() {
		public String execute (Connection con) {
			try {
				PreparedStatement stmt =con.prepareStatement(query);
				return processResultSet(stmt.executeQuery());
			} catch (Exception e) {
				LOG.error(e);
				throw new ApplicationRuntimeException("Error occurred while executing SQL statement",e);
			}	
		}
	});
}

public String dataExist(final String query, final String ... params) {
	return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<String>() {
		public String execute (Connection con) {
			try {
				PreparedStatement stmt =con.prepareStatement(query);
				for (int i = 0;i<params.length;i++) {
					stmt.setString(i+1, params[i]);
				}
				return String.valueOf(stmt.executeQuery().next());
			} catch (Exception e) {
				LOG.error(e);
				throw new ApplicationRuntimeException("Error occurred while executing SQL statement",e);
			}	
		}
	});
}

public String executeWithParam(final String query, final String ... params) {
	return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<String>() {
		public String execute (Connection con) {
			try {
				PreparedStatement stmt =con.prepareStatement(query);
				for (int i = 0;i<params.length;i++) {
					stmt.setString(i+1, params[i]);
				}
				return processResultSet(stmt.executeQuery());
			} catch (Exception e) {
				LOG.error(e);
				throw new ApplicationRuntimeException("Error occurred while executing SQL statement",e);
			}	
		}
	});
}%>
<%
    String result="";
try {
	String type = request.getParameter("type"); 
	if(type.equalsIgnoreCase("coaSubMinorCode")) { //TESTED
		final String accountCode = SecurityUtils.checkSQLInjection(request.getParameter("id"));
		final String classValue = SecurityUtils.checkSQLInjection(request.getParameter("classification"));
		final String query="select glcode as \"code\" from chartofaccounts where classification=? and glcode like ? order by glcode ";
		result = executeWithParam(query,classValue,accountCode+"%");		
	} else if (type.equalsIgnoreCase("getAllCoaCodes")) {//TESTED
		final String query="select glcode||'`-`'||name||'`-`'||ID as \"code\" from chartofaccounts where classification=4 and isActiveForPosting=true order by glcode ";
		result = simpleExecute(query);
	} else if(type.equalsIgnoreCase("getAllCoaNames")){//TESTED
		final String query="select name||'`-`'||glcode||'`-`'||ID as \"code\" from chartofaccounts where classification=4 and isActiveForPosting=true order by glcode ";
		result = simpleExecute(query);
	} else if(type.equalsIgnoreCase("getAllAssetCodes")){//TESTED
		final String query="select glcode||'`-`'||name as \"code\" from chartofaccounts where classification=4 and isActiveForPosting=true and type = 'A' order by glcode ";
		result = simpleExecute(query);
	} else if(type.equalsIgnoreCase("getAllLiabCodes")){//TESTED
		final String query="select glcode||'`-`'||name as \"code\" from chartofaccounts where classification=4 and isActiveForPosting=true and type = 'L' order by glcode ";
		result = simpleExecute(query);
	} else if(type.equalsIgnoreCase("coaDetailCode")){ //TESTED
		String accountCode= SecurityUtils.checkSQLInjection(request.getParameter("glCode"));
		String query="select glcode as \"code\" from chartofaccounts where classification=4 and isActiveForPosting=true and glcode like ? order by glcode ";
		result = executeWithParam(query,accountCode+"%");
	} else if(type.equalsIgnoreCase("coaDetailCodeType")){//TESTED
		String accountCode= SecurityUtils.checkSQLInjection(request.getParameter("glCode"));
		String typeClass= SecurityUtils.checkSQLInjection(request.getParameter("typeClass"));
		String query="select glcode as \"code\" from chartofaccounts where classification=4 and isActiveForPosting=true and glcode like ? and type = ? order by glcode ";
		result = executeWithParam(query,accountCode+"%",typeClass);
	} else if(type.equalsIgnoreCase("functionName")){//TESTED
		String functionCode= SecurityUtils.checkSQLInjection(request.getParameter("name"));
		String query="select name as \"code\" from function where  isactive = true AND isnotleaf=false and upper(name) like upper(?)  order by name ";
		result = executeWithParam(query,functionCode+"%");
	} else if(type.equalsIgnoreCase("getAllFunctionName")){//TESTED
		final String query="select name||'`-`'||id as \"code\" from function where  isactive = true AND isnotleaf=false order by name ";
		result = simpleExecute(query);
	}  else if(type.equalsIgnoreCase("getAllFunctionCode")){
		final String query="select code||'`~`'||name||'`~~`'||id as \"code\" from function where  isactive = true AND isnotleaf=false order by name ";
		result = simpleExecute(query);
	}else if(type.equalsIgnoreCase("getAllBankName")){//TESTED
		final String query="select name||'`-`'||id as \"code\" from bank where  isactive = true order by name ";
		result = simpleExecute(query);
	} else if(type.equalsIgnoreCase("contractorName")){//TESTED
 		final String query="select name||'`-`'||code as \"code\" from relation where id in(select relationid from worksdetail where totalvalue>0 and isactive=true) and isactive=true and relationTypeid=2  order by upper(\"code\") ";
 		result = simpleExecute(query);
	} else if(type.equalsIgnoreCase("supplierName")){//TESTED
		final String query="select name||'`-`'||code as \"code\" from relation where id in(select relationid from worksdetail where totalvalue>0 and isactive=true) and isactive=true and relationTypeid=1  order by upper(\"code\") ";
		result = simpleExecute(query);
	} else if(type.equalsIgnoreCase("workDetailName")){//TESTED
		final String relTypeId= SecurityUtils.checkSQLInjection(request.getParameter("relationTypeId"));
		final String relationId= SecurityUtils.checkSQLInjection(request.getParameter("relationId"));
		final String query="select ' '||wd.name||'`-`'||wd.code as \"code\" from worksdetail wd,relation r where wd.relationid=r.id and r.relationtypeid=? and r.isactive=true and wd.relationid=? order by upper(wd.name) ";
		result = executeWithParam(query,relTypeId,relationId);
	} else if(type.equalsIgnoreCase("getGLreportCodes")){//TESTED
		final String query="SELECT concat(concat(concat(concat(glCode,'`-`'), name),'-$-'), ID) as \"code\" FROM chartofaccounts WHERE glcode not in (select glcode from chartofaccounts where glcode like '47%' AND glcode not like '471%' AND glcode !='4741') "+
		" AND glcode not in (select glcode from chartofaccounts where glcode='471%') AND isactiveforposting=true AND classification=4 ORDER BY glcode ";
		result = simpleExecute(query);
	} else if(type.equalsIgnoreCase("getActiveContractorListwithCode")){//TESTED
		final String query="SELECT   NAME || '`--`' || code  || '`-`' ||ID  AS \"code\"  FROM relation  WHERE relationtypeid = 2 AND isactive = true ORDER BY NAME";
		result = simpleExecute(query);
	} else if(type.equalsIgnoreCase("getActiveSupplierListwithCode")){//TESTED
		final String query="SELECT   NAME || '`--`' || code  || '`-`' ||ID  AS \"code\"  FROM relation  WHERE relationtypeid = 1 AND isactive = true ORDER BY NAME";
		result = simpleExecute(query);
	} else if(type.equalsIgnoreCase("getBankGlcode")){//TESTED
		// For Dishonored cheque fill Bank Charges glcode and Receipt reversal Chque glcode
		final String query="select ca.glcode || '`--`' || ca.name || '`--`' || ca.id as \"code\" "
		+" from Chartofaccounts ca where purposeid=30 ORDER BY ca.glcode";
		result = simpleExecute(query);
	} else if(type.equalsIgnoreCase("getUserByEnteringCode")){//TESTED -- CODE FISH
		final String userName= SecurityUtils.checkSQLInjection(request.getParameter("code"));
		final java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
		String includeRolesList = EGovConfig.getProperty("INCLUDE_ROLES","","IP-BASED-LOGIN");
		final String[] roleNames = includeRolesList.split(",");
		final StringBuilder query = new StringBuilder();
		query.append("SELECT DISTINCT userimpl0_.user_name|| '`--`' ||userimpl0_.id_user as \"code\" ");
		query.append("FROM eg_user userimpl0_ LEFT OUTER JOIN eg_userrole userroles1_ ON ");
		query.append("userimpl0_.id_user = userroles1_.userid  LEFT OUTER JOIN eg_role roleimpl2_ ON ");
		query.append("userroles1_.roleid = roleimpl2_.id WHERE (roleimpl2_.name IN (");
		for(String ary : roleNames) {
			query.append("?,");
		}
		query.deleteCharAt(query.length() - 1);
		query.append(")) AND userimpl0_.isactive = true AND userimpl0_.USER_NAME=? ");
		query.append("AND ((userroles1_.todate IS NULL) AND userroles1_.fromdate <= ? OR userroles1_.fromdate <= ? AND ");
		query.append("userroles1_.todate >= ?) AND ((userimpl0_.todate IS NULL) AND userimpl0_.fromdate <=? ");
		query.append("OR userimpl0_.fromdate <=? AND userimpl0_.todate >=?) ");
		result = HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<String>() {
			public String execute (Connection con) {
				try {
					PreparedStatement stmt =con.prepareStatement(query.toString());
					int i = 1;
					for(String roleName : roleNames) {
						stmt.setString(i++,roleName);
					}
					stmt.setString(i++,userName);
					stmt.setDate(i++,currentDate);
					stmt.setDate(i++,currentDate);
					stmt.setDate(i++,currentDate);
					stmt.setDate(i++,currentDate);
					stmt.setDate(i++,currentDate);
					stmt.setDate(i++,currentDate);		
					return processResultSet(stmt.executeQuery());
				} catch (Exception e) {
					LOG.error(e);
					throw new ApplicationRuntimeException("Error occurred while executing SQL statement",e);
				}	
			}
		});
	} else if(type.equalsIgnoreCase("getAllUserNames")) {//TESTED
		final java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
		final String includeRolesList = EGovConfig.getProperty("INCLUDE_ROLES","","IP-BASED-LOGIN");
		final String[] roleNames = includeRolesList.split(",");
		final StringBuilder query = new StringBuilder();
		query.append("SELECT DISTINCT userimpl0_.user_name|| '`--`' ||userimpl0_.id_user as \"code\" ");
		query.append("FROM eg_user userimpl0_ LEFT OUTER JOIN eg_userrole userroles1_ ON ");
		query.append("userimpl0_.userid = userroles1_.id_user  LEFT OUTER JOIN eg_role roleimpl2_ ON ");
		query.append("userroles1_.roleid = roleimpl2_.id WHERE roleimpl2_.name IN (");
		for(String ary : roleNames) {
			query.append("?,");
		}
		query.deleteCharAt(query.length() - 1);
		query.append(") AND userimpl0_.isactive = true AND ((userroles1_.todate IS NULL) ");
		query.append("AND userroles1_.fromdate <= ? OR userroles1_.fromdate <= ? AND ");
		query.append("userroles1_.todate >= ?) AND ((userimpl0_.todate IS NULL) AND userimpl0_.fromdate <=? ");
		query.append("OR userimpl0_.fromdate <=? AND userimpl0_.todate >= ?) ");
		result = HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<String>() {
			public String execute (Connection con) {
				try {
					PreparedStatement stmt =con.prepareStatement(query.toString());
					int i = 1;
					for(String roleName : roleNames) {
						stmt.setString(i++,roleName);
					}
					stmt.setDate(i++,currentDate);
					stmt.setDate(i++,currentDate);
					stmt.setDate(i++,currentDate);
					stmt.setDate(i++,currentDate);
					stmt.setDate(i++,currentDate);
					stmt.setDate(i++,currentDate);		
					return processResultSet(stmt.executeQuery());
				} catch (Exception e) {
					LOG.error(e);
					throw new ApplicationRuntimeException("Error occurred while executing SQL statement",e);
				}	
			}
		});
	} else if(type.equalsIgnoreCase("getAllBoundary")){//TESTED
		final String btypeId= SecurityUtils.checkSQLInjection(request.getParameter("btypeId"));
		final String query="SELECT  ID_BNDRY,NAME    FROM EG_BOUNDARY b  WHERE b.IS_HISTORY='N' and b.ID_BNDRY_TYPE=? ORDER BY ID_BNDRY";
		result = HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<String>() {
			public String execute (Connection con) {
				try {
					PreparedStatement stmt =con.prepareStatement(query);
					stmt.setString(1,btypeId);
				    ResultSet rs = stmt.executeQuery();
				    StringBuffer id=new StringBuffer();
					StringBuffer names=new StringBuffer();
					int i = 0;
					while(rs.next()){
						if(i > 0) {
							id.append("+");
							id.append(rs.getString(1));
							names.append("+");
							names.append(rs.getString(2));
						} else {
							id.append(rs.getString(1));
							names.append(rs.getString(2));
							i++;
						}
					}
					return id.append("^").append(names).append("^").toString();
				} catch (Exception e) {
					LOG.error(e);
					throw new ApplicationRuntimeException("Error occurred while executing SQL statement",e);
				}	
			}
		});	
	} else if(type.equalsIgnoreCase("getChqueGlcode")){//TESTED
		final String vouchHeaderId = SecurityUtils.checkSQLInjection(request.getParameter("vouchHeaderId"));
		final String query="select gl.glcode || '`-`' || ca.name || '`-`' || gl.glcodeid as \"code\"  from generalledger gl,Chartofaccounts ca "
		+" WHERE gl.glcodeid=ca.id and gl.creditamount>0 and gl.VOUCHERHEADERID=?  ORDER BY gl.glcode";
		result = executeWithParam(query,vouchHeaderId);
	} else if(type.equalsIgnoreCase("getBillNo")){//TESTED NOT FUNCTIONAL FOR NEW SCHEMA		
		final String query=" SELECT eb.billnumber||'`-`'||v.id AS code FROM VOUCHERHEADER v,OTHERBILLDETAIL ob,EG_BILLREGISTER eb"+
          " WHERE v.name='Contingency Journal' AND v.id NOT IN"+
          "(SELECT m.BILLVHID FROM MISCBILLDETAIL m, VOUCHERHEADER vh ,PAYMENTHEADER p WHERE m.billvhid IS NOT NULL "+
          " AND (m.isreversed=0 OR m.isreversed IS NULL) AND p.VOUCHERHEADERID=vh.id  AND p.MISCBILLDETAILID=m.id AND"+
          " vh.STATUS!=4 )   AND v.status=0 AND v.id = ob.voucherheaderid AND ob.billid =eb.id";
		if((request.getParameter("mode").equalsIgnoreCase("paymentBank"))|| (request.getParameter("mode").equalsIgnoreCase("paymentCash"))) { 
			result = simpleExecute(query);
	    } else { 
	    	String queryedit=" SELECT eb.billnumber||'`-`'||v.id AS code FROM VOUCHERHEADER v,OTHERBILLDETAIL ob,EG_BILLREGISTER eb"+
	                  " WHERE v.name='Contingency Journal' AND v.id IN"+
	                  "(SELECT m.BILLVHID FROM MISCBILLDETAIL m, VOUCHERHEADER vh ,PAYMENTHEADER p WHERE m.billvhid IS NOT NULL "+
	                  " AND (m.isreversed=0 OR m.isreversed IS NULL) AND p.VOUCHERHEADERID=vh.id  AND p.MISCBILLDETAILID=m.id AND"+
          " vh.STATUS!=4 and vh.cgn = ?)   AND v.status=0 AND v.id = ob.voucherheaderid AND ob.billid =eb.id";
	    	String cgn = SecurityUtils.checkSQLInjection(request.getParameter("cgn"));
	    	result = executeWithParam(query+" union "+queryedit,cgn);
       	}
	} else if(type.equalsIgnoreCase("checkJurisdictionDates")){//TESTED
		String fDate = SecurityUtils.checkSQLInjection(request.getParameter("fDate"));
		String tDate = SecurityUtils.checkSQLInjection(request.getParameter("tDate"));
		String Id = SecurityUtils.checkSQLInjection(request.getParameter("Id"));
		String query="SELECT  NAME  AS \"code\"  FROM EG_BOUNDARY b  WHERE b.IS_HISTORY='N' and b.ID_BNDRY=? and  "+
		"((b.TODATE IS NULL and b.FROMDATE <= TO_Date(?, 'DD/MM/YYYY')) "+
		"OR (b.FROMDATE<= TO_Date(?, 'DD/MM/YYYY') "+
		"AND b.TODATE>=TO_Date(?, 'DD/MM/YYYY'))) ORDER BY ID_BNDRY";
		result = dataExist(query,Id,fDate,fDate,tDate);
	} else if(type.equalsIgnoreCase("checkBaseUrlwithQueryParam")){//TESTED
		String baseURLObj1 = SecurityUtils.checkSQLInjection(request.getParameter("baseURLObj1"));
		String queryParamsObj1 = SecurityUtils.checkSQLInjection(request.getParameter("queryParamsObj1"));
		String query="SELECT  NAME  AS \"code\"  FROM EG_ACTION a  WHERE a.URL=? and a.QUERYPARAMS=? ";
		result = dataExist(query,baseURLObj1,queryParamsObj1);
	} else if(type.equalsIgnoreCase("checkWithChildAndParentBoudary")){//TESTED
		String heirarchyType = SecurityUtils.checkSQLInjection(request.getParameter("heirarchyType"));
		String boundaryName = SecurityUtils.checkSQLInjection(request.getParameter("boundaryName"));
		String boudaryId = SecurityUtils.checkSQLInjection(request.getParameter("boudaryId"));
		String query="SELECT  NAME  AS \"code\"  FROM EG_BOUNDARY_TYPE b WHERE b.ID_HEIRARCHY_TYPE=? and b.NAME=? and b.ID_BNDRY_TYPE!=? ";
		result = dataExist(query,heirarchyType,boundaryName,boudaryId);
	} else if(type.equalsIgnoreCase("checkingChildForParent")){//TESTED
		String heirarchyType = SecurityUtils.checkSQLInjection(request.getParameter("heirarchyType"));
		String bndryTypeId = SecurityUtils.checkSQLInjection(request.getParameter("bndryTypeId"));
		String query="SELECT  NAME  AS \"code\"  FROM EG_BOUNDARY_TYPE b WHERE b.ID_HEIRARCHY_TYPE=? and b.PARENT=? ";
		result = dataExist(query,heirarchyType,bndryTypeId);	
	} else if (type.equalsIgnoreCase("createOnlyForRootNode")){//TESTED
		final String heirarchyType = SecurityUtils.checkSQLInjection(request.getParameter("heirarchyType"));
		final String boundaryType = SecurityUtils.checkSQLInjection(request.getParameter("boundaryType"));
		final String query="SELECT  PARENT  AS \"code\"  FROM EG_BOUNDARY_TYPE b WHERE b.ID_HEIRARCHY_TYPE=? and b.ID_BNDRY_TYPE=? ";
		result = HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<String>() {
			public String execute (Connection con) {
				try {
					PreparedStatement stmt =con.prepareStatement(query);
					 stmt.setString(1,heirarchyType);
				    stmt.setString(2,boundaryType);
				    ResultSet rs = stmt.executeQuery();
				    if(rs.next()) {
						int parentId=rs.getInt("code");
						if(parentId==0) {
							return "true";
						} else {
							return "false";
						}
					} else {
						return "false";
					}
				} catch (Exception e) {
					LOG.error(e);
					throw new ApplicationRuntimeException("Error occurred while executing SQL statement",e);
				}	
			}
		});
	} else if(type.equalsIgnoreCase("checkForUniqueHeirarchyName")){//TESTED
		String name = SecurityUtils.checkSQLInjection(request.getParameter("name"));
		String heirarchyID = SecurityUtils.checkSQLInjection(request.getParameter("heirarchyID"));
		String query="SELECT  TYPE_NAME  AS \"code\"  FROM EG_HEIRARCHY_TYPE ht WHERE ht.TYPE_NAME=? and ht.ID_HEIRARCHY_TYPE!=? ";
		result = dataExist(query,name,heirarchyID);
	} else if(type.equalsIgnoreCase("checkForUniqueHeirarchyCode")){//TESTED
		String code = SecurityUtils.checkSQLInjection(request.getParameter("code"));
		String heirarchyID = SecurityUtils.checkSQLInjection(request.getParameter("heirarchyID"));
		String query="SELECT  TYPE_NAME  AS \"code\"  FROM EG_HEIRARCHY_TYPE ht WHERE ht.TYPE_CODE=? and ht.ID_HEIRARCHY_TYPE!=? ";
		result = dataExist(query,code,heirarchyID);
	} else if(type.equalsIgnoreCase("checkForUniqueDeptName")){//TESTED
		String deptName = SecurityUtils.checkSQLInjection(request.getParameter("deptName"));
		String deptid = SecurityUtils.checkSQLInjection(request.getParameter("deptid"));
		String query="SELECT DEPT_NAME AS \"code\"  FROM EG_DEPARTMENT dt WHERE dt.DEPT_NAME=? and dt.ID_DEPT!=? ";
		result = dataExist(query,deptName,deptid);
	} else if(type.equalsIgnoreCase("checkForUniqueDesignationName")){//TESTED
		String designationName = SecurityUtils.checkSQLInjection(request.getParameter("designationName"));
		String designationId = SecurityUtils.checkSQLInjection(request.getParameter("designationId"));
		String query="SELECT DESIGNATION_NAME AS \"code\"  FROM EG_DESIGNATION dt WHERE dt.DESIGNATION_NAME=? and dt.DESIGNATIONID!=? ";
		result = dataExist(query,designationName,designationId);
	} else if(type.equalsIgnoreCase("checkForUniquePositionName")){//TESTED
		String positionName = SecurityUtils.checkSQLInjection(request.getParameter("positionName"));
		String positionId = SecurityUtils.checkSQLInjection(request.getParameter("positionId"));
		String query="SELECT NAME AS \"code\"  FROM EG_POSITION dt WHERE dt.NAME=? and dt.id!=? ";
		result = dataExist(query,positionName,positionId);
	} else if(type.equalsIgnoreCase("checkUniqueForBoundaryName")){//TESTED
		String boundaryId = SecurityUtils.checkSQLInjection(request.getParameter("boundaryId"));
		String bndryName = SecurityUtils.checkSQLInjection(request.getParameter("bndryName"));
		String query="SELECT NAME AS \"code\"  FROM EG_BOUNDARY b WHERE b.ID_BNDRY_TYPE=? and b.NAME=? and b.IS_HISTORY='N' ";
		result = dataExist(query,boundaryId,bndryName);
	} else if(type.equalsIgnoreCase("checkUniqueChildBoundaryName")){//TESTED
		String boundaryId = SecurityUtils.checkSQLInjection(request.getParameter("boundaryId"));
		String bndryName = SecurityUtils.checkSQLInjection(request.getParameter("bndryName"));
		String query="SELECT NAME AS \"code\"  FROM EG_BOUNDARY b WHERE b.parent=? and b.NAME=? and b.IS_HISTORY='N' ";
		result = dataExist(query,boundaryId,bndryName);
	} else if(type.equalsIgnoreCase("checkUniqueForBoundaryNum")){//TESTED
		String boundaryId = SecurityUtils.checkSQLInjection(request.getParameter("boundaryId"));
		String bndryNum = SecurityUtils.checkSQLInjection(request.getParameter("bndryNum"));
		String query="SELECT NAME AS \"code\"  FROM EG_BOUNDARY b WHERE b.ID_BNDRY_TYPE=? and b.bndry_num=? and b.IS_HISTORY='N' ";
		result = dataExist(query,boundaryId,bndryNum);
	} else if(type.equalsIgnoreCase("checkUniqueChildBoundaryNum")){//TESTED
		String boundaryId = SecurityUtils.checkSQLInjection(request.getParameter("boundaryId"));
		String bndryNum = SecurityUtils.checkSQLInjection(request.getParameter("bndryNum"));
		String query="SELECT NAME AS \"code\"  FROM EG_BOUNDARY b WHERE b.parent=? and b.bndry_num=? and b.IS_HISTORY='N' ";
		result = dataExist(query,boundaryId,bndryNum);
	} else if(type.equalsIgnoreCase("checkUniqueActionNameDB")){//TESTED
		String actionName = SecurityUtils.checkSQLInjection(request.getParameter("actionName"));
		String query="SELECT  NAME  AS \"code\"  FROM EG_ACTION a  WHERE a.NAME=? ";
		result = dataExist(query,actionName);
	} else if(type.equalsIgnoreCase("checkForEmployeePositionId")){//TESTED
		String positionId = SecurityUtils.checkSQLInjection(request.getParameter("posId"));
		String query="SELECT ID AS \"code\"  FROM EG_EMP_ASSIGNMENT dt WHERE dt.POSITION_ID=? ";
		result = dataExist(query,positionId);
	}

	response.setContentType("text/plain;charset=utf-8");
	response.setHeader("Cache-Control", "no-cache");
	response.getWriter().write(result);
} catch(Exception e) {
	LOG.error(e);
	throw new ApplicationRuntimeException("Error occurred while processing");
}
%>
