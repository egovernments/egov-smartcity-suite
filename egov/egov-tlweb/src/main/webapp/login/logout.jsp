#-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#     accountability and the service delivery of the government  organizations.
#  
#      Copyright (C) <2015>  eGovernments Foundation
#  
#      The updated version of eGov suite of products as by eGovernments Foundation 
#      is available at http://www.egovernments.org
#  
#      This program is free software: you can redistribute it and/or modify
#      it under the terms of the GNU General Public License as published by
#      the Free Software Foundation, either version 3 of the License, or
#      any later version.
#  
#      This program is distributed in the hope that it will be useful,
#      but WITHOUT ANY WARRANTY; without even the implied warranty of
#      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#      GNU General Public License for more details.
#  
#      You should have received a copy of the GNU General Public License
#      along with this program. If not, see http://www.gnu.org/licenses/ or 
#      http://www.gnu.org/licenses/gpl.html .
#  
#      In addition to the terms of the GPL license to be adhered to in using this
#      program, the following additional terms are to be complied with:
#  
#  	1) All versions of this program, verbatim or modified must carry this 
#  	   Legal Notice.
#  
#  	2) Any misrepresentation of the origin of the material is prohibited. It 
#  	   is required that all modified versions of this material be marked in 
#  	   reasonable ways as different from the original version.
#  
#  	3) This license does not grant any rights to any user of the program 
#  	   with regards to rights under trademark law for use of the trade names 
#  	   or trademarks of eGovernments Foundation.
#  
#    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------
<%@ include file="/includes/taglibs.jsp"%>
<%@ page import="javax.servlet.http.Cookie" %>
<%@ page import="org.acegisecurity.ui.rememberme.TokenBasedRememberMeServices" %>

<%
if (request.getSession(false) != null) {
    session.invalidate();
}
Cookie terminate = new Cookie(TokenBasedRememberMeServices.ACEGI_SECURITY_HASHED_REMEMBER_ME_COOKIE_KEY, null);
String contextPath = request.getContextPath();
terminate.setPath(contextPath != null && contextPath.length() > 0 ? contextPath : "/");
terminate.setMaxAge(0);
response.addCookie(terminate);
%>


<HTML>
<head>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
 <title>Logout Page</title>
	    <link href="${pageContext.request.contextPath}/css/professionaltax.css" rel="stylesheet" type="text/css" />
<link href="/commons/css/commonegov.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/pt.js"></script>  
	

 
</head>

<BODY>

 <br>
<div class="formmainbox"><div class="formheading"></div><div class="insidecontent">
  <div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>
	  <div class="rbcontent2">
	    <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="59%"><div class="logouttext"><img src="../images/disconnect.png" alt="Disconnect" width="16" height="16" align="absmiddle" /> You are logged out... </div>
              <div class="logoutmessage"> You have successfully logged out from eGov Admin System. 
		Please <a  href='<c:url value="/eGov.jsp" />' style="font-size:12px">click here</a> to go to the Home Page.</div></td>

          </tr>
        </table>
	  </div>
	  <div class="rbbot2"><div></div></div>
</div>
  </div>
</div>
<div class="buttonbottom">City Administration System Designed and Implemented by <a href="http://www.egovernments.org/">eGovernments Foundation</a> All Rights Reserved </div>

</BODY>
</HTML>




