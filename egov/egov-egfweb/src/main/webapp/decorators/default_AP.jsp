#-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#      accountability and the service delivery of the government  organizations.
#   
#       Copyright (C) <2015>  eGovernments Foundation
#   
#       The updated version of eGov suite of products as by eGovernments Foundation 
#       is available at http://www.egovernments.org
#   
#       This program is free software: you can redistribute it and/or modify
#       it under the terms of the GNU General Public License as published by
#       the Free Software Foundation, either version 3 of the License, or
#       any later version.
#   
#       This program is distributed in the hope that it will be useful,
#       but WITHOUT ANY WARRANTY; without even the implied warranty of
#       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#       GNU General Public License for more details.
#   
#       You should have received a copy of the GNU General Public License
#       along with this program. If not, see http://www.gnu.org/licenses/ or 
#       http://www.gnu.org/licenses/gpl.html .
#   
#       In addition to the terms of the GPL license to be adhered to in using this
#       program, the following additional terms are to be complied with:
#   
#   	1) All versions of this program, verbatim or modified must carry this 
#   	   Legal Notice.
#   
#   	2) Any misrepresentation of the origin of the material is prohibited. It 
#   	   is required that all modified versions of this material be marked in 
#   	   reasonable ways as different from the original version.
#   
#   	3) This license does not grant any rights to any user of the program 
#   	   with regards to rights under trademark law for use of the trade names 
#   	   or trademarks of eGovernments Foundation.
#   
#     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------
<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib uri="/egovAuthz" prefix="egov-authz" %> 
<%@page import="org.egov.infstr.utils.EgovUtils" %>


<%@ page import="java.sql.Connection,java.sql.PreparedStatement,
org.egov.infstr.client.administration.rjbac.user.UserForm,
org.egov.infstr.utils.HibernateUtil,
org.egov.infstr.utils.ServiceLocator,
org.egov.lib.rjbac.user.User,
org.egov.lib.rjbac.user.ejb.api.UserManager,
org.egov.lib.rjbac.user.ejb.api.UserManagerHome;"
%>
<%@ page import="org.apache.log4j.Logger"%>

<html>
    <head>
        <%@ include file="/includes/meta.jsp" %>
        <title>eGov  - <decorator:title/> </title>

        <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/css/egov.css'/>" />
      	<link rel="stylesheet" type="text/css" href="<c:url value='/commonyui/b.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/script/calendar.juild/reset/reset.css'/>" />
	<link rel="stylesheet" type="text/css" href="<c:url value='/commonyui/build/fonts/fonts.css'/>" />

        <script type="text/javascript" src="<c:url value='/javascript/calenders'/>" ></script>
	<script type="text/javascript" src="<c:url value='/javascript/validations.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/javascript/SASvalidation.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/javascript/dateValidation.js'/>"></script>
	
	<script type="text/javascript" src="<c:url value='/commonyui/build/yahoo/yahoo.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/build/dom/dom.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/build/autocomplete/autocomplete-debug.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/build/event/event-debug.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/build/animation/animation.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonjs/ajaxCommonFunctions.js'/>"></script>
	
	<SCRIPT type="text/javascript" src="<c:url value='/script/jsCommonMethods.js' />"></SCRIPT>
	
        <decorator:head/>
    </head>
    
<body <decorator:getProperty property="body.id" writeEntireProperty="yes"/><decorator:getProperty property="body.class" writeEntireProperty="true"/> <decorator:getProperty property="body.onload" writeEntireProperty="true"/>  >

  <!-- Header Section Begins -->
          <%@ include file="/egovSitemeshHeader.jsp" %>
  <!-- Header Section Ends -->
<%
Logger LOGGER = Logger.getLogger("default_AP.jsp");
boolean admin=false;
Integer userID= (Integer)request.getSession().getAttribute("com.egov.user.LoginUserId");
LOGGER.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Egov Site mesh Header userID "+userID);
 String userName="";
 ServiceLocator serviceloc = ServiceLocator.getInstance();
 UserManagerHome uhome = (UserManagerHome)serviceloc.getLocalHome("UserManagerHome");
UserManager userManager = uhome.create();
if(userID !=null)
{
User user = userManager.getUserByID(userID);
 userName=user.getUserName();

}
 %> 
   <!-- Body Begins -->
  <table align="center" height=30 cellSpacing=0 cellPadding=0  summary="" >
    <tbody>
    <tr>
      <td height=5 width="762">
      <% if(userID!=null) 
      {
      %>
        <p align="center"><b><font face="Verdana" size="2">Welcome &nbsp;<span id="headerusername">
        <%=userName%>&nbsp;</span></font><font face="Verdana" size="1">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font></b><font size="2">
       <%
       }
       else
        {
        %>
        <p align="center"><b><font face="Verdana" size="2">Welcome &nbsp;<span id="headerusername">
        &nbsp;</span></font><font face="Verdana" size="1">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font></b><font size="2">
        <%
        }
         %>
       
		<egov-authz:authorize actionName="Administration">
		 <a id="admin" name="admin" href="#" onclick="javascript:window.open('/egi/eGov.jsp','')"><b><font face="Verdana">Administration</font></b></a> 
		</egov-authz:authorize>

        <a id="loginhref" name="loginhref" href="#" onclick="javascript:top.location='<%=request.getContextPath()%>/eGov.jsp'"><b><font face="Verdana">Home</font></b></a> 
        <a id="loginhref" name="loginhref" href="#" onclick="javascript:top.location='<%=request.getContextPath()%>/logout.jsp'"><b><font face="Verdana">Logout</font></b></a></font></p>
      
     
       </span></font>
      </td></tr>
  
    </tbody>
  
  
</table>

 <table align='center' id="table2" >
  <tr>
  <td>
  <DIV id=main>
  <DIV id=m2>
  <DIV id=m3>
  <div align="center">
  <center>

  
	<decorator:body/>

	</center>
	</div></div></div></div>
	
	</td>
	</tr>
	</table>
	</body>
	<%@ include file="/egovfooter.html" %> 
</html>
 
