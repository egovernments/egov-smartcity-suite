<%@ include file="/includes/taglibs.jsp" %>
<%--
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
--%>

<%@ page import="java.sql.*,org.egov.infstr.utils.HibernateUtil,java.util.*,java.text.SimpleDateFormat" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">        
        <link rel="stylesheet" type="text/css" href="/css/egov.css">
        <title>History</title>
    </head>
    
    <script type="text/javascript" src="script/rowDetails.js"></script> 
    <script type="text/javascript" src="exility/PageManager.js"></script> 
    <SCRIPT type="text/javascript" src="script/jsCommonMethods.js"></Script>
    
    <script>
    	  
        
    </script>
    
	<body class="bodyStyle">
	<center>
	<br>
	<table align='center' id="table2">
	<tr><td>
	<div id="topStyle"><div id="bottomStyle"><div id="tabletextStyle">
	
	<table align='center' class="smallTableStyle" id="table3"> 
	 <tr>
	 <td colspan=4 class="tableheader" align="center">History</td>
	 </tr>
	 <tr>
	  <td colspan=4 class="thStlyle" align="center"><%= request.getParameter("objNumber") %></td>
	 </tr>
	<tr><td colspan=4>&nbsp;</td></tr>
	<tr>
	<td>
	<table cellpadding="1" cellspacing="1" align="center" width="100%">	
	
	<%
	
	Connection con=null;
	ResultSet rs=null;
	Statement stmt=null;
	ResultSet rs1=null;
	Statement stmt1=null;
	String values = "";
	try
	{
	con = HibernateUtil.getCurrentSession().connection();
	stmt=con.createStatement();
	stmt1=con.createStatement();
	}
	catch(Exception e)
	{
	System.out.println(e.getMessage());
	throw new Exception("Cannot obtain a connection object");
	}

	String observation = "";
	String processInstanceId=request.getParameter("processInstanceId");
	
	String userquery = "select egu.user_name,egu.first_name || egu.middle_name || egu.last_name as fullname, empV.name as empName  from eg_user egu, eg_eis_employeeinfo empV where empV.USER_ID = egu.ID_USER "+
			" and egu.user_name = (select stringvalue_  "+
			" from jbpm_variableinstance where processinstance_="+processInstanceId+" and name_ = 'StartUser' ) ";
	//System.out.println("start user query --> "+userquery);
	
	String query="select egu.user_name,egu.first_name || egu.middle_name || egu.last_name as fullname, ti.description_, empV.name as empName from eg_user egu, jbpm_taskinstance ti, eg_eis_employeeinfo empV where empV.USER_ID = egu.ID_USER and "+
			"  egu.id_user = ti.actorid_ and ti.procinst_ = "+processInstanceId+" order by id_";
	//System.out.println("History query --> "+query);
	rs=stmt.executeQuery(query);
	rs1=stmt1.executeQuery(userquery);
	 %>
	 
	 <tr>
<!-- <td class="thStlyle" align="center" width="30%"><div align="center">UserName</div></td>
	 <td class="thStlyle" align="center" width="30%"><div align="center">FullName</div></td>	-->
	 <td class="thStlyle" align="center" width="30%"><div align="center"><b>Employee name</b></div></td>
	 <td class="thStlyle" align="center" width="30%"><div align="center"><b>Observation</b></div></td>
	 </tr>
	 
	 
	
	<% 
	while(rs1.next()) { 
	%>
	<tr>
<!-- <td class="tdStlyle" align="center" width="30%"><%= rs1.getString(1) %></td>
	 <td class="tdStlyle" align="center" width="30%"><%= rs1.getString(2)  %></td>	-->
	 <td class="tdStlyle" align="center" width="30%"><%= rs1.getString("empName")  %></td>
	 <td class="tdStlyle" align="center" width="30%">Created</td>
	 </tr >
	 
	<% 
	}
	%>
	 
	 <tr>
	
	<% while(rs.next()) { 
	if(rs.getString(3) != null)
		observation = rs.getString(3);
	%>
	
<!-- <td class="tdStlyle" align="center" width="30%"><%= rs.getString(1) %></td>
	 <td class="tdStlyle" align="center" width="30%"><%= rs.getString(2)  %></td>	-->
	 <td class="tdStlyle" align="center" width="30%"><%= rs.getString("empName")  %></td>
	 <td class="tdStlyle" align="center" width="30%">&nbsp;<%= observation  %></td>

	 </tr >

	 <% 
	 observation="";
	 }
	 %>

	 <tr><td colspan=4>&nbsp;</td></tr>
	 <tr>
	 <td  align="center" colspan=4>
	 <html:button styleClass="button" value="  Close  " property="b3" onclick="window.close()" />
	 </td>
	 </tr>

	 </table>
	 
	 </div></div></div>
	 </td></tr>
	 </table>
	 </center>
	 </body>
	</html>