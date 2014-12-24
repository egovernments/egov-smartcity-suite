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
        <link rel="stylesheet" type="text/css" href="<c:url value='/cssnew/index.css'/>"></script>
        <link rel="stylesheet" type="text/css" href="<c:url value='/cssnew/commonegovnew.css'/>"></script>
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
     <div class="topbar">
     	<div class="egov"><img src="image/eGov.png" alt="eGov" width="54" height="58" /></div>
     	<div class="gov"><img src="image/Chennai_logo.jpg" alt="CHENNAICMC" width="54" height="58" /></div>
 		<div class="mainheading">City Administration  </div>
 	</div>
 	<div class="navibar">
 		<div align="right">
   			<table width="100%" border="0" cellspacing="0" cellpadding="0">
 				<tr>
            		<td><div align="left"><ul id="tabmenu" class="tabmenu">
                		<li ><a id="loginhref" name="loginhref" href="#" onclick="javascript:top.location='/egi/logout.do'">Logout</a></li>
        				</ul></div>
        			</td>
 				</tr>
 			</table>
 		</div>
	</div>

  <!-- Header Section Ends -->
<%
boolean admin=false;
Integer userID= (Integer)request.getSession().getAttribute("com.egov.user.LoginUserId");
 System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Egov Site mesh Header userID "+userID);
UserServiceImpl userServiceImpl = new UserServiceImpl();
if(userID !=null)
{
User user = userServiceImpl.getUserByID(userID);
 userName=user.getUserName();

}
 %> 
   
 <table align='center' id="table2" width="100%">
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
</html>
 
