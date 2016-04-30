<!--
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
  -->
<%@ include file="/includes/taglibs.jsp"%>
<%@ page import="org.acegisecurity.ui.rememberme.TokenBasedRememberMeServices" %>
<%@ page import="javax.servlet.http.Cookie" %>

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
<title>Logout Page</title>
  <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/commoncss/egov.css'/>" />
 <LINK REL=stylesheet HREF="/commoncss/ccMenu.css" TYPE="text/css">
<head>

<BODY>


<div align=center>
<table align='middle' id="signup"  >

<tr>
	<td style="buffer:0pt; padding:0pt; margin:0pt">

<div id="main"><div id="m2"><div id="m3">
<div align="center">

<table border="1" align="center" cellspacing="0" width="393" cellpadding="5" height="147" >
<tr>
	<td width="385" height="15" class="smalllabelcell"><b>LOGOUT</b></td>
  </tr>
  <tr>
    <td width="385" height="108">
	  <font color="#0000FF"><b>You have successfully logged out from eGov Financial System.
	  <p>Please click <a id="here" name = "here" href='<c:url value="/login/securityLogin.jsp" />'> 
	  <font size="2" color="#000000">here</font></a> to go to the Home Page.<br></p></b></font> 

    </td>
  </tr>

</table>

</div>
</div></div></div>
</td></tr>
</table>
</div>


<p align=center>
	     <font style="font-SIZE: 10pt">City
		Administration System Designed and Implemented by <font color=#0000ff>
				<a href="http://www.egovernments.org">eGovernments Foundation</a></font></font>
				<font style="font-SIZE: 10pt"><br><a href="http://egovernments.org"></a>All Rights Reserved</font></p>

    </P></P>


</BODY>
</HTML>




