<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib uri="/egovAuthz" prefix="egov-authz" %> 
<%@page import="org.egov.infstr.utils.EgovUtils" %>


<%@ page import="java.sql.Connection,java.sql.PreparedStatement,
org.egov.infstr.client.administration.rjbac.user.UserForm,
org.egov.infstr.utils.HibernateUtil,
org.egov.lib.rjbac.user.ejb.server.UserServiceImpl,
org.egov.lib.rjbac.user.User"
%>

<html>
    <head>
        <%@ include file="/includes/meta.jsp" %>
        <title>eGov  - <decorator:title/> </title>

        <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/css/egov.css'/>" />
      	<link rel="stylesheet" type="text/css" href="<c:url value='/commonyui/b.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/script/calendar.juild/reset/reset.css'/>" />
	<link rel="stylesheet" type="text/css" href="<c:url value='/commonyui/build/fonts/fonts.css'/>" />

        <script type="text/javascript" src="<c:url value='/javascript/calenders'/>" ></script>
	<script type="text/javascript" src="<c:url value='/dhtml.js'/>"></script>
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
boolean admin=false;
Integer userID= (Integer)request.getSession().getAttribute("com.egov.user.LoginUserId");
 System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Egov Site mesh Header userID "+userID);
 String userName="";
UserServiceImpl userServiceImpl = new UserServiceImpl();
if(userID !=null)
{
User user = userServiceImpl.getUserByID(userID);
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
 