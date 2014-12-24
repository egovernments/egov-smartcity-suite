<%@ page contentType="text/html; charset=Cp1252" %>
<%@ page import= "org.egov.lib.rjbac.dept.ejb.api.*,
		org.egov.lib.rjbac.dept.*,
		org.egov.lib.admbndry.ejb.api.*,
		org.egov.lib.admbndry.*,
		org.egov.infstr.utils.*,
 		java.sql.Date,
 		java.text.SimpleDateFormat,
 		java.util.*,
 		javax.naming.*,
 		org.egov.infstr.utils.*,
		org.egov.commons.utils.EgovInfrastrUtilInteface,
		org.egov.commons.utils.EgovInfrastrUtil,
		org.egov.infstr.utils.*,
		org.egov.lib.rjbac.dept.ejb.api.*,
		org.egov.lib.rjbac.dept.*,
		org.egov.lib.admbndry.ejb.api.*,
		org.egov.lib.admbndry.*,
		org.egov.lib.rjbac.role.ejb.api.*,
		javax.naming.*,
		org.egov.lib.admbndry.BoundaryImpl,
		org.egov.lib.admbndry.ejb.*,
		java.util.*,
		org.egov.lib.rjbac.user.User,
		org.egov.lib.rjbac.user.ejb.api.UserService,
		org.egov.lib.rjbac.user.ejb.server.UserServiceImpl"
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>eGov EIS Payroll</title>
<link href="../common/css/eispayroll.css" rel="stylesheet" type="text/css" />
<link href="../common/css/commonegov.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../common/js/legal.js"></script>
</head>
<body>

<div class="topbar2"><div class="egov"><img src="../common/image/eGov.png" alt="eGov" width="54" height="58" /></div><div class="gov"><img src="../common/image/india.png" alt="India" width="54" height="58" /></div>
</div>
<div class="navibarwk"><div class="piconwk"><a href="#">
<img src="../common/image/help.png" alt="Help" width="16" height="16" border="0" /></a></div>
</div><div class="navibarshadowwk"></div>
<div class="formmainbox">
  <div class="insidecontent">
  <div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>
	  <div class="rbcontent2">
	    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	    <td align="right" colspan="2">
	    <%
	   	if (request.getUserPrincipal() == null) //user has logged in
	   	{
	   	
        %>
          <a id="loginhref" name="loginhref" href="${pageContext.request.contextPath}/login/securityLogin.jsp"><font id="welcome_font">Log In</font></a>
          <%
    	}
    	else
    	
    	{
          %>
		   
         <font id="welcome_font">Logged in as:<b><%=EgovUtils.getPrincipalName(request.getUserPrincipal().getName()) %></b><span id="headerusername">
                     &nbsp;</span></font>
         &nbsp;&nbsp;<a id="loginhref" name="loginhref" href="#" onclick="javascript:top.location='/egi/logout.do'"><font id="welcome_font">Logout</font></a>
          <%
    	}

          %>
          </td>
          <tr>
          
    
            <td width="41%"><img src="../common/image/iconPayment.jpg" alt="Login" width="368" height="292" /></td>
            <td width="59%"><div class="welcomestyles"></div>
            <div class="welcomestylel">Welcome to <br>eGov Payroll System</div>
            <div class="welcomestylearrow"><img src="../common/image/arrow_left.png" alt="Left" width="16" height="16" align="absmiddle" /> Use the links on the left to navigate 
            through the application.</div>
            <div class="loginboxnew1"></div></td>
            
          </tr>
        </table>
	  </div>
	  <div class="rbbot2"><div></div></div>
</div>
  </div>
</div>
<div class="buttonbottom">City Administration System Designed and Implemented by <a href="http://www.egovernments.org/">eGovernments Foundation</a> All Rights Reserved </div>
</body>
</html>

